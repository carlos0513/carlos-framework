package com.carlos.minio.exception;

import com.carlos.core.exception.ComponentException;
import com.carlos.core.response.StatusCode;

/**
 * <p>
 * minio异常父类
 * </p>
 *
 * @author yunjin
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

    public MinioException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public MinioException(StatusCode statusCode) {
        super(statusCode);
    }
}
