package com.carlos.mq.config.conditional;

import com.carlos.mq.support.MqType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * MQ 类型条件判断
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
public class OnMqTypeCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnMqType.class.getName());
        if (attributes == null) {
            return false;
        }

        MqType requiredType = (MqType) attributes.get("value");
        String configuredType = context.getEnvironment().getProperty("carlos.mq.type", "auto");

        MqType actualType = MqType.fromCode(configuredType);

        // 如果是 AUTO，则需要通过类路径检测
        if (actualType == MqType.AUTO) {
            actualType = detectMqType();
        }

        boolean matches = requiredType == actualType;

        if (log.isDebugEnabled()) {
            log.debug("MQ type condition check: required={}, actual={}, matches={}",
                requiredType, actualType, matches);
        }

        return matches;
    }

    /**
     * 检测 MQ 类型
     *
     * @return 检测到的 MQ 类型
     */
    private MqType detectMqType() {
        ClassLoader classLoader = getClass().getClassLoader();

        // 优先检测 RocketMQ
        if (isClassPresent("org.apache.rocketmq.spring.core.RocketMQTemplate", classLoader)) {
            return MqType.ROCKETMQ;
        }

        // 其次检测 RabbitMQ
        if (isClassPresent("org.springframework.amqp.rabbit.core.RabbitTemplate", classLoader)) {
            return MqType.RABBITMQ;
        }

        // 最后检测 Kafka
        if (isClassPresent("org.springframework.kafka.core.KafkaTemplate", classLoader)) {
            return MqType.KAFKA;
        }

        return MqType.AUTO;
    }

    /**
     * 判断类是否存在
     *
     * @param className   类名
     * @param classLoader 类加载器
     * @return 是否存在
     */
    private boolean isClassPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
