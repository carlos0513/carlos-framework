package com.carlos.audit.clickhouse;

import com.carlos.audit.config.AuditProperties;
import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseProtocol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * ClickHouse 配置类
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "carlos.audit.clickhouse", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ClickHouseConfig {

    private final AuditProperties auditProperties;

    /**
     * ClickHouse 节点配置
     */
    @Bean
    public ClickHouseNode clickHouseNode() {
        AuditProperties.ClickHouse clickHouse = auditProperties.getClickhouse();
        String endpoint = String.format("http://%s:%d/%s", clickHouse.getHost(), clickHouse.getPort(), clickHouse.getDatabase());
        log.info("初始化 ClickHouse 节点: {}", endpoint);

        ClickHouseNode.Builder builder = ClickHouseNode.builder()
            .host(clickHouse.getHost())
            .port(ClickHouseProtocol.HTTP, clickHouse.getPort())
            .database(clickHouse.getDatabase());

        if (clickHouse.getUsername() != null && !clickHouse.getUsername().isEmpty()) {
            builder.addOption("user", clickHouse.getUsername());
        }
        if (clickHouse.getPassword() != null && !clickHouse.getPassword().isEmpty()) {
            builder.addOption("password", clickHouse.getPassword());
        }

        return builder.build();
    }

    /**
     * ClickHouse 客户端
     */
    @Bean(destroyMethod = "close")
    public ClickHouseClient clickHouseClient() {
        log.info("初始化 ClickHouse 客户端");
        return ClickHouseClient.newInstance(ClickHouseProtocol.HTTP);
    }
}
