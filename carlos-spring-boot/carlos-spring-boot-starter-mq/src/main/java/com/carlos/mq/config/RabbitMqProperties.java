package com.carlos.mq.config;

import lombok.Data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * RabbitMQ 配置属性
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Data
public class RabbitMqProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 服务器地址
     */
    private String host = "localhost";

    /**
     * 端口
     */
    private int port = 5672;

    /**
     * 用户名
     */
    private String username = "guest";

    /**
     * 密码
     */
    private String password = "guest";

    /**
     * 虚拟主机
     */
    private String virtualHost = "/";

    /**
     * 连接超时时间
     */
    private Duration connectionTimeout = Duration.ofSeconds(60);

    /**
     * 请求心跳
     */
    private Duration requestedHeartbeat = Duration.ofSeconds(60);

    /**
     * 生产者配置
     */
    private ProducerConfig producer = new ProducerConfig();

    /**
     * 消费者配置
     */
    private ConsumerConfig consumer = new ConsumerConfig();

    /**
     * 交换机配置列表
     */
    private List<ExchangeConfig> exchanges = new ArrayList<>();

    /**
     * 队列配置列表
     */
    private List<QueueConfig> queues = new ArrayList<>();

    /**
     * 绑定配置列表
     */
    private List<BindingConfig> bindings = new ArrayList<>();

    /**
     * 生产者配置
     */
    @Data
    public static class ProducerConfig {

        /**
         * 确认模式：none / simple / correlated
         */
        private String confirmType = "correlated";

        /**
         * 是否启用返回回调
         */
        private boolean returns = true;

        /**
         * 重试次数
         */
        private int retryAttempts = 3;

        /**
         * 重试间隔
         */
        private Duration retryInterval = Duration.ofMillis(1000);
    }

    /**
     * 消费者配置
     */
    @Data
    public static class ConsumerConfig {

        /**
         * 并发消费者数
         */
        private int concurrency = 1;

        /**
         * 最大并发消费者数
         */
        private int maxConcurrency = 10;

        /**
         * 每次预取数量
         */
        private int prefetch = 1;

        /**
         * 是否自动确认
         */
        private boolean autoAck = false;

        /**
         * 重试配置
         */
        private RetryConfig retry = new RetryConfig();

        /**
         * 死信队列配置
         */
        private DlxConfig dlx = new DlxConfig();
    }

    /**
     * 重试配置
     */
    @Data
    public static class RetryConfig {

        /**
         * 是否启用重试
         */
        private boolean enabled = true;

        /**
         * 最大重试次数
         */
        private int maxAttempts = 3;

        /**
         * 初始间隔
         */
        private Duration initialInterval = Duration.ofMillis(1000);

        /**
         * 间隔乘数
         */
        private double multiplier = 1.0;

        /**
         * 最大间隔
         */
        private Duration maxInterval = Duration.ofSeconds(10);
    }

    /**
     * 死信队列配置
     */
    @Data
    public static class DlxConfig {

        /**
         * 是否启用死信队列
         */
        private boolean enabled = true;

        /**
         * 死信交换机名称
         */
        private String exchange = "dlx.exchange";

        /**
         * 死信队列名称前缀
         */
        private String queuePrefix = "dlx.queue";

        /**
         * 死信路由键前缀
         */
        private String routingKeyPrefix = "dlx.routing";
    }

    /**
     * 交换机配置
     */
    @Data
    public static class ExchangeConfig {

        /**
         * 交换机名称
         */
        private String name;

        /**
         * 类型：direct / topic / fanout / headers
         */
        private String type = "direct";

        /**
         * 是否持久化
         */
        private boolean durable = true;

        /**
         * 是否自动删除
         */
        private boolean autoDelete = false;

        /**
         * 其他参数
         */
        private java.util.Map<String, Object> arguments;
    }

    /**
     * 队列配置
     */
    @Data
    public static class QueueConfig {

        /**
         * 队列名称
         */
        private String name;

        /**
         * 是否持久化
         */
        private boolean durable = true;

        /**
         * 是否排他
         */
        private boolean exclusive = false;

        /**
         * 是否自动删除
         */
        private boolean autoDelete = false;

        /**
         * 消息 TTL（毫秒）
         */
        private Long messageTtl;

        /**
         * 队列长度限制
         */
        private Long maxLength;

        /**
         * 死信交换机
         */
        private String deadLetterExchange;

        /**
         * 死信路由键
         */
        private String deadLetterRoutingKey;

        /**
         * 其他参数
         */
        private java.util.Map<String, Object> arguments;
    }

    /**
     * 绑定配置
     */
    @Data
    public static class BindingConfig {

        /**
         * 队列名称
         */
        private String queue;

        /**
         * 交换机名称
         */
        private String exchange;

        /**
         * 路由键
         */
        private String routingKey;

        /**
         * 其他参数
         */
        private java.util.Map<String, Object> arguments;
    }
}
