package com.carlos.docking.linkage.exception;

import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamLinkAgeException extends DockingLinkAgeException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamLinkAgeException() {
        super("engine param exception!");
    }

    public DockingRequestParamLinkAgeException(String message) {
        super(message);
    }

    public DockingRequestParamLinkAgeException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamLinkAgeException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamLinkAgeException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamLinkAgeException(String message, Throwable cause) {
        super(message, cause);
    }

}
