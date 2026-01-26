package com.yunjin.docking.rzt.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamRztException extends DockingRztException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamRztException() {
        super("engine param exception!");
    }

    public DockingRequestParamRztException(String message) {
        super(message);
    }

    public DockingRequestParamRztException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamRztException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamRztException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamRztException(String message, Throwable cause) {
        super(message, cause);
    }

}
