package com.yunjin.mq.exception;


import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

public class MessageException extends ComponentException {

    public MessageException() {
        super("消息组件异常！");
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public MessageException(StatusCode statusCode) {
        super(statusCode);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
