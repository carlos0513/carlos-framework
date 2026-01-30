package com.carlos.docking.dingtalk.exception;

import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamDingtalkException extends DockingDingtalkException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamDingtalkException() {
        super("engine param exception!");
    }

    public DockingRequestParamDingtalkException(String message) {
        super(message);
    }

    public DockingRequestParamDingtalkException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamDingtalkException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamDingtalkException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamDingtalkException(String message, Throwable cause) {
        super(message, cause);
    }

}
