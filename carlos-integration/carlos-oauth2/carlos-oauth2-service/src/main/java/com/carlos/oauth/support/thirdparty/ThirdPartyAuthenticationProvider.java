package com.carlos.oauth.support.thirdparty;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.oauth.support.base.BaseAuthenticationProvider;
import com.carlos.oauth.support.base.BaseAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * 第三方登录认证提供者抽象基类
 *
 * <p>封装第三方登录的通用逻辑，子类只需实现特定平台的参数获取方法。</p>
 *
 * <h3>子类实现：</h3>
 * <ul>
 *   <li>{@link WechatAuthenticationProvider} - 微信登录</li>
 *   <li>{@link DingtalkAuthenticationProvider} - 钉钉登录</li>
 *   <li>{@link SmsAuthenticationProvider} - 短信登录</li>
 * </ul>
 *
 * @param <T> 认证 Token 类型
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
public abstract class ThirdPartyAuthenticationProvider<T extends BaseAuthenticationToken>
        extends BaseAuthenticationProvider<T> {

    /**
     * 第三方登录服务
     */
    private final ThirdPartyLoginService loginService;

    public ThirdPartyAuthenticationProvider(AuthenticationManager authenticationManager,
                                            OAuth2AuthorizationService authorizationService,
                                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                            ThirdPartyLoginService loginService) {
        super(authenticationManager, authorizationService, tokenGenerator);
        this.loginService = loginService;
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, String> reqParameters) {
        // 第三方登录不需要用户名密码验证，直接返回 null
        // 实际的认证逻辑在 authenticate 方法中处理
        return null;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        // 检查客户端是否支持此第三方登录类型
        ThirdPartyType type = getThirdPartyType();
        String grantType = type.getGrantType();

        if (!registeredClient.getAuthorizationGrantTypes().contains(
                new AuthorizationGrantType(grantType))) {
            log.warn("Client {} does not support grant type: {}",
                    registeredClient.getClientId(), grantType);
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    /**
     * 执行第三方登录
     *
     * <p>子类可以重写此方法以实现自定义逻辑。</p>
     *
     * @param authentication 认证 Token
     * @return Authentication 认证结果
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        @SuppressWarnings("unchecked")
        T token = (T) authentication;

        // 1. 获取认证码（微信 code、钉钉 code、短信 mobile:code 等）
        String authCode = getAuthCode(token);

        // 2. 调用第三方登录服务
        LoginUserInfo userInfo = loginService.login(authCode);

        if (userInfo == null) {
            log.error("Third party login failed for type: {}", getThirdPartyType());
            throw new ThirdPartyLoginException(
                    "第三方登录失败，请检查授权码是否有效",
                    getThirdPartyType()
            );
        }

        // 3. 构建认证结果
        return createSuccessAuthentication(token, userInfo);
    }

    /**
     * 创建成功的认证结果
     *
     * @param token 原始认证 Token
     * @param userInfo 用户信息
     * @return Authentication 认证结果
     */
    protected Authentication createSuccessAuthentication(T token, LoginUserInfo userInfo) {
        // 构建 UsernamePasswordAuthenticationToken 作为 principal
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken(
                        userInfo.getAccount(),
                        null,
                        loginService.getType().getAuthorities()
                );

        principal.setDetails(userInfo);

        // 调用父类方法完成 Token 生成
        return super.authenticate(createWrapperToken(token, principal));
    }

    /**
     * 创建包装 Token
     *
     * <p>将 principal 替换为第三方登录获取的用户信息。</p>
     *
     * @param originalToken 原始 Token
     * @param principal 用户信息
     * @return T 包装后的 Token
     */
    protected abstract T createWrapperToken(T originalToken, UsernamePasswordAuthenticationToken principal);

    /**
     * 获取授权码
     *
     * @param authentication 认证 Token
     * @return String 授权码
     */
    protected abstract String getAuthCode(T authentication);

    /**
     * 获取第三方平台类型
     *
     * @return ThirdPartyType 平台类型
     */
    protected abstract ThirdPartyType getThirdPartyType();
}
