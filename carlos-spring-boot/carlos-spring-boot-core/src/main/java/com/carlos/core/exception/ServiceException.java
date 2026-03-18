package com.carlos.core.exception;

import com.carlos.core.response.StatusCode;

/**
 * 业务异常
 *
 * @author carlos
 */
public final class ServiceException extends GlobalException {

    public ServiceException() {
        super("业务处理异常！");
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(StatusCode statusCode) {
        super(statusCode);
    }

    public ServiceException(StatusCode statusCode, String message) {
        super(statusCode.getCode(), message);
    }

    public ServiceException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}