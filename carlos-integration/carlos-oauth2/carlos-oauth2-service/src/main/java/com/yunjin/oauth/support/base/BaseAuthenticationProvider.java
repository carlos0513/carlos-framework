package com.carlos.oauth.support.base;

import com.carlos.core.exception.ComponentException;
import com.carlos.oauth.exception.UserNotFoundException;
import com.carlos.oauth.exception.VerificationCodeException;
import com.carlos.oauth.support.OAuth2ErrorCodesExpand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import jakarta.security.auth.login.AccountExpiredException;
import java.security.Principal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * <p>
 *
 * </p>
 *
 * @author yunjin
 * @date 2022/11/4 17:42
 */
@Slf4j
public abstract class BaseAuthenticationProvider<T extends BaseAuthenticationToken> implements AuthenticationProvider {

    private static final String ERROR_URI = "";


    private OAuth2AuthorizationService authorizationService;

    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private AuthenticationManager authenticationManager;


    @Deprecated
    private Supplier<String> refreshTokenGenerator;


    public BaseAuthenticationProvider(AuthenticationManager authenticationManager,
                                      OAuth2AuthorizationService authorizationService,
                                      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;

    }


    public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, String> reqParameters);

    /**
     * 当前provider是否支持此令牌类型
     */
    @Override
    public abstract boolean supports(Class<?> authentication);

    /**
     * 当前的请求客户端是否支持此模式
     */
    public abstract void checkClient(RegisteredClient registeredClient);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        T authenticationToken = (T) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authenticationToken);

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        checkClient(registeredClient);

        Set<String> authorizedScopes;
        // Default to configured scopes
        if (!CollectionUtils.isEmpty(authenticationToken.getScopes())) {
            for (String requestedScope : authenticationToken.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(authenticationToken.getScopes());
        } else {
            throw new ComponentException("scope is null");
        }

        Map<String, String> reqParameters = authenticationToken.getParameters();
        try {

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(reqParameters);

            log.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);

            Authentication usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // @formatter:off
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(usernamePasswordAuthentication)
                    // .providerContext(ProviderContextHolder.getProviderContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .authorizationGrant(authenticationToken);
            // @formatter:on

            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                    .withRegisteredClient(registeredClient).principalName(usernamePasswordAuthentication.getName())
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    // .attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes)
                    ;

            // ----- Access token -----
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            if (generatedAccessToken == null) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the access token.", ERROR_URI);
                throw
                        new OAuth2AuthenticationException(error);
            }
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
            if (generatedAccessToken instanceof ClaimAccessor) {
                authorizationBuilder.id(accessToken.getTokenValue())
                        .token(accessToken,
                                (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                                        ((ClaimAccessor) generatedAccessToken).getClaims()))
                        // .attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes)
                        .attribute(Principal.class.getName(), usernamePasswordAuthentication);
            } else {
                authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
            }

            // ----- Refresh token -----
            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                    // Do not issue refresh token to public client
                    !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

                if (this.refreshTokenGenerator != null) {
                    Instant issuedAt = Instant.now();
                    Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
                    refreshToken = new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt, expiresAt);
                } else {
                    tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                    OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                    if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                                "The token generator failed to generate the refresh token.", ERROR_URI);
                        throw new OAuth2AuthenticationException(error);
                    }
                    refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                }
                authorizationBuilder.refreshToken(refreshToken);
            }

            OAuth2Authorization authorization = authorizationBuilder.build();

            this.authorizationService.save(authorization);

            log.debug("returning OAuth2AccessTokenAuthenticationToken");

            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                    refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));

        } catch (Exception ex) {
            log.error("problem in authenticate", ex);
            throw oAuth2AuthenticationException(authenticationToken, (AuthenticationException) ex);
        }

    }

    /**
     * 登录异常转换为oauth2异常
     *
     * @param authentication          身份验证
     * @param authenticationException 身份验证异常
     * @return {@link OAuth2AuthenticationException}
     */
    private OAuth2AuthenticationException oAuth2AuthenticationException(Authentication authentication,
                                                                        AuthenticationException authenticationException) {
        if (authenticationException instanceof UsernameNotFoundException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND, "用户名不存在", ""));
        }
        if (authenticationException instanceof BadCredentialsException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.BAD_CREDENTIALS, "用户名或密码错误", ""));
        }
        if (authenticationException instanceof LockedException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_LOCKED, "用户已锁定", ""));
        }
        if (authenticationException instanceof DisabledException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_DISABLE, "用户已禁用", ""));
        }
        if (authenticationException instanceof AccountExpiredException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_EXPIRED, "用户已过期", ""));
        }
        if (authenticationException instanceof CredentialsExpiredException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.CREDENTIALS_EXPIRED, "用户密钥已过期", ""));
        }
        if (authenticationException instanceof VerificationCodeException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.VERIFICATION_CODE_ERROR, "验证码错误", ""));
        }
        if (authenticationException instanceof UserNotFoundException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_NOT_FOUND, "用户不存在", ""));
        }

        return new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.UN_KNOW_LOGIN_ERROR);
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication) {

        OAuth2ClientAuthenticationToken clientPrincipal = null;

        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

}
