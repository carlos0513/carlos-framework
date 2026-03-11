package com.carlos.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
@Component
@ConfigurationProperties(prefix = "carlos.message")
public class MessageProperties {

    private boolean enabled = true;

    private String consumerId = "message-consumer-1";

    private QueueProperties queue = new QueueProperties();

    private RateLimiterProperties rateLimiter = new RateLimiterProperties();

    private RetryProperties retry = new RetryProperties();

    @Data
    public static class QueueProperties {
        private String type = "redis";
        private String streamPrefix = "stream:message";
        private String consumerGroup = "message-consumer-group";
        private int consumerCount = 3;
    }

    @Data
    public static class RateLimiterProperties {
        private boolean enabled = true;
        private int defaultQps = 100;
        private Map<String, Integer> channels = new HashMap<>();
    }

    @Data
    public static class RetryProperties {
        private int maxAttempts = 3;
        private long initialInterval = 1000;
        private double multiplier = 2;
        private long maxInterval = 30000;
    }
}
