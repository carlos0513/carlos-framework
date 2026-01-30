package com.carlos.core.exception;

import com.carlos.core.response.StatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局异常
 *
 * @author carlos
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误提示
     */
    private String message;


    /**
     * 空构造方法，避免反序列化问题
     */
    public GlobalException() {
        super("服务异常，请联系管理员");
    }

    public GlobalException(String message) {
        this.message = message;
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public GlobalException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.errorCode = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public GlobalException(StatusCode statusCode, Throwable cause) {
        super(statusCode.getMessage(), cause);
        this.errorCode = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public GlobalException(Integer statusCode, Throwable cause) {
        super(StatusCode.getApiCode(statusCode).getMessage(), cause);
        this.errorCode = statusCode;
        this.message = StatusCode.getApiCode(statusCode).getMessage();
    }

    public GlobalException(Integer statusCode, String message) {
        super(message);
        this.errorCode = statusCode;
        this.message = message;
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

}