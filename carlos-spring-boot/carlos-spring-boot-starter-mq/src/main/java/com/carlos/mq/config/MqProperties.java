package com.carlos.mq.config;

import com.carlos.mq.support.MqType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * MQ 配置属性
 * <p>
 * 配置前缀: carlos.mq
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Data
@ConfigurationProperties(prefix = "carlos.mq")
public class MqProperties {

    /**
     * 是否启用 MQ
     */
    private boolean enabled = true;

    /**
     * MQ 类型：auto(自动检测) / rabbitmq / rocketmq / kafka
     */
    private MqType type = MqType.AUTO;

    /**
     * 全局配置
     */
    private GlobalConfig global = new GlobalConfig();

    /**
     * RabbitMQ 配置
     */
    private RabbitMqProperties rabbitmq = new RabbitMqProperties();

    /**
     * RocketMQ 配置
     */
    private RocketMqProperties rocketmq = new RocketMqProperties();

    /**
     * Kafka 配置
     */
    private KafkaMqProperties kafka = new KafkaMqProperties();

    /**
     * 全局配置
     */
    @Data
    public static class GlobalConfig {

        /**
         * 默认发送超时时间
         */
        private Duration sendTimeout = Duration.ofSeconds(3);

        /**
         * 默认最大重试次数
         */
        private int maxRetryTimes = 3;

        /**
         * 是否启用消息轨迹
         */
        private boolean enableTrace = true;

        /**
         * 是否启用消息压缩
         */
        private boolean enableCompress = false;

        /**
         * 压缩阈值（字节）
         */
        private int compressThreshold = 4096;
    }
}
