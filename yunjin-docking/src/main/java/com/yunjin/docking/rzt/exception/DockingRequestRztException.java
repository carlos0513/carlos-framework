package com.yunjin.docking.rzt.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestRztException extends DockingRztException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestRztException() {
        super("Voice engine request exception!");
    }

    public DockingRequestRztException(String message) {
        super(message);
    }

    public DockingRequestRztException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestRztException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestRztException(Throwable cause) {
        super(cause);
    }

    public DockingRequestRztException(String message, Throwable cause) {
        super(message, cause);
    }

}
