package com.yunjin.docking.yjai.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamYjAIException extends DockingYjAIException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamYjAIException() {
        super("engine param exception!");
    }

    public DockingRequestParamYjAIException(String message) {
        super(message);
    }

    public DockingRequestParamYjAIException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamYjAIException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamYjAIException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamYjAIException(String message, Throwable cause) {
        super(message, cause);
    }

}
