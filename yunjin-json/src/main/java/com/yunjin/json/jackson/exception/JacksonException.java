package com.yunjin.json.jackson.exception;


import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

/**
 * 组件异常父类
 *
 * @author yunjin
 * @date 2020-09-20 00:06:26
 */
public class JacksonException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public JacksonException() {
        super("Jackson组件异常！");
    }

    public JacksonException(String message) {
        super(message);
    }


    public JacksonException(StatusCode statusCode) {
        super(statusCode);
    }

    public JacksonException(Throwable cause) {
        super(cause);
    }

    public JacksonException(String message, Throwable cause) {
        super(message, cause);
    }
}
