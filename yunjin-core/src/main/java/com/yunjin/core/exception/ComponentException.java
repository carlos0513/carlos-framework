package com.yunjin.core.exception;


import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 组件异常父类
 * </p>
 *
 * @author yunjin
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

    public ComponentException(StatusCode statusCode) {
        super(statusCode);
    }

    public ComponentException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public ComponentException(Integer statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public ComponentException(Integer statusCode, String message) {
        super(statusCode, message);
    }

    public ComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
