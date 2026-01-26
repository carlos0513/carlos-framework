package com.yunjin.mq.exception;


import com.yunjin.core.response.StatusCode;

public class MessageListenException extends MessageException {

    public MessageListenException() {
        super("消息监听异常！");
    }

    public MessageListenException(String message) {
        super(message);
    }

    public MessageListenException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public MessageListenException(StatusCode statusCode) {
        super(statusCode);
    }

    public MessageListenException(Throwable cause) {
        super(cause);
    }

    public MessageListenException(String message, Throwable cause) {
        super(message, cause);
    }
}
