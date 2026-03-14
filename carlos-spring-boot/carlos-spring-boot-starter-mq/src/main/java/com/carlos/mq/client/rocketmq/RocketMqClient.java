package com.carlos.mq.client.rocketmq;

import com.carlos.mq.config.MqProperties;
import com.carlos.mq.config.RocketMqProperties;
import com.carlos.mq.core.*;
import com.carlos.mq.support.DelayLevel;
import com.carlos.mq.support.MqType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * RocketMQ 客户端实现
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RocketMqClient implements MqClient {

    private final Object rocketMQTemplate;
    private final MqProperties mqProperties;
    private final RocketMqProperties rocketMqProperties;
    private final ObjectMapper objectMapper;

    private final Map<String, MqMessageListener> listenerMap = new ConcurrentHashMap<>();
    private final Map<String, MqTransactionListener> transactionListenerMap = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public RocketMqClient(Object rocketMQTemplate, MqProperties mqProperties) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.mqProperties = mqProperties;
        this.rocketMqProperties = mqProperties.getRocketmq();
        this.objectMapper = new ObjectMapper();

        init();
    }

    @Override
    public void init() {
        if (initialized || rocketMQTemplate == null) {
            return;
        }

        initialized = true;
        log.info("RocketMqClient initialized");
    }

    @Override
    public void shutdown() {
        listenerMap.clear();
        transactionListenerMap.clear();
        log.info("RocketMqClient shutdown");
    }

    @Override
    public SendResult send(MqMessage<?> message) {
        if (rocketMQTemplate == null) {
            return SendResult.failure(message.getMessageId(), "NOT_INITIALIZED", "RocketMQTemplate not available");
        }
        try {
            Message<?> springMessage = buildSpringMessage(message);

            Object result = rocketMQTemplate.getClass()
                .getMethod("syncSend", String.class, Message.class, long.class)
                .invoke(rocketMQTemplate, buildDestination(message.getTopic(), message.getTag()),
                    springMessage, rocketMqProperties.getProducer().getSendTimeout().toMillis());

            message.markSent();
            return convertSendResult(result, message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send message to RocketMQ: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public void sendAsync(MqMessage<?> message, SendCallback callback) {
        try {
            Message<?> springMessage = buildSpringMessage(message);

            Class<?> sendCallbackClass = Class.forName("org.apache.rocketmq.client.producer.SendCallback");
            Object rocketCallback = java.lang.reflect.Proxy.newProxyInstance(
                sendCallbackClass.getClassLoader(),
                new Class<?>[]{sendCallbackClass},
                (proxy, method, args) -> {
                    if ("onSuccess".equals(method.getName())) {
                        callback.onSuccess(convertSendResult(args[0], message.getMessageId()));
                    } else if ("onException".equals(method.getName())) {
                        callback.onException((Throwable) args[0]);
                    }
                    return null;
                });

            rocketMQTemplate.getClass()
                .getMethod("asyncSend", String.class, Message.class, sendCallbackClass, long.class)
                .invoke(rocketMQTemplate, buildDestination(message.getTopic(), message.getTag()),
                    springMessage, rocketCallback, rocketMqProperties.getProducer().getSendTimeout().toMillis());
        } catch (Exception e) {
            log.error("Failed to send async message: {}", message.getMessageId(), e);
            callback.onException(e);
        }
    }

    @Override
    public void sendOneWay(MqMessage<?> message) {
        try {
            Message<?> springMessage = buildSpringMessage(message);
            rocketMQTemplate.getClass()
                .getMethod("sendOneWay", String.class, Message.class)
                .invoke(rocketMQTemplate, buildDestination(message.getTopic(), message.getTag()), springMessage);
        } catch (Exception e) {
            log.error("Failed to send one-way message: {}", message.getMessageId(), e);
        }
    }

    @Override
    public SendResult sendDelayed(MqMessage<?> message, DelayLevel delayLevel) {
        try {
            Message<?> springMessage = buildSpringMessage(message);
            Object result = rocketMQTemplate.getClass()
                .getMethod("syncSendDelayTimeSeconds", String.class, Message.class, int.class)
                .invoke(rocketMQTemplate, buildDestination(message.getTopic(), message.getTag()),
                    springMessage, (int) (delayLevel.toMillis() / 1000));

            message.markSent();
            return convertSendResult(result, message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send delayed message: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "DELAY_SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public SendResult sendDelayed(MqMessage<?> message, long delayTime, TimeUnit timeUnit) {
        DelayLevel delayLevel = DelayLevel.fromMillis(timeUnit.toMillis(delayTime));
        return sendDelayed(message, delayLevel);
    }

    @Override
    public SendResult sendOrdered(MqMessage<?> message, String hashKey) {
        try {
            Message<?> springMessage = buildSpringMessage(message);
            Object result = rocketMQTemplate.getClass()
                .getMethod("syncSendOrderly", String.class, Message.class, String.class, long.class)
                .invoke(rocketMQTemplate, buildDestination(message.getTopic(), message.getTag()),
                    springMessage, hashKey, rocketMqProperties.getProducer().getSendTimeout().toMillis());

            message.markSent();
            return convertSendResult(result, message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send ordered message: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "ORDERED_SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public SendResult sendTransaction(MqMessage<?> message, Object arg) {
        try {
            Message<?> springMessage = buildSpringMessage(message);
            Object result = rocketMQTemplate.getClass()
                .getMethod("sendMessageInTransaction", String.class, String.class, Message.class, Object.class)
                .invoke(rocketMQTemplate, rocketMqProperties.getProducer().getGroup(),
                    buildDestination(message.getTopic(), message.getTag()), springMessage, arg);

            message.markSent();
            return convertTransactionSendResult(result, message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send transaction message: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "TRANSACTION_SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public void subscribe(String topic, String group, MqMessageListener listener) {
        subscribe(topic, "*", group, listener);
    }

    @Override
    public void subscribe(String topic, String tag, String group, MqMessageListener listener) {
        String listenerKey = topic + ":" + group;
        listenerMap.put(listenerKey, listener);
        log.info("Subscribed to topic: {}, tag: {}, group: {}", topic, tag, group);
    }

    @Override
    public void unsubscribe(String topic, String group) {
        String listenerKey = topic + ":" + group;
        listenerMap.remove(listenerKey);
        log.info("Unsubscribed from topic: {}, group: {}", topic, group);
    }

    @Override
    public boolean isConnected() {
        return rocketMQTemplate != null && initialized;
    }

    @Override
    public String getClientType() {
        return MqType.ROCKETMQ.getCode();
    }

    public void registerTransactionListener(String topic, MqTransactionListener transactionListener) {
        transactionListenerMap.put(topic, transactionListener);
    }

    private Message<?> buildSpringMessage(MqMessage<?> message) {
        MessageBuilder<?> builder = MessageBuilder.withPayload(message.getBody())
            .setHeader("messageId", message.getMessageId())
            .setHeader("topic", message.getTopic())
            .setHeader("tag", message.getTag())
            .setHeader("key", message.getKey())
            .setHeader("createTime", message.getCreateTime());

        if (message.getHeaders() != null) {
            message.getHeaders().forEach(builder::setHeader);
        }

        return builder.build();
    }

    private String buildDestination(String topic, String tag) {
        if (tag == null || tag.isEmpty() || "*".equals(tag)) {
            return topic;
        }
        return topic + ":" + tag;
    }

    private SendResult convertSendResult(Object result, String messageId) throws Exception {
        if (result == null) {
            return SendResult.failure(messageId, "NULL_RESULT", "Send result is null");
        }

        Class<?> resultClass = result.getClass();
        Object sendStatus = resultClass.getMethod("getSendStatus").invoke(result);
        boolean success = sendStatus != null && "SEND_OK".equals(sendStatus.toString());

        SendResult.SendResultBuilder builder = SendResult.builder()
            .success(success)
            .messageId(messageId)
            .sendTime(System.currentTimeMillis())
            .code(sendStatus != null ? sendStatus.toString() : "UNKNOWN");

        try {
            Object msgId = resultClass.getMethod("getMsgId").invoke(result);
            builder.message((String) msgId);
        } catch (Exception ignored) {
        }

        try {
            Object messageQueue = resultClass.getMethod("getMessageQueue").invoke(result);
            if (messageQueue != null) {
                Object queueId = messageQueue.getClass().getMethod("getQueueId").invoke(messageQueue);
                builder.queueId((Integer) queueId);
            }
        } catch (Exception ignored) {
        }

        try {
            Object queueOffset = resultClass.getMethod("getQueueOffset").invoke(result);
            builder.queueOffset((Long) queueOffset);
        } catch (Exception ignored) {
        }

        try {
            Object regionId = resultClass.getMethod("getRegionId").invoke(result);
            builder.regionId((String) regionId);
        } catch (Exception ignored) {
        }

        return builder.build();
    }

    private SendResult convertTransactionSendResult(Object result, String messageId) throws Exception {
        if (result == null) {
            return SendResult.failure(messageId, "NULL_RESULT", "Send result is null");
        }

        Class<?> resultClass = result.getClass();
        Object sendStatus = resultClass.getMethod("getSendStatus").invoke(result);
        boolean success = sendStatus != null && "SEND_OK".equals(sendStatus.toString());

        Object transactionId = resultClass.getMethod("getTransactionId").invoke(result);

        return SendResult.builder()
            .success(success)
            .messageId(messageId)
            .sendTime(System.currentTimeMillis())
            .code(sendStatus != null ? sendStatus.toString() : "UNKNOWN")
            .transactionId((String) transactionId)
            .build();
    }

    public MqMessageListener getListener(String topic, String group) {
        return listenerMap.get(topic + ":" + group);
    }
}
