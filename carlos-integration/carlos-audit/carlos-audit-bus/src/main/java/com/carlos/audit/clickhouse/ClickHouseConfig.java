package com.carlos.audit.clickhouse;

import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Configuration
@ConditionalOnProperty(prefix = "carlos.audit.clickhouse", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ClickHouseConfig {

    @Value("${carlos.audit.clickhouse.host:localhost}")
    private String host;

    @Value("${carlos.audit.clickhouse.port:8123}")
    private int port;

    @Value("${carlos.audit.clickhouse.database:default}")
    private String database;

    @Value("${carlos.audit.clickhouse.username:default}")
    private String username;

    @Value("${carlos.audit.clickhouse.password:}")
    private String password;

    @Value("${carlos.audit.clickhouse.socket-timeout:300000}")
    private int socketTimeout;

    /**
     * ClickHouse 节点配置
     */
    @Bean
    public ClickHouseNode clickHouseNode() {
        String endpoint = String.format("http://%s:%d/%s", host, port, database);
        log.info("初始化 ClickHouse 节点: {}", endpoint);

        ClickHouseNode.Builder builder = ClickHouseNode.builder()
            .host(host)
            .port(ClickHouseProtocol.HTTP, port)
            .database(database);

        if (username != null && !username.isEmpty()) {
            builder.addOption("user", username);
        }
        if (password != null && !password.isEmpty()) {
            builder.addOption("password", password);
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
