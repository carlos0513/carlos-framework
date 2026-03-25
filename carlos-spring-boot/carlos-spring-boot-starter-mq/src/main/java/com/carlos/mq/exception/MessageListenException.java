package com.carlos.mq.exception;


import com.carlos.core.response.ErrorCode;

public class MessageListenException extends MessageException {

    public MessageListenException() {
        super("消息监听异常！");
    }

    public MessageListenException(String message) {
        super(message);
    }

    public MessageListenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MessageListenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageListenException(Throwable cause) {
        super(cause);
    }

    public MessageListenException(String message, Throwable cause) {
        super(message, cause);
    }
}
