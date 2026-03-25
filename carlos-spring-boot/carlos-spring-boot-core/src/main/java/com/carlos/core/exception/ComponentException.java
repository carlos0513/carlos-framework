package com.carlos.core.exception;


import com.carlos.core.response.ErrorCode;

/**
 * <p>
 * 组件异常父类
 * </p>
 *
 * @author carlos
 * @date 2022/10/24 13:19
 */
public class ComponentException extends GlobalException {

    private static final long serialVersionUID = -2303357122330162359L;

    public ComponentException() {
        super("组件异常！");
    }

    public ComponentException(String message) {
        super(message);
    }

    public ComponentException(Throwable cause) {
        super(cause);
    }

    public ComponentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ComponentException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ComponentException(ErrorCode statusCode, String message) {
        super(statusCode, message);
    }

    public ComponentException(String statusCode, String message) {
        super(statusCode, message);
    }


    public ComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
