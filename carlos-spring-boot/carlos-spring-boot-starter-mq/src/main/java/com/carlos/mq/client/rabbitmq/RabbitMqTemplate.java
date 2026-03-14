package com.carlos.mq.client.rabbitmq;

import com.carlos.mq.core.MqMessage;
import com.carlos.mq.core.MqTemplate;
import com.carlos.mq.core.SendCallback;
import com.carlos.mq.core.SendResult;
import com.carlos.mq.support.DelayLevel;
import com.carlos.mq.support.MqType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ 模板实现
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@RequiredArgsConstructor
public class RabbitMqTemplate implements MqTemplate {

    private final RabbitMqClient rabbitMqClient;

    @Override
    public SendResult send(String topic, Object message) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .build();
        return rabbitMqClient.send(mqMessage);
    }

    @Override
    public SendResult send(String topic, String tag, Object message) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .build();
        return rabbitMqClient.send(mqMessage);
    }

    @Override
    public SendResult send(String topic, String tag, String key, Object message) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .key(key)
            .body(message)
            .build();
        return rabbitMqClient.send(mqMessage);
    }

    @Override
    public void sendAsync(String topic, Object message, SendCallback callback) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .build();
        rabbitMqClient.sendAsync(mqMessage, callback);
    }

    @Override
    public void sendAsync(String topic, String tag, Object message, SendCallback callback) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .build();
        rabbitMqClient.sendAsync(mqMessage, callback);
    }

    @Override
    public void sendOneWay(String topic, Object message) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .build();
        rabbitMqClient.sendOneWay(mqMessage);
    }

    @Override
    public void sendOneWay(String topic, String tag, Object message) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .build();
        rabbitMqClient.sendOneWay(mqMessage);
    }

    @Override
    public SendResult sendDelayed(String topic, Object message, DelayLevel delayLevel) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .delayLevel(delayLevel)
            .build();
        return rabbitMqClient.sendDelayed(mqMessage, delayLevel);
    }

    @Override
    public SendResult sendDelayed(String topic, String tag, Object message, DelayLevel delayLevel) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .delayLevel(delayLevel)
            .build();
        return rabbitMqClient.sendDelayed(mqMessage, delayLevel);
    }

    @Override
    public SendResult sendDelayed(String topic, Object message, long delayTime, TimeUnit timeUnit) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .delayTime(timeUnit.toMillis(delayTime))
            .build();
        return rabbitMqClient.sendDelayed(mqMessage, delayTime, timeUnit);
    }

    @Override
    public SendResult sendDelayed(String topic, String tag, Object message, long delayTime, TimeUnit timeUnit) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .delayTime(timeUnit.toMillis(delayTime))
            .build();
        return rabbitMqClient.sendDelayed(mqMessage, delayTime, timeUnit);
    }

    @Override
    public SendResult sendOrdered(String topic, Object message, String hashKey) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .hashKey(hashKey)
            .ordered(true)
            .build();
        return rabbitMqClient.sendOrdered(mqMessage, hashKey);
    }

    @Override
    public SendResult sendOrdered(String topic, String tag, Object message, String hashKey) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .hashKey(hashKey)
            .ordered(true)
            .build();
        return rabbitMqClient.sendOrdered(mqMessage, hashKey);
    }

    @Override
    public SendResult sendTransaction(String topic, Object message, Object arg) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .body(message)
            .transaction(true)
            .transactionArg(arg)
            .build();
        return rabbitMqClient.sendTransaction(mqMessage, arg);
    }

    @Override
    public SendResult sendTransaction(String topic, String tag, Object message, Object arg) {
        MqMessage<Object> mqMessage = MqMessage.builder()
            .topic(topic)
            .tag(tag)
            .body(message)
            .transaction(true)
            .transactionArg(arg)
            .build();
        return rabbitMqClient.sendTransaction(mqMessage, arg);
    }

    @Override
    public boolean ping() {
        return rabbitMqClient.isConnected();
    }

    @Override
    public String getMqType() {
        return MqType.RABBITMQ.getCode();
    }
}
