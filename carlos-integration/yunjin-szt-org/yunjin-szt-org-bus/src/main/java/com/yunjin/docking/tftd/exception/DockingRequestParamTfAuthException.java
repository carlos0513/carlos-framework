package com.yunjin.docking.tftd.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamTfAuthException extends DockingTfAuthException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamTfAuthException() {
        super("engine param exception!");
    }

    public DockingRequestParamTfAuthException(String message) {
        super(message);
    }

    public DockingRequestParamTfAuthException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamTfAuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamTfAuthException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamTfAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
