package com.carlos.core.exception;

import com.carlos.core.response.ErrorCode;

/**
 * 业务异常父类
 *
 * @author carlos
 * @date 2020-09-20 00:06:26
 */
public class RestException extends GlobalException {

    private static final long serialVersionUID = -2303357122330162359L;

    public RestException() {
        super("接口层处理异常");
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);
    }


    public RestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RestException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }


    public RestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public RestException(Throwable cause) {
        super(cause);
    }
}
