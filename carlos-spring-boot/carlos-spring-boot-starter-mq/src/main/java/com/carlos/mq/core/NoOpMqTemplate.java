package com.carlos.mq.core;

import com.carlos.mq.support.DelayLevel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 空操作 MQ Template
 * <p>
 * 用于当没有 MQ 实现时提供默认实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
public class NoOpMqTemplate implements MqTemplate {

    @Override
    public SendResult send(String topic, Object message) {
        log.warn("NoOpMqTemplate: send operation not supported");
        return SendResult.failure(null, "NO_OP", "MQ not configured");
    }

    @Override
    public SendResult send(String topic, String tag, Object message) {
        return send(topic, message);
    }

    @Override
    public SendResult send(String topic, String tag, String key, Object message) {
        return send(topic, message);
    }

    @Override
    public void sendAsync(String topic, Object message, SendCallback callback) {
        log.warn("NoOpMqTemplate: sendAsync operation not supported");
        if (callback != null) {
            callback.onException(new UnsupportedOperationException("MQ not configured"));
        }
    }

    @Override
    public void sendAsync(String topic, String tag, Object message, SendCallback callback) {
        sendAsync(topic, message, callback);
    }

    @Override
    public void sendOneWay(String topic, Object message) {
        log.warn("NoOpMqTemplate: sendOneWay operation not supported");
    }

    @Override
    public void sendOneWay(String topic, String tag, Object message) {
        sendOneWay(topic, message);
    }

    @Override
    public SendResult sendDelayed(String topic, Object message, DelayLevel delayLevel) {
        return send(topic, message);
    }

    @Override
    public SendResult sendDelayed(String topic, String tag, Object message, DelayLevel delayLevel) {
        return send(topic, message);
    }

    @Override
    public SendResult sendDelayed(String topic, Object message, long delayTime, TimeUnit timeUnit) {
        return send(topic, message);
    }

    @Override
    public SendResult sendDelayed(String topic, String tag, Object message, long delayTime, TimeUnit timeUnit) {
        return send(topic, message);
    }

    @Override
    public SendResult sendOrdered(String topic, Object message, String hashKey) {
        return send(topic, message);
    }

    @Override
    public SendResult sendOrdered(String topic, String tag, Object message, String hashKey) {
        return send(topic, message);
    }

    @Override
    public SendResult sendTransaction(String topic, Object message, Object arg) {
        return send(topic, message);
    }

    @Override
    public SendResult sendTransaction(String topic, String tag, Object message, Object arg) {
        return send(topic, message);
    }

    @Override
    public boolean ping() {
        return false;
    }

    @Override
    public String getMqType() {
        return "none";
    }
}
