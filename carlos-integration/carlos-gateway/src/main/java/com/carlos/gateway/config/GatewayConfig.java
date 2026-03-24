package com.carlos.gateway.config;

import com.carlos.gateway.cache.CacheProperties;
import com.carlos.gateway.cache.ResponseCacheFilter;
import com.carlos.gateway.circuitbreaker.Resilience4jCircuitBreakerFilter;
import com.carlos.gateway.filter.PathPrefixFilter;
import com.carlos.gateway.gray.GrayReleaseFilter;
import com.carlos.gateway.gray.GrayReleaseProperties;
import com.carlos.gateway.oauth2.*;
import com.carlos.gateway.observability.MetricsFilter;
import com.carlos.gateway.observability.TracingFilter;
import com.carlos.gateway.observability.TracingProperties;
import com.carlos.gateway.security.ReplayProtectionFilter;
import com.carlos.gateway.security.ReplayProtectionProperties;
import com.carlos.gateway.security.WafFilter;
import com.carlos.gateway.security.WafProperties;
import com.carlos.gateway.transform.RequestTransformFilter;
import com.carlos.gateway.transform.TransformProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * <p>
 * 现代网关自动配置类
 * 整合所有网关组件
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
    GatewayProperties.class,
    OAuth2GatewayProperties.class,
    GrayReleaseProperties.class,
    WafProperties.class,
    ReplayProtectionProperties.class,
    CacheProperties.class,
    TracingProperties.class,
    TransformProperties.class,
    ExceptionProperties.class
})
public class GatewayConfig {

    // ==================== 路径前缀配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.prefix-enabled", havingValue = "true", matchIfMissing = true)
    public PathPrefixFilter pathPrefixFilter(GatewayProperties gatewayProperties) {
        log.info("Initializing Path Prefix Filter with prefix: {}", gatewayProperties.getPrefix());
        return new PathPrefixFilter(gatewayProperties);
    }

    // ==================== OAuth2 认证配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.oauth2.enabled", havingValue = "true", matchIfMissing = true)
    public TokenValidator tokenValidator(OAuth2GatewayProperties properties,
                                         ReactiveStringRedisTemplate redisTemplate,
                                         WebClient.Builder webClientBuilder) {
        return switch (properties.getTokenType()) {
            case JWT -> new JwtTokenValidator(properties, redisTemplate);
            case OPAQUE -> new OpaqueTokenValidator(properties, webClientBuilder, redisTemplate);
        };
    }

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.oauth2.enabled", havingValue = "true", matchIfMissing = true)
    public OAuth2AuthenticationFilter oAuth2AuthenticationFilter(
        OAuth2GatewayProperties properties,
        TokenValidator tokenValidator) {
        log.info("Initializing OAuth2 Authentication Filter with type: {}", properties.getTokenType());
        return new OAuth2AuthenticationFilter(properties, tokenValidator);
    }

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.oauth2.authorization-enabled", havingValue = "true")
    public OAuth2AuthorizationFilter oAuth2AuthorizationFilter(
        OAuth2GatewayProperties properties,
        DefaultPermissionProvider permissionProvider) {
        log.info("Initializing OAuth2 Authorization Filter with mode: {}", properties.getAuthorizationMode());
        return new OAuth2AuthorizationFilter(properties, permissionProvider);
    }

    // ==================== 熔断配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.circuitbreaker.enabled", havingValue = "true", matchIfMissing = true)
    public Resilience4jCircuitBreakerFilter circuitBreakerFilter() {
        log.info("Initializing Circuit Breaker Filter");
        return new Resilience4jCircuitBreakerFilter();
    }

    // ==================== 灰度发布配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.gray.enabled", havingValue = "true")
    public GrayReleaseFilter grayReleaseFilter(GrayReleaseProperties properties,
                                               ReactiveDiscoveryClient discoveryClient) {
        log.info("Initializing Gray Release Filter");
        return new GrayReleaseFilter(properties, discoveryClient);
    }

    // ==================== 安全防护配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.waf.enabled", havingValue = "true", matchIfMissing = true)
    public WafFilter wafFilter(WafProperties properties) {
        log.info("Initializing WAF Filter");
        return new WafFilter(properties);
    }

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.replay.enabled", havingValue = "true", matchIfMissing = true)
    public ReplayProtectionFilter replayProtectionFilter(
        ReplayProtectionProperties properties,
        ReactiveStringRedisTemplate redisTemplate) {
        log.info("Initializing Replay Protection Filter");
        return new ReplayProtectionFilter(properties, redisTemplate);
    }

    // ==================== 缓存配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.cache.enabled", havingValue = "true", matchIfMissing = true)
    public ResponseCacheFilter responseCacheFilter(CacheProperties properties) {
        log.info("Initializing Response Cache Filter");
        return new ResponseCacheFilter(properties);
    }

    // ==================== 可观测性配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.tracing.enabled", havingValue = "true", matchIfMissing = true)
    public TracingFilter tracingFilter(
        Tracer tracer,
        Propagator propagator,
        TracingProperties properties) {
        log.info("Initializing Tracing Filter");
        return new TracingFilter(tracer, propagator, properties);
    }

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.metrics.enabled", havingValue = "true", matchIfMissing = true)
    public MetricsFilter metricsFilter(
        MeterRegistry meterRegistry) {
        log.info("Initializing Metrics Filter");
        return new MetricsFilter(meterRegistry);
    }

    // ==================== 转换配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.transform.enabled", havingValue = "true", matchIfMissing = true)
    public RequestTransformFilter requestTransformFilter(TransformProperties properties) {
        log.info("Initializing Request Transform Filter");
        return new RequestTransformFilter(properties);
    }

    // ==================== 工具 Bean ====================

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "webClientBuilder")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    @Bean
    @ConditionalOnMissingBean
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(
        ReactiveRedisConnectionFactory connectionFactory) {
        return new ReactiveStringRedisTemplate(connectionFactory);
    }

    @Bean
    public GatewayRunnerWorker gatewayRunnerWorker() {
        return new GatewayRunnerWorker();
    }

    // ==================== 全局异常处理配置 ====================

    /**
     * 网关全局异常处理器
     * 覆盖 Spring Boot 默认的异常处理，返回统一的 JSON 格式错误响应
     * 优先级最高，确保最先处理异常
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler gatewayErrorWebExceptionHandler(ExceptionProperties exceptionProperties) {
        log.info("Initializing Gateway Exception Handler (devMode: {}, showStackTrace: {})",
            exceptionProperties.isDevMode(), exceptionProperties.isShowStackTrace());
        return new GatewayExceptionHandler(exceptionProperties.isDevMode(), exceptionProperties.isShowStackTrace());
    }

    /**
     * 错误属性扩展
     * 扩展 Spring Boot 默认的错误属性，添加网关特定的字段
     */
    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes(ExceptionProperties exceptionProperties) {
        log.info("Initializing Gateway Error Attributes (devMode: {})", exceptionProperties.isDevMode());
        GatewayErrorAttributes attributes = new GatewayErrorAttributes(exceptionProperties.isDevMode());
        return attributes;
    }
}
