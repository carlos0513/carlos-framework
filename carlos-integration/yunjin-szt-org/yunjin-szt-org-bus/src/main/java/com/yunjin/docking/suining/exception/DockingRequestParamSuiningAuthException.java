package com.yunjin.docking.suining.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamSuiningAuthException extends DockingSuiningAuthException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamSuiningAuthException() {
        super("engine param exception!");
    }

    public DockingRequestParamSuiningAuthException(String message) {
        super(message);
    }

    public DockingRequestParamSuiningAuthException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamSuiningAuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamSuiningAuthException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamSuiningAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
