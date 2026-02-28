package com.carlos.audit.exceptions;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * <p>
 * 服务主异常
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午4:24:06
 */
public class CarlosAuditException extends ComponentException {

    public CarlosAuditException() {
        super("CarlosAudit  exception!");
    }

    public CarlosAuditException(String message) {
        super(message);
    }

    public CarlosAuditException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public CarlosAuditException(StatusCode statusCode) {
        super(statusCode);
    }

    public CarlosAuditException(Throwable cause) {
        super(cause);
    }

    public CarlosAuditException(String message, Throwable cause) {
        super(message, cause);
    }

}