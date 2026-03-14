package com.carlos.mq.exception;

import com.carlos.core.exception.GlobalException;

/**
 * MQ 基础异常
 *
 * @author Carlos
 * @date 2026/3/14
 */
public class MqException extends GlobalException {

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqException(Throwable cause) {
        super(cause);
    }
}
