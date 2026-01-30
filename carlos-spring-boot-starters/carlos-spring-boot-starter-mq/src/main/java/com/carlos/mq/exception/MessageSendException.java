package com.carlos.mq.exception;


import com.carlos.core.response.StatusCode;

public class MessageSendException extends MessageException {

    public MessageSendException() {
        super("消息发送异常！");
    }

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public MessageSendException(StatusCode statusCode) {
        super(statusCode);
    }

    public MessageSendException(Throwable cause) {
        super(cause);
    }

    public MessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
