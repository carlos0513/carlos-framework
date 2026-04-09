package com.carlos.auth.security.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.carlos.auth.oauth2.grant.CustomGrantTypes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扩展认证转换器
 *
 * <p>支持多种认证方式的请求转换，将 HTTP 请求转换为统一的 ExtendAuthenticationToken。</p>
 * <p><strong>注意：</strong>密码认证已通过表单登录（/login）端点实现，不再通过扩展授权流程处理。</p>
 *
 * <h3>支持的认证方式：</h3>
 * <ul>
 *   <li>sms_code - 短信验证码认证</li>
 *   <li>email_code - 邮箱验证码认证</li>
 *   <li>qr_code - 扫码认证</li>
 *   <li>social - 第三方登录</li>
 * </ul>
 *
 * <h3>请求参数：</h3>
 * <table border="1">
 *   <tr><th>参数名</th><th>必填</th><th>说明</th></tr>
 *   <tr><td>grant_type</td><td>是</td><td>授权类型</td></tr>
 *   <tr><td>username</td><td>条件</td><td>用户名（短信认证时）</td></tr>
 *   <tr><td>password</td><td>条件</td><td>验证码（短信认证时）</td></tr>
 *   <tr><td>phone</td><td>条件</td><td>手机号（短信认证时）</td></tr>
 *   <tr><td>sms_code</td><td>条件</td><td>短信验证码</td></tr>
 *   <tr><td>email</td><td>条件</td><td>邮箱（邮箱认证时必填）</td></tr>
 *   <tr><td>email_code</td><td>条件</td><td>邮箱验证码</td></tr>
 *   <tr><td>qr_token</td><td>条件</td><td>扫码令牌</td></tr>
 *   <tr><td>social_type</td><td>条件</td><td>第三方类型（第三方登录时）</td></tr>
 *   <tr><td>social_code</td><td>条件</td><td>第三方授权码</td></tr>
 *   <tr><td>scope</td><td>否</td><td>授权范围</td></tr>
 * </table>
 *
 * <h3>扩展方式：</h3>
 * <p>如需支持更多认证方式，可继承此类并重写以下方法：</p>
 * <ul>
 *   <li>{@link #supports(String)} - 判断是否支持该授权类型</li>
 *   <li>{@link #extractPrincipal(Map, String)} - 提取认证主体</li>
 *   <li>{@link #extractCredentials(Map, String)} - 提取凭证</li>
 *   <li>{@link #validateParameters(Map, String)} - 验证参数</li>
 * </ul>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see ExtendAuthenticationToken
 * @see ExtendAuthenticationProvider
 */
@Slf4j
public class ExtendAuthenticationConverter implements AuthenticationConverter {

    /**
     * 支持的授权类型（不包含密码授权，密码登录请使用 /login 表单登录端点）
     */
    protected static final Set<String> SUPPORTED_GRANT_TYPES = new HashSet<>(Arrays.asList(
        CustomGrantTypes.SMS_CODE.getValue(),
        CustomGrantTypes.EMAIL_CODE.getValue(),
        CustomGrantTypes.QR_CODE.getValue(),
        CustomGrantTypes.SOCIAL.getValue()
    ));

    /**
     * 转换请求为认证令牌
     *
     * @param request HTTP 请求
     * @return 认证令牌，不支持返回 null
     * @throws OAuth2AuthenticationException 参数验证失败时抛出
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 提取请求参数
        Map<String, String> params = JakartaServletUtil.getParamMap(request);
        String grantType = params.get(OAuth2ParameterNames.GRANT_TYPE);

        // 检查是否支持
        if (!supports(grantType)) {
            log.debug("AuthenticationConverter [{}] does not support grant_type: {}",
                this.getClass().getSimpleName(), grantType);
            return null;
        }

        // 验证客户端已认证
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null || !(clientPrincipal instanceof OAuth2ClientAuthenticationToken)) {
            log.warn("Client authentication not found or invalid");
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }

        OAuth2ClientAuthenticationToken clientAuth = (OAuth2ClientAuthenticationToken) clientPrincipal;

        // 提取 scope
        Set<String> scopes = extractScopes(params);

        // 验证参数
        validateParameters(params, grantType);

        // 提取认证信息
        Object principal = extractPrincipal(params, grantType);
        Object credentials = extractCredentials(params, grantType);

        if (principal == null || (principal instanceof String && StrUtil.isBlank((String) principal))) {
            log.warn("Principal is empty for grant_type: {}", grantType);
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "认证主体不能为空", ""));
        }

        // 构建令牌
        ExtendAuthenticationToken token = new ExtendAuthenticationToken(
            principal,
            credentials,
            new AuthorizationGrantType(grantType),
            clientAuth,
            scopes,
            params
        );

        log.debug("Converted request to ExtendAuthenticationToken: grant_type={}, principal={}",
            grantType, principal);

        return token;
    }

    /**
     * 判断是否支持指定的授权类型
     *
     * <p>子类可重写此方法添加新的授权类型支持。</p>
     *
     * @param grantType 授权类型
     * @return true-支持，false-不支持
     */
    protected boolean supports(String grantType) {
        return grantType != null && SUPPORTED_GRANT_TYPES.contains(grantType);
    }

    /**
     * 提取授权范围
     *
     * @param params 请求参数
     * @return 授权范围集合
     */
    protected Set<String> extractScopes(Map<String, String> params) {
        String scope = params.get(OAuth2ParameterNames.SCOPE);
        if (!StringUtils.hasText(scope)) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
    }

    /**
     * 验证请求参数
     *
     * <p>子类可重写此方法自定义参数验证逻辑。</p>
     *
     * @param params 请求参数
     * @param grantType 授权类型
     * @throws OAuth2AuthenticationException 验证失败时抛出
     */
    protected void validateParameters(Map<String, String> params, String grantType) {
        switch (grantType) {
            case "sms_code":
                validateSmsParams(params);
                break;
            case "email_code":
                validateEmailParams(params);
                break;
            case "qr_code":
                validateQrCodeParams(params);
                break;
            case "social":
                validateSocialParams(params);
                break;
            default:
                // 其他类型由子类处理
                break;
        }
    }

    /**
     * 验证短信认证参数
     */
    protected void validateSmsParams(Map<String, String> params) {
        String phone = params.get("phone");
        String smsCode = params.get("sms_code");

        if (StrUtil.isBlank(phone)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "手机号不能为空", ""));
        }
        if (StrUtil.isBlank(smsCode)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "短信验证码不能为空", ""));
        }
        // 手机号格式校验
        if (!isValidPhone(phone)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "手机号格式不正确", ""));
        }
    }

    /**
     * 验证邮箱认证参数
     */
    protected void validateEmailParams(Map<String, String> params) {
        String email = params.get("email");
        String emailCode = params.get("email_code");

        if (StrUtil.isBlank(email)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "邮箱不能为空", ""));
        }
        if (StrUtil.isBlank(emailCode)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "邮箱验证码不能为空", ""));
        }
    }

    /**
     * 验证扫码认证参数
     */
    protected void validateQrCodeParams(Map<String, String> params) {
        String qrToken = params.get("qr_token");

        if (StrUtil.isBlank(qrToken)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "扫码令牌不能为空", ""));
        }
    }

    /**
     * 验证第三方登录参数
     */
    protected void validateSocialParams(Map<String, String> params) {
        String socialType = params.get("social_type");
        String socialCode = params.get("social_code");

        if (StrUtil.isBlank(socialType)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "第三方类型不能为空", ""));
        }
        if (StrUtil.isBlank(socialCode)) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "第三方授权码不能为空", ""));
        }
    }

    /**
     * 提取认证主体
     *
     * <p>根据授权类型从请求参数中提取认证主体（用户名/手机号/邮箱等）。</p>
     *
     * @param params 请求参数
     * @param grantType 授权类型
     * @return 认证主体
     */
    protected Object extractPrincipal(Map<String, String> params, String grantType) {
        switch (grantType) {
            case "sms_code":
                return params.get("phone");
            case "email_code":
                return params.get("email");
            case "qr_code":
                return params.get("qr_token");
            case "social":
                return params.get("social_type") + ":" + params.get("social_code");
            default:
                return params.get(OAuth2ParameterNames.USERNAME);
        }
    }

    /**
     * 提取凭证
     *
     * <p>根据授权类型从请求参数中提取凭证（密码/验证码等）。</p>
     *
     * @param params 请求参数
     * @param grantType 授权类型
     * @return 凭证
     */
    protected Object extractCredentials(Map<String, String> params, String grantType) {
        switch (grantType) {
            case "sms_code":
                return params.get("sms_code");
            case "email_code":
                return params.get("email_code");
            case "qr_code":
            case "social":
                return null; // 这些方式不需要凭证
            default:
                return null;
        }
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return true-格式正确
     */
    protected boolean isValidPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        // 中国大陆手机号简单校验
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 获取支持的授权类型
     *
     * @return 授权类型集合
     */
    public Set<String> getSupportedGrantTypes() {
        return new HashSet<>(SUPPORTED_GRANT_TYPES);
    }

}
