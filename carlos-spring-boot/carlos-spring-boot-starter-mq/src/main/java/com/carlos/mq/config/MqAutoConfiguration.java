package com.carlos.mq.config;

import com.carlos.mq.core.MqTemplate;
import com.carlos.mq.support.MqType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * MQ 自动配置类
 * <p>
 * 根据配置自动装配 MQ 相关组件
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MqProperties.class)
@ConditionalOnProperty(prefix = "carlos.mq", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({
    RabbitMqAutoConfiguration.class,
    RocketMqAutoConfiguration.class,
    KafkaMqAutoConfiguration.class
})
public class MqAutoConfiguration {

    @Autowired
    private MqProperties mqProperties;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        MqType mqType = mqProperties.getType();
        log.info("Carlos MQ module initialized, type: {}", mqType);

        if (mqType == MqType.AUTO) {
            MqType detectedType = com.carlos.mq.config.MqClientSelector.detectMqType();
            log.info("Auto-detected MQ type: {}", detectedType);
        }
    }

    /**
     * 默认的 MqTemplate
     * <p>
     * 当没有其他 MqTemplate 实现时创建
     * </p>
     *
     * @return MqTemplate
     */
    @Bean
    @ConditionalOnMissingBean(MqTemplate.class)
    @ConditionalOnProperty(prefix = "carlos.mq", name = "type", havingValue = "auto")
    public MqTemplate mqTemplate() {
        MqType detectedType = MqClientSelector.detectMqType();

        if (detectedType == MqType.AUTO) {
            log.warn("No MQ implementation detected, creating no-op template");
            return new com.carlos.mq.core.NoOpMqTemplate();
        }

        // 实际实现由各个子配置类提供
        throw new IllegalStateException(
            "MQ implementation for type " + detectedType + " not found. " +
                "Please check your configuration.");
    }
}
