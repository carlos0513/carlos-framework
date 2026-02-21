package com.carlos.message.exception;


import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 消息中心异常
 * </p>
 *
 * @author Carlos
 * @date 2022/1/12
 */
public class MessageException extends ComponentException {

    public MessageException() {
        super("消息中心异常！");
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
