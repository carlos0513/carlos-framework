package com.yunjin.docking.suining.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestSuiningAuthException extends DockingSuiningAuthException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestSuiningAuthException() {
        super("Voice engine request exception!");
    }

    public DockingRequestSuiningAuthException(String message) {
        super(message);
    }

    public DockingRequestSuiningAuthException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestSuiningAuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestSuiningAuthException(Throwable cause) {
        super(cause);
    }

    public DockingRequestSuiningAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
