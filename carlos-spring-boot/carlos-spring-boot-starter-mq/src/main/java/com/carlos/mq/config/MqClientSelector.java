package com.carlos.mq.config;

import com.carlos.mq.core.MqClient;
import com.carlos.mq.support.MqType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * MQ 客户端选择器
 * <p>
 * 用于根据配置自动选择对应的 MQ 客户端
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
public class MqClientSelector {

    private static final Map<MqType, Supplier<Boolean>> MQ_DETECTORS = new ConcurrentHashMap<>();

    static {
        MQ_DETECTORS.put(MqType.ROCKETMQ, () -> ClassUtils.isPresent(
            "org.apache.rocketmq.spring.core.RocketMQTemplate",
            MqClientSelector.class.getClassLoader()));

        MQ_DETECTORS.put(MqType.RABBITMQ, () -> ClassUtils.isPresent(
            "org.springframework.amqp.rabbit.core.RabbitTemplate",
            MqClientSelector.class.getClassLoader()));

        MQ_DETECTORS.put(MqType.KAFKA, () -> ClassUtils.isPresent(
            "org.springframework.kafka.core.KafkaTemplate",
            MqClientSelector.class.getClassLoader()));
    }

    /**
     * 检测 MQ 类型
     *
     * @return 检测到的 MQ 类型
     */
    public static MqType detectMqType() {
        for (Map.Entry<MqType, Supplier<Boolean>> entry : MQ_DETECTORS.entrySet()) {
            if (entry.getValue().get()) {
                log.info("Detected MQ type: {}", entry.getKey());
                return entry.getKey();
            }
        }
        log.warn("No supported MQ implementation found in classpath");
        return MqType.AUTO;
    }

    /**
     * 检查是否支持指定 MQ 类型
     *
     * @param mqType MQ 类型
     * @return 是否支持
     */
    public static boolean isSupported(MqType mqType) {
        Supplier<Boolean> detector = MQ_DETECTORS.get(mqType);
        return detector != null && detector.get();
    }

    /**
     * 获取可用的 MQ 类型列表
     *
     * @return 可用的 MQ 类型列表
     */
    public static java.util.List<MqType> getAvailableMqTypes() {
        java.util.List<MqType> availableTypes = new java.util.ArrayList<>();
        for (Map.Entry<MqType, Supplier<Boolean>> entry : MQ_DETECTORS.entrySet()) {
            if (entry.getValue().get()) {
                availableTypes.add(entry.getKey());
            }
        }
        return availableTypes;
    }

    /**
     * 选择 MQ 客户端
     *
     * @param mqType      指定的 MQ 类型
     * @param clientMap   客户端映射
     * @return 选中的客户端
     */
    public static MqClient selectClient(MqType mqType, Map<MqType, MqClient> clientMap) {
        MqType actualType = mqType == MqType.AUTO ? detectMqType() : mqType;

        if (actualType == MqType.AUTO) {
            throw new IllegalStateException("No MQ implementation found in classpath. " +
                "Please add one of the following dependencies: " +
                "rocketmq-spring-boot-starter / spring-boot-starter-amqp / spring-kafka");
        }

        MqClient client = clientMap.get(actualType);
        if (client == null) {
            throw new IllegalStateException("MQ client not found for type: " + actualType);
        }

        log.info("Selected MQ client: {}", actualType);
        return client;
    }
}
