package com.yunjin.docking.linkage.exception;

import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestLinkAgeException extends DockingLinkAgeException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestLinkAgeException() {
        super("Voice engine request exception!");
    }

    public DockingRequestLinkAgeException(String message) {
        super(message);
    }

    public DockingRequestLinkAgeException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestLinkAgeException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestLinkAgeException(Throwable cause) {
        super(cause);
    }

    public DockingRequestLinkAgeException(String message, Throwable cause) {
        super(message, cause);
    }

}
