package com.yunjin.docking.rzt.exception;

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
public class DockingRztException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRztException() {
        super("Docking Rzt component exception!");
    }

    public DockingRztException(String message) {
        super(message);
    }

    public DockingRztException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRztException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRztException(Throwable cause) {
        super(cause);
    }

    public DockingRztException(String message, Throwable cause) {
        super(message, cause);
    }

}
