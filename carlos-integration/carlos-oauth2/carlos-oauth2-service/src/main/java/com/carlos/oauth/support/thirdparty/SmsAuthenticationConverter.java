package com.carlos.oauth.support.thirdparty;

import com.carlos.oauth.support.base.BaseAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 短信验证码登录认证转换器
 *
 * <h3>请求示例：</h3>
 * <pre>{@code
 * POST /oauth2/token
 * Content-Type: application/x-www-form-urlencoded
 *
 * grant_type=sms_code&
 * mobile=13800138000&
 * code=123456&
 * client_id=your-client-id
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class SmsAuthenticationConverter extends BaseAuthenticationConverter<SmsAuthenticationToken> {

    private static final String GRANT_TYPE = "sms_code";

    @Override
    public boolean support(String grantType) {
        return GRANT_TYPE.equalsIgnoreCase(grantType);
    }

    @Override
    public SmsAuthenticationToken buildToken(Authentication clientPrincipal,
                                             Set<String> requestedScopes,
                                             Map<String, String> additionalParameters) {
        return new SmsAuthenticationToken(
                new AuthorizationGrantType(GRANT_TYPE),
                clientPrincipal,
                requestedScopes,
                additionalParameters
        );
    }

    @Override
    public void checkParams(Map<String, String> request) {
        String mobile = request.get("mobile");
        String code = request.get("code");

        if (mobile == null || mobile.isEmpty()) {
            throw new ThirdPartyLoginException("手机号不能为空", ThirdPartyType.SMS);
        }

        // 简单的手机号格式校验
        if (!mobile.matches("^1[3-9]\\d{9}$")) {
            throw new ThirdPartyLoginException("手机号格式不正确", ThirdPartyType.SMS);
        }

        if (code == null || code.isEmpty()) {
            throw new ThirdPartyLoginException("短信验证码不能为空", ThirdPartyType.SMS);
        }
    }
}
