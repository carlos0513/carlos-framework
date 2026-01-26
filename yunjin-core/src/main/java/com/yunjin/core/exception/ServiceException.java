package com.yunjin.core.exception;

import com.yunjin.core.response.StatusCode;

/**
 * 业务异常
 *
 * @author yunjin
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

    public ServiceException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}