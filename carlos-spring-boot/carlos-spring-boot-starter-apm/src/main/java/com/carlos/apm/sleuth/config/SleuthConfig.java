package com.carlos.apm.sleuth.config;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation;
import brave.sampler.Sampler;
import com.carlos.apm.config.ApmProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sleuth 追踪配置类
 * <p>
 * 配置 Micrometer Tracing with Brave 实现分布式追踪
 *
 * @author Carlos
 * @date 2024-12-06
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnClass(Tracer.class)
@ConditionalOnProperty(prefix = "carlos.apm.sleuth", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ApmProperties.class)
public class SleuthConfig {

    private final ApmProperties apmProperties;

    @PostConstruct
    public void init() {
        log.info("[Carlos APM] Sleuth 追踪模块已启用，采样率: {}",
            apmProperties.getSleuth().getProbability());
    }

    /**
     * 配置采样器
     */
    @Bean
    @ConditionalOnMissingBean
    public Sampler sleuthSampler() {
        double probability = apmProperties.getSleuth().getProbability();
        if (probability >= 1.0) {
            return Sampler.create(1.0f);
        } else if (probability <= 0.0) {
            return Sampler.create(0.0f);
        }
        return Sampler.create((float) probability);
    }

    /**
     * 配置传播类型 - B3 格式
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "carlos.apm.sleuth", name = "propagation-type", havingValue = "B3", matchIfMissing = true)
    public Propagation.Factory b3PropagationFactory() {
        return B3Propagation.FACTORY;
    }

    /**
     * 自定义 Tracing 配置
     */
    @Bean
    @ConditionalOnMissingBean
    public Tracing braveTracing(Sampler sampler) {
        return Tracing.newBuilder()
            .sampler(sampler)
            .build();
    }
}
