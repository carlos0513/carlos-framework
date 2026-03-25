package com.carlos.core.exception;

import com.carlos.core.response.CommonErrorCode;
import com.carlos.core.response.ErrorCode;
import lombok.Getter;

/**
 * 系统异常
 * <p>
 * 用于表示不可预期的系统错误，如：
 * <ul>
 *   <li>数据库连接失败</li>
 *   <li>Redis 异常</li>
 *   <li>网络异常</li>
 *   <li>内部服务调用失败</li>
 * </ul>
 * <p>
 * 使用方式：
 * <pre>
 * // 简单消息
 * throw new SystemException("数据库连接超时");
 *
 * // 带原因
 * throw new SystemException("数据库操作失败", e);
 *
 * // 指定错误码
 * throw new SystemException(CommonErrorCode.CACHE_ERROR, "Redis 连接失败");
 * </pre>
 *
 * @author carlos
 * @since 3.0.0
 */
@Getter
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    /**
     * 创建系统异常
     *
     * @param message 错误消息
     */
    public SystemException(String message) {
        super(message);
        this.errorCode = CommonErrorCode.INTERNAL_ERROR;
    }

    /**
     * 创建系统异常（带原因）
     *
     * @param message 错误消息
     * @param cause   原始异常
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = CommonErrorCode.INTERNAL_ERROR;
    }

    /**
     * 创建系统异常（指定错误码）
     *
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 创建系统异常（指定错误码和原因）
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @param cause     原始异常
     */
    public SystemException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
