package com.carlos.message.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 消息客户端自动配置
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "carlos.message.client", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MessageClientProperties.class)
@ComponentScan("com.carlos.message.api")
public class MessageClientAutoConfiguration {

    public MessageClientAutoConfiguration() {
        log.info("MessageClientAutoConfiguration initialized");
    }
}
