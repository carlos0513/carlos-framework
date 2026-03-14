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
 * RocketMQ 自动配置类
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
@ConditionalOnProperty(prefix = "carlos.mq.rocketmq", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RocketMqAutoConfiguration {

    /**
     * RocketMQ 配置类
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
    @ConditionalOnMqType(MqType.ROCKETMQ)
    static class RocketMqConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public com.carlos.mq.client.rocketmq.RocketMqClient rocketMqClient(
            ObjectProvider<?> rocketMQTemplateProvider,
            MqProperties mqProperties) {
            log.info("Creating RocketMqClient");
            return new com.carlos.mq.client.rocketmq.RocketMqClient(
                rocketMQTemplateProvider.getIfAvailable(),
                mqProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        public com.carlos.mq.core.MqTemplate rocketMqTemplate(
            com.carlos.mq.client.rocketmq.RocketMqClient rocketMqClient) {
            log.info("Creating RocketMqTemplate");
            return new com.carlos.mq.client.rocketmq.RocketMqTemplate(rocketMqClient);
        }
    }
}
