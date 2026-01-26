package com.yunjin.apm.exception;


import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

/**
 * 组件异常父类
 *
 * @author yunjin
 * @date 2020-09-20 00:06:26
 */
public class ApmException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public ApmException() {
        super("Apm组件异常！");
    }

    public ApmException(String message) {
        super(message);
    }


    public ApmException(StatusCode statusCode) {
        super(statusCode);
    }

    public ApmException(Throwable cause) {
        super(cause);
    }

    public ApmException(String message, Throwable cause) {
        super(message, cause);
    }
}
