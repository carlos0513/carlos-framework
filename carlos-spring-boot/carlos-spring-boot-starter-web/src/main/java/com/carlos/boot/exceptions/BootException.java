package com.carlos.boot.exceptions;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.ErrorCode;

/**
 * Springboot组件异常父类
 *
 * @author carlos
 * @date 2020-09-20 00:06:26
 */
public class BootException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public BootException() {
        super("Springboot组件异常！");
    }

    public BootException(final String message) {
        super(message);
    }

    public BootException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }

    public BootException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public BootException(final Throwable cause) {
        super(cause);
    }

    public BootException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
