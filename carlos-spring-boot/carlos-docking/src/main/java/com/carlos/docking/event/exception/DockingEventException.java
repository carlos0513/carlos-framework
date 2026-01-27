package com.carlos.docking.event.exception;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 音频处理引擎异常
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:12
 */
public class DockingEventException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingEventException() {
        super("Docking event component exception!");
    }

    public DockingEventException(String message) {
        super(message);
    }

    public DockingEventException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingEventException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingEventException(Throwable cause) {
        super(cause);
    }

    public DockingEventException(String message, Throwable cause) {
        super(message, cause);
    }

}
