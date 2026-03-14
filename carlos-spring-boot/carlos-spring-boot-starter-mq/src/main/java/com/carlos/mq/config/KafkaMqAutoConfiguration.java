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
 * Kafka 自动配置类
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.kafka.core.KafkaTemplate")
@ConditionalOnProperty(prefix = "carlos.mq.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
public class KafkaMqAutoConfiguration {

    /**
     * Kafka 配置类
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.kafka.core.KafkaTemplate")
    @ConditionalOnMqType(MqType.KAFKA)
    static class KafkaMqConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public com.carlos.mq.client.kafka.KafkaMqClient kafkaMqClient(
            ObjectProvider<?> kafkaTemplateProvider,
            MqProperties mqProperties) {
            log.info("Creating KafkaMqClient");
            return new com.carlos.mq.client.kafka.KafkaMqClient(
                kafkaTemplateProvider.getIfAvailable(),
                mqProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        public com.carlos.mq.core.MqTemplate kafkaMqTemplate(
            com.carlos.mq.client.kafka.KafkaMqClient kafkaMqClient) {
            log.info("Creating KafkaMqTemplate");
            return new com.carlos.mq.client.kafka.KafkaMqTemplate(kafkaMqClient);
        }
    }
}
