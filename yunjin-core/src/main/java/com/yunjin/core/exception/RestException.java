package com.yunjin.core.exception;

import com.yunjin.core.response.StatusCode;

/**
 * 业务异常父类
 *
 * @author yunjin
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

    public RestException(StatusCode statusCode) {
        super(statusCode);
    }

    public RestException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public RestException(Throwable cause) {
        super(cause);
    }
}
