package com.yunjin.docking.jct.exception;

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
public class DockingLJAppAggrException extends ComponentException {

    private static final long serialVersionUID = -2303357122330162359L;

    public DockingLJAppAggrException() {
        super("Docking LJAppAggrAuth component exception!");
    }

    public DockingLJAppAggrException(String message) {
        super(message);
    }

    public DockingLJAppAggrException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DockingLJAppAggrException(StatusCode statusCode) {
        super(statusCode);
    }

    public DockingLJAppAggrException(Throwable cause) {
        super(cause);
    }

    public DockingLJAppAggrException(String message, Throwable cause) {
        super(message, cause);
    }

}
