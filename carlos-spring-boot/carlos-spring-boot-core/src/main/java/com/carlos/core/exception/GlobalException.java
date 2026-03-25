package com.carlos.core.exception;

import com.carlos.core.response.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局异常基类
 * <p>
 * 所有自定义异常的父类，提供统一的错误码和错误消息管理。
 * 支持新旧两种错误码体系：
 * <ul>
 *   <li>新版：使用 {@link ErrorCode} 接口（5位字符串错误码）</li>
 *   <li>旧版：使用 Integer 错误码（4位数字，兼容保留）</li>
 * </ul>
 *
 * @author carlos
 * @since 1.0.0
 * @see BusinessException
 * @see SystemException
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码（新版为5位字符串，旧版为4位整数）
     */
    private String errorCode;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public GlobalException() {
        super("服务异常，请联系管理员");
        this.message = "服务异常，请联系管理员";
    }

    /**
     * 使用消息创建异常
     *
     * @param message 错误消息
     */
    public GlobalException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * 使用消息和原因创建异常
     *
     * @param message 错误消息
     * @param cause   原始异常
     */
    public GlobalException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * 使用 ErrorCode 创建异常
     *
     * @param errorCode 错误码
     */
    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    /**
     * 使用 ErrorCode 创建异常
     *
     * @param errorCode 错误码
     */
    public GlobalException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getCode();
        this.message = message;
    }


    /**
     * 使用字符串错误码和错误消息创建异常
     *
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public GlobalException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * 使用 ErrorCode 和原因创建异常
     *
     * @param errorCode 错误码
     * @param cause     原始异常
     */
    public GlobalException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public GlobalException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode.getCode();
        this.message = message;
    }

    /**
     * 使用原始异常创建
     *
     * @param cause 原始异常
     */
    public GlobalException(Throwable cause) {
        super(cause);
        this.message = cause != null ? cause.getMessage() : null;
    }
}
