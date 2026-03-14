package com.carlos.mq.annotation;

import com.carlos.mq.support.ConsumeMode;

import java.lang.annotation.*;

/**
 * MQ 消息监听注解
 * <p>
 * 用于标注方法作为消息监听处理器
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqListener {

    /**
     * 主题/队列名称
     * <p>
     * 支持多个主题，用逗号分隔
     * </p>
     */
    String[] topic();

    /**
     * 标签（用于消息过滤）
     * <p>
     * - RocketMQ: 支持 Tag 过滤，如 "tag1||tag2" 或 "*"<br>
     * - RabbitMQ: 作为路由键使用<br>
     * - Kafka: 暂不支持标签过滤
     * </p>
     */
    String tag() default "*";

    /**
     * 消费组
     * <p>
     * 如果不指定，将使用默认消费组
     * </p>
     */
    String group() default "";

    /**
     * 消费模式
     * <p>
     * - CLUSTER: 集群模式（默认），同一条消息只被一个消费者消费<br>
     * - BROADCAST: 广播模式，同一条消息被所有消费者消费
     * </p>
     */
    ConsumeMode consumeMode() default ConsumeMode.CLUSTER;

    /**
     * 最大重试次数
     * <p>
     * 默认使用全局配置
     * </p>
     */
    int maxRetryTimes() default -1;

    /**
     * 是否顺序消费
     */
    boolean ordered() default false;

    /**
     * 并发线程数（消费者数）
     */
    int concurrency() default 1;

    /**
     * 是否自动提交偏移量（Kafka 有效）
     */
    boolean autoCommit() default false;

    /**
     * 消费者 ID（用于区分同一消费组内的不同消费者）
     */
    String consumerId() default "";
}
