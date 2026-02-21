package com.yunjin.docking.suining.exception;

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
public class DockingSuiningAuthException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingSuiningAuthException() {
        super("Docking SuiningAuth component exception!");
    }

    public DockingSuiningAuthException(String message) {
        super(message);
    }

    public DockingSuiningAuthException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingSuiningAuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingSuiningAuthException(Throwable cause) {
        super(cause);
    }

    public DockingSuiningAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
