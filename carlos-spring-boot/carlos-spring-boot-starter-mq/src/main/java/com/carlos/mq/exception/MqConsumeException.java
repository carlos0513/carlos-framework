package com.carlos.mq.exception;

/**
 * MQ 消费异常
 *
 * @author Carlos
 * @date 2026/3/14
 */
public class MqConsumeException extends MqException {

    private final String messageId;
    private final String topic;
    private final boolean retryable;

    public MqConsumeException(String message) {
        super(message);
        this.messageId = null;
        this.topic = null;
        this.retryable = true;
    }

    public MqConsumeException(String message, boolean retryable) {
        super(message);
        this.messageId = null;
        this.topic = null;
        this.retryable = retryable;
    }

    public MqConsumeException(String message, Throwable cause) {
        super(message, cause);
        this.messageId = null;
        this.topic = null;
        this.retryable = true;
    }

    public MqConsumeException(String messageId, String topic, String message, Throwable cause, boolean retryable) {
        super("Failed to consume message [%s] from topic [%s]: %s".formatted(messageId, topic, message), cause);
        this.messageId = messageId;
        this.topic = topic;
        this.retryable = retryable;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getTopic() {
        return topic;
    }

    /**
     * 是否可重试
     *
     * @return true 表示可以重试，false 表示不可重试
     */
    public boolean isRetryable() {
        return retryable;
    }
}
