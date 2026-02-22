package com.carlos.oauth.support.thirdparty;

import com.carlos.oauth.support.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 钉钉登录认证 Token
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class DingtalkAuthenticationToken extends BaseAuthenticationToken {

    /**
     * 钉钉授权码
     */
    private final String code;

    /**
     * 临时授权码（免登场景）
     */
    private final String authCode;

    public DingtalkAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                       Authentication clientPrincipal,
                                       Set<String> scopes,
                                       Map<String, String> params) {
        super(authorizationGrantType, clientPrincipal, scopes, params);
        this.code = params.get("code");
        this.authCode = params.get("authCode");
    }

    public String getCode() {
        return code;
    }

    public String getAuthCode() {
        return authCode;
    }
}
