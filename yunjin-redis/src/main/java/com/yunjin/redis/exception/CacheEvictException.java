package com.yunjin.redis.exception;

import com.yunjin.core.response.StatusCode;

/**
 * 缓存更新异常
 *
 * @author yunjin
 * @date 2020-09-20 00:06:26
 */
public class CacheEvictException extends CacheException {

    private static final long serialVersionUID = -2303357122330162359L;

    public CacheEvictException() {
        super("cache put exception!");
    }

    public CacheEvictException(String message) {
        super(message);
    }

    public CacheEvictException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public CacheEvictException(StatusCode statusCode) {
        super(statusCode);
    }

    public CacheEvictException(Throwable cause) {
        super(cause);
    }

    public CacheEvictException(String message, Throwable cause) {
        super(message, cause);
    }
}
