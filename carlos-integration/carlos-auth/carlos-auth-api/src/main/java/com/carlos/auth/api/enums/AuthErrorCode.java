package com.carlos.auth.api.enums;

import com.carlos.core.exception.BusinessException;
import com.carlos.core.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 认证授权模块错误码
 * <p>
 * 模块编码：02（认证授权服务）
 * <p>
 * 错误码格式：A-BB-CC
 * <ul>
 *   <li>A - 错误级别：1客户端错误，2业务错误，3第三方错误，5系统错误</li>
 *   <li>BB - 模块编码：02认证授权</li>
 *   <li>CC - 具体错误序号</li>
 * </ul>
 *
 * @author carlos
 * @since 3.0.0
 */
@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // ==================== 客户端错误 (1-02-xx) ====================
    AUTH_PARAM_CLIENT_ID_MISSING("10201", "客户端ID不能为空", 400),
    AUTH_PARAM_CLIENT_SECRET_MISSING("10202", "客户端密钥不能为空", 400),
    AUTH_PARAM_GRANT_TYPE_INVALID("10203", "授权类型无效", 400),
    AUTH_PARAM_REDIRECT_URI_INVALID("10204", "重定向URI无效", 400),
    AUTH_PARAM_SCOPE_INVALID("10205", "授权范围无效", 400),
    AUTH_PARAM_CODE_INVALID("10206", "授权码无效或已过期", 400),

    // ==================== 业务错误 (2-02-xx) ====================
    // 客户端相关
    AUTH_CLIENT_NOT_FOUND("20201", "应用客户端不存在", 404),
    AUTH_CLIENT_DISABLED("20202", "应用客户端已被禁用", 403),
    AUTH_CLIENT_SECRET_ERROR("20203", "客户端密钥错误", 401),
    AUTH_CLIENT_GRANT_TYPE_NOT_SUPPORTED("20204", "该客户端不支持此授权类型", 400),
    AUTH_CLIENT_SCOPE_NOT_SUPPORTED("20205", "该客户端不支持此授权范围", 403),

    // Token 相关
    AUTH_TOKEN_EXPIRED("20211", "访问令牌已过期", 401),
    AUTH_TOKEN_INVALID("20212", "访问令牌无效", 401),
    AUTH_TOKEN_REVOKED("20213", "访问令牌已被吊销", 401),
    AUTH_REFRESH_TOKEN_EXPIRED("20214", "刷新令牌已过期", 401),
    AUTH_REFRESH_TOKEN_INVALID("20215", "刷新令牌无效", 401),

    // 认证相关
    AUTH_USER_AUTH_FAILED("20221", "用户认证失败", 401),
    AUTH_USER_CREDENTIALS_EXPIRED("20222", "用户凭证已过期", 401),
    AUTH_USER_ACCOUNT_LOCKED("20223", "用户账号已被锁定", 403),
    AUTH_USER_ACCOUNT_DISABLED("20224", "用户账号已被禁用", 403),
    AUTH_USER_ACCOUNT_EXPIRED("20225", "用户账号已过期", 403),

    // 授权相关
    AUTH_ACCESS_DENIED("20231", "访问被拒绝，权限不足", 403),
    AUTH_INSUFFICIENT_SCOPE("20232", "授权范围不足", 403),
    AUTH_RESOURCE_ACCESS_DENIED("20233", "资源访问被拒绝", 403),

    // 授权码相关
    AUTH_CODE_EXPIRED("20241", "授权码已过期", 400),
    AUTH_CODE_INVALID("20242", "授权码无效", 400),
    AUTH_CODE_REUSED("20243", "授权码已被使用", 400),

    // ==================== 第三方错误 (3-02-xx) ====================
    AUTH_IDP_ERROR("30201", "身份提供者服务异常", 500),
    AUTH_LDAP_CONNECTION_ERROR("30202", "LDAP 连接失败", 500),
    AUTH_SAML_ERROR("30203", "SAML 认证失败", 500),
    AUTH_OAUTH2_PROVIDER_ERROR("30204", "OAuth2 认证服务异常", 500),

    // ==================== 系统错误 (5-02-xx) ====================
    AUTH_SYSTEM_ERROR("50201", "认证系统内部错误", 500),
    AUTH_TOKEN_GENERATE_ERROR("50202", "令牌生成失败", 500),
    AUTH_TOKEN_PARSE_ERROR("50203", "令牌解析失败", 500),
    AUTH_KEY_STORE_ERROR("50204", "密钥存储异常", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    /**
     * 创建业务异常（使用默认消息）
     *
     * @return BusinessException
     */
    @Override
    public BusinessException exception() {
        return new BusinessException(this);
    }

    /**
     * 创建业务异常（自定义消息）
     *
     * @param customMessage 自定义消息
     * @return BusinessException
     */
    @Override
    public BusinessException exception(String customMessage) {
        return new BusinessException(this, customMessage);
    }

    /**
     * 创建业务异常（格式化消息）
     *
     * @param format 格式字符串
     * @param args   参数
     * @return BusinessException
     */
    @Override
    public BusinessException exception(String format, Object... args) {
        return new BusinessException(this, String.format(format, args));
    }
}
