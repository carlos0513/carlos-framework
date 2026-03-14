package com.carlos.mq.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MQ 类型枚举
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Getter
@RequiredArgsConstructor
public enum MqType {

    /**
     * 自动检测
     */
    AUTO("auto", "自动检测"),

    /**
     * RabbitMQ
     */
    RABBITMQ("rabbitmq", "RabbitMQ"),

    /**
     * RocketMQ
     */
    ROCKETMQ("rocketmq", "RocketMQ"),

    /**
     * Kafka
     */
    KAFKA("kafka", "Kafka");

    /**
     * 类型编码
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 根据编码获取类型
     *
     * @param code 编码
     * @return MQ 类型
     */
    public static MqType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return AUTO;
        }
        for (MqType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return AUTO;
    }
}
