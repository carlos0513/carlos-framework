package com.yunjin.docking.jct.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestLJAppAggrException extends DockingLJAppAggrException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestLJAppAggrException() {
        super("Voice engine request exception!");
    }

    public DockingRequestLJAppAggrException(String message) {
        super(message);
    }

    public DockingRequestLJAppAggrException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestLJAppAggrException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestLJAppAggrException(Throwable cause) {
        super(cause);
    }

    public DockingRequestLJAppAggrException(String message, Throwable cause) {
        super(message, cause);
    }

}
