package com.carlos.auth.exception;

import com.carlos.auth.api.enums.AuthErrorCode;
import com.carlos.core.response.ErrorCode;
import com.carlos.core.response.Result;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局处理OAUTH2得异常
 * </p>
 *
 * @author carlos
 * @date 2021/11/4 11:49
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {

    /**
     * 将 OAuth2 标准错误码和自定义扩展错误码映射为框架错误码
     *
     * @param oauth2ErrorCode OAuth2 错误码
     * @return 框架错误码
     */
    private ErrorCode mapOAuth2ErrorToCode(String oauth2ErrorCode) {
        if (oauth2ErrorCode == null) {
            return AuthErrorCode.AUTH_SYSTEM_ERROR;
        }

        return switch (oauth2ErrorCode) {
            // ===== Spring Security OAuth2 标准错误码 =====
            case OAuth2ErrorCodes.INVALID_REQUEST -> AuthErrorCode.AUTH_PARAM_INVALID;
            case OAuth2ErrorCodes.INVALID_CLIENT -> AuthErrorCode.AUTH_CLIENT_NOT_FOUND;
            case OAuth2ErrorCodes.INVALID_GRANT -> AuthErrorCode.AUTH_USER_CREDENTIALS_EXPIRED;
            case OAuth2ErrorCodes.UNAUTHORIZED_CLIENT -> AuthErrorCode.AUTH_CLIENT_GRANT_TYPE_NOT_SUPPORTED;
            case OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE -> AuthErrorCode.AUTH_PARAM_GRANT_TYPE_INVALID;
            case OAuth2ErrorCodes.INVALID_SCOPE -> AuthErrorCode.AUTH_PARAM_SCOPE_INVALID;
            case OAuth2ErrorCodes.ACCESS_DENIED -> AuthErrorCode.AUTH_ACCESS_DENIED;
            case OAuth2ErrorCodes.INSUFFICIENT_SCOPE -> AuthErrorCode.AUTH_INSUFFICIENT_SCOPE;
            case OAuth2ErrorCodes.SERVER_ERROR -> AuthErrorCode.AUTH_SYSTEM_ERROR;
            case OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE -> AuthErrorCode.AUTH_SYSTEM_ERROR;

            // ===== 自定义扩展错误码 (OAuth2ErrorCodesExpand) =====
            case "username_not_found" -> AuthErrorCode.AUTH_USER_NOT_FOUND;
            case "bad_credentials" -> AuthErrorCode.AUTH_USER_PASSWORD_ERROR;
            case "user_locked" -> AuthErrorCode.AUTH_USER_ACCOUNT_LOCKED;
            case "user_disable" -> AuthErrorCode.AUTH_USER_ACCOUNT_DISABLED;
            case "user_expired" -> AuthErrorCode.AUTH_USER_ACCOUNT_EXPIRED;
            case "credentials_expired" -> AuthErrorCode.AUTH_USER_CREDENTIALS_EXPIRED;
            case "scope_is_empty" -> AuthErrorCode.AUTH_PARAM_SCOPE_INVALID;
            case "token_missing" -> AuthErrorCode.AUTH_TOKEN_INVALID;
            case "verification_code_error" -> AuthErrorCode.AUTH_VERIFICATION_CODE_ERROR;
            case "user_not_found" -> AuthErrorCode.AUTH_USER_NOT_FOUND;
            case "un_know_login_error" -> AuthErrorCode.AUTH_USER_AUTH_FAILED;

            // 未知错误码，返回系统错误
            default -> AuthErrorCode.AUTH_SYSTEM_ERROR;
        };
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public Result<String> handleOauth2Auth(OAuth2AuthenticationException e) {
        // 解析具体的错误码
        OAuth2Error error = e.getError();
        ErrorCode errorCode = mapOAuth2ErrorToCode(error.getErrorCode());
        return Result.error(errorCode, error.getDescription());
    }

    @ExceptionHandler(OAuth2AuthorizationException.class)
    public Result<String> handleOauth2Authorize(OAuth2AuthorizationException e) {
        return Result.error(AuthErrorCode.AUTH_ACCESS_DENIED,
            "授权失败: " + e.getMessage());
    }
}
