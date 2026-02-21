package com.yunjin.docking.jct.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎参数异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestParamLJAppAggrException extends DockingLJAppAggrException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestParamLJAppAggrException() {
        super("param exception!");
    }

    public DockingRequestParamLJAppAggrException(String message) {
        super(message);
    }

    public DockingRequestParamLJAppAggrException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestParamLJAppAggrException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestParamLJAppAggrException(Throwable cause) {
        super(cause);
    }

    public DockingRequestParamLJAppAggrException(String message, Throwable cause) {
        super(message, cause);
    }

}
