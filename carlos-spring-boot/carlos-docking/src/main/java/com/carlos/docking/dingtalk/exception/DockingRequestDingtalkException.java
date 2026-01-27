package com.carlos.docking.dingtalk.exception;

import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 引擎请求发生异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingRequestDingtalkException extends DockingDingtalkException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingRequestDingtalkException() {
        super("Voice engine request exception!");
    }

    public DockingRequestDingtalkException(String message) {
        super(message);
    }

    public DockingRequestDingtalkException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingRequestDingtalkException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingRequestDingtalkException(Throwable cause) {
        super(cause);
    }

    public DockingRequestDingtalkException(String message, Throwable cause) {
        super(message, cause);
    }

}
