package com.carlos.oauth.support.thirdparty;

import com.carlos.oauth.support.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 短信验证码登录认证 Token
 *
 * <h3>请求参数：</h3>
 * <ul>
 *   <li>mobile - 手机号</li>
 *   <li>code - 短信验证码</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class SmsAuthenticationToken extends BaseAuthenticationToken {

    /**
     * 手机号
     */
    private final String mobile;

    /**
     * 短信验证码
     */
    private final String smsCode;

    public SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                  Authentication clientPrincipal,
                                  Set<String> scopes,
                                  Map<String, String> params) {
        super(authorizationGrantType, clientPrincipal, scopes, params);
        this.mobile = params.get("mobile");
        this.smsCode = params.get("code");
    }

    public String getMobile() {
        return mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }
}
