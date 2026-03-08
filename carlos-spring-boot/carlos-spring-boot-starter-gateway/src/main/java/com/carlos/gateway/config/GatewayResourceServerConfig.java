package com.carlos.gateway.config;

import com.carlos.gateway.properties.GatewayOAuth2Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Gateway 资源服务器自动配置（响应式版本）
 *
 * <p>本配置类为 Spring Cloud Gateway 提供 OAuth2 资源服务器功能，
 * 用于在网关层统一验证 JWT Token。</p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>统一验证请求中的 JWT Token</li>
 *   <li>从 Token 中提取用户身份和权限</li>
 *   <li>保护 API 端点，拒绝无效 Token</li>
 *   <li>支持方法级安全（@PreAuthorize, @PostAuthorize）</li>
 * </ul>
 *
 * <h3>启用条件：</h3>
 * <p>需要在配置中设置：</p>
 * <pre>{@code
 * carlos:
 *   gateway:
 *     oauth2:
 *       resource-server:
 *         enabled: true
 *         jwk-set-uri: http://auth-server:9000/oauth2/jwks
 * }</pre>
 *
 * <h3>使用场景：</h3>
 * <ul>
 *   <li><strong>网关统一认证</strong> - 在网关层统一验证 Token，减轻下游服务压力</li>
 *   <li><strong>微服务安全</strong> - 保护微服务接口，拒绝非法请求</li>
 *   <li><strong>Token 转发</strong> - 验证后将用户信息传递给下游服务</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(GatewayOAuth2Properties.class)
@ConditionalOnProperty(
    prefix = "carlos.gateway.oauth2.resource-server",
    name = "enabled",
    havingValue = "true"
)
public class GatewayResourceServerConfig {

    /**
     * OAuth2 配置属性
     */
    private final GatewayOAuth2Properties oauth2Properties;

    /**
     * 资源服务器安全过滤器链（响应式）
     *
     * <p>配置资源服务器的安全策略，包括：</p>
     * <ul>
     *   <li>无状态会话管理（Token 机制不需要 Session）</li>
     *   <li>JWT Token 验证</li>
     *   <li>路径白名单配置</li>
     *   <li>权限提取</li>
     * </ul>
     *
     * <h3>白名单配置：</h3>
     * <p>以下路径默认不需要认证：</p>
     * <ul>
     *   <li>/error - 错误页面</li>
     *   <li>/actuator/health - 健康检查</li>
     *   <li>/swagger-ui/**, /doc.html - API 文档</li>
     *   <li>用户配置的 permit-all-paths</li>
     * </ul>
     *
     * @param http ServerHttpSecurity 配置构建器
     * @return SecurityWebFilterChain 安全过滤器链
     */
    @Bean
    @ConditionalOnMissingBean(name = "gatewaySecurityFilterChain")
    public SecurityWebFilterChain gatewaySecurityFilterChain(ServerHttpSecurity http) {
        GatewayOAuth2Properties.ResourceServerProperties resourceServer =
            oauth2Properties.getResourceServer();

        http
            // 禁用 CSRF（使用 Token 机制不需要 CSRF 保护）
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            // 配置请求授权
            .authorizeExchange(exchanges -> {
                // 白名单路径
                List<String> permitAllPaths = resourceServer.getPermitAllPaths();
                if (permitAllPaths != null && !permitAllPaths.isEmpty()) {
                    exchanges.pathMatchers(
                        permitAllPaths.toArray(new String[0])
                    ).permitAll();
                    log.debug("Configured permit-all paths: {}", permitAllPaths);
                }

                // 其他请求需要认证
                exchanges.anyExchange().authenticated();
            })
            // 配置 JWT 资源服务器
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtSpec ->
                    jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        log.info("Gateway OAuth2 Resource Server configured");
        if (resourceServer.getJwkSetUri() != null) {
            log.info("  - JWK Set URI: {}", resourceServer.getJwkSetUri());
        }
        if (resourceServer.getIssuerUri() != null) {
            log.info("  - Issuer URI: {}", resourceServer.getIssuerUri());
        }

        return http.build();
    }

    /**
     * JWT 解码器（响应式版本）
     *
     * <p>用于解码和验证 JWT Token。</p>
     *
     * <h3>配置优先级：</h3>
     * <ol>
     *   <li>如果配置了 {@code jwk-set-uri}，使用 JWK Set URI 获取公钥</li>
     *   <li>如果配置了 {@code issuer-uri}，从 Issuer 发现端点获取配置</li>
     *   <li>否则抛出异常（必须配置其一）</li>
     * </ol>
     *
     * <h3>配置示例：</h3>
     * <pre>{@code
     * # 方式1：使用 JWK Set URI（推荐）
     * carlos:
     *   gateway:
     *     oauth2:
     *       resource-server:
     *         jwk-set-uri: http://auth-server:9000/oauth2/jwks
     *
     * # 方式2：使用 Issuer URI
     * carlos:
     *   gateway:
     *     oauth2:
     *       resource-server:
     *         issuer-uri: http://auth-server:9000
     * }</pre>
     *
     * @return ReactiveJwtDecoder 响应式 JWT 解码器
     * @throws IllegalStateException 如果未配置 jwk-set-uri 或 issuer-uri
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        GatewayOAuth2Properties.ResourceServerProperties resourceServer =
            oauth2Properties.getResourceServer();

        // 优先使用 JWK Set URI
        if (resourceServer.getJwkSetUri() != null) {
            log.info("Configuring reactive JWT decoder with JWK Set URI: {}",
                resourceServer.getJwkSetUri());
            return NimbusReactiveJwtDecoder.withJwkSetUri(resourceServer.getJwkSetUri()).build();
        }

        // 其次使用 Issuer URI - 响应式版本需要通过 NimbusReactiveJwtDecoder 配置
        if (resourceServer.getIssuerUri() != null) {
            log.info("Configuring reactive JWT decoder with Issuer URI: {}",
                resourceServer.getIssuerUri());
            // 从 Issuer 获取 JWK Set URI
            String jwkSetUri = resourceServer.getIssuerUri() + "/oauth2/jwks";
            return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
        }

        // 必须配置其一
        throw new IllegalStateException(
            "Gateway OAuth2 Resource Server configuration error: " +
                "Either 'carlos.gateway.oauth2.resource-server.jwk-set-uri' or " +
                "'carlos.gateway.oauth2.resource-server.issuer-uri' must be configured. " +
                "Please check your application.yml configuration."
        );
    }

    /**
     * JWT 认证转换器（响应式适配器）
     *
     * <p>将 JWT Token 转换为 Spring Security 的 Authentication 对象。</p>
     *
     * <h3>提取的信息：</h3>
     * <ul>
     *   <li>sub - 用户名（subject）</li>
     *   <li>scope - 作用域权限</li>
     *   <li>authorities - 自定义权限（扩展声明）</li>
     *   <li>role_ids - 角色 ID 列表（扩展声明）</li>
     * </ul>
     *
     * @return Converter JWT 认证转换器
     */
    @Bean
    @ConditionalOnMissingBean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // 配置权限提取器
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());

        // 设置主体声明名称（默认是 sub）
        converter.setPrincipalClaimName("sub");

        // 包装为响应式适配器
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    /**
     * JWT 权限转换器
     *
     * <p>从 JWT Claims 中提取用户权限。</p>
     *
     * <h3>权限来源（按优先级）：</h3>
     * <ol>
     *   <li><strong>scope</strong> - OAuth2 标准作用域，转换为 SCOPE_* 权限</li>
     *   <li><strong>authorities</strong> - 自定义权限声明，逗号分隔</li>
     *   <li><strong>role_ids</strong> - 角色 ID 列表，转换为 ROLE_* 权限</li>
     * </ol>
     *
     * <h3>权限格式：</h3>
     * <ul>
     *   <li>scope:read -> SCOPE_read</li>
     *   <li>admin -> ROLE_1（role_id 为 1 的角色）</li>
     * </ul>
     *
     * @return Converter JWT 权限转换器
     */
    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            // 1. 从 scope 中提取权限（OAuth2 标准）
            JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
            authorities.addAll(scopeConverter.convert(jwt));

            // 2. 从自定义 authorities 声明中提取权限
            String authoritiesClaim = jwt.getClaimAsString("authorities");
            if (authoritiesClaim != null && !authoritiesClaim.isEmpty()) {
                String[] authArray = authoritiesClaim.split(",");
                for (String auth : authArray) {
                    if (auth != null && !auth.trim().isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority(auth.trim()));
                    }
                }
            }

            // 3. 从 role_ids 中提取角色权限
            @SuppressWarnings("unchecked")
            List<Long> roleIds = jwt.getClaim("role_ids");
            if (roleIds != null && !roleIds.isEmpty()) {
                for (Long roleId : roleIds) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleId));
                }
            }

            log.debug("Extracted authorities from JWT for user '{}': {}",
                jwt.getSubject(), authorities);

            return authorities;
        };
    }
}
