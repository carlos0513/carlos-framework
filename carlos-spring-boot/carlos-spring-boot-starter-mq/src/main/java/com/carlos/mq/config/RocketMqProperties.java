package com.carlos.mq.config;

import lombok.Data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ 配置属性
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Data
public class RocketMqProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * NameServer 地址
     */
    private String nameServer = "localhost:9876";

    /**
     * 访问通道：LOCAL / CLOUD
     */
    private String accessChannel = "LOCAL";

    /**
     * 生产者配置
     */
    private ProducerConfig producer = new ProducerConfig();

    /**
     * 消费者配置
     */
    private ConsumerConfig consumer = new ConsumerConfig();

    /**
     * 扩展配置
     */
    private Map<String, String> ext = new HashMap<>();

    /**
     * 生产者配置
     */
    @Data
    public static class ProducerConfig {

        /**
         * 生产者组
         */
        private String group = "default-producer-group";

        /**
         * 发送超时时间
         */
        private Duration sendTimeout = Duration.ofSeconds(3);

        /**
         * 消息最大大小（字节）
         */
        private int maxMessageSize = 1024 * 1024 * 4;

        /**
         * 同步发送失败重试次数
         */
        private int retryTimesWhenSendFailed = 2;

        /**
         * 异步发送失败重试次数
         */
        private int retryTimesWhenSendAsyncFailed = 2;

        /**
         * 是否在内部发送失败时重试另一个 Broker
         */
        private boolean retryAnotherBrokerWhenNotStoreOK = false;

        /**
         * 压缩消息阈值（字节）
         */
        private int compressMsgBodyOverHowmuch = 1024 * 4;

        /**
         * 轨迹配置
         */
        private TraceConfig trace = new TraceConfig();
    }

    /**
     * 消费者配置
     */
    @Data
    public static class ConsumerConfig {

        /**
         * 消费者组
         */
        private String group = "default-consumer-group";

        /**
         * 消费线程池最小线程数
         */
        private int consumeThreadMin = 5;

        /**
         * 消费线程池最大线程数
         */
        private int consumeThreadMax = 20;

        /**
         * 批量消费大小
         */
        private int consumeMessageBatchMaxSize = 1;

        /**
         * 批量拉取大小
         */
        private int pullBatchSize = 32;

        /**
         * 最大重消费次数
         */
        private int maxReconsumeTimes = -1;

        /**
         * 消费超时时间（分钟）
         */
        private int consumeTimeout = 15;

        /**
         * 消费模式：CLUSTERING / BROADCASTING
         */
        private String messageModel = "CLUSTERING";

        /**
         * 消费线程挂起时间（毫秒）
         */
        private long consumeThreadSuspendTimeMillis = 1000;

        /**
         * 最大消费重试延迟级别
         */
        private int maxReconsumeTimesLevel = 0;

        /**
         * 轨迹配置
         */
        private TraceConfig trace = new TraceConfig();
    }

    /**
     * 轨迹配置
     */
    @Data
    public static class TraceConfig {

        /**
         * 是否启用轨迹
         */
        private boolean enabled = true;

        /**
         * 轨迹主题
         */
        private String topic = "RMQ_SYS_TRACE_TOPIC";
    }
}
