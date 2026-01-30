package com.carlos.oauth2.config;

import com.carlos.oauth2.enhancer.JwtTokenEnhancer;
import com.carlos.oauth2.service.DefaultOAuth2UserDetailsService;
import com.carlos.oauth2.service.OAuth2UserDetailsService;
import com.carlos.oauth2.util.JwtKeyUtil;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * OAuth2授权服务器配置
 *
 * @author carlos
 * @date 2026-01-25
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2Properties.class)
@ConditionalOnProperty(prefix = "carlos.oauth2.authorization-server", name = "enabled", havingValue = "true")
public class OAuth2AuthorizationServerConfig {

    private final OAuth2Properties oAuth2Properties;

    /**
     * 授权服务器安全过滤器链
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())
                )
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) ->
                        exceptions.authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")
                        )
                )
                .oauth2ResourceServer((resourceServer) ->
                        resourceServer.jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    /**
     * 默认安全过滤器链
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/login", "/error", "/oauth2/**", "/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 注册客户端仓库
     */
    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        List<RegisteredClient> clients = new ArrayList<>();

        // 从配置文件加载客户端
        if (oAuth2Properties.getClients() != null && !oAuth2Properties.getClients().isEmpty()) {
            for (OAuth2Properties.ClientConfig clientConfig : oAuth2Properties.getClients()) {
                clients.add(buildRegisteredClient(clientConfig, passwordEncoder));
            }
        } else {
            // 默认客户端
            clients.add(buildDefaultClient(passwordEncoder));
        }

        log.info("Registered {} OAuth2 clients", clients.size());
        return new InMemoryRegisteredClientRepository(clients);
    }

    /**
     * 构建注册客户端
     */
    private RegisteredClient buildRegisteredClient(OAuth2Properties.ClientConfig config, PasswordEncoder passwordEncoder) {
        RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(config.getClientId())
                .clientSecret(passwordEncoder.encode(config.getClientSecret()))
                .clientName(config.getClientName());

        // 客户端认证方法
        builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);

        // 授权类型
        if (config.getAuthorizationGrantTypes() != null && !config.getAuthorizationGrantTypes().isEmpty()) {
            for (String grantType : config.getAuthorizationGrantTypes()) {
                builder.authorizationGrantType(new AuthorizationGrantType(grantType));
            }
        } else {
            builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
            builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);
            builder.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
        }

        // 重定向URI
        if (config.getRedirectUris() != null && !config.getRedirectUris().isEmpty()) {
            config.getRedirectUris().forEach(builder::redirectUri);
        }

        // 作用域
        if (config.getScopes() != null && !config.getScopes().isEmpty()) {
            config.getScopes().forEach(builder::scope);
        } else {
            builder.scope("read");
            builder.scope("write");
        }

        // 客户端设置
        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder()
                .requireAuthorizationConsent(config.isRequireAuthorizationConsent())
                .requireProofKey(config.isRequireProofKey());
        builder.clientSettings(clientSettingsBuilder.build());

        // Token设置
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();

        if (config.getAccessTokenTimeToLive() != null) {
            tokenSettingsBuilder.accessTokenTimeToLive(config.getAccessTokenTimeToLive());
        } else {
            tokenSettingsBuilder.accessTokenTimeToLive(
                    oAuth2Properties.getAuthorizationServer().getAccessTokenTimeToLive()
            );
        }

        if (config.getRefreshTokenTimeToLive() != null) {
            tokenSettingsBuilder.refreshTokenTimeToLive(config.getRefreshTokenTimeToLive());
        } else {
            tokenSettingsBuilder.refreshTokenTimeToLive(
                    oAuth2Properties.getAuthorizationServer().getRefreshTokenTimeToLive()
            );
        }

        tokenSettingsBuilder.reuseRefreshTokens(
                oAuth2Properties.getAuthorizationServer().isReuseRefreshTokens()
        );

        builder.tokenSettings(tokenSettingsBuilder.build());

        return builder.build();
    }

    /**
     * 构建默认客户端
     */
    private RegisteredClient buildDefaultClient(PasswordEncoder passwordEncoder) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("carlos-client")
                .clientSecret(passwordEncoder.encode("carlos-secret"))
                .clientName("carlos Default Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .redirectUri("http://localhost:8080/login/oauth2/code/carlos")
                .redirectUri("http://localhost:8080/authorized")
                .scope("read")
                .scope("write")
                .scope("all")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(oAuth2Properties.getAuthorizationServer().getAccessTokenTimeToLive())
                        .refreshTokenTimeToLive(oAuth2Properties.getAuthorizationServer().getRefreshTokenTimeToLive())
                        .reuseRefreshTokens(oAuth2Properties.getAuthorizationServer().isReuseRefreshTokens())
                        .build())
                .build();
    }

    /**
     * JWK源
     */
    @Bean
    @ConditionalOnMissingBean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = JwtKeyUtil.generateRsaKey();
        return JwtKeyUtil.createJwkSource(keyPair, oAuth2Properties.getJwt().getKeyId());
    }

    /**
     * JWT解码器
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 授权服务器设置
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationServerSettings authorizationServerSettings() {
        OAuth2Properties.AuthorizationServer config = oAuth2Properties.getAuthorizationServer();

        return AuthorizationServerSettings.builder()
                .issuer(oAuth2Properties.getJwt().getIssuer())
                .authorizationEndpoint(config.getAuthorizationEndpoint())
                .tokenEndpoint(config.getTokenEndpoint())
                .tokenRevocationEndpoint(config.getTokenRevocationEndpoint())
                .tokenIntrospectionEndpoint(config.getTokenIntrospectionEndpoint())
                .jwkSetEndpoint(config.getJwkSetEndpoint())
                .build();
    }

    /**
     * 密码编码器
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 用户详情服务
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserDetailsService oAuth2UserDetailsService(PasswordEncoder passwordEncoder) {
        log.warn("Using default OAuth2UserDetailsService. Please provide your own implementation for production.");
        return new DefaultOAuth2UserDetailsService(passwordEncoder);
    }

    /**
     * JWT Token增强器
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(OAuth2UserDetailsService userDetailsService) {
        if (oAuth2Properties.getJwt().isIncludeUserInfo()) {
            return new JwtTokenEnhancer(userDetailsService);
        }
        return context -> {
            // 不增强Token
        };
    }
}
