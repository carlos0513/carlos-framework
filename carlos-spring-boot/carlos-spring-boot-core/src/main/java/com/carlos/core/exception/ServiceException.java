package com.carlos.core.exception;

import com.carlos.core.response.ErrorCode;

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

    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }


    public ServiceException(String errorCode, String message) {
        super(errorCode, message);
    }


    public ServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
