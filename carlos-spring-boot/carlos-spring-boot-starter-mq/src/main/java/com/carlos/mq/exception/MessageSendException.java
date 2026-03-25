package com.carlos.mq.exception;


import com.carlos.core.response.ErrorCode;

public class MessageSendException extends MessageException {

    public MessageSendException() {
        super("消息发送异常！");
    }

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MessageSendException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageSendException(Throwable cause) {
        super(cause);
    }

    public MessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
