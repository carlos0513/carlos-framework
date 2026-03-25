package com.carlos.minio.exception;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.ErrorCode;

/**
 * <p>
 * minio异常父类
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 13:14
 */
public class MinioException extends ComponentException {

    public MinioException() {
        super("Minio Exception");
    }

    public MinioException(String message) {
        super(message);
    }

    public MinioException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioException(Throwable cause) {
        super(cause);
    }

    public MinioException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MinioException(ErrorCode errorCode) {
        super(errorCode);
    }
}
