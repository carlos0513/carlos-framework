package com.yunjin.docking.tftd.exception;

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
public class DockingTfAuthException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingTfAuthException() {
        super("Docking TfAuth component exception!");
    }

    public DockingTfAuthException(String message) {
        super(message);
    }

    public DockingTfAuthException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingTfAuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingTfAuthException(Throwable cause) {
        super(cause);
    }

    public DockingTfAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
