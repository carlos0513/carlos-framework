package com.carlos.oauth.support.thirdparty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * 短信验证码登录认证提供者
 *
 * <p>处理短信验证码登录的认证逻辑。</p>
 *
 * <h3>使用方式：</h3>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     clients:
 *       - client-id: sms-app
 *         authorization-grant-types:
 *           - sms_code  # 添加短信登录授权类型
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
public class SmsAuthenticationProvider extends ThirdPartyAbstractProvider<SmsAuthenticationToken> {

    public SmsAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     ThirdPartyLoginService loginService) {
        super(authorizationService, tokenGenerator, loginService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = SmsAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("SmsAuthenticationProvider supports {}: {}", authentication, supports);
        return supports;
    }

    @Override
    protected String getGrantType() {
        return ThirdPartyType.SMS.getGrantType();
    }

    @Override
    protected String getAuthCode(SmsAuthenticationToken authentication) {
        // 返回 mobile:code 格式，由 SmsLoginService 解析
        return authentication.getMobile() + ":" + authentication.getSmsCode();
    }

    @Override
    protected ThirdPartyType getThirdPartyType() {
        return ThirdPartyType.SMS;
    }
}
