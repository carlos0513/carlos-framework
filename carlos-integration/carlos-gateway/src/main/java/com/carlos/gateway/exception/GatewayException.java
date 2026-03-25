package com.carlos.gateway.exception;

import com.carlos.core.exception.GlobalException;
import com.carlos.core.response.CommonErrorCode;
import com.carlos.core.response.ErrorCode;
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
public class GatewayException extends GlobalException {

    private static final long serialVersionUID = 1L;

    /**
     * HTTP 状态码
     */
    private final int httpStatus;

    /**
     * 错误路径
     */
    private String path;

    public GatewayException(String message) {
        super(CommonErrorCode.INTERNAL_ERROR);
        this.httpStatus = 500;
    }

    public GatewayException(String message, int httpStatus) {
        super(CommonErrorCode.INTERNAL_ERROR);
        this.httpStatus = httpStatus;
    }

    public GatewayException(String message, int httpStatus, String errorCode) {
        super(CommonErrorCode.INTERNAL_ERROR, message);
        this.httpStatus = httpStatus;
    }

    public GatewayException(String message, Throwable cause) {
        super(CommonErrorCode.INTERNAL_ERROR, cause);
        this.httpStatus = 500;
    }


    public GatewayException(ErrorCode errorCode) {
        super(errorCode);
        this.httpStatus = errorCode.getHttpStatus();
    }

    public GatewayException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        this.httpStatus = errorCode.getHttpStatus();
    }

    public GatewayException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.httpStatus = errorCode.getHttpStatus();
    }

    public GatewayException(String message, int httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public GatewayException withPath(String path) {
        this.path = path;
        return this;
    }
}
