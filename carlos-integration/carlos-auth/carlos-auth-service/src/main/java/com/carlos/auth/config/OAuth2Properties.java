package com.carlos.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Carlos OAuth2 统一配置属性
 *
 * <p>本配置类提供了 OAuth2 授权服务器和资源服务器的完整配置选项。
 * 通过 application.yml 或 application.properties 文件进行配置。</p>
 *
 * <p><strong>配置前缀：</strong>{@code carlos.oauth2}</p>
 *
 * <h3>快速开始配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     # 启用 OAuth2 功能
 *     enabled: true
 *
 *     # 授权服务器配置
 *     authorization-server:
 *       enabled: true
 *       # Token 有效期配置
 *       access-token-time-to-live: 2h
 *       refresh-token-time-to-live: 7d
 *
 *     # JWT 配置
 *     jwt:
 *       issuer: https://auth.example.com
 *       key-id: auth-key-1
 *
 *     # 注册客户端配置
 *     clients:
 *       - client-id: web-client
 *         client-secret: secret
 *         client-name: Web Application
 *         redirect-uris:
 *           - http://localhost:8080/login/oauth2/code/client
 *         scopes:
 *           - read
 *           - write
 * }</pre>
 *
 * <h3>配置项说明：</h3>
 * <ul>
 *   <li>{@code enabled} - 总开关，控制是否启用 OAuth2 功能</li>
 *   <li>{@code authorization-server} - 授权服务器相关配置</li>
 *   <li>{@code jwt} - JWT Token 签名和验证配置</li>
 *   <li>{@code clients} - 预注册的 OAuth2 客户端列表</li>
 *   <li>{@code security} - 安全相关配置（密码策略、登录限制等）</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Data
@ConfigurationProperties(prefix = "carlos.auth")
public class OAuth2Properties {

    /**
     * 是否启用 OAuth2 功能
     *
     * <p>这是 OAuth2 模块的总开关。设置为 false 时，所有 OAuth2 相关功能将被禁用，
     * 包括授权服务器、资源服务器和自动配置。</p>
     *
     * <p><strong>默认值：</strong>true</p>
     * <p><strong>适用场景：</strong></p>
     * <ul>
     *   <li>开发调试时临时关闭认证</li>
     *   <li>某些服务不需要认证功能时</li>
     *   <li>使用外部认证服务时禁用内置授权服务器</li>
     * </ul>
     */
    private boolean enabled = true;

    /**
     * 授权服务器配置
     *
     * <p>授权服务器负责颁发和管理 OAuth2 Token。
     * 包括授权码、访问令牌、刷新令牌的颁发和验证。</p>
     *
     * @see AuthorizationServerProperties
     */
    @NestedConfigurationProperty
    private AuthorizationServerProperties authorizationServer = new AuthorizationServerProperties();

    /**
     * JWT 配置
     *
     * <p>配置 JWT Token 的签名算法、密钥、Issuer 等信息。</p>
     *
     * @see JwtProperties
     */
    @NestedConfigurationProperty
    private JwtProperties jwt = new JwtProperties();

    /**
     * 预注册的 OAuth2 客户端列表
     *
     * <p>应用启动时会自动注册这些客户端到内存中。
     * 适用于客户端数量较少且固定的场景。</p>
     *
     * <p><strong>注意：</strong>如果需要动态管理客户端，请实现
     * {@link org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository}
     * 接口并注册为 Bean。</p>
     *
     * @see ClientProperties
     */
    private List<ClientProperties> clients = new ArrayList<>();

    /**
     * 安全配置
     *
     * <p>包括密码加密、登录限制、Token 存储等安全相关配置。</p>
     *
     * @see SecurityProperties
     */
    @NestedConfigurationProperty
    private SecurityProperties security = new SecurityProperties();

    // ==================== 内部配置类 ====================

    /**
     * 授权服务器配置属性
     *
     * <p>配置 OAuth2 授权服务器的端点路径、Token 有效期、授权模式等。</p>
     *
     * <h3>常用配置项：</h3>
     * <ul>
     *   <li>{@code enabled} - 是否启用授权服务器</li>
     *   <li>{@code *-endpoint} - 各类端点路径配置</li>
     *   <li>{@code *-time-to-live} - Token 有效期配置</li>
     *   <li>{@code reuse-refresh-tokens} - 是否重用刷新令牌</li>
     * </ul>
     */
    @Data
    public static class AuthorizationServerProperties {

        /**
         * 是否启用授权服务器
         *
         * <p>设置为 true 时，应用将作为一个 OAuth2 授权服务器运行，
         * 提供 /oauth2/authorize、/oauth2/token 等端点。</p>
         *
         * <p><strong>默认值：</strong>false</p>
         * <p><strong>注意：</strong>一个系统中通常只有一个授权服务器，
         * 其他服务应该作为资源服务器启用。</p>
         */
        private boolean enabled = false;

        /**
         * 授权端点路径
         *
         * <p>用于获取授权码（Authorization Code）的端点。</p>
         * <p><strong>默认值：</strong>/oauth2/authorize</p>
         * <p><strong>适用场景：</strong>授权码模式（authorization_code）</p>
         */
        private String authorizationEndpoint = "/oauth2/authorize";

        /**
         * Token 端点路径
         *
         * <p>用于获取访问令牌（Access Token）的端点。</p>
         * <p><strong>默认值：</strong>/oauth2/token</p>
         * <p><strong>支持的授权类型：</strong></p>
         * <ul>
         *   <li>authorization_code - 授权码模式</li>
         *   <li>password - 密码模式（扩展）</li>
         *   <li>sms_code - 短信验证码模式（扩展）</li>
         *   <li>client_credentials - 客户端凭证模式</li>
         *   <li>refresh_token - 刷新令牌</li>
         * </ul>
         */
        private String tokenEndpoint = "/oauth2/token";

        /**
         * Token 撤销端点路径
         *
         * <p>用于撤销（使失效）访问令牌或刷新令牌的端点。</p>
         * <p><strong>默认值：</strong>/oauth2/revoke</p>
         */
        private String tokenRevocationEndpoint = "/oauth2/revoke";

        /**
         * Token 自省端点路径
         *
         * <p>用于查询 Token 信息（是否有效、过期时间等）的端点。</p>
         * <p><strong>默认值：</strong>/oauth2/introspect</p>
         */
        private String tokenIntrospectionEndpoint = "/oauth2/introspect";

        /**
         * JWK Set 端点路径
         *
         * <p>用于获取 JWT 签名公钥的端点，资源服务器使用此端点验证 Token。</p>
         * <p><strong>默认值：</strong>/oauth2/jwks</p>
         */
        private String jwkSetEndpoint = "/oauth2/jwks";

        /**
         * OIDC 用户信息端点路径
         *
         * <p>OpenID Connect 协议中用于获取用户信息的端点。</p>
         * <p><strong>默认值：</strong>/userinfo</p>
         */
        private String oidcUserInfoEndpoint = "/userinfo";

        /**
         * 授权码有效期
         *
         * <p>授权码（Authorization Code）的有效期，过期后无法换取 Token。</p>
         * <p><strong>默认值：</strong>5 分钟</p>
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * # 方式1：使用 ISO-8601 格式
         * authorization-code-time-to-live: PT5M
         *
         * # 方式2：使用 Spring Boot 简化格式
         * authorization-code-time-to-live: 5m
         * }</pre>
         */
        private Duration authorizationCodeTimeToLive = Duration.ofMinutes(5);

        /**
         * 访问令牌有效期
         *
         * <p>访问令牌（Access Token）的有效期，过期后需要使用刷新令牌或重新登录。</p>
         * <p><strong>默认值：</strong>2 小时</p>
         * <p><strong>建议：</strong></p>
         * <ul>
         *   <li>Web 应用：2-24 小时</li>
         *   <li>移动端应用：7-30 天</li>
         *   <li>高安全性应用：15-60 分钟</li>
         * </ul>
         */
        private Duration accessTokenTimeToLive = Duration.ofHours(2);

        /**
         * 刷新令牌有效期
         *
         * <p>刷新令牌（Refresh Token）的有效期，用于在访问令牌过期后获取新的访问令牌。</p>
         * <p><strong>默认值：</strong>7 天</p>
         * <p><strong>注意：</strong>刷新令牌过期后，用户需要重新登录。</p>
         */
        private Duration refreshTokenTimeToLive = Duration.ofDays(7);

        /**
         * 是否重用刷新令牌
         *
         * <p>设置为 true 时，刷新令牌在有效期内可以重复使用；
         * 设置为 false 时，每次使用刷新令牌后都会生成新的刷新令牌。</p>
         *
         * <p><strong>默认值：</strong>false（更安全，推荐使用）</p>
         * <p><strong>安全建议：</strong>生产环境建议设置为 false，
         * 防止刷新令牌被盗用后长期有效。</p>
         */
        private boolean reuseRefreshTokens = false;

        /**
         * 设备码有效期（设备码授权模式）
         *
         * <p>设备码（Device Code）的有效期，用于设备码授权流程。</p>
         * <p><strong>默认值：</strong>5 分钟</p>
         */
        private Duration deviceCodeTimeToLive = Duration.ofMinutes(5);

        /**
         * 是否启用 OIDC 支持
         *
         * <p>OpenID Connect (OIDC) 是基于 OAuth2 的身份认证协议。</p>
         * <p><strong>默认值：</strong>true</p>
         */
        private boolean oidcEnabled = true;
    }

    /**
     * JWT 配置属性
     *
     * <p>配置 JWT Token 的签名算法、密钥信息、Issuer 等。</p>
     *
     * <h3>签名算法选择：</h3>
     * <ul>
     *   <li><strong>RS256 (推荐)</strong> - RSA 签名，需要密钥对，安全性高</li>
     *   <li><strong>ES256</strong> - ECDSA 签名，密钥更短，性能更好</li>
     *   <li><strong>HS256</strong> - HMAC 签名，使用单一密钥，适用于内部服务</li>
     * </ul>
     */
    @Data
    public static class JwtProperties {

        /**
         * JWT 签名算法
         *
         * <p>支持：RS256、RS384、RS512、ES256、ES384、ES512、HS256、HS384、HS512</p>
         * <p><strong>默认值：</strong>RS256</p>
         *
         * <h3>算法选择建议：</h3>
         * <ul>
         *   <li><strong>RS256</strong> - 通用选择，安全性好，支持密钥轮换</li>
         *   <li><strong>ES256</strong> - 移动端或带宽敏感场景</li>
         *   <li><strong>HS256</strong> - 内部微服务，需要共享密钥的场景</li>
         * </ul>
         */
        private String algorithm = "RS256";

        /**
         * RSA 私钥路径（用于签名）
         *
         * <p>指定 PEM 格式的 RSA 私钥文件路径，可以是类路径或文件系统路径。</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * # 类路径
         * private-key-path: classpath:keys/private.pem
         *
         * # 文件系统路径
         * private-key-path: file:/etc/oauth2/private.pem
         * }</pre>
         *
         * <p><strong>注意：</strong>如果不配置，将自动生成临时密钥对（仅用于开发测试）。</p>
         */
        private String privateKeyPath;

        /**
         * RSA 公钥路径（用于验证）
         *
         * <p>指定 PEM 格式的 RSA 公钥文件路径。</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * public-key-path: classpath:keys/public.pem
         * }</pre>
         *
         * <p><strong>注意：</strong>资源服务器可以只配置公钥路径，不需要私钥。</p>
         */
        private String publicKeyPath;

        /**
         * 密钥 ID
         *
         * <p>用于标识密钥，支持密钥轮换。</p>
         * <p><strong>默认值：</strong>carlos-key</p>
         * <p>密钥轮换时，可以通过 key-id 区分新旧密钥。</p>
         */
        private String keyId = "carlos-key";

        /**
         * JWT Issuer
         *
         * <p>JWT Token 的签发者标识，会写入 Token 的 iss 声明。</p>
         *
         * <p><strong>默认值：</strong>http://localhost:8080</p>
         * <p><strong>建议：</strong>使用授权服务器的公网地址或域名</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * issuer: https://auth.example.com
         * }</pre>
         */
        private String issuer = "http://localhost:8080";

        /**
         * JKS 密钥库路径
         *
         * <p>指定 JKS 格式的密钥库文件路径，可以是类路径或文件系统路径。</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * # 类路径
         * key-store: classpath:auth.jks
         *
         * # 文件系统路径
         * key-store: file:/etc/carlos/auth.jks
         * }</pre>
         *
         * <p><strong>生产环境建议：</strong></p>
         * <ul>
         *   <li>使用文件系统路径，避免密钥打包在 JAR 中</li>
         *   <li>将密钥库文件备份到安全的位置</li>
         *   <li>设置强密码保护密钥库</li>
         * </ul>
         */
        private String keyStore;

        /**
         * 密钥库密码
         *
         * <p>访问 JKS 密钥库的密码。</p>
         *
         * <p><strong>安全提示：</strong></p>
         * <ul>
         *   <li>不要在代码中硬编码密码</li>
         *   <li>使用环境变量或配置中心管理密码</li>
         *   <li>生产环境使用强密码（至少 16 位，包含大小写字母、数字、特殊字符）</li>
         * </ul>
         */
        private String keyStorePassword;

        /**
         * 密钥别名
         *
         * <p>密钥在密钥库中的别名。</p>
         * <p><strong>默认值：</strong>auth-key</p>
         */
        private String keyAlias = "auth-key";

        /**
         * 密钥密码
         *
         * <p>访问密钥的密码。如果不配置，默认使用密钥库密码。</p>
         */
        private String keyPassword;

        /**
         * 密钥长度（仅生成新密钥时使用）
         *
         * <p>指定生成 RSA 密钥的长度。</p>
         * <p><strong>可选项：</strong>2048 或 4096</p>
         * <p><strong>默认值：</strong>2048（开发测试）</p>
         * <p><strong>生产环境建议：</strong>4096</p>
         */
        private Integer keySize = 2048;

        /**
         * Token 中是否包含用户信息
         *
         * <p>设置为 true 时，Token 中会包含用户 ID、角色、权限等信息。</p>
         * <p><strong>默认值：</strong>true</p>
         *
         * <p><strong>注意事项：</strong></p>
         * <ul>
         *   <li>true - Token 较大，但资源服务器可以直接获取用户信息，减少查询</li>
         *   <li>false - Token 较小，资源服务器需要从 userinfo 端点获取用户信息</li>
         * </ul>
         */
        private boolean includeUserInfo = true;

        /**
         * Token 自定义声明
         *
         * <p>添加自定义的 JWT 声明（Claims）。</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * custom-claims:
         *   app-version: 3.0.0
         *   env: production
         * }</pre>
         */
        private java.util.Map<String, Object> customClaims = new java.util.HashMap<>();
    }

    /**
     * OAuth2 客户端配置属性
     *
     * <p>配置单个 OAuth2 客户端的信息，包括客户端 ID、密钥、授权类型等。</p>
     *
     * <h3>常用客户端类型配置示例：</h3>
     *
     * <h4>1. Web 应用（授权码模式）</h4>
     * <pre>{@code
     * - client-id: web-app
     *   client-secret: web-secret
     *   client-name: Web Application
     *   authorization-grant-types:
     *     - authorization_code
     *     - refresh_token
     *   redirect-uris:
     *     - http://localhost:8080/login/oauth2/code/carlos
     *   scopes:
     *     - read
     *     - write
     *   require-authorization-consent: false
     * }</pre>
     *
     * <h4>2. 移动端应用（PKCE）</h4>
     * <pre>{@code
     * - client-id: mobile-app
     *   client-name: Mobile Application
     *   authorization-grant-types:
     *     - authorization_code
     *     - refresh_token
     *   redirect-uris:
     *     - com.example.app:/oauth2redirect
     *   scopes:
     *     - read
     *   require-proof-key: true  # 启用 PKCE
     * }</pre>
     *
     * <h4>3. 后端服务（客户端凭证模式）</h4>
     * <pre>{@code
     * - client-id: backend-service
     *   client-secret: service-secret
     *   client-name: Backend Service
     *   authorization-grant-types:
     *     - client_credentials
     *   scopes:
     *     - internal
     * }</pre>
     */
    @Data
    public static class ClientProperties {

        /**
         * 客户端 ID
         *
         * <p>客户端的唯一标识，用于识别不同的应用或系统。</p>
         * <p><strong>示例：</strong>web-app、mobile-app、backend-service</p>
         */
        private String clientId;

        /**
         * 客户端密钥
         *
         * <p>客户端的密码，用于客户端认证。</p>
         *
         * <p><strong>安全提示：</strong></p>
         * <ul>
         *   <li>生产环境应使用强随机字符串</li>
         *   <li>不要在代码中硬编码密钥</li>
         *   <li>定期轮换密钥</li>
         *   <li>公共客户端（如 SPA、移动端）可以不需要密钥</li>
         * </ul>
         */
        private String clientSecret;

        /**
         * 客户端名称
         *
         * <p>客户端的可读名称，用于展示。</p>
         * <p><strong>示例：</strong>Web Application、Mobile App</p>
         */
        private String clientName;

        /**
         * 授权类型（Grant Types）
         *
         * <p>客户端支持的 OAuth2 授权类型。</p>
         *
         * <h3>支持的类型：</h3>
         * <ul>
         *   <li><strong>authorization_code</strong> - 授权码模式，最安全，推荐用于 Web、移动端</li>
         *   <li><strong>password</strong> - 密码模式，用于受信任的客户端（已废弃，不建议使用）</li>
         *   <li><strong>client_credentials</strong> - 客户端凭证模式，用于服务间调用</li>
         *   <li><strong>refresh_token</strong> - 刷新令牌</li>
         *   <li><strong>sms_code</strong> - 短信验证码模式（本框架扩展）</li>
         * </ul>
         */
        private List<String> authorizationGrantTypes = new ArrayList<>();

        /**
         * 重定向 URI 列表
         *
         * <p>授权码模式下，授权服务器重定向回客户端的地址。</p>
         *
         * <p><strong>安全提示：</strong>必须精确匹配，不能使用通配符</p>
         * <p><strong>示例：</strong></p>
         * <pre>{@code
         * redirect-uris:
         *   - http://localhost:8080/login/oauth2/code/carlos
         *   - https://app.example.com/callback
         * }</pre>
         */
        private List<String> redirectUris = new ArrayList<>();

        /**
         * 作用域（Scopes）
         *
         * <p>定义客户端可以请求的权限范围。</p>
         *
         * <h3>标准作用域：</h3>
         * <ul>
         *   <li><strong>openid</strong> - OIDC 必需</li>
         *   <li><strong>profile</strong> - 访问用户基本信息</li>
         *   <li><strong>email</strong> - 访问用户邮箱</li>
         *   <li><strong>read</strong> - 读取权限</li>
         *   <li><strong>write</strong> - 写入权限</li>
         *   <li><strong>admin</strong> - 管理权限</li>
         * </ul>
         *
         * <p><strong>自定义作用域：</strong>可以定义业务相关的作用域，如 order:read、user:write</p>
         */
        private List<String> scopes = new ArrayList<>();

        /**
         * 是否需要授权确认
         *
         * <p>设置为 true 时，用户在登录后需要确认授权客户端访问其信息。</p>
         * <p><strong>默认值：</strong>false</p>
         *
         * <p><strong>适用场景：</strong></p>
         * <ul>
         *   <li>true - 第三方应用，需要用户明确同意</li>
         *   <li>false - 第一方应用，跳过确认步骤</li>
         * </ul>
         */
        private boolean requireAuthorizationConsent = false;

        /**
         * 是否需要 PKCE
         *
         * <p>PKCE (Proof Key for Code Exchange) 用于保护授权码流程，
         * 防止授权码被拦截。</p>
         *
         * <p><strong>默认值：</strong>false</p>
         * <p><strong>建议：</strong>公共客户端（SPA、移动端）设置为 true</p>
         */
        private boolean requireProofKey = false;

        /**
         * 访问令牌有效期（覆盖全局配置）
         *
         * <p>此客户端特定的访问令牌有效期，如不设置则使用全局配置。</p>
         */
        private Duration accessTokenTimeToLive;

        /**
         * 刷新令牌有效期（覆盖全局配置）
         *
         * <p>此客户端特定的刷新令牌有效期，如不设置则使用全局配置。</p>
         */
        private Duration refreshTokenTimeToLive;
    }

    /**
     * 安全配置属性
     *
     * <p>密码加密、登录限制、Token 存储等安全相关配置。</p>
     */
    @Data
    public static class SecurityProperties {

        /**
         * 密码编码器类型
         *
         * <p>支持：bcrypt、sm4（国密）</p>
         * <p><strong>默认值：</strong>bcrypt</p>
         *
         * <h3>算法说明：</h3>
         * <ul>
         *   <li><strong>bcrypt</strong> - 业界标准，安全性高，推荐</li>
         *   <li><strong>sm4</strong> - 国密算法，符合国内合规要求</li>
         * </ul>
         */
        private String passwordEncoder = "bcrypt";

        /**
         * 登录失败限制
         *
         * <p>防止暴力破解密码。</p>
         *
         * @see LoginLimitProperties
         */
        @NestedConfigurationProperty
        private LoginLimitProperties loginLimit = new LoginLimitProperties();

        /**
         * 登录限制配置
         */
        @Data
        public static class LoginLimitProperties {

            /**
             * 是否启用登录限制
             * <p><strong>默认值：</strong>true</p>
             */
            private boolean enabled = true;

            /**
             * 最大失败次数
             * <p><strong>默认值：</strong>5</p>
             */
            private int maxAttempts = 5;

            /**
             * 锁定时间
             * <p><strong>默认值：</strong>30 分钟</p>
             */
            private Duration lockDuration = Duration.ofMinutes(30);
        }
    }
}
