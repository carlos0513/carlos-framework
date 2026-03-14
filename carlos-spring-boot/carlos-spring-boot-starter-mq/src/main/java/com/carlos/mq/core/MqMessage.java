package com.carlos.mq.core;

import com.carlos.mq.support.DelayLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 统一消息实体
 * <p>
 * 封装所有 MQ 类型的通用消息格式
 * </p>
 *
 * @param <T> 消息体类型
 * @author Carlos
 * @date 2026/3/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息唯一ID
     */
    @Builder.Default
    private String messageId = generateMessageId();

    /**
     * 消息主题/队列
     */
    private String topic;

    /**
     * 消息标签（RocketMQ 支持）
     */
    private String tag;

    /**
     * 消息键（用于查询和去重）
     */
    private String key;

    /**
     * 消息体
     */
    private T body;

    /**
     * 消息头
     */
    @Builder.Default
    private Map<String, String> headers = new HashMap<>();

    /**
     * 延迟级别
     */
    private DelayLevel delayLevel;

    /**
     * 自定义延迟时间（毫秒）
     */
    private Long delayTime;

    /**
     * 消息创建时间戳
     */
    @Builder.Default
    private Long createTime = System.currentTimeMillis();

    /**
     * 消息发送时间戳
     */
    private Long sendTime;

    /**
     * 消息消费时间戳
     */
    private Long consumeTime;

    /**
     * 重试次数
     */
    @Builder.Default
    private Integer retryTimes = 0;

    /**
     * 最大重试次数
     */
    private Integer maxRetryTimes;

    /**
     * 是否顺序消息
     */
    @Builder.Default
    private Boolean ordered = false;

    /**
     * 顺序消息分片键
     */
    private String hashKey;

    /**
     * 是否事务消息
     */
    @Builder.Default
    private Boolean transaction = false;

    /**
     * 事务参数
     */
    private Object transactionArg;

    /**
     * 消息来源（用于追踪）
     */
    private String source;

    /**
     * 生成消息ID
     *
     * @return 消息ID
     */
    public static String generateMessageId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 添加消息头
     *
     * @param key   键
     * @param value 值
     * @return this
     */
    public MqMessage<T> addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * 获取消息头
     *
     * @param key 键
     * @return 值
     */
    public String getHeader(String key) {
        return this.headers != null ? this.headers.get(key) : null;
    }

    /**
     * 标记消息已发送
     *
     * @return this
     */
    public MqMessage<T> markSent() {
        this.sendTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 标记消息已消费
     *
     * @return this
     */
    public MqMessage<T> markConsumed() {
        this.consumeTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 增加重试次数
     *
     * @return this
     */
    public MqMessage<T> incrementRetry() {
        if (this.retryTimes == null) {
            this.retryTimes = 0;
        }
        this.retryTimes++;
        return this;
    }

    /**
     * 判断是否超过最大重试次数
     *
     * @return 是否超过
     */
    public boolean isRetryExhausted() {
        if (maxRetryTimes == null || maxRetryTimes <= 0) {
            return false;
        }
        return retryTimes != null && retryTimes >= maxRetryTimes;
    }

    /**
     * 获取消息消费耗时（毫秒）
     *
     * @return 耗时，如果未消费返回 -1
     */
    public long getConsumeDuration() {
        if (sendTime == null || consumeTime == null) {
            return -1;
        }
        return consumeTime - sendTime;
    }
}
