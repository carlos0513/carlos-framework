package com.yunjin.docking.tftd.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestTfAuthException extends DockingTfAuthException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestTfAuthException() {
        super("Voice engine request exception!");
    }

    public DockingRequestTfAuthException(String message) {
        super(message);
    }

    public DockingRequestTfAuthException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestTfAuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestTfAuthException(Throwable cause) {
        super(cause);
    }

    public DockingRequestTfAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
