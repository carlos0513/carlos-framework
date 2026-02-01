package com.carlos.oss.exception;

/**
 * OSS 异常
 *
 * @author carlos
 * @date 2026-02-01
 */
public class OssException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OssException(String message) {
        super(message);
    }

    public OssException(String message, Throwable cause) {
        super(message, cause);
    }

    public OssException(Throwable cause) {
        super(cause);
    }
}
