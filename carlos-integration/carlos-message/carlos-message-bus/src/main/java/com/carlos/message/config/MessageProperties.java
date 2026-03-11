package com.carlos.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 消息中心配置属性
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@ConfigurationProperties(prefix = "carlos.message")
public class MessageProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 消费者ID
     */
    private String consumerId = "message-consumer-1";

    /**
     * 队列配置
     */
    private QueueProperties queue = new QueueProperties();

    /**
     * 限流配置
     */
    private RateLimiterProperties rateLimiter = new RateLimiterProperties();

    /**
     * 重试配置
     */
    private RetryProperties retry = new RetryProperties();

    /**
     * 队列配置
     */
    @Data
    public static class QueueProperties {
        /**
         * 队列类型
         */
        private String type = "redis";

        /**
         * Stream前缀
         */
        private String streamPrefix = "stream:message";

        /**
         * 消费者组
         */
        private String consumerGroup = "message-consumer-group";

        /**
         * 消费者数量
         */
        private int consumerCount = 3;
    }

    /**
     * 限流配置
     */
    @Data
    public static class RateLimiterProperties {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 默认限流QPS
         */
        private int defaultQps = 100;

        /**
         * 渠道限流配置
         */
        private Map<String, Integer> channels = new HashMap<>();
    }

    /**
     * 重试配置
     */
    @Data
    public static class RetryProperties {
        /**
         * 最大重试次数
         */
        private int maxAttempts = 3;

        /**
         * 初始间隔（毫秒）
         */
        private long initialInterval = 1000;

        /**
         * 倍数
         */
        private double multiplier = 2;

        /**
         * 最大间隔（毫秒）
         */
        private long maxInterval = 30000;
    }
}
