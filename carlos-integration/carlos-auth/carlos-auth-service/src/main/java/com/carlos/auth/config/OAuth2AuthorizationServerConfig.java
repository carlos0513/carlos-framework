package com.carlos.auth.config;

import com.carlos.auth.config.repository.JdbcOAuth2AuthorizationService;
import com.carlos.auth.config.repository.RedisOAuth2AuthorizationConsentService;
import com.carlos.auth.config.repository.RedisOAuth2AuthorizationService;
import com.carlos.auth.security.manager.KeyPairManager;
import com.carlos.auth.service.ExtendUserDetailsService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * OAuth2 授权服务器自动配置
 *
 * <p>本配置类基于 Spring Authorization Server 1.x（兼容 Spring Boot 3.x）构建，
 * 提供完整的 OAuth2 授权服务器功能。</p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>配置授权服务器安全过滤器链</li>
 *   <li>注册 OAuth2 客户端</li>
 *   <li>配置 JWT 签名和验证</li>
 *   <li>支持 OIDC 协议</li>
 * </ul>
 *
 * <h3>启用条件：</h3>
 * <p>需要在配置中设置：</p>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     authorization-server:
 *       enabled: true
 * }</pre>
 *
 * <h3>扩展点：</h3>
 * <ul>
 *   <li>{@link ExtendUserDetailsService} - 自定义用户加载逻辑</li>
 *   <li>{@link OAuth2TokenCustomizer} - 自定义 Token 内容</li>
 *   <li>{@link RegisteredClientRepository} - 自定义客户端存储</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2Properties.class)
@ConditionalOnProperty(
    prefix = "carlos.oauth2.authorization-server",
    name = "enabled",
    havingValue = "true"
)
public class OAuth2AuthorizationServerConfig {

    /**
     * OAuth2 配置属性
     */
    private final OAuth2Properties oauth2Properties;

    /**
     * 授权服务器安全过滤器链
     *
     * <p>配置授权服务器的所有端点，包括：</p>
     * <ul>
     *   <li>/oauth2/authorize - 授权端点</li>
     *   <li>/oauth2/token - 令牌端点</li>
     *   <li>/oauth2/introspect - 令牌自省端点</li>
     *   <li>/oauth2/revoke - 令牌撤销端点</li>
     *   <li>/oauth2/jwks - JWK Set 端点</li>
     *   <li>/userinfo - 用户信息端点（OIDC）</li>
     * </ul>
     *
     * <p><strong>优先级：</strong>最高优先级（Ordered.HIGHEST_PRECEDENCE），
     * 确保授权服务器端点在其他过滤器之前处理。</p>
     *
     * <h3>默认端点路径：</h3>
     * <table border="1">
     *   <tr><th>端点</th><th>路径</th><th>说明</th></tr>
     *   <tr><td>授权端点</td><td>/oauth2/authorize</td><td>获取授权码</td></tr>
     *   <tr><td>令牌端点</td><td>/oauth2/token</td><td>获取访问令牌</td></tr>
     *   <tr><td>自省端点</td><td>/oauth2/introspect</td><td>查询令牌信息</td></tr>
     *   <tr><td>撤销端点</td><td>/oauth2/revoke</td><td>撤销令牌</td></tr>
     *   <tr><td>JWK Set</td><td>/oauth2/jwks</td><td>获取公钥</td></tr>
     *   <tr><td>用户信息</td><td>/userinfo</td><td>OIDC 用户信息</td></tr>
     *   <tr><td>OIDC 配置</td><td>/.well-known/openid-configuration</td><td>OIDC 发现端点</td></tr>
     * </table>
     *
     * @param http HttpSecurity 配置构建器
     * @return SecurityFilterChain 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 应用授权服务器默认配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 获取授权服务器配置器
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            // 启用 OIDC 支持（OpenID Connect 1.0）
            .oidc(Customizer.withDefaults());

        // 配置异常处理和登录入口
        http
            // 未认证时重定向到登录页面
            .exceptionHandling(exceptions ->
                exceptions.authenticationEntryPoint(
                    new LoginUrlAuthenticationEntryPoint("/login")
                )
            )
            // 启用 JWT 资源服务器支持（用于验证自己的 Token）
            .oauth2ResourceServer(resourceServer ->
                resourceServer.jwt(Customizer.withDefaults())
            );

        log.info("OAuth2 Authorization Server configured with endpoints:");
        log.info("  - Authorization endpoint: {}", oauth2Properties.getAuthorizationServer().getAuthorizationEndpoint());
        log.info("  - Token endpoint: {}", oauth2Properties.getAuthorizationServer().getTokenEndpoint());
        log.info("  - JWK Set endpoint: {}", oauth2Properties.getAuthorizationServer().getJwkSetEndpoint());

        return http.build();
    }

    /**
     * 默认安全过滤器链
     *
     * <p>处理非授权服务器端点的请求，包括登录页面等。</p>
     *
     * <h3>配置说明：</h3>
     * <ul>
     *   <li>放行登录页面和错误页面</li>
     *   <li>启用表单登录</li>
     *   <li>其他请求需要认证</li>
     * </ul>
     *
     * @param http HttpSecurity 配置构建器
     * @return SecurityFilterChain 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 配置请求授权
            .authorizeHttpRequests(authorize -> authorize
                // 放行的路径
                .requestMatchers(
                    "/login",           // 登录页面
                    "/error",           // 错误页面
                    "/oauth2/**",       // OAuth2 端点（由授权服务器过滤器处理）
                    "/actuator/**",     // 健康检查
                    "/assets/**",       // 静态资源
                    "/webjars/**",      // WebJars 资源
                    "/swagger-ui/**",   // Swagger UI
                    "/doc.html",        // Knife4j 文档
                    "/v3/api-docs/**"   // OpenAPI 文档
                ).permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 禁用 CSRF（OAuth2 使用 Token 机制，不需要 CSRF 保护）
            .csrf(AbstractHttpConfigurer::disable)
            // 启用表单登录
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 注册客户端仓库
     *
     * <p>管理 OAuth2 客户端信息，包括客户端 ID、密钥、授权类型、作用域等。</p>
     *
     * <h3>默认实现：</h3>
     * <p>使用内存存储（InMemoryRegisteredClientRepository），从配置文件加载客户端。</p>
     *
     * <h3>自定义扩展：</h3>
     * <p>如果需要从数据库加载客户端，可以实现 {@link RegisteredClientRepository} 接口
     * 并注册为 Bean，本默认实现会被自动替换。</p>
     *
     * <h3>默认客户端：</h3>
     * <p>如果未配置任何客户端，将自动创建一个默认客户端：</p>
     * <ul>
     *   <li>client-id: carlos-client</li>
     *   <li>client-secret: carlos-secret</li>
     *   <li>授权类型: authorization_code, refresh_token, client_credentials</li>
     *   <li>作用域: read, write, all</li>
     * </ul>
     *
     * @param passwordEncoder 密码编码器
     * @return RegisteredClientRepository 客户端仓库
     */
    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        List<RegisteredClient> clients = new ArrayList<>();

        // 从配置文件加载客户端
        List<OAuth2Properties.ClientProperties> clientConfigs = oauth2Properties.getClients();
        if (clientConfigs != null && !clientConfigs.isEmpty()) {
            for (OAuth2Properties.ClientProperties config : clientConfigs) {
                clients.add(buildRegisteredClient(config, passwordEncoder));
                log.info("Registered OAuth2 client: {}", config.getClientId());
            }
        } else {
            // 创建默认客户端
            clients.add(buildDefaultClient(passwordEncoder));
            log.warn("No OAuth2 clients configured, using default client (client-id: carlos-client)");
            log.warn("Please configure clients in application.yml for production use");
        }

        return new InMemoryRegisteredClientRepository(clients);
    }

    /**
     * 构建注册客户端
     *
     * <p>根据配置属性创建 RegisteredClient 实例。</p>
     *
     * @param config 客户端配置属性
     * @param passwordEncoder 密码编码器
     * @return RegisteredClient 注册客户端
     */
    private RegisteredClient buildRegisteredClient(
        OAuth2Properties.ClientProperties config,
        PasswordEncoder passwordEncoder) {

        RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(config.getClientId())
            .clientName(config.getClientName() != null ? config.getClientName() : config.getClientId());

        // 配置客户端密钥（如果有）
        if (config.getClientSecret() != null && !config.getClientSecret().isEmpty()) {
            builder.clientSecret(passwordEncoder.encode(config.getClientSecret()));
            // 支持客户端密钥基本认证和 POST 认证
            builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
            builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);
        } else {
            // 公共客户端（如 SPA、移动端）不需要密钥
            builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
        }

        // 配置授权类型
        if (config.getAuthorizationGrantTypes() != null && !config.getAuthorizationGrantTypes().isEmpty()) {
            for (String grantType : config.getAuthorizationGrantTypes()) {
                builder.authorizationGrantType(new AuthorizationGrantType(grantType));
            }
        } else {
            // 默认授权类型
            builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
            builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);
            builder.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
        }

        // 配置重定向 URI
        if (config.getRedirectUris() != null) {
            config.getRedirectUris().forEach(builder::redirectUri);
        }

        // 配置作用域
        if (config.getScopes() != null && !config.getScopes().isEmpty()) {
            config.getScopes().forEach(builder::scope);
        } else {
            // 默认作用域
            builder.scope(OidcScopes.OPENID);
            builder.scope(OidcScopes.PROFILE);
            builder.scope("read");
        }

        // 客户端设置
        ClientSettings clientSettings = ClientSettings.builder()
            // 是否需要授权确认
            .requireAuthorizationConsent(config.isRequireAuthorizationConsent())
            // 是否需要 PKCE
            .requireProofKey(config.isRequireProofKey())
            .build();
        builder.clientSettings(clientSettings);

        // Token 设置
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();

        // 访问令牌有效期
        Duration accessTokenTTL = config.getAccessTokenTimeToLive() != null
            ? config.getAccessTokenTimeToLive()
            : oauth2Properties.getAuthorizationServer().getAccessTokenTimeToLive();
        tokenSettingsBuilder.accessTokenTimeToLive(accessTokenTTL);

        // 刷新令牌有效期
        Duration refreshTokenTTL = config.getRefreshTokenTimeToLive() != null
            ? config.getRefreshTokenTimeToLive()
            : oauth2Properties.getAuthorizationServer().getRefreshTokenTimeToLive();
        tokenSettingsBuilder.refreshTokenTimeToLive(refreshTokenTTL);

        // 是否重用刷新令牌
        tokenSettingsBuilder.reuseRefreshTokens(
            oauth2Properties.getAuthorizationServer().isReuseRefreshTokens()
        );

        builder.tokenSettings(tokenSettingsBuilder.build());

        return builder.build();
    }

    /**
     * 构建默认客户端
     *
     * <p>当未配置任何客户端时使用的默认配置。</p>
     *
     * @param passwordEncoder 密码编码器
     * @return RegisteredClient 默认注册客户端
     */
    private RegisteredClient buildDefaultClient(PasswordEncoder passwordEncoder) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("carlos-client")
            .clientSecret(passwordEncoder.encode("carlos-secret"))
            .clientName("Carlos Default Client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://localhost:8080/login/oauth2/code/carlos")
            .redirectUri("http://localhost:8080/authorized")
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/carlos")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope("read")
            .scope("write")
            .scope("all")
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .requireProofKey(false)
                .build())
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(
                    oauth2Properties.getAuthorizationServer().getAccessTokenTimeToLive()
                )
                .refreshTokenTimeToLive(
                    oauth2Properties.getAuthorizationServer().getRefreshTokenTimeToLive()
                )
                .reuseRefreshTokens(
                    oauth2Properties.getAuthorizationServer().isReuseRefreshTokens()
                )
                .build())
            .build();
    }

    /**
     * JWK 源
     *
     * <p>JWT 签名所需的密钥源，包含 RSA 密钥对。</p>
     *
     * <h3>密钥来源：</h3>
     * <ol>
     *   <li>如果配置了 {@code jwt.private-key-path} 和 {@code jwt.public-key-path}，
     *       从文件加载密钥</li>
     *   <li>否则生成临时密钥对（仅用于开发和测试）</li>
     * </ol>
     *
     * <h3>生产环境配置：</h3>
     * <pre>{@code
     * carlos:
     *   oauth2:
     *     jwt:
     *       private-key-path: classpath:keys/private.pem
     *       public-key-path: classpath:keys/public.pem
     *       key-id: prod-key-2024
     * }</pre>
     *
     * @return JWKSource JWT 密钥源
     */
    @Bean
    @ConditionalOnMissingBean
    public JWKSource<SecurityContext> jwkSource() {
        // 从 JKS 文件加载 RSA 密钥对（支持密钥持久化）
        String keyStorePath = oauth2Properties.getJwt().getKeyStore();
        String keyStorePassword = oauth2Properties.getJwt().getKeyStorePassword();
        String keyAlias = oauth2Properties.getJwt().getKeyAlias();
        String keyPassword = oauth2Properties.getJwt().getKeyPassword();
        int keySize = oauth2Properties.getJwt().getKeySize() != null ? oauth2Properties.getJwt().getKeySize() : 2048;

        KeyPair keyPair;
        if (keyStorePath != null && !keyStorePath.isEmpty()) {
            // 从 JKS 文件加载或生成密钥对
            keyPair = KeyPairManager.loadOrGenerateKeyPair(keyStorePath, keyStorePassword, keyAlias, keyPassword, keySize);
            log.info("JWK Source initialized from JKS file: {}, key-id: {}", keyStorePath, oauth2Properties.getJwt().getKeyId());
        } else {
            // 生成临时密钥对（用于开发测试）
            log.warn("No keystore configured, generating in-memory RSA key pair (for development only)");
            keyPair = generateRsaKey();
        }

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 构建 RSAKey
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(oauth2Properties.getJwt().getKeyId())
            .build();

        // 创建 JWK Set
        JWKSet jwkSet = new JWKSet(rsaKey);

        log.info("JWK Source initialized with key-id: {}", oauth2Properties.getJwt().getKeyId());

        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成 RSA 密钥对
     *
     * <p>用于开发测试环境自动生成密钥。</p>
     *
     * <p><strong>警告：</strong>生产环境应使用固定的密钥对，
     * 避免重启后所有 Token 失效。</p>
     *
     * @return KeyPair RSA 密钥对
     */
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            log.error("Failed to generate RSA key pair", e);
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }

    /**
     * JWT 解码器
     *
     * <p>用于验证 JWT Token 的签名。</p>
     *
     * @param jwkSource JWK 密钥源
     * @return JwtDecoder JWT 解码器
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * JWT 编码器
     *
     * <p>用于生成 JWT Token 的签名。</p>
     *
     * @param jwkSource JWK 密钥源
     * @return JwtEncoder JWT 编码器
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * 授权服务器设置
     *
     * <p>配置授权服务器的端点路径和 Issuer 标识。</p>
     *
     * @return AuthorizationServerSettings 授权服务器设置
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationServerSettings authorizationServerSettings() {
        OAuth2Properties.AuthorizationServerProperties config =
            oauth2Properties.getAuthorizationServer();

        return AuthorizationServerSettings.builder()
            // Issuer 标识
            .issuer(oauth2Properties.getJwt().getIssuer())
            // 授权端点
            .authorizationEndpoint(config.getAuthorizationEndpoint())
            // 令牌端点
            .tokenEndpoint(config.getTokenEndpoint())
            // 令牌撤销端点
            .tokenRevocationEndpoint(config.getTokenRevocationEndpoint())
            // 令牌自省端点
            .tokenIntrospectionEndpoint(config.getTokenIntrospectionEndpoint())
            // JWK Set 端点
            .jwkSetEndpoint(config.getJwkSetEndpoint())
            // OIDC 用户信息端点
            .oidcUserInfoEndpoint(config.getOidcUserInfoEndpoint())
            .build();
    }

    /**
     * 密码编码器
     *
     * <p>用于加密客户端密钥和用户密码。</p>
     *
     * <h3>支持的编码器：</h3>
     * <ul>
     *   <li><strong>bcrypt</strong> - 默认，安全性高</li>
     *   <li><strong>sm4</strong> - 国密算法（需配置密钥）</li>
     * </ul>
     *
     * <h3>自定义扩展：</h3>
     * <p>如果需要使用其他编码器，可以注册 PasswordEncoder Bean 替换默认实现。</p>
     *
     * @param oauth2Properties OAuth2 配置属性
     * @return PasswordEncoder 密码编码器
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder(OAuth2Properties oauth2Properties) {
        String encoderType = oauth2Properties.getSecurity().getPasswordEncoder();

        if ("sm4".equalsIgnoreCase(encoderType)) {
            // TODO: Carlos 2026-04-07 需重新组织加密
            // String sm4Key = oauth2Properties.getSecurity().getSm4Key();
            // if (StringUtils.hasText(sm4Key)) {
            //     log.info("Using SM4 password encoder with configured key");
            //     return new SM4PasswordEncoder(sm4Key);
            // } else {
            //     log.warn("SM4 key not configured, falling back to BCrypt");
            //     log.warn("Please configure: carlos.oauth2.security.sm4-key");
            // }
        }

        log.info("Using BCrypt password encoder");
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT Token 自定义增强器
     *
     * <p>向 JWT Token 中添加自定义声明（Claims），如用户 ID、角色等。</p>
     *
     * <h3>默认实现：</h3>
     * <p>如果配置了 {@code jwt.include-user-info=true}，
     * 会添加以下声明：</p>
     * <ul>
     *   <li>user_id - 用户 ID</li>
     *   <li>username - 用户名</li>
     *   <li>role_ids - 角色 ID 列表</li>
     *   <li>authorities - 权限列表</li>
     * </ul>
     *
     * <h3>自定义扩展：</h3>
     * <p>可以注册自己的 {@link OAuth2TokenCustomizer} Bean 替换或增强默认实现。</p>
     *
     * @param userDetailsService 用户详情服务
     * @return OAuth2TokenCustomizer Token 自定义增强器
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(
        ExtendUserDetailsService userDetailsService) {

        if (oauth2Properties.getJwt().isIncludeUserInfo()) {
            log.info("JWT token customizer enabled with user info inclusion");
            return new Oauth2JwtTokenCustomizer(userDetailsService);
        }

        log.info("JWT token customizer enabled without user info");
        return context -> {
            // 不添加用户信息，只添加自定义声明
            if (oauth2Properties.getJwt().getCustomClaims() != null) {
                oauth2Properties.getJwt().getCustomClaims().forEach(
                    (key, value) -> context.getClaims().claim(key, value)
                );
            }
        };
    }

    /**
     * 授权服务
     *
     * <p>管理 OAuth2 授权信息，包括访问令牌、刷新令牌、授权码等。</p>
     *
     * <h3>存储方式：</h3>
     * <ul>
     *   <li><strong>redis</strong> - 基于 Redis 存储（推荐生产环境使用）</li>
     *   <li><strong>jdbc</strong> - 基于 JDBC 数据库存储</li>
     *   <li><strong>memory</strong> - 基于内存存储（仅开发测试使用）</li>
     * </ul>
     *
     * <p>通过配置 {@code carlos.auth.security.token-storage} 选择存储方式。</p>
     *
     * @param redisService Redis 授权服务（如果启用 Redis 存储）
     * @param jdbcService JDBC 授权服务（如果启用 JDBC 存储）
     * @return OAuth2AuthorizationService 授权服务
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationService authorizationService(
            @Autowired(required = false) RedisOAuth2AuthorizationService redisService,
            @Autowired(required = false) JdbcOAuth2AuthorizationService jdbcService) {

        String storageType = oauth2Properties.getSecurity().getTokenStorage();

        if ("redis".equalsIgnoreCase(storageType)) {
            if (redisService != null) {
                log.info("==========================================================================");
                log.info(" Using Redis OAuth2 Authorization Service (production ready)");
                log.info("==========================================================================");
                return redisService;
            } else {
                log.warn("Redis OAuth2 Authorization Service not available, falling back to in-memory storage");
                log.warn("Please ensure Redis is configured and RedisTemplate bean is present");
            }
        } else if ("jdbc".equalsIgnoreCase(storageType)) {
            if (jdbcService != null) {
                log.info("==========================================================================");
                log.info(" Using JDBC OAuth2 Authorization Service (production ready)");
                log.info("==========================================================================");
                return jdbcService;
            } else {
                log.warn("JDBC OAuth2 Authorization Service not available, falling back to in-memory storage");
                log.warn("Please ensure DataSource bean is present and database is configured");
            }
        }

        log.info("Using In-Memory OAuth2 Authorization Service (for development only)");
        log.warn("WARNING: Authorization data will be lost on application restart!");
        return new org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService();
    }

    /**
     * 授权确认服务
     *
     * <p>管理用户的授权确认信息（用户同意客户端访问其资源）。</p>
     *
     * <h3>使用场景：</h3>
     * <p>当客户端配置了 requireAuthorizationConsent=true 时，
     * 用户登录后需要确认授权，确认信息会存储在此服务中。</p>
     *
     * <h3>存储方式：</h3>
     * <ul>
     *   <li><strong>redis</strong> - 基于 Redis 存储（当 token-storage=redis 时自动启用）</li>
     *   <li><strong>memory</strong> - 基于内存存储（当 token-storage=memory 时使用）</li>
     * </ul>
     *
     * @param redisConsentService Redis 授权许可服务（如果启用 Redis 存储）
     * @return OAuth2AuthorizationConsentService 授权确认服务
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationConsentService authorizationConsentService(@Autowired(required = false) RedisOAuth2AuthorizationConsentService redisConsentService) {

        String storageType = oauth2Properties.getSecurity().getTokenStorage();

        if ("redis".equalsIgnoreCase(storageType) && redisConsentService != null) {
            log.info("Using Redis OAuth2 Authorization Consent Service");
            return redisConsentService;
        }

        log.info("Using In-Memory OAuth2 Authorization Consent Service");
        return new org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService();
    }
}
