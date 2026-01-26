package com.yunjin.oauth2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2配置属性
 *
 * @author yunjin
 * @date 2026-01-25
 */
@Data
@ConfigurationProperties(prefix = "yunjin.oauth2")
public class OAuth2Properties {

    /**
     * 是否启用OAuth2
     */
    private boolean enabled = true;

    /**
     * 授权服务器配置
     */
    @NestedConfigurationProperty
    private AuthorizationServer authorizationServer = new AuthorizationServer();

    /**
     * 资源服务器配置
     */
    @NestedConfigurationProperty
    private ResourceServer resourceServer = new ResourceServer();

    /**
     * JWT配置
     */
    @NestedConfigurationProperty
    private Jwt jwt = new Jwt();

    /**
     * 客户端配置列表
     */
    private List<ClientConfig> clients = new ArrayList<>();

    /**
     * 授权服务器配置
     */
    @Data
    public static class AuthorizationServer {
        /**
         * 是否启用授权服务器
         */
        private boolean enabled = false;

        /**
         * 授权端点路径
         */
        private String authorizationEndpoint = "/oauth2/authorize";

        /**
         * Token端点路径
         */
        private String tokenEndpoint = "/oauth2/token";

        /**
         * Token撤销端点路径
         */
        private String tokenRevocationEndpoint = "/oauth2/revoke";

        /**
         * Token自省端点路径
         */
        private String tokenIntrospectionEndpoint = "/oauth2/introspect";

        /**
         * JWK Set端点路径
         */
        private String jwkSetEndpoint = "/oauth2/jwks";

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
    }

    /**
     * 资源服务器配置
     */
    @Data
    public static class ResourceServer {
        /**
         * 是否启用资源服务器
         */
        private boolean enabled = false;

        /**
         * JWT验证器的JWK Set URI
         */
        private String jwkSetUri;

        /**
         * JWT验证器的Issuer URI
         */
        private String issuerUri;

        /**
         * 不需要认证的路径
         */
        private List<String> permitAllPaths = new ArrayList<>();
    }

    /**
     * JWT配置
     */
    @Data
    public static class Jwt {
        /**
         * JWT签名算法
         */
        private String algorithm = "RS256";

        /**
         * RSA私钥路径（用于签名）
         */
        private String privateKeyPath;

        /**
         * RSA公钥路径（用于验证）
         */
        private String publicKeyPath;

        /**
         * 密钥ID
         */
        private String keyId = "yunjin-key-id";

        /**
         * Issuer
         */
        private String issuer = "http://localhost:8080";

        /**
         * 是否包含用户信息
         */
        private boolean includeUserInfo = true;
    }

    /**
     * 客户端配置
     */
    @Data
    public static class ClientConfig {
        /**
         * 客户端ID
         */
        private String clientId;

        /**
         * 客户端密钥
         */
        private String clientSecret;

        /**
         * 客户端名称
         */
        private String clientName;

        /**
         * 授权类型
         */
        private List<String> authorizationGrantTypes = new ArrayList<>();

        /**
         * 重定向URI
         */
        private List<String> redirectUris = new ArrayList<>();

        /**
         * 作用域
         */
        private List<String> scopes = new ArrayList<>();

        /**
         * 是否需要授权同意
         */
        private boolean requireAuthorizationConsent = false;

        /**
         * 是否需要PKCE
         */
        private boolean requireProofKey = false;

        /**
         * 访问令牌有效期
         */
        private Duration accessTokenTimeToLive;

        /**
         * 刷新令牌有效期
         */
        private Duration refreshTokenTimeToLive;
    }
}
