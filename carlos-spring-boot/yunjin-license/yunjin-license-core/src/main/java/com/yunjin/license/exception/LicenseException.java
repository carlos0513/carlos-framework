package com.yunjin.license.exception;


import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

/**
 * 缓存异常父类
 *
 * @author yunjin
 * @date 2020-09-20 00:06:26
 */
public class LicenseException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public LicenseException() {
        super("snowflake exception!");
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public LicenseException(StatusCode statusCode) {
        super(statusCode);
    }

    public LicenseException(Throwable cause) {
        super(cause);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
