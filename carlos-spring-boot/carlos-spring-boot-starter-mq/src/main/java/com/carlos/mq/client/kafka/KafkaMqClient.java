package com.carlos.mq.client.kafka;

import com.carlos.mq.config.KafkaMqProperties;
import com.carlos.mq.config.MqProperties;
import com.carlos.mq.core.*;
import com.carlos.mq.support.DelayLevel;
import com.carlos.mq.support.MqType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Kafka 客户端实现
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@SuppressWarnings("unchecked")
public class KafkaMqClient implements MqClient {

    private final Object kafkaTemplate;
    private final MqProperties mqProperties;
    private final KafkaMqProperties kafkaMqProperties;
    private final ObjectMapper objectMapper;

    private final Map<String, MqMessageListener> listenerMap = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public KafkaMqClient(Object kafkaTemplate, MqProperties mqProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.mqProperties = mqProperties;
        this.kafkaMqProperties = mqProperties.getKafka();
        this.objectMapper = new ObjectMapper();

        init();
    }

    @Override
    public void init() {
        if (initialized || kafkaTemplate == null) {
            return;
        }

        initialized = true;
        log.info("KafkaMqClient initialized");
    }

    @Override
    public void shutdown() {
        listenerMap.clear();
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.getClass().getMethod("destroy").invoke(kafkaTemplate);
            } catch (Exception e) {
                log.warn("Failed to destroy KafkaTemplate: {}", e.getMessage());
            }
        }
        log.info("KafkaMqClient shutdown");
    }

    @Override
    public SendResult send(MqMessage<?> message) {
        if (kafkaTemplate == null) {
            return SendResult.failure(message.getMessageId(), "NOT_INITIALIZED", "KafkaTemplate not available");
        }
        try {
            String key = message.getKey() != null ? message.getKey() : message.getMessageId();
            String value = serializeMessage(message);

            Object record = createProducerRecord(message.getTopic(), key, value, message);

            Object future = kafkaTemplate.getClass()
                .getMethod("send", Class.forName("org.apache.kafka.clients.producer.ProducerRecord"))
                .invoke(kafkaTemplate, record);

            Object result = future.getClass().getMethod("get", long.class, TimeUnit.class)
                .invoke(future, kafkaMqProperties.getProducer().getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);

            message.markSent();
            return convertSendResult(result, message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send message to Kafka: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public void sendAsync(MqMessage<?> message, SendCallback callback) {
        try {
            String key = message.getKey() != null ? message.getKey() : message.getMessageId();
            String value = serializeMessage(message);

            Object record = createProducerRecord(message.getTopic(), key, value, message);

            Object future = kafkaTemplate.getClass()
                .getMethod("send", Class.forName("org.apache.kafka.clients.producer.ProducerRecord"))
                .invoke(kafkaTemplate, record);

            // 添加回调
            Object successCallback = createListenableFutureCallback(
                result -> {
                    try {
                        callback.onSuccess(convertSendResult(result, message.getMessageId()));
                    } catch (Exception e) {
                        callback.onException(e);
                    }
                },
                ex -> callback.onException(ex)
            );

            future.getClass().getMethod("addCallback",
                    Class.forName("org.springframework.util.concurrent.SuccessCallback"),
                    Class.forName("org.springframework.util.concurrent.FailureCallback"))
                .invoke(future, successCallback, successCallback);
        } catch (Exception e) {
            log.error("Failed to send async message: {}", message.getMessageId(), e);
            callback.onException(e);
        }
    }

    @Override
    public void sendOneWay(MqMessage<?> message) {
        try {
            String key = message.getKey() != null ? message.getKey() : message.getMessageId();
            String value = serializeMessage(message);

            kafkaTemplate.getClass()
                .getMethod("send", String.class, Object.class, Object.class)
                .invoke(kafkaTemplate, message.getTopic(), key, value);
        } catch (Exception e) {
            log.error("Failed to send one-way message: {}", message.getMessageId(), e);
        }
    }

    @Override
    public SendResult sendDelayed(MqMessage<?> message, DelayLevel delayLevel) {
        return sendDelayed(message, delayLevel.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public SendResult sendDelayed(MqMessage<?> message, long delayTime, TimeUnit timeUnit) {
        CompletableFuture.delayedExecutor(delayTime, timeUnit).execute(() -> send(message));
        message.markSent();
        return SendResult.success(message.getMessageId());
    }

    @Override
    public SendResult sendOrdered(MqMessage<?> message, String hashKey) {
        try {
            String value = serializeMessage(message);
            Object record = createProducerRecordWithTimestamp(message.getTopic(), hashKey,
                System.currentTimeMillis(), value, message);

            Object future = kafkaTemplate.getClass()
                .getMethod("send", Class.forName("org.apache.kafka.clients.producer.ProducerRecord"))
                .invoke(kafkaTemplate, record);

            Object result = future.getClass().getMethod("get", long.class, TimeUnit.class)
                .invoke(future, kafkaMqProperties.getProducer().getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);

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
            String value = serializeMessage(message);

            // 使用 Kafka 事务发送
            Object result = kafkaTemplate.getClass()
                .getMethod("executeInTransaction", Class.forName("org.springframework.kafka.core.KafkaOperations$OperationsCallback"))
                .invoke(kafkaTemplate, (org.springframework.kafka.core.KafkaOperations.OperationsCallback<?, ?, ?>) ops -> {
                    try {
                        ops.send(message.getTopic(), message.getKey(), value);
                        return true;
                    } catch (Exception e) {
                        log.error("Failed in transaction", e);
                        return false;
                    }
                });

            message.markSent();
            return SendResult.success(message.getMessageId());
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
        return kafkaTemplate != null && initialized;
    }

    @Override
    public String getClientType() {
        return MqType.KAFKA.getCode();
    }

    private String serializeMessage(MqMessage<?> message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }

    private Object createProducerRecord(String topic, String key, String value, MqMessage<?> message) throws Exception {
        Class<?> recordClass = Class.forName("org.apache.kafka.clients.producer.ProducerRecord");
        Object record = recordClass.getConstructor(String.class, String.class, String.class)
            .newInstance(topic, key, value);

        // 添加消息头
        if (message.getHeaders() != null) {
            for (Map.Entry<String, String> entry : message.getHeaders().entrySet()) {
                recordClass.getMethod("headers").invoke(record)
                    .getClass().getMethod("add", String.class, byte[].class)
                    .invoke(recordClass.getMethod("headers").invoke(record),
                        entry.getKey(), entry.getValue().getBytes(StandardCharsets.UTF_8));
            }
        }

        return record;
    }

    private Object createProducerRecordWithTimestamp(String topic, String key, long timestamp, String value, MqMessage<?> message) throws Exception {
        Class<?> recordClass = Class.forName("org.apache.kafka.clients.producer.ProducerRecord");
        // 使用带时间戳的构造方法
        return recordClass.getConstructor(String.class, Integer.class, Long.class, String.class, String.class)
            .newInstance(topic, null, timestamp, key, value);
    }

    private SendResult convertSendResult(Object result, String messageId) throws Exception {
        if (result == null) {
            return SendResult.failure(messageId, "NULL_RESULT", "Send result is null");
        }

        Class<?> resultClass = result.getClass();
        Object recordMetadata = resultClass.getMethod("getRecordMetadata").invoke(result);

        if (recordMetadata == null) {
            return SendResult.failure(messageId, "NULL_METADATA", "Record metadata is null");
        }

        Class<?> metadataClass = recordMetadata.getClass();
        String topic = (String) metadataClass.getMethod("topic").invoke(recordMetadata);
        Integer partition = (Integer) metadataClass.getMethod("partition").invoke(recordMetadata);
        Long offset = (Long) metadataClass.getMethod("offset").invoke(recordMetadata);

        return SendResult.builder()
            .success(true)
            .messageId(messageId)
            .topic(topic)
            .sendTime(System.currentTimeMillis())
            .code("SUCCESS")
            .message("Send success")
            .queueId(partition)
            .queueOffset(offset)
            .build();
    }

    private Object createListenableFutureCallback(java.util.function.Consumer<Object> onSuccess,
                                                  java.util.function.Consumer<Throwable> onFailure) throws Exception {
        Class<?> callbackClass = Class.forName("org.springframework.util.concurrent.ListenableFutureCallback");
        return java.lang.reflect.Proxy.newProxyInstance(
            callbackClass.getClassLoader(),
            new Class<?>[]{callbackClass},
            (proxy, method, args) -> {
                if ("onSuccess".equals(method.getName())) {
                    onSuccess.accept(args[0]);
                } else if ("onFailure".equals(method.getName())) {
                    onFailure.accept((Throwable) args[0]);
                }
                return null;
            });
    }

    public MqMessageListener getListener(String topic, String group) {
        return listenerMap.get(topic + ":" + group);
    }
}
