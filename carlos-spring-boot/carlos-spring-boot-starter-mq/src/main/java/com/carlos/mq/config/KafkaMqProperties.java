package com.carlos.mq.config;

import lombok.Data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kafka 配置属性
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Data
public class KafkaMqProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * Bootstrap 服务器地址
     */
    private List<String> bootstrapServers = new ArrayList<>();

    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 生产者配置
     */
    private ProducerConfig producer = new ProducerConfig();

    /**
     * 消费者配置
     */
    private ConsumerConfig consumer = new ConsumerConfig();

    /**
     * 额外属性
     */
    private Map<String, String> properties = new HashMap<>();

    public KafkaMqProperties() {
        bootstrapServers.add("localhost:9092");
    }

    /**
     * 生产者配置
     */
    @Data
    public static class ProducerConfig {

        /**
         * ACK 模式：0 / 1 / all
         */
        private String acks = "all";

        /**
         * 重试次数
         */
        private int retries = 3;

        /**
         * 批量大小（字节）
         */
        private int batchSize = 16384;

        /**
         * 缓冲区大小（字节）
         */
        private int bufferMemory = 33554432;

        /**
         * 键序列化器
         */
        private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

        /**
         * 值序列化器
         */
        private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

        /**
         * 压缩类型：none / gzip / snappy / lz4 / zstd
         */
        private String compressionType = "none";

        /**
         * 连接最大空闲时间
         */
        private Duration connectionsMaxIdle = Duration.ofMinutes(9);

        /**
         * 请求超时时间
         */
        private Duration requestTimeout = Duration.ofSeconds(30);

        /**
         * 元数据最大空闲时间
         */
        private Duration metadataMaxIdle = Duration.ofMinutes(5);

        /**
         * 额外属性
         */
        private Map<String, String> properties = new HashMap<>();
    }

    /**
     * 消费者配置
     */
    @Data
    public static class ConsumerConfig {

        /**
         * 消费者组 ID
         */
        private String groupId = "default-consumer-group";

        /**
         * 自动偏移重置：earliest / latest / none
         */
        private String autoOffsetReset = "earliest";

        /**
         * 是否自动提交
         */
        private boolean enableAutoCommit = false;

        /**
         * 自动提交间隔
         */
        private Duration autoCommitInterval = Duration.ofSeconds(5);

        /**
         * 会话超时时间
         */
        private Duration sessionTimeout = Duration.ofSeconds(45);

        /**
         * 最大轮询记录数
         */
        private int maxPollRecords = 500;

        /**
         * 最大轮询间隔
         */
        private Duration maxPollInterval = Duration.ofMinutes(5);

        /**
         * 心跳间隔
         */
        private Duration heartbeatInterval = Duration.ofSeconds(3);

        /**
         * 键反序列化器
         */
        private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

        /**
         * 值反序列化器
         */
        private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

        /**
         * 隔离级别：read_uncommitted / read_committed
         */
        private String isolationLevel = "read_uncommitted";

        /**
         * 获取最小字节数
         */
        private int fetchMinSize = 1;

        /**
         * 获取最大字节数
         */
        private int fetchMaxSize = 52428800;

        /**
         * 获取最大等待时间
         */
        private Duration fetchMaxWait = Duration.ofMillis(500);

        /**
         * 额外属性
         */
        private Map<String, String> properties = new HashMap<>();
    }
}
