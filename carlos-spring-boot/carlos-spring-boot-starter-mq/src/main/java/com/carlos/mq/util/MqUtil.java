package com.carlos.mq.util;

import com.carlos.mq.core.MqMessage;
import com.carlos.mq.core.MqTemplate;
import com.carlos.mq.core.SendCallback;
import com.carlos.mq.core.SendResult;
import com.carlos.mq.support.DelayLevel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MQ 工具类
 * <p>
 * 提供便捷的静态方法操作 MQ
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@Component
public class MqUtil {

    private static MqTemplate STATIC_TEMPLATE;

    @Autowired
    private MqTemplate mqTemplate;

    @PostConstruct
    public void init() {
        STATIC_TEMPLATE = mqTemplate;
        log.info("MqUtil initialized");
    }

    /**
     * 发送消息（同步）
     *
     * @param topic   主题
     * @param message 消息内容
     * @return 发送结果
     */
    public static SendResult send(String topic, Object message) {
        checkTemplate();
        return STATIC_TEMPLATE.send(topic, message);
    }

    /**
     * 发送消息（带标签）
     *
     * @param topic   主题
     * @param tag     标签
     * @param message 消息内容
     * @return 发送结果
     */
    public static SendResult send(String topic, String tag, Object message) {
        checkTemplate();
        return STATIC_TEMPLATE.send(topic, tag, message);
    }

    /**
     * 发送消息（带 Key）
     *
     * @param topic   主题
     * @param tag     标签
     * @param key     消息键
     * @param message 消息内容
     * @return 发送结果
     */
    public static SendResult send(String topic, String tag, String key, Object message) {
        checkTemplate();
        return STATIC_TEMPLATE.send(topic, tag, key, message);
    }

    /**
     * 发送延迟消息
     *
     * @param topic      主题
     * @param message    消息内容
     * @param delayLevel 延迟级别
     * @return 发送结果
     */
    public static SendResult sendDelayed(String topic, Object message, DelayLevel delayLevel) {
        checkTemplate();
        return STATIC_TEMPLATE.sendDelayed(topic, message, delayLevel);
    }

    /**
     * 发送延迟消息（自定义时间）
     *
     * @param topic     主题
     * @param message   消息内容
     * @param delayTime 延迟时间
     * @param timeUnit  时间单位
     * @return 发送结果
     */
    public static SendResult sendDelayed(String topic, Object message, long delayTime, TimeUnit timeUnit) {
        checkTemplate();
        return STATIC_TEMPLATE.sendDelayed(topic, message, delayTime, timeUnit);
    }

    /**
     * 发送顺序消息
     *
     * @param topic   主题
     * @param message 消息内容
     * @param hashKey 分片键
     * @return 发送结果
     */
    public static SendResult sendOrdered(String topic, Object message, String hashKey) {
        checkTemplate();
        return STATIC_TEMPLATE.sendOrdered(topic, message, hashKey);
    }

    /**
     * 发送事务消息
     *
     * @param topic   主题
     * @param message 消息内容
     * @param arg     事务参数
     * @return 发送结果
     */
    public static SendResult sendTransaction(String topic, Object message, Object arg) {
        checkTemplate();
        return STATIC_TEMPLATE.sendTransaction(topic, message, arg);
    }

    /**
     * 发送单向消息
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public static void sendOneWay(String topic, Object message) {
        checkTemplate();
        STATIC_TEMPLATE.sendOneWay(topic, message);
    }

    /**
     * 异步发送消息
     *
     * @param topic    主题
     * @param message  消息内容
     * @param callback 回调
     */
    public static void sendAsync(String topic, Object message, SendCallback callback) {
        checkTemplate();
        STATIC_TEMPLATE.sendAsync(topic, message, callback);
    }

    /**
     * 检查 MQ 连接状态
     *
     * @return 是否可用
     */
    public static boolean ping() {
        checkTemplate();
        return STATIC_TEMPLATE.ping();
    }

    /**
     * 获取 MQ 类型
     *
     * @return MQ 类型
     */
    public static String getMqType() {
        checkTemplate();
        return STATIC_TEMPLATE.getMqType();
    }

    /**
     * 构建消息
     *
     * @param <T>   消息体类型
     * @param topic 主题
     * @param body  消息体
     * @return MqMessageBuilder
     */
    public static <T> MqMessageBuilder<T> builder(String topic, T body) {
        return new MqMessageBuilder<>(topic, body);
    }

    /**
     * 检查模板是否初始化
     */
    private static void checkTemplate() {
        if (STATIC_TEMPLATE == null) {
            throw new IllegalStateException("MqTemplate not initialized. Please check MQ configuration.");
        }
    }

    /**
     * 消息构建器
     *
     * @param <T> 消息体类型
     */
    public static class MqMessageBuilder<T> {
        private String topic;
        private T body;
        private String tag;
        private String key;
        private DelayLevel delayLevel;
        private Long delayTime;
        private Map<String, String> headers = new HashMap<>();
        private Boolean ordered = false;
        private String hashKey;
        private Boolean transaction = false;

        public MqMessageBuilder(String topic, T body) {
            this.topic = topic;
            this.body = body;
        }

        public MqMessageBuilder<T> tag(String tag) {
            this.tag = tag;
            return this;
        }

        public MqMessageBuilder<T> key(String key) {
            this.key = key;
            return this;
        }

        public MqMessageBuilder<T> delayLevel(DelayLevel delayLevel) {
            this.delayLevel = delayLevel;
            return this;
        }

        public MqMessageBuilder<T> delayTime(long delayTime) {
            this.delayTime = delayTime;
            return this;
        }

        public MqMessageBuilder<T> header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public MqMessageBuilder<T> ordered(boolean ordered) {
            this.ordered = ordered;
            return this;
        }

        public MqMessageBuilder<T> hashKey(String hashKey) {
            this.hashKey = hashKey;
            return this;
        }

        public MqMessageBuilder<T> transaction(boolean transaction) {
            this.transaction = transaction;
            return this;
        }

        public MqMessage<T> build() {
            MqMessage<T> message = new MqMessage<>();
            message.setTopic(topic);
            message.setBody(body);
            message.setTag(tag);
            message.setKey(key);
            message.setDelayLevel(delayLevel);
            message.setDelayTime(delayTime);
            message.setHeaders(headers);
            message.setOrdered(ordered);
            message.setHashKey(hashKey);
            message.setTransaction(transaction);
            return message;
        }

        public SendResult send() {
            return MqUtil.send(topic, build());
        }
    }
}
