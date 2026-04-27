package com.carlos.gateway.filter;

import com.carlos.gateway.cache.CacheProperties;
import com.carlos.gateway.cache.ResponseCacheFilter;
import com.carlos.gateway.config.GatewayProperties;
import com.carlos.gateway.gray.GrayReleaseFilter;
import com.carlos.gateway.gray.GrayReleaseProperties;
import com.carlos.gateway.observability.MetricsFilter;
import com.carlos.gateway.security.ReplayProtectionFilter;
import com.carlos.gateway.security.ReplayProtectionProperties;
import com.carlos.gateway.security.WafFilter;
import com.carlos.gateway.security.WafProperties;
import com.carlos.gateway.transform.RequestTransformFilter;
import com.carlos.gateway.transform.TransformProperties;
import com.carlos.gateway.whitelist.UnifiedWhitelistFilter;
import com.carlos.gateway.whitelist.UnifiedWhitelistProperties;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

/**
 * 网关过滤器自动配置类 - 性能优化版
 * <p>
 * 负责过滤器相关的 Bean 配置：
 * - 统一白名单过滤器（最先执行，避免重复检查）
 * - 路径前缀过滤器
 * - WAF 过滤器
 * - 防重放攻击过滤器
 * - 请求转换过滤器
 * - 灰度发布过滤器
 * - 指标收集过滤器
 * - 响应缓存过滤器
 *
 * @author carlos
 * @date 2026/3/27
 * @updated 2026/4/10 添加统一白名单过滤器
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
    UnifiedWhitelistProperties.class
})
public class FilterAutoConfiguration {

    // ==================== 统一白名单过滤器（最先执行）====================

    /**
     * 统一白名单过滤器
     * 最先执行，统一检查所有类型的白名单，将结果存入 Exchange Attributes
     * 后续过滤器直接从 Attributes 读取，避免重复检查
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.whitelist.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public UnifiedWhitelistFilter unifiedWhitelistFilter(UnifiedWhitelistProperties properties) {
        log.info("Initializing Unified Whitelist Filter (optimized path matching)");
        return new UnifiedWhitelistFilter(properties);
    }

    // ==================== 路径处理过滤器 ====================

    /**
     * 路径前缀过滤器
     * 统一处理网关接口路径前缀
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.prefix-enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public PathPrefixFilter pathPrefixFilter(GatewayProperties gatewayProperties) {
        log.info("Initializing Path Prefix Filter with prefix: {}", gatewayProperties.getPrefix());
        return new PathPrefixFilter(gatewayProperties);
    }

    /**
     * 选择性路径前缀剥离过滤器工厂
     * 用于处理 Swagger/Knife4j 文档路径的前缀剥离
     */
    @Bean
    @ConditionalOnMissingBean
    public SelectStripPrefixGatewayFilterFactory selectStripPrefixGatewayFilterFactory() {
        log.info("Initializing Select Strip Prefix Gateway Filter Factory (Optimized with Caffeine Cache)");
        return new SelectStripPrefixGatewayFilterFactory();
    }

    // ==================== 安全过滤器 ====================

    /**
     * WAF 过滤器（性能优化版）
     * 防护：SQL 注入、XSS、CSRF、路径遍历等
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.waf.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public WafFilter wafFilter(WafProperties wafProperties) {
        log.info("Initializing WAF Filter (Optimized with ThreadLocal Matcher)");
        return new WafFilter(wafProperties);
    }

    /**
     * 防重放攻击过滤器（性能优化版）
     * 基于请求签名和时间戳验证
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.replay.enabled", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    public ReplayProtectionFilter replayProtectionFilter(
        ReplayProtectionProperties properties,
        ReactiveStringRedisTemplate redisTemplate) {
        log.info("Initializing Replay Protection Filter (Optimized with ThreadLocal Mac)");
        return new ReplayProtectionFilter(properties, redisTemplate);
    }

    // ==================== 转换过滤器 ====================

    /**
     * 请求转换过滤器（性能优化版）
     * 支持：API 版本控制、路径重写、Header 转换
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.transform.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public RequestTransformFilter requestTransformFilter(TransformProperties properties) {
        log.info("Initializing Request Transform Filter (Optimized with Pattern Cache)");
        return new RequestTransformFilter(properties);
    }

    // ==================== 灰度发布过滤器 ====================

    /**
     * 灰度发布过滤器（性能优化版）
     * 支持基于用户、IP、权重的灰度策略
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.gray.enabled", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    @ConditionalOnBean(ReactiveDiscoveryClient.class)
    public GrayReleaseFilter grayReleaseFilter(
        GrayReleaseProperties properties,
        ReactiveDiscoveryClient discoveryClient) {
        log.info("Initializing Gray Release Filter (Optimized with MurmurHash3)");
        return new GrayReleaseFilter(properties, discoveryClient);
    }

    // ==================== 可观测性过滤器 ====================

    /**
     * 指标收集过滤器（性能优化版）
     * 基于 Micrometer 实现 Prometheus 指标暴露
     */
    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    @ConditionalOnMissingBean
    public MetricsFilter metricsFilter(MeterRegistry meterRegistry) {
        log.info("Initializing Metrics Filter (Optimized with Counter Cache)");
        return new MetricsFilter(meterRegistry);
    }

    // ==================== 缓存过滤器 ====================

    /**
     * 响应缓存过滤器（性能优化版）
     * 两级缓存：本地 Caffeine + Redis
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.cache.enabled", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    public ResponseCacheFilter responseCacheFilter(CacheProperties properties) {
        log.info("Initializing Response Cache Filter (Optimized with MurmurHash3)");
        return new ResponseCacheFilter(properties);
    }
}
