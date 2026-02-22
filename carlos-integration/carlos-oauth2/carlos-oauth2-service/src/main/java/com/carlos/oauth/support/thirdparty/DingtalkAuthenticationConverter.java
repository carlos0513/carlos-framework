package com.carlos.oauth.support.thirdparty;

import com.carlos.oauth.support.base.BaseAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 钉钉登录认证转换器
 *
 * <p>支持钉钉扫码登录和免登两种模式。</p>
 *
 * <h3>请求示例（扫码登录）：</h3>
 * <pre>{@code
 * POST /oauth2/token
 * Content-Type: application/x-www-form-urlencoded
 *
 * grant_type=dt_code&
 * code=DINGTALK_AUTH_CODE&
 * client_id=your-client-id
 * }</pre>
 *
 * <h3>请求示例（免登）：</h3>
 * <pre>{@code
 * POST /oauth2/token
 * Content-Type: application/x-www-form-urlencoded
 *
 * grant_type=dt_code&
 * authCode=DINGTALK_AUTH_CODE&
 * client_id=your-client-id
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public class DingtalkAuthenticationConverter extends BaseAuthenticationConverter<DingtalkAuthenticationToken> {

    private static final String GRANT_TYPE = "dt_code";

    @Override
    public boolean support(String grantType) {
        return GRANT_TYPE.equalsIgnoreCase(grantType);
    }

    @Override
    public DingtalkAuthenticationToken buildToken(Authentication clientPrincipal,
                                                  Set<String> requestedScopes,
                                                  Map<String, String> additionalParameters) {
        return new DingtalkAuthenticationToken(
                new AuthorizationGrantType(GRANT_TYPE),
                clientPrincipal,
                requestedScopes,
                additionalParameters
        );
    }

    @Override
    public void checkParams(Map<String, String> request) {
        String code = request.get("code");
        String authCode = request.get("authCode");

        if ((code == null || code.isEmpty()) && (authCode == null || authCode.isEmpty())) {
            throw new ThirdPartyLoginException(
                    "钉钉授权码不能为空（code 或 authCode 至少提供一个）",
                    ThirdPartyType.DINGTALK
            );
        }
    }
}
