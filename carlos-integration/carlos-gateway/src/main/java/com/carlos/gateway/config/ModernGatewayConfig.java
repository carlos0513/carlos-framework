package com.carlos.gateway.config;

import com.carlos.gateway.cache.CacheProperties;
import com.carlos.gateway.circuitbreaker.Resilience4jCircuitBreakerFilter;
import com.carlos.gateway.gray.GrayReleaseFilter;
import com.carlos.gateway.gray.GrayReleaseProperties;
import com.carlos.gateway.oauth2.*;
import com.carlos.gateway.observability.TracingFilter;
import com.carlos.gateway.observability.TracingProperties;
import com.carlos.gateway.ratelimit.RedisRateLimiter;
import com.carlos.gateway.security.ReplayProtectionFilter;
import com.carlos.gateway.security.ReplayProtectionProperties;
import com.carlos.gateway.security.WafFilter;
import com.carlos.gateway.security.WafProperties;
import com.carlos.gateway.transform.RequestTransformFilter;
import com.carlos.gateway.transform.TransformProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    OAuth2GatewayProperties.class,
    GrayReleaseProperties.class,
    WafProperties.class,
    ReplayProtectionProperties.class,
    CacheProperties.class,
    TracingProperties.class,
    TransformProperties.class
})
public class ModernGatewayConfig {

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
        com.carlos.gateway.oauth2.DefaultPermissionProvider permissionProvider) {
        log.info("Initializing OAuth2 Authorization Filter with mode: {}", properties.getAuthorizationMode());
        return new OAuth2AuthorizationFilter(properties, permissionProvider);
    }

    // ==================== 限流配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.ratelimit.enabled", havingValue = "true", matchIfMissing = true)
    public RedisRateLimiter redisRateLimiter(ReactiveStringRedisTemplate redisTemplate) {
        log.info("Initializing Redis Rate Limiter");
        return new RedisRateLimiter(redisTemplate);
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
    public com.carlos.gateway.cache.ResponseCacheFilter responseCacheFilter(CacheProperties properties) {
        log.info("Initializing Response Cache Filter");
        return new com.carlos.gateway.cache.ResponseCacheFilter(properties);
    }

    // ==================== 可观测性配置 ====================

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.tracing.enabled", havingValue = "true", matchIfMissing = true)
    public TracingFilter tracingFilter(
        io.micrometer.tracing.Tracer tracer,
        io.micrometer.tracing.propagation.Propagator propagator,
        TracingProperties properties) {
        log.info("Initializing Tracing Filter");
        return new TracingFilter(tracer, propagator, properties);
    }

    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.metrics.enabled", havingValue = "true", matchIfMissing = true)
    public com.carlos.gateway.observability.MetricsFilter metricsFilter(
        io.micrometer.core.instrument.MeterRegistry meterRegistry) {
        log.info("Initializing Metrics Filter");
        return new com.carlos.gateway.observability.MetricsFilter(meterRegistry);
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
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(
        org.springframework.data.redis.connection.ReactiveRedisConnectionFactory connectionFactory) {
        return new ReactiveStringRedisTemplate(connectionFactory);
    }
}
