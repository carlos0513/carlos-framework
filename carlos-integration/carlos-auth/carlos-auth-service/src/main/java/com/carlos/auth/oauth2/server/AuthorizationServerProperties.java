package com.carlos.auth.oauth2.server;

import lombok.Data;

import java.time.Duration;

/**
 * OAuth2 授权服务器配置属性
 *
 * <p>配置 OAuth2 授权服务器的端点路径、Token 有效期、授权模式等。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Data
public class AuthorizationServerProperties {

    /**
     * 是否启用授权服务器
     */
    private boolean enabled = false;

    /**
     * 授权端点路径
     */
    private String authorizationEndpoint = "/oauth2/authorize";

    /**
     * Token 端点路径
     */
    private String tokenEndpoint = "/oauth2/token";

    /**
     * Token 撤销端点路径
     */
    private String tokenRevocationEndpoint = "/oauth2/revoke";

    /**
     * Token 自省端点路径
     */
    private String tokenIntrospectionEndpoint = "/oauth2/introspect";

    /**
     * JWK Set 端点路径
     */
    private String jwkSetEndpoint = "/oauth2/jwks";

    /**
     * OIDC 用户信息端点路径
     */
    private String oidcUserInfoEndpoint = "/userinfo";

    /**
     * 授权码有效期
     */
    private Duration authorizationCodeTimeToLive = Duration.ofMinutes(5);

    /**
     * 访问令牌有效期
     */
    private Duration accessTokenTimeToLive = Duration.ofHours(2);

    /**
     * 刷新令牌有效期
     */
    private Duration refreshTokenTimeToLive = Duration.ofDays(7);

    /**
     * 是否重用刷新令牌
     */
    private boolean reuseRefreshTokens = false;

    /**
     * 设备码有效期
     */
    private Duration deviceCodeTimeToLive = Duration.ofMinutes(5);

    /**
     * 是否启用 OIDC 支持
     */
    private boolean oidcEnabled = true;

}
