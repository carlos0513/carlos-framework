package com.carlos.message.exception;


import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 消息发送异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/10
 */
public class MessageSendException extends com.carlos.message.exception.MessageClientException {

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
