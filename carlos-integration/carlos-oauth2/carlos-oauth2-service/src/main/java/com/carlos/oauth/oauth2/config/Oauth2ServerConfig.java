package com.carlos.oauth.oauth2.config;

import com.carlos.oauth.app.service.AppClientService;
import com.carlos.oauth.oauth2.Sm4PasswordEncoder;
import com.carlos.oauth.oauth2.client.ClientSecretPostBodyAuthenticationConverter;
import com.carlos.oauth.oauth2.client.CustomizeClientOAuth2AccessTokenGenerator;
import com.carlos.oauth.oauth2.client.CustomizeClientOAuth2TokenCustomizer;
import com.carlos.oauth.oauth2.client.CustomizeRegisteredClientRepository;
import com.carlos.oauth.oauth2.customize.CustomizeOAuth2AuthorizationConsentService;
import com.carlos.oauth.oauth2.customize.CustomizeOAuth2AuthorizationService;
import com.carlos.oauth.oauth2.user.CustomizeUserOAuth2AccessTokenGenerator;
import com.carlos.oauth.oauth2.user.CustomizeUserOAuth2TokenCustomizer;
import com.carlos.oauth.security.handle.CustomAuthenticationFailureHandler;
import com.carlos.oauth.security.handle.CustomAuthenticationSuccessHandler;
import com.carlos.oauth.support.core.YJDaoAuthenticationProvider;
import com.carlos.oauth.support.password.PasswordAuthenticationConverter;
import com.carlos.oauth.support.password.PasswordAuthenticationProvider;
import com.carlos.oauth.support.sms.SmsAuthenticationConverter;
import com.carlos.oauth.support.sms.SmsAuthenticationProvider;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * <p>
 * 认证服务器配置
 * <p>
 * GET::/oauth2/authorize POST::/oauth2/authorize POST::/oauth2/token POST::/oauth2/introspect POST::/oauth2/revoke
 * GET::/.well-known/openid-configuration GET::/userinfo POST::/userinfo GET::/oauth2/jwks
 * GET::/.well-known/oauth-authorization-server
 * </p>
 *
 * @author yunjin
 * @date 2021/12/6 10:26
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class Oauth2ServerConfig {
    private final AppClientService clientService;


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 认证端点配置 可以对认证服务进行自定义配置
        OAuth2AuthorizationServerConfigurer authServerConfig = new OAuth2AuthorizationServerConfigurer();

        authServerConfig.tokenEndpoint(tokenEndpoint -> {
            // 注入自定义的授权认证Converter
            tokenEndpoint
                    // 登录信息转换器
                    .accessTokenRequestConverter(
                            new DelegatingAuthenticationConverter(
                                    Lists.newArrayList(
                                            // 密码模式
                                            new PasswordAuthenticationConverter(),
                                            // 手机模式
                                            new SmsAuthenticationConverter(),
                                            // 刷新模式
                                            new OAuth2RefreshTokenAuthenticationConverter(),
                                            // 授权码模式
                                            new OAuth2AuthorizationCodeAuthenticationConverter(),
                                            // 客户端模式
                                            new OAuth2ClientCredentialsAuthenticationConverter(),
                                            new OAuth2AuthorizationCodeRequestAuthenticationConverter()
                                    )
                            )
                    )
                    // 登录成功处理器
                    .accessTokenResponseHandler(new CustomAuthenticationSuccessHandler())
                    // 登录失败处理器
                    .errorResponseHandler(new CustomAuthenticationFailureHandler());
        });
        // 配置client信息转换器
        authServerConfig.clientAuthentication(clientAuthConfig -> {
            // 配置复合转换器可以支持多种 客户端信息转换
            AuthenticationConverter converter = new DelegatingAuthenticationConverter(
                    Lists.newArrayList(
                            new JwtClientAssertionAuthenticationConverter(),
                            new ClientSecretBasicAuthenticationConverter(),
                            new ClientSecretPostAuthenticationConverter(),
                            new ClientSecretPostBodyAuthenticationConverter(),
                            new PublicClientAuthenticationConverter())
            );
            clientAuthConfig.authenticationConverter(converter);
            // .authenticationSuccessHandler(new CustomClientAuthenticationSuccessHandler())
            // .errorResponseHandler(new CustomClientAuthenticationFailureHandler());
        });
        authServerConfig.authorizationEndpoint(
                authorizationEndpoint ->
                        // 授权码端点个性化confirm页面
                        authorizationEndpoint.consentPage("/token/confirm_access"));
        // TODO: Carlos 2026-02-22 - Commented out due to API incompatibility with OAuth2 Authorization Server 0.4.5
        // http.apply(authServerConfig);
        // ;
        // // 自定义token 存储
        // // .registeredClientRepository(registeredClientRepository())
        // http.requestMatcher(authServerConfig.getEndpointsMatcher()).authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated()).apply(authServerConfig.authorizationService(authorizationService()).authorizationConsentService(authorizationConsentService())).and()
        //         // 授权码登录的登录页个性化
        //         .apply(new FormIdentityLoginConfigurer());
        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);
        return http.build();
    }


    /**
     * 自定义客户端管理
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new CustomizeRegisteredClientRepository(clientService);
    }

    /**
     * 注入授权模式实现提供方
     * <p>
     * 1. 密码模式 </br> 2. 短信登录 </br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        PasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new PasswordAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        SmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new SmsAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new YJDaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }


    /**
     * 资源拥有者的OAuth2授权状态信息OAuth2Authorization也需要持久化管理，Spring Authorization
     * Server提供了OAuth2AuthorizationService来负责这个工作，我们同样需要启用内置的JDBC实现以代替默认的内存实现：
     */
    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new CustomizeOAuth2AuthorizationService();
    }

    /**
     * 授权确认状态持久化 如果该客户端配置ClientSettings开启了授权确认REQUIRE_AUTHORIZATION_CONSENT ,授权确认的信息也要持久化管理，需要启用内置的JDBC实现以代替默认的内存实现：
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new CustomizeOAuth2AuthorizationConsentService();
    }

    /**
     * AuthorizationServerSettings Spring Authorization Server的实例 主要功能包括：
     * <p>
     * 端点路径配置 定义授权端点（/oauth2/authorize）、令牌端点（/oauth2/token）等 URL 路径。 签发者（Issuer）设置 指定授权服务器的唯一标识（如
     * https://auth.example.com ），用于 JWT 令牌的 iss 声明。 其他元数据 如令牌响应格式、支持的签名算法等。
     */
    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    /**
     * <p>
     * 加密工具
     * </p>
     *
     * @author yunjin
     * @date 2022/11/4 13:33
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sm4PasswordEncoder();
    }


    /**
     * 令牌生成规则实现 </br> client:username:uuid 区分客户端令牌 用户令牌
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator() {
        CustomizeUserOAuth2AccessTokenGenerator userTokenGenerator = new CustomizeUserOAuth2AccessTokenGenerator();
        // 用户令牌
        userTokenGenerator.setAccessTokenCustomizer(new CustomizeUserOAuth2TokenCustomizer());

        // client 令牌
        CustomizeClientOAuth2AccessTokenGenerator clientTokenGenerator = new CustomizeClientOAuth2AccessTokenGenerator();
        clientTokenGenerator.setAccessTokenCustomizer(new CustomizeClientOAuth2TokenCustomizer());

        // 刷新令牌
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(userTokenGenerator, clientTokenGenerator, refreshTokenGenerator);
    }

}
