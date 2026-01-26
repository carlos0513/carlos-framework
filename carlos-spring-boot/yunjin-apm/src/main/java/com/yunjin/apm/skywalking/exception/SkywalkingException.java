package com.yunjin.apm.skywalking.exception;


import com.yunjin.apm.exception.ApmException;
import com.yunjin.core.response.StatusCode;

/**
 * 组件异常父类
 *
 * @author yunjin
 * @date 2020-09-20 00:06:26
 */
public class SkywalkingException extends ApmException {

    private static final long serialVersionUID = -2303357122330162359L;

    public SkywalkingException() {
        super("Apm组件异常！");
    }

    public SkywalkingException(String message) {
        super(message);
    }


    public SkywalkingException(StatusCode statusCode) {
        super(statusCode);
    }

    public SkywalkingException(Throwable cause) {
        super(cause);
    }

    public SkywalkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
