package com.carlos.gateway.exception;

import com.carlos.core.response.StatusCode;
import lombok.Getter;

/**
 * <p>
 * 网关基础异常
 * 所有网关特定异常的父类
 * </p>
 *
 * @author carlos
 * @date 2026/3/24
 */
@Getter
public class GatewayException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * HTTP 状态码
     */
    private final int httpStatus;

    /**
     * 业务错误码
     */
    private final Integer errorCode;

    /**
     * 错误路径
     */
    private String path;

    public GatewayException(String message) {
        super(message);
        this.httpStatus = 500;
        this.errorCode = StatusCode.FAIL.getCode();
    }

    public GatewayException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = StatusCode.FAIL.getCode();
    }

    public GatewayException(String message, int httpStatus, Integer errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public GatewayException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = 500;
        this.errorCode = StatusCode.FAIL.getCode();
    }

    public GatewayException(String message, int httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = StatusCode.FAIL.getCode();
    }

    public GatewayException withPath(String path) {
        this.path = path;
        return this;
    }
}
