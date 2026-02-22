package com.carlos.oauth.support.thirdparty;

import com.carlos.oauth.support.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 微信登录认证 Token
 *
 * <p>封装微信登录所需的参数和认证信息。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class WechatAuthenticationToken extends BaseAuthenticationToken {

    /**
     * 微信授权码
     */
    private final String code;

    /**
     * 微信用户唯一标识（认证成功后设置）
     */
    private String openid;

    public WechatAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                     Authentication clientPrincipal,
                                     Set<String> scopes,
                                     Map<String, String> params,
                                     String code) {
        super(authorizationGrantType, clientPrincipal, scopes, params);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
