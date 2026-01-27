package com.carlos.snowflake.exception;


import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * 缓存异常父类
 *
 * @author yunjin
 * @date 2020-09-20 00:06:26
 */
public class SnowflakeException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public SnowflakeException() {
        super("snowflake exception!");
    }

    public SnowflakeException(String message) {
        super(message);
    }

    public SnowflakeException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public SnowflakeException(StatusCode statusCode) {
        super(statusCode);
    }

    public SnowflakeException(Throwable cause) {
        super(cause);
    }

    public SnowflakeException(String message, Throwable cause) {
        super(message, cause);
    }
}
