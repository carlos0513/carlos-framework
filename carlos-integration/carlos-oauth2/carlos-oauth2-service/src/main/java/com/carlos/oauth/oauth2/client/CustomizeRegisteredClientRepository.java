package com.carlos.oauth.oauth2.client;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ComponentException;
import com.carlos.oauth.app.pojo.dto.AppClientDTO;
import com.carlos.oauth.app.pojo.dto.Oauth2ClientSettings;
import com.carlos.oauth.app.pojo.dto.Oauth2TokenSettings;
import com.carlos.oauth.app.service.AppClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 自定义client仓库  此处提供仓库的来源
 * <p>
 * 用于定义、初始化客户端信息 将ClientDetailsServiceConfigurer（从您的回调AuthorizationServerConfigurer）可以用来定义一个内存中或JDBC实现客户的细节服务。客户的重要属性是:
 * clientId：（必填）客户端ID。 secret：（对于受信任的客户端是必需的）客户端密钥（如果有）。 scope：客户端的范围受到限制。如果范围未定义或为空（默认值），则客户端不受范围的限制。
 * authorizedGrantTypes：授权客户使用的授权类型。默认值为空。 authorities：授予客户端的权限（常规的Spring Security权限）。
 * 可以通过直接访问底层存储（例如的情况下为数据库表JdbcClientDetailsService）或通过ClientDetailsManager接口（这两种实现都ClientDetailsService可以实现）来更新正在运行的应用程序中的客户端详细信息。
 *
 * @author carlos
 * @date 2022/11/4 13:04
 */
@Slf4j
@RequiredArgsConstructor
public class CustomizeRegisteredClientRepository implements RegisteredClientRepository {

    private final AppClientService clientService;


    @Override
    public void save(RegisteredClient registeredClient) {
        AppClientDTO client = new AppClientDTO();
        client.setAppKey(registeredClient.getClientId());
        client.setAppName(registeredClient.getClientName());
        client.setAppSecret(registeredClient.getClientSecret());
        client.setClientSecretExpiresAt(LocalDateTime.ofInstant(Objects.requireNonNull(registeredClient.getClientSecretExpiresAt()), ZoneId.systemDefault()));
        client.setClientIssuedAt(LocalDateTime.ofInstant(Objects.requireNonNull(registeredClient.getClientIdIssuedAt()), ZoneId.systemDefault()));
        client.setAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet()));
        client.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet()));
        client.setScopes(registeredClient.getScopes());
        client.setRedirectUris(registeredClient.getRedirectUris());
        Optional.ofNullable(registeredClient.getClientSettings()).ifPresent(setting -> {
            Oauth2ClientSettings settings = new Oauth2ClientSettings();
            settings.setJwkSetUrl(setting.getJwkSetUrl());
            client.setClientSettings(settings);
        });
        Optional.ofNullable(registeredClient.getTokenSettings()).ifPresent(setting -> {
            Oauth2TokenSettings settings = new Oauth2TokenSettings();
            settings.setAuthorizationCodeTimeToLive(Optional.ofNullable(setting.getAuthorizationCodeTimeToLive()).map(Duration::toMinutes).orElse(null));
            settings.setAccessTokenTimeToLive(Optional.ofNullable(setting.getAccessTokenTimeToLive()).map(Duration::toMinutes).orElse(null));
            settings.setAccessTokenFormat(Optional.ofNullable(setting.getAccessTokenFormat()).map(i -> i.getValue()).orElse(null));
            settings.setRefreshTokenTimeToLive(Optional.ofNullable(setting.getRefreshTokenTimeToLive()).map(Duration::toMinutes).orElse(null));
            client.setTokenSettings(settings);
        });
        clientService.addAppClient(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        AppClientDTO dto = null;
        try {
            dto = clientService.findById(id, false);
        } catch (Exception e) {
            log.error("read client info error, id:{}", id, e);
            throw new ComponentException("读取客户端信息失败");
        }
        return convert(dto);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        AppClientDTO dto = null;
        try {
            dto = clientService.findByAppkey(clientId, false);
        } catch (Exception e) {
            log.error("read client info error, clientId:{}", clientId, e);
            throw new ComponentException("读取客户端信息失败");
        }
        return convert(dto);
    }

    private RegisteredClient convert(AppClientDTO dto) {
        RegisteredClient.Builder builder = RegisteredClient
                .withId(String.valueOf(dto.getId()))
                .clientId(dto.getAppKey())
                .clientName(dto.getAppName())
                .clientSecret(dto.getAppSecret())
                .clientSecretExpiresAt(dto.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC))
                .clientIdIssuedAt(dto.getClientIssuedAt().toInstant(ZoneOffset.UTC));
        // 配置客户端相关的配置项，包括验证密钥或者 是否需要授权页面
        Optional.ofNullable(dto.getClientSettings()).ifPresent(settings -> {
            ClientSettings.Builder clientBuilder = ClientSettings.builder()
                    .requireAuthorizationConsent(settings.getRequireAuthorizationConsent())
                    .requireProofKey(settings.getRequireProofKey());
            if (StrUtil.isNotBlank(settings.getJwkSetUrl())) {
                clientBuilder.jwkSetUrl(settings.getJwkSetUrl());
            }

            builder.clientSettings(clientBuilder.build());
        });
        // //JWT的配置项 包括TTL  是否复用refreshToken等等
        Optional.ofNullable(dto.getTokenSettings()).ifPresent(settings -> {
            builder.tokenSettings(TokenSettings.builder()
                    .authorizationCodeTimeToLive(Optional.ofNullable(settings.getAuthorizationCodeTimeToLive()).map(Duration::ofMinutes).orElse(Duration.ofMinutes(5)))
                    .accessTokenTimeToLive(Optional.ofNullable(settings.getAccessTokenTimeToLive()).map(Duration::ofMinutes).orElse(Duration.ofMinutes(5)))
                    .accessTokenFormat(Optional.ofNullable(settings.getAccessTokenFormat()).map(OAuth2TokenFormat::new).orElse(OAuth2TokenFormat.SELF_CONTAINED))
                    .reuseRefreshTokens(settings.getReuseRefreshTokens())
                    .refreshTokenTimeToLive(Optional.ofNullable(settings.getRefreshTokenTimeToLive()).map(Duration::ofMinutes).orElse(Duration.ofMinutes(60)))
                    .build());
        });

        // 授权方法
        Optional.ofNullable(dto.getAuthenticationMethods())
                .ifPresent(methods -> methods.forEach(s -> builder.clientAuthenticationMethod(new ClientAuthenticationMethod(s))));
        // 授权模式
        Optional.ofNullable(dto.getAuthorizationGrantTypes())
                .ifPresent(grants -> grants.forEach(s -> builder.authorizationGrantType(new AuthorizationGrantType(s))));
        // 回调地址
        Optional.ofNullable(dto.getRedirectUris()).ifPresent(uris -> uris.forEach(builder::redirectUri));
        // scope
        Optional.ofNullable(dto.getScopes()).ifPresent(scopes -> scopes.forEach(builder::scope));
        return builder.build();
    }


}
