package com.carlos.security.config;

import com.carlos.redis.util.RedisUtil;
import com.carlos.security.permission.PermissionProvider;
import com.carlos.security.permission.PermissionService;
import com.carlos.security.permission.cache.PermissionCacheSyncManager;
import com.carlos.security.permission.evaluator.CarlosPermissionEvaluator;
import com.carlos.security.permission.provider.CachedPermissionProvider;
import com.carlos.security.permission.provider.RedisPermissionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Duration;
import java.util.Collections;

/**
 * <p>
 * 资源服务器自动配置
 * </p>
 *
 * <p>本配置类自动启用以下功能：</p>
 * <ol>
 *   <li><b>安全过滤器链</b> - 配置无状态会话和 X-User-Id 认证</li>
 *   <li><b>方法级安全</b> - 启用 @PreAuthorize/@PostAuthorize 注解</li>
 *   <li><b>权限提供者</b> - 自动配置 Redis + Caffeine 多层缓存</li>
 *   <li><b>缓存同步</b> - Redis Pub/Sub 实现多实例缓存同步</li>
 *   <li><b>权限评估器</b> - 支持 hasPermission() SpEL 表达式</li>
 * </ol>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   resource-server:
 *     enabled: true
 *     permission:
 *       cache:
 *         enabled: true
 *         local-ttl: 600
 *         max-size: 10000
 *       redis-ttl: 604800
 * }</pre>
 *
 * <h3>使用说明：</h3>
 * <p>网关认证后，通过 HTTP Header 传递用户信息（使用 {@link com.carlos.core.constant.HttpHeadersConstant} 中定义的常量）：</p>
 * <pre>
 * X-User-Id: 10001
 * X-User-Name: admin
 * X-User-Roles: 1,2,3
 * </pre>
 * <p>本过滤器提取这些信息构建 Spring Security 上下文。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(ResourceServerProperties.class)
@ConditionalOnProperty(prefix = "carlos.resource-server", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({RedisUtil.class})
@RequiredArgsConstructor
public class ResourceServerAutoConfiguration {

    private final ResourceServerProperties properties;

    // ==================== Redis Pub/Sub 配置 ====================

    /**
     * Redis 消息监听器容器（用于 Pub/Sub）
     */
    @Bean
    @ConditionalOnMissingBean(RedisMessageListenerContainer.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory connectionFactory,
        PermissionCacheSyncManager permissionCacheSyncManager) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 添加权限变更消息监听器
        container.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                permissionCacheSyncManager.onMessage(message.getBody());
            }
        }, new ChannelTopic(PermissionCacheSyncManager.PERMISSION_CHANGE_CHANNEL));

        log.info("RedisMessageListenerContainer initialized for permission cache sync");
        return container;
    }

    // ==================== 权限提供者配置 ====================

    /**
     * Redis 权限提供者
     */
    @Bean
    @ConditionalOnMissingBean(RedisPermissionProvider.class)
    public RedisPermissionProvider redisPermissionProvider() {
        log.info("RedisPermissionProvider initialized");
        return new RedisPermissionProvider();
    }

    /**
     * 权限提供者（带本地缓存）
     * <p>根据配置决定是否启用 Caffeine 本地缓存</p>
     */
    @Bean
    @ConditionalOnMissingBean(PermissionProvider.class)
    public PermissionProvider permissionProvider(RedisPermissionProvider redisProvider) {
        if (properties.getPermission().getCache().isEnabled()) {
            long localTtl = properties.getPermission().getCache().getLocalTtl();
            int maxSize = properties.getPermission().getCache().getMaxSize();

            CachedPermissionProvider cachedProvider = new CachedPermissionProvider(
                redisProvider,
                Duration.ofSeconds(localTtl),
                maxSize,
                "resource-server-cache"
            );

            log.info("CachedPermissionProvider initialized: localTtl={}s, maxSize={}", localTtl, maxSize);
            return cachedProvider;
        }

        log.info("PermissionProvider using Redis without local cache");
        return redisProvider;
    }

    /**
     * 权限同步管理器（Pub/Sub）
     */
    @Bean
    @ConditionalOnMissingBean(PermissionCacheSyncManager.class)
    public PermissionCacheSyncManager permissionCacheSyncManager(PermissionProvider permissionProvider) {
        PermissionCacheSyncManager syncManager = new PermissionCacheSyncManager();

        // 如果权限提供者是 CachedPermissionProvider，添加到管理器
        if (permissionProvider instanceof CachedPermissionProvider) {
            syncManager.setCachedProviders(
                Collections.singletonList((CachedPermissionProvider) permissionProvider)
            );
        }

        log.info("PermissionCacheSyncManager initialized");
        return syncManager;
    }

    /**
     * 权限服务
     */
    @Bean
    @ConditionalOnMissingBean(PermissionService.class)
    public PermissionService permissionService(PermissionProvider permissionProvider,
                                               PermissionCacheSyncManager syncManager) {
        return new PermissionService(permissionProvider, syncManager);
    }

    // ==================== 安全过滤器配置 ====================

    /**
     * 用户上下文认证过滤器
     * <p>从 X-User-Id/X-User-Roles Header 构建 SecurityContext</p>
     */
    @Bean
    @ConditionalOnMissingBean(UserContextAuthenticationFilter.class)
    public UserContextAuthenticationFilter userContextAuthenticationFilter(
        PermissionProvider permissionProvider) {
        UserContextAuthenticationFilter filter = new UserContextAuthenticationFilter(permissionProvider);
        filter.setWhitelistPaths(properties.getAuth().getWhitelistPaths());
        log.info("UserContextAuthenticationFilter initialized");
        return filter;
    }

    /**
     * 安全过滤器链配置
     * <p>无状态会话 + Header 认证</p>
     */
    @Bean
    @Order(100)
    @ConditionalOnMissingBean(name = "resourceServerSecurityFilterChain")
    public SecurityFilterChain resourceServerSecurityFilterChain(
        HttpSecurity http,
        UserContextAuthenticationFilter userContextFilter) throws Exception {

        http
            // 禁用 CSRF（API 无状态）
            .csrf(AbstractHttpConfigurer::disable)

            // 无状态会话
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 添加自定义过滤器（在 UsernamePasswordAuthenticationFilter 之前）
            .addFilterBefore(userContextFilter, UsernamePasswordAuthenticationFilter.class)

            // 授权配置（默认允许所有，使用 @PreAuthorize 控制）
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(properties.getAuth().getWhitelistPaths().toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
            )

            // 异常处理
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .accessDeniedHandler(new ForbiddenAccessDeniedHandler())
            );

        log.info("SecurityFilterChain configured for resource server");
        return http.build();
    }

    // ==================== 方法级安全配置 ====================

    /**
     * 启用方法级安全注解
     */
    @Configuration
    @EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
    )
    @ConditionalOnProperty(prefix = "carlos.resource-server", name = "method-security-enabled", havingValue = "true", matchIfMissing = true)
    public static class MethodSecurityConfig {

        /**
         * 自定义权限评估器
         * <p>支持 hasPermission() 表达式</p>
         */
        @Bean
        @ConditionalOnMissingBean(name = "permissionEvaluator")
        public CarlosPermissionEvaluator permissionEvaluator() {
            log.info("PermissionEvaluator initialized");
            return new CarlosPermissionEvaluator();
        }

        /**
         * 方法安全表达式处理器
         */
        @Bean
        @ConditionalOnMissingBean(MethodSecurityExpressionHandler.class)
        public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            CarlosPermissionEvaluator permissionEvaluator) {
            DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
            handler.setPermissionEvaluator(permissionEvaluator);
            log.info("MethodSecurityExpressionHandler configured with custom PermissionEvaluator");
            return handler;
        }
    }
}
