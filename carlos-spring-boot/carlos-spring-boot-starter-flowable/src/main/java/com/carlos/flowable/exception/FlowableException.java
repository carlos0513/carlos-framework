package com.carlos.flowable.exception;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.ErrorCode;

/**
 * Flowable工作流异常父类
 *
 * @author Carlos
 * @date 2026-01-27
 */
public class FlowableException extends ComponentException {

    public FlowableException() {
        super("Flowable Exception");
    }

    public FlowableException(String message) {
        super(message);
    }

    public FlowableException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowableException(Throwable cause) {
        super(cause);
    }

    public FlowableException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public FlowableException(ErrorCode errorCode) {
        super(errorCode);
    }
}
