package com.carlos.mq.client.rabbitmq;

import com.carlos.mq.config.MqProperties;
import com.carlos.mq.config.RabbitMqProperties;
import com.carlos.mq.core.*;
import com.carlos.mq.support.DelayLevel;
import com.carlos.mq.support.MqType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ 客户端实现
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RabbitMqClient implements MqClient {

    private final Object rabbitTemplate;
    private final Object connectionFactory;
    private final Object amqpAdmin;
    private final MqProperties mqProperties;
    private final RabbitMqProperties rabbitMqProperties;
    private final ObjectMapper objectMapper;

    private final Map<String, MqMessageListener> listenerMap = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public RabbitMqClient(Object rabbitTemplate,
                          Object connectionFactory,
                          Object amqpAdmin,
                          MqProperties mqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.connectionFactory = connectionFactory;
        this.amqpAdmin = amqpAdmin;
        this.mqProperties = mqProperties;
        this.rabbitMqProperties = mqProperties.getRabbitmq();
        this.objectMapper = new ObjectMapper();

        init();
    }

    @Override
    public void init() {
        if (initialized || rabbitTemplate == null) {
            return;
        }

        try {
            // 配置消息转换器 - 使用反射调用
            Class<?> jsonConverterClass = Class.forName("org.springframework.amqp.support.converter.Jackson2JsonMessageConverter");
            Object jsonConverter = jsonConverterClass.getDeclaredConstructor().newInstance();

            rabbitTemplate.getClass().getMethod("setMessageConverter",
                    Class.forName("org.springframework.amqp.support.converter.MessageConverter"))
                .invoke(rabbitTemplate, jsonConverter);

            // 配置 Confirm 回调
            if ("correlated".equalsIgnoreCase(rabbitMqProperties.getProducer().getConfirmType())) {
                Class<?> confirmCallbackClass = Class.forName("org.springframework.amqp.rabbit.core.RabbitTemplate$ConfirmCallback");
                Object confirmCallback = java.lang.reflect.Proxy.newProxyInstance(
                    confirmCallbackClass.getClassLoader(),
                    new Class<?>[]{confirmCallbackClass},
                    (proxy, method, args) -> {
                        if ("confirm".equals(method.getName())) {
                            Object correlationData = args[0];
                            Boolean ack = (Boolean) args[1];
                            String cause = (String) args[2];
                            if (ack) {
                                log.debug("Message confirmed: {}", correlationData);
                            } else {
                                log.error("Message not confirmed: {}, cause: {}", correlationData, cause);
                            }
                        }
                        return null;
                    });
                rabbitTemplate.getClass().getMethod("setConfirmCallback", confirmCallbackClass)
                    .invoke(rabbitTemplate, confirmCallback);
            }

            // 配置 Returns 回调
            if (rabbitMqProperties.getProducer().isReturns()) {
                Class<?> returnsCallbackClass = Class.forName("org.springframework.amqp.rabbit.core.RabbitTemplate$ReturnsCallback");
                Object returnsCallback = java.lang.reflect.Proxy.newProxyInstance(
                    returnsCallbackClass.getClassLoader(),
                    new Class<?>[]{returnsCallbackClass},
                    (proxy, method, args) -> {
                        if ("returnedMessage".equals(method.getName())) {
                            Object returned = args[0];
                            log.error("Message returned: exchange={}, routingKey={}, replyCode={}, replyText={}",
                                returned.getClass().getMethod("getExchange").invoke(returned),
                                returned.getClass().getMethod("getRoutingKey").invoke(returned),
                                returned.getClass().getMethod("getReplyCode").invoke(returned),
                                returned.getClass().getMethod("getReplyText").invoke(returned));
                        }
                        return null;
                    });
                rabbitTemplate.getClass().getMethod("setReturnsCallback", returnsCallbackClass)
                    .invoke(rabbitTemplate, returnsCallback);
            }
        } catch (Exception e) {
            log.warn("Failed to initialize RabbitMQ callbacks: {}", e.getMessage());
        }

        initialized = true;
        log.info("RabbitMqClient initialized");
    }

    @Override
    public void shutdown() {
        listenerMap.clear();
        log.info("RabbitMqClient shutdown");
    }

    @Override
    public SendResult send(MqMessage<?> message) {
        if (rabbitTemplate == null) {
            return SendResult.failure(message.getMessageId(), "NOT_INITIALIZED", "RabbitTemplate not available");
        }
        try {
            String routingKey = buildRoutingKey(message.getTopic(), message.getTag());
            declareQueueAndExchange(message.getTopic());

            // 发送消息
            byte[] body = serializeMessage(message);
            Object amqpMessage = createAmqpMessage(body, message);

            Object correlationData = createCorrelationData(message.getMessageId());

            rabbitTemplate.getClass().getMethod("send", String.class, String.class,
                    Class.forName("org.springframework.amqp.core.Message"),
                    Class.forName("org.springframework.amqp.rabbit.connection.CorrelationData"))
                .invoke(rabbitTemplate, message.getTopic(), routingKey, amqpMessage, correlationData);

            message.markSent();
            return SendResult.success(message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public void sendAsync(MqMessage<?> message, SendCallback callback) {
        CompletableFuture.runAsync(() -> {
            SendResult result = send(message);
            if (result.isSuccess()) {
                callback.onSuccess(result);
            } else {
                callback.onException(result.getException());
            }
        });
    }

    @Override
    public void sendOneWay(MqMessage<?> message) {
        try {
            String routingKey = buildRoutingKey(message.getTopic(), message.getTag());
            declareQueueAndExchange(message.getTopic());

            byte[] body = serializeMessage(message);
            Object amqpMessage = createAmqpMessage(body, message);

            // 使用 convertAndSend 不等待确认
            rabbitTemplate.getClass().getMethod("convertAndSend", String.class, String.class, Object.class)
                .invoke(rabbitTemplate, message.getTopic(), routingKey, amqpMessage);
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
        try {
            String originalTopic = message.getTopic();
            String delayQueueName = originalTopic + ".delay";
            String dlxExchange = rabbitMqProperties.getConsumer().getDlx().getExchange();
            String dlxRoutingKey = originalTopic;

            // 创建延迟队列（使用死信队列实现）
            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", dlxExchange);
            args.put("x-dead-letter-routing-key", dlxRoutingKey);
            args.put("x-message-ttl", timeUnit.toMillis(delayTime));

            // 声明延迟队列
            declareQueue(delayQueueName, true, false, false, args);
            declareDlxQueue(originalTopic);

            // 发送消息到延迟队列
            byte[] body = serializeMessage(message);
            Object amqpMessage = createAmqpMessage(body, message);

            rabbitTemplate.getClass().getMethod("send", String.class, String.class,
                    Class.forName("org.springframework.amqp.core.Message"))
                .invoke(rabbitTemplate, "", delayQueueName, amqpMessage);

            message.markSent();
            return SendResult.success(message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send delayed message: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "DELAY_SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public SendResult sendOrdered(MqMessage<?> message, String hashKey) {
        try {
            String routingKey = message.getTopic() + ".ordered." + hashKey;
            declareQueueAndExchange(message.getTopic() + ".ordered");

            byte[] body = serializeMessage(message);
            Object amqpMessage = createAmqpMessage(body, message);

            rabbitTemplate.getClass().getMethod("send", String.class, String.class,
                    Class.forName("org.springframework.amqp.core.Message"))
                .invoke(rabbitTemplate, message.getTopic() + ".ordered", routingKey, amqpMessage);

            message.markSent();
            return SendResult.success(message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send ordered message: {}", message.getMessageId(), e);
            return SendResult.failure(message.getMessageId(), "ORDERED_SEND_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public SendResult sendTransaction(MqMessage<?> message, Object arg) {
        return send(message); // RabbitMQ 事务简化实现
    }

    @Override
    public void subscribe(String topic, String group, MqMessageListener listener) {
        subscribe(topic, "#", group, listener);
    }

    @Override
    public void subscribe(String topic, String tag, String group, MqMessageListener listener) {
        String listenerKey = topic + ":" + group;
        listenerMap.put(listenerKey, listener);
        declareQueueAndExchange(topic);
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
        return rabbitTemplate != null && initialized;
    }

    @Override
    public String getClientType() {
        return MqType.RABBITMQ.getCode();
    }

    /**
     * 声明队列和交换机
     */
    private void declareQueueAndExchange(String topic) {
        try {
            if (amqpAdmin == null) {
                return;
            }

            // 声明交换机
            Class<?> exchangeClass = Class.forName("org.springframework.amqp.core.TopicExchange");
            Object exchange = exchangeClass.getConstructor(String.class, boolean.class, boolean.class)
                .newInstance(topic, true, false);
            amqpAdmin.getClass().getMethod("declareExchange", Class.forName("org.springframework.amqp.core.Exchange"))
                .invoke(amqpAdmin, exchange);

            // 声明队列
            declareQueue(topic, true, false, false, null);

            // 绑定
            Class<?> bindingClass = Class.forName("org.springframework.amqp.core.Binding");
            Object binding = bindingClass.getConstructor(String.class,
                    Class.forName("org.springframework.amqp.core.Binding$DestinationType"),
                    String.class, String.class, Map.class)
                .newInstance(topic,
                    Enum.valueOf((Class<Enum>) Class.forName("org.springframework.amqp.core.Binding$DestinationType"), "QUEUE"),
                    topic, "#", null);
            amqpAdmin.getClass().getMethod("declareBinding", Class.forName("org.springframework.amqp.core.Binding"))
                .invoke(amqpAdmin, binding);
        } catch (Exception e) {
            log.warn("Failed to declare queue and exchange for topic: {}", topic, e);
        }
    }

    /**
     * 声明队列
     */
    private void declareQueue(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> args) {
        try {
            if (amqpAdmin == null) {
                return;
            }

            Class<?> queueClass = Class.forName("org.springframework.amqp.core.Queue");
            Object queue;
            if (args != null) {
                queue = queueClass.getConstructor(String.class, boolean.class, boolean.class, boolean.class, Map.class)
                    .newInstance(queueName, durable, exclusive, autoDelete, args);
            } else {
                queue = queueClass.getConstructor(String.class, boolean.class, boolean.class, boolean.class)
                    .newInstance(queueName, durable, exclusive, autoDelete);
            }
            amqpAdmin.getClass().getMethod("declareQueue", Class.forName("org.springframework.amqp.core.Queue"))
                .invoke(amqpAdmin, queue);
        } catch (Exception e) {
            log.warn("Failed to declare queue: {}", queueName, e);
        }
    }

    /**
     * 声明死信队列
     */
    private void declareDlxQueue(String originalTopic) {
        try {
            String dlxExchange = rabbitMqProperties.getConsumer().getDlx().getExchange();

            Class<?> exchangeClass = Class.forName("org.springframework.amqp.core.TopicExchange");
            Object exchange = exchangeClass.getConstructor(String.class, boolean.class, boolean.class)
                .newInstance(dlxExchange, true, false);
            amqpAdmin.getClass().getMethod("declareExchange", Class.forName("org.springframework.amqp.core.Exchange"))
                .invoke(amqpAdmin, exchange);

            declareQueue(originalTopic, true, false, false, null);

            Class<?> bindingClass = Class.forName("org.springframework.amqp.core.Binding");
            Object binding = bindingClass.getConstructor(String.class,
                    Class.forName("org.springframework.amqp.core.Binding$DestinationType"),
                    String.class, String.class, Map.class)
                .newInstance(originalTopic,
                    Enum.valueOf((Class<Enum>) Class.forName("org.springframework.amqp.core.Binding$DestinationType"), "QUEUE"),
                    dlxExchange, originalTopic, null);
            amqpAdmin.getClass().getMethod("declareBinding", Class.forName("org.springframework.amqp.core.Binding"))
                .invoke(amqpAdmin, binding);
        } catch (Exception e) {
            log.warn("Failed to declare DLX queue for topic: {}", originalTopic, e);
        }
    }

    private String buildRoutingKey(String topic, String tag) {
        if (tag == null || tag.isEmpty() || "*".equals(tag)) {
            return topic;
        }
        return topic + "." + tag;
    }

    private byte[] serializeMessage(MqMessage<?> message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
    }

    private Object createAmqpMessage(byte[] body, MqMessage<?> message) throws Exception {
        Class<?> messagePropertiesClass = Class.forName("org.springframework.amqp.core.MessageProperties");
        Object props = messagePropertiesClass.getDeclaredConstructor().newInstance();

        messagePropertiesClass.getMethod("setMessageId", String.class).invoke(props, message.getMessageId());
        messagePropertiesClass.getMethod("setContentType", String.class).invoke(props, "application/json");
        messagePropertiesClass.getMethod("setContentEncoding", String.class).invoke(props, "UTF-8");

        if (message.getHeaders() != null) {
            for (Map.Entry<String, String> entry : message.getHeaders().entrySet()) {
                messagePropertiesClass.getMethod("setHeader", String.class, Object.class)
                    .invoke(props, entry.getKey(), entry.getValue());
            }
        }

        Class<?> messageClass = Class.forName("org.springframework.amqp.core.Message");
        return messageClass.getConstructor(byte[].class, messagePropertiesClass).newInstance(body, props);
    }

    private Object createCorrelationData(String messageId) throws Exception {
        Class<?> correlationDataClass = Class.forName("org.springframework.amqp.rabbit.connection.CorrelationData");
        return correlationDataClass.getConstructor(String.class).newInstance(messageId);
    }

    public MqMessageListener getListener(String topic, String group) {
        return listenerMap.get(topic + ":" + group);
    }
}
