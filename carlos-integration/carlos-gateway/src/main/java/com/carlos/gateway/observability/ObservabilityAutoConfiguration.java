package com.carlos.gateway.observability;

import brave.Tracer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.propagation.Propagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关可观测性自动配置类
 * <p>
 * 负责可观测性相关的 Bean 配置：
 * - 请求链路追踪过滤器（Request ID / Trace ID）
 * - 访问日志过滤器
 * - 指标收集过滤器
 *
 * @author carlos
 * @date 2026/3/27
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
    TracingProperties.class,
    AccessLogProperties.class
})
public class ObservabilityAutoConfiguration {

    /**
     * 统一链路追踪过滤器
     * 整合 Request ID 和 Trace ID 处理
     */
    @Bean
    @ConditionalOnBean({Tracer.class, Propagator.class})
    @ConditionalOnProperty(name = "carlos.gateway.tracing.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public RequestTracingFilter requestTracingFilter(
        Tracer tracer,
        Propagator propagator,
        TracingProperties properties) {
        log.info("Initializing Request Tracing Filter");
        return new RequestTracingFilter(tracer, propagator, properties);
    }

    /**
     * 访问日志过滤器
     * 记录所有请求的访问日志
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.access-log.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public AccessLogFilter accessLogFilter() {
        log.info("Initializing Access Log Filter");
        return new AccessLogFilter();
    }

    /**
     * 指标收集过滤器
     * 基于 Micrometer 实现 Prometheus 指标暴露
     */
    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    @ConditionalOnProperty(name = "carlos.gateway.metrics.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public MetricsFilter metricsFilter(MeterRegistry meterRegistry) {
        log.info("Initializing Metrics Filter");
        return new MetricsFilter(meterRegistry);
    }
}
