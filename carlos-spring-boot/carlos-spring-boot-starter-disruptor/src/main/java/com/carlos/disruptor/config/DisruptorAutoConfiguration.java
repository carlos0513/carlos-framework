package com.carlos.disruptor.config;

import com.carlos.disruptor.core.DisruptorManager;
import com.carlos.disruptor.metrics.DisruptorHealthIndicator;
import com.carlos.disruptor.metrics.DisruptorMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Disruptor 自动配置类
 *
 * @author Carlos
 * @date 2026-03-14
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(DisruptorProperties.class)
@ConditionalOnProperty(prefix = "carlos.disruptor", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DisruptorAutoConfiguration {

    /**
     * Disruptor 监控指标
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MeterRegistry.class)
    public DisruptorMetrics disruptorMetrics(MeterRegistry meterRegistry) {
        return new DisruptorMetrics(meterRegistry);
    }

    /**
     * Disruptor 监控指标（无 Micrometer 时）
     */
    @Bean
    @ConditionalOnMissingBean
    public DisruptorMetrics disruptorMetricsWithoutMeter() {
        return new DisruptorMetrics(null);
    }

    /**
     * Disruptor 管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DisruptorManager disruptorManager(DisruptorProperties properties, DisruptorMetrics metrics) {
        if (log.isInfoEnabled()) {
            log.info("Disruptor Manager initializing. bufferSize={}, waitStrategy={}",
                properties.getBufferSize(), properties.getWaitStrategy());
        }
        return new DisruptorManager(properties, metrics);
    }

    /**
     * Disruptor 健康检查
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(HealthIndicator.class)
    public DisruptorHealthIndicator disruptorHealthIndicator(DisruptorManager disruptorManager) {
        return new DisruptorHealthIndicator(disruptorManager);
    }
}
