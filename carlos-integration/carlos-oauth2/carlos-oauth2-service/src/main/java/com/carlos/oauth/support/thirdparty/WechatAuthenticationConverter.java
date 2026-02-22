package com.carlos.oauth.support.thirdparty;

import com.carlos.oauth.support.base.BaseAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 微信登录认证转换器
 *
 * <p>将微信登录请求转换为 WechatAuthenticationToken。</p>
 *
 * <h3>请求示例：</h3>
 * <pre>{@code
 * POST /oauth2/token
 * Content-Type: application/x-www-form-urlencoded
 *
 * grant_type=wx_code&
 * code=WECHAT_AUTH_CODE&
 * client_id=your-client-id&
 * client_secret=your-client-secret
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class WechatAuthenticationConverter extends BaseAuthenticationConverter<WechatAuthenticationToken> {

    /**
     * 微信登录的 grant_type
     */
    private static final String GRANT_TYPE = "wx_code";

    /**
     * 授权码参数名
     */
    private static final String PARAM_CODE = "code";

    @Override
    public boolean support(String grantType) {
        return GRANT_TYPE.equalsIgnoreCase(grantType);
    }

    @Override
    public WechatAuthenticationToken buildToken(Authentication clientPrincipal,
                                                Set<String> requestedScopes,
                                                Map<String, String> additionalParameters) {
        String code = additionalParameters.get(PARAM_CODE);
        return new WechatAuthenticationToken(
                new AuthorizationGrantType(GRANT_TYPE),
                clientPrincipal,
                requestedScopes,
                additionalParameters,
                code
        );
    }

    @Override
    public void checkParams(Map<String, String> request) {
        String code = request.get(PARAM_CODE);
        if (code == null || code.isEmpty()) {
            throw new ThirdPartyLoginException("微信授权码不能为空", ThirdPartyType.WECHAT);
        }
    }
}
