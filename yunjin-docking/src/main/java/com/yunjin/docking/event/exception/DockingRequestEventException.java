package com.yunjin.docking.event.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestEventException extends DockingEventException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestEventException() {
        super("Voice engine request exception!");
    }

    public DockingRequestEventException(String message) {
        super(message);
    }

    public DockingRequestEventException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestEventException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestEventException(Throwable cause) {
        super(cause);
    }

    public DockingRequestEventException(String message, Throwable cause) {
        super(message, cause);
    }

}
