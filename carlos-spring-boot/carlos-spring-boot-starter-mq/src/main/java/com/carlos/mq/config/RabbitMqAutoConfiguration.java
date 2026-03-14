package com.carlos.mq.config;

import com.carlos.mq.config.conditional.ConditionalOnMqType;
import com.carlos.mq.support.MqType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 自动配置类
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
@ConditionalOnProperty(prefix = "carlos.mq.rabbitmq", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMqAutoConfiguration {

    /**
     * RabbitMQ 配置类
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
    @ConditionalOnMqType(MqType.RABBITMQ)
    static class RabbitMqConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @SuppressWarnings("unchecked")
        public com.carlos.mq.client.rabbitmq.RabbitMqClient rabbitMqClient(
            ObjectProvider<?> rabbitTemplateProvider,
            ObjectProvider<?> connectionFactoryProvider,
            ObjectProvider<?> amqpAdminProvider,
            MqProperties mqProperties) {
            log.info("Creating RabbitMqClient");
            return new com.carlos.mq.client.rabbitmq.RabbitMqClient(
                rabbitTemplateProvider.getIfAvailable(),
                connectionFactoryProvider.getIfAvailable(),
                amqpAdminProvider.getIfAvailable(),
                mqProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        public com.carlos.mq.core.MqTemplate rabbitMqTemplate(
            com.carlos.mq.client.rabbitmq.RabbitMqClient rabbitMqClient) {
            log.info("Creating RabbitMqTemplate");
            return new com.carlos.mq.client.rabbitmq.RabbitMqTemplate(rabbitMqClient);
        }
    }
}
