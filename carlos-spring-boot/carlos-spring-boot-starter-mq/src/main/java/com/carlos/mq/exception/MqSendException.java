package com.carlos.mq.exception;

/**
 * MQ 发送异常
 *
 * @author Carlos
 * @date 2026/3/14
 */
public class MqSendException extends MqException {

    private final String messageId;
    private final String topic;

    public MqSendException(String message) {
        super(message);
        this.messageId = null;
        this.topic = null;
    }

    public MqSendException(String message, Throwable cause) {
        super(message, cause);
        this.messageId = null;
        this.topic = null;
    }

    public MqSendException(String messageId, String topic, String message, Throwable cause) {
        super("Failed to send message [%s] to topic [%s]: %s".formatted(messageId, topic, message), cause);
        this.messageId = messageId;
        this.topic = topic;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getTopic() {
        return topic;
    }
}
