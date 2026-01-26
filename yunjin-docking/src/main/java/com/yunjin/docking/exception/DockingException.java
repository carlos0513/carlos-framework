package com.yunjin.docking.exception;

import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 音频处理引擎异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingException() {
        super("rxt api exception!");
    }

    public DockingException(String message) {
        super(message);
    }

    public DockingException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingException(Throwable cause) {
        super(cause);
    }

    public DockingException(String message, Throwable cause) {
        super(message, cause);
    }

}
