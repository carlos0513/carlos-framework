package com.carlos.core.exception;

import com.carlos.core.response.ErrorCode;
import lombok.Getter;

/**
 * 业务异常
 * <p>
 * 用于表示可预期的业务错误，如：
 * <ul>
 *   <li>用户不存在</li>
 *   <li>权限不足</li>
 *   <li>业务规则限制</li>
 *   <li>数据状态不正确</li>
 * </ul>
 * <p>
 * 使用方式：
 * <pre>
 * // 使用默认消息
 * throw CommonErrorCode.USER_NOT_FOUND.exception();
 *
 * // 自定义消息
 * throw CommonErrorCode.USER_NOT_FOUND.exception("该手机号未注册");
 *
 * // 格式化消息
 * throw CommonErrorCode.USER_ACCOUNT_LOCKED.exception("账号已被锁定，%d分钟后重试", 30);
 * </pre>
 *
 * @author carlos
 * @since 3.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    /**
     * 自定义错误消息（为空时使用错误码默认消息）
     */
    private final String customMessage;

    /**
     * 创建业务异常（使用错误码默认消息）
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = null;
    }

    /**
     * 创建业务异常（自定义消息）
     *
     * @param errorCode 错误码
     * @param message   自定义消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.customMessage = message;
    }

    /**
     * 创建业务异常（自定义消息和原因）
     *
     * @param errorCode 错误码
     * @param message   自定义消息
     * @param cause     原始异常
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.customMessage = message;
    }

    /**
     * 获取最终错误消息
     *
     * @return 自定义消息或错误码默认消息
     */
    public String getFinalMessage() {
        return customMessage != null ? customMessage : errorCode.getMessage();
    }
}
