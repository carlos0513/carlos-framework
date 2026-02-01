package com.carlos.redis.exception;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * 缓存异常父类
 *
 * @author carlos
 * @date 2020-09-20 00:06:26
 */
public class CacheException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public CacheException() {
        super("redis exception!");
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public CacheException(StatusCode statusCode) {
        super(statusCode);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
