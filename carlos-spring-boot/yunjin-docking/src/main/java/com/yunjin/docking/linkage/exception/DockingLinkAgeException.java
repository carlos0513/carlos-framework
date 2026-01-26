package com.yunjin.docking.linkage.exception;

import com.yunjin.core.exception.ComponentException;
import com.yunjin.core.response.StatusCode;

/**
 * <p>
 * 音频处理引擎异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingLinkAgeException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingLinkAgeException() {
        super("Docking Link-Age component exception!");
    }

    public DockingLinkAgeException(String message) {
        super(message);
    }

    public DockingLinkAgeException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingLinkAgeException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingLinkAgeException(Throwable cause) {
        super(cause);
    }

    public DockingLinkAgeException(String message, Throwable cause) {
        super(message, cause);
    }

}
