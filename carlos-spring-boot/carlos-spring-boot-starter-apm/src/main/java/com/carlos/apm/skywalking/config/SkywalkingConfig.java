package com.carlos.apm.skywalking.config;

import com.carlos.apm.config.ApmProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SkyWalking 配置类
 * <p>
 * 提供 SkyWalking Toolkit 集成支持，包括：
 * - 分布式上下文传播
 * - 自定义追踪标签
 * - 日志关联
 *
 * @author Carlos
 * @date 2024-12-06
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnClass(TraceContext.class)
@ConditionalOnProperty(prefix = "carlos.apm.skywalking", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ApmProperties.class)
public class SkywalkingConfig {

    private final ApmProperties apmProperties;

    @PostConstruct
    public void init() {
        log.info("[Carlos APM] SkyWalking Toolkit 模块已启用，日志上报: {}",
            apmProperties.getSkywalking().isLogReportEnabled());
    }
}
