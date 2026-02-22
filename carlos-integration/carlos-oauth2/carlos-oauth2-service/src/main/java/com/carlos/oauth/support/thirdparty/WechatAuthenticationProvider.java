package com.carlos.oauth.support.thirdparty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * 微信登录认证提供者
 *
 * <p>处理微信登录的认证逻辑。</p>
 *
 * <h3>认证流程：</h3>
 * <ol>
 *   <li>验证客户端是否支持微信登录授权类型（wx_code）</li>
 *   <li>获取微信授权码 code</li>
 *   <li>调用 WechatLoginService 使用 code 换取用户信息</li>
 *   <li>查找或创建本地用户绑定</li>
 *   <li>生成 OAuth2 Token</li>
 * </ol>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     clients:
 *       - client-id: wechat-app
 *         client-secret: secret
 *         authorization-grant-types:
 *           - wx_code  # 添加微信登录授权类型
 *         scopes:
 *           - read
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 * @see WechatAuthenticationToken
 * @see WechatAuthenticationConverter
 */
@Slf4j
public class WechatAuthenticationProvider extends ThirdPartyAbstractProvider<WechatAuthenticationToken> {

    public WechatAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                        ThirdPartyLoginService loginService) {
        super(authorizationService, tokenGenerator, loginService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = WechatAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("WechatAuthenticationProvider supports {}: {}", authentication, supports);
        return supports;
    }

    @Override
    protected String getGrantType() {
        return ThirdPartyType.WECHAT.getGrantType();
    }

    @Override
    protected String getAuthCode(WechatAuthenticationToken authentication) {
        return authentication.getCode();
    }

    @Override
    protected ThirdPartyType getThirdPartyType() {
        return ThirdPartyType.WECHAT;
    }
}
