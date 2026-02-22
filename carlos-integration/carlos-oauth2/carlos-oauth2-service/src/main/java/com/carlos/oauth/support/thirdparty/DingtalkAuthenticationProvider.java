package com.carlos.oauth.support.thirdparty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * 钉钉登录认证提供者
 *
 * <p>处理钉钉登录的认证逻辑。</p>
 *
 * <h3>使用方式：</h3>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     clients:
 *       - client-id: dingtalk-app
 *         authorization-grant-types:
 *           - dt_code  # 添加钉钉登录授权类型
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
public class DingtalkAuthenticationProvider extends ThirdPartyAbstractProvider<DingtalkAuthenticationToken> {

    public DingtalkAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          ThirdPartyLoginService loginService) {
        super(authorizationService, tokenGenerator, loginService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = DingtalkAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("DingtalkAuthenticationProvider supports {}: {}", authentication, supports);
        return supports;
    }

    @Override
    protected String getGrantType() {
        return ThirdPartyType.DINGTALK.getGrantType();
    }

    @Override
    protected String getAuthCode(DingtalkAuthenticationToken authentication) {
        // 优先使用 code，其次使用 authCode
        String code = authentication.getCode();
        if (code != null && !code.isEmpty()) {
            return code;
        }
        return authentication.getAuthCode();
    }

    @Override
    protected ThirdPartyType getThirdPartyType() {
        return ThirdPartyType.DINGTALK;
    }
}
