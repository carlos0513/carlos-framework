package com.carlos.audit.config;

import com.carlos.audit.clickhouse.ClickHouseBatchWriter;
import com.carlos.audit.clickhouse.ClickHouseHealthIndicator;
import com.carlos.audit.service.AuditLogQueryService;
import com.carlos.audit.service.impl.AuditLogQueryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * 审计日志自动配置类
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "carlos.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditLogQueryService auditLogQueryService() {
        return new AuditLogQueryServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "carlos.audit.clickhouse", name = "enabled", havingValue = "true", matchIfMissing = true)
    public HealthIndicator clickHouseHealthIndicator(ClickHouseBatchWriter batchWriter) {
        return new ClickHouseHealthIndicator(batchWriter);
    }
}
