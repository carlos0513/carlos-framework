package com.carlos.docking.event.exception;

import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamEventException extends DockingEventException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamEventException() {
        super("engine param exception!");
    }

    public DockingRequestParamEventException(String message) {
        super(message);
    }

    public DockingRequestParamEventException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamEventException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamEventException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamEventException(String message, Throwable cause) {
        super(message, cause);
    }

}
