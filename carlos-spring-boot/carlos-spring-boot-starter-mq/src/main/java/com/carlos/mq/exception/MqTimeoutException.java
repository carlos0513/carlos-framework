package com.carlos.mq.exception;

import java.time.Duration;

/**
 * MQ 超时异常
 *
 * @author Carlos
 * @date 2026/3/14
 */
public class MqTimeoutException extends MqException {

    private final Duration timeout;
    private final String operation;

    public MqTimeoutException(String message) {
        super(message);
        this.timeout = null;
        this.operation = null;
    }

    public MqTimeoutException(String operation, Duration timeout) {
        super(String.format("MQ operation [%s] timeout after %d ms", operation, timeout.toMillis()));
        this.operation = operation;
        this.timeout = timeout;
    }

    public MqTimeoutException(String operation, Duration timeout, Throwable cause) {
        super(String.format("MQ operation [%s] timeout after %d ms", operation, timeout.toMillis()), cause);
        this.operation = operation;
        this.timeout = timeout;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getOperation() {
        return operation;
    }
}
