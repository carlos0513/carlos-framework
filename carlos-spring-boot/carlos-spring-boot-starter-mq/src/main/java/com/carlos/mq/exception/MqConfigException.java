package com.carlos.mq.exception;

/**
 * MQ 配置异常
 *
 * @author Carlos
 * @date 2026/3/14
 */
public class MqConfigException extends MqException {

    public MqConfigException(String message) {
        super(message);
    }

    public MqConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqConfigException(String configKey, String expected, String actual) {
        super(String.format("Invalid MQ configuration [%s]: expected [%s] but got [%s]", configKey, expected, actual));
    }
}
