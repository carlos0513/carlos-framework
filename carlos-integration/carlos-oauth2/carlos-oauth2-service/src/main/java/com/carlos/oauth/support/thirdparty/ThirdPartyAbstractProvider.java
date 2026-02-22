package com.carlos.oauth.support.thirdparty;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.oauth.support.base.BaseAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.*;

/**
 * 第三方登录认证提供者抽象基类
 *
 * <p>专门处理第三方登录（微信、钉钉、短信等）的认证流程。</p>
 *
 * <h3>认证流程：</h3>
 * <ol>
 *   <li>验证客户端是否支持此第三方登录授权类型</li>
 *   <li>获取第三方授权码</li>
 *   <li>调用 {@link ThirdPartyLoginService#login(String)} 获取用户信息</li>
 *   <li>生成 OAuth2 Access Token 和 Refresh Token</li>
 * </ol>
 *
 * @param <T> 认证 Token 类型
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
public abstract class ThirdPartyAbstractProvider<T extends BaseAuthenticationToken> implements AuthenticationProvider {

    private static final String ERROR_URI = "";

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final ThirdPartyLoginService loginService;

    public ThirdPartyAbstractProvider(OAuth2AuthorizationService authorizationService,
                                      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                      ThirdPartyLoginService loginService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(loginService, "loginService cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.loginService = loginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        @SuppressWarnings("unchecked")
        T authenticationToken = (T) authentication;

        // 1. 获取已认证的客户端
        OAuth2ClientAuthenticationToken clientPrincipal =
                getAuthenticatedClientElseThrowInvalidClient(authenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // 2. 检查客户端是否支持此授权类型
        checkClient(registeredClient);

        // 3. 处理授权范围
        Set<String> authorizedScopes = processScopes(authenticationToken, registeredClient);

        // 4. 获取授权码并执行第三方登录
        String authCode = getAuthCode(authenticationToken);
        log.debug("Authenticating with {} using auth code: {}", getThirdPartyType(), authCode);

        LoginUserInfo userInfo = loginService.login(authCode);
        if (userInfo == null) {
            log.error("{} login failed: user info is null", getThirdPartyType().getDescription());
            throw new ThirdPartyLoginException(
                    "第三方登录失败，请检查授权码是否有效",
                    getThirdPartyType()
            );
        }

        log.info("{} login success for user: {}", getThirdPartyType().getDescription(), userInfo.getAccount());

        // 5. 构建用户认证信息
        UsernamePasswordAuthenticationToken userPrincipal = buildUserPrincipal(userInfo);

        // 6. 生成 OAuth2 Token
        return generateOAuth2Tokens(authenticationToken, clientPrincipal, registeredClient,
                userPrincipal, authorizedScopes);
    }

    /**
     * 处理授权范围
     */
    private Set<String> processScopes(T authenticationToken, RegisteredClient registeredClient) {
        if (!CollectionUtils.isEmpty(authenticationToken.getScopes())) {
            for (String requestedScope : authenticationToken.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            return new LinkedHashSet<>(authenticationToken.getScopes());
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
    }

    /**
     * 构建用户主体信息
     */
    private UsernamePasswordAuthenticationToken buildUserPrincipal(LoginUserInfo userInfo) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 添加角色权限
        if (userInfo.getRoleIds() != null) {
            for (java.io.Serializable roleId : userInfo.getRoleIds()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleId));
            }
        }

        // 添加默认角色
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
                userInfo.getAccount(),
                null,
                authorities
        );
        principal.setDetails(userInfo);

        return principal;
    }

    /**
     * 生成 OAuth2 Token
     */
    private Authentication generateOAuth2Tokens(T authenticationToken,
                                                OAuth2ClientAuthenticationToken clientPrincipal,
                                                RegisteredClient registeredClient,
                                                UsernamePasswordAuthenticationToken userPrincipal,
                                                Set<String> authorizedScopes) {

        // 构建 Token 上下文
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(userPrincipal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(new AuthorizationGrantType(getGrantType()))
                .authorizationGrant(authenticationToken);

        // 构建授权信息
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(userPrincipal.getName())
                .authorizationGrantType(new AuthorizationGrantType(getGrantType()))
                .attribute(Principal.class.getName(), userPrincipal);

        // ----- Access Token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .build();

        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(),
                tokenContext.getAuthorizedScopes()
        );

        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder
                    .id(accessToken.getTokenValue())
                    .token(accessToken, (metadata) ->
                            metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                                    ((ClaimAccessor) generatedAccessToken).getClaims()))
                    .attribute(Principal.class.getName(), userPrincipal);
        } else {
            authorizationBuilder
                    .id(accessToken.getTokenValue())
                    .accessToken(accessToken);
        }

        // ----- Refresh Token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder
                    .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                    .build();

            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (generatedRefreshToken instanceof OAuth2RefreshToken) {
                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(refreshToken);
            }
        }

        // 保存授权信息
        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        log.debug("Generated OAuth2 tokens for user: {}", userPrincipal.getName());

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,
                clientPrincipal,
                accessToken,
                refreshToken,
                Objects.requireNonNull(authorization.getAccessToken().getClaims())
        );
    }

    /**
     * 获取已认证的客户端
     */
    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication) {

        OAuth2ClientAuthenticationToken clientPrincipal = null;

        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
                authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 检查客户端是否支持此授权类型
     */
    protected void checkClient(RegisteredClient registeredClient) {
        String grantType = getGrantType();

        if (!registeredClient.getAuthorizationGrantTypes().contains(
                new AuthorizationGrantType(grantType))) {
            log.warn("Client {} does not support grant type: {}",
                    registeredClient.getClientId(), grantType);
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    /**
     * 获取授权码
     *
     * @param authentication 认证 Token
     * @return String 授权码
     */
    protected abstract String getAuthCode(T authentication);

    /**
     * 获取授权类型（grant_type）
     *
     * @return String 授权类型
     */
    protected abstract String getGrantType();

    /**
     * 获取第三方平台类型
     *
     * @return ThirdPartyType 平台类型
     */
    protected abstract ThirdPartyType getThirdPartyType();
}
