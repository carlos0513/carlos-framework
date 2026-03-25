package com.carlos.mq.exception;


import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.ErrorCode;

public class MessageException extends ComponentException {

    public MessageException() {
        super("消息组件异常！");
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MessageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
