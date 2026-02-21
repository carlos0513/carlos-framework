package com.yunjin.docking.yjai.exception;

import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 元景大模型异常父类
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingYjAIException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingYjAIException() {
        super("Docking YjAI component exception!");
    }

    public DockingYjAIException(String message) {
        super(message);
    }

    public DockingYjAIException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingYjAIException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingYjAIException(Throwable cause) {
        super(cause);
    }

    public DockingYjAIException(String message, Throwable cause) {
        super(message, cause);
    }

}
