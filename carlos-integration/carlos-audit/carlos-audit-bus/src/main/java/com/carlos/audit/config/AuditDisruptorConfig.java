package com.carlos.audit.config;

import com.carlos.audit.disruptor.AuditLogEventHandler;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.disruptor.core.DisruptorManager;
import com.carlos.disruptor.core.DisruptorTemplate;
import com.lmax.disruptor.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Disruptor 配置类（基于通用 Disruptor 框架）
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "carlos.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditDisruptorConfig {

    private final AuditProperties auditProperties;
    private final AuditLogEventHandler eventHandler;

    /**
     * 审计日志 Disruptor 模板
     */
    @Bean("auditDisruptorTemplate")
    @ConditionalOnMissingBean(name = "auditDisruptorTemplate")
    public DisruptorTemplate<AuditLogMainDTO> auditDisruptorTemplate(DisruptorManager disruptorManager) {
        AuditProperties.Disruptor disruptorProps = auditProperties.getDisruptor();

        int bufferSize = disruptorProps.getBufferSize();
        WaitStrategy waitStrategy = createWaitStrategy(disruptorProps.getWaitStrategy());

        log.info("初始化审计日志 Disruptor，bufferSize={}, waitStrategy={}",
            bufferSize, disruptorProps.getWaitStrategy());

        return disruptorManager.create(
            "audit-disruptor",
            bufferSize,
            waitStrategy,
            eventHandler
        );
    }

    /**
     * 创建等待策略
     */
    private WaitStrategy createWaitStrategy(String name) {
        if (name == null) {
            return new BlockingWaitStrategy();
        }
        return switch (name.toLowerCase()) {
            case "busy_spin", "busyspin" -> new BusySpinWaitStrategy();
            case "sleeping" -> new SleepingWaitStrategy();
            case "yielding" -> new YieldingWaitStrategy();
            case "blocking" -> new BlockingWaitStrategy();
            default -> new BlockingWaitStrategy();
        };
    }
}
