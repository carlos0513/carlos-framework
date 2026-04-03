package com.carlos.auth.config.converter;

import com.carlos.auth.pojo.OAuth2AuthorizationPOJO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCodeType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * OAuth2 授权信息转换器
 *
 * <p>负责 OAuth2Authorization 和 OAuth2AuthorizationPOJO 之间的转换</p>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
public class OAuth2AuthorizationConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 OAuth2Authorization 转换为 POJO
     *
     * @param authorization OAuth2Authorization
     * @return OAuth2AuthorizationPOJO
     */
    public static OAuth2AuthorizationPOJO toPOJO(OAuth2Authorization authorization) {
        if (authorization == null) {
            return null;
        }

        OAuth2AuthorizationPOJO pojo = new OAuth2AuthorizationPOJO();
        
        pojo.setId(authorization.getId());
        pojo.setRegisteredClientId(authorization.getRegisteredClientId());
        pojo.setPrincipalName(authorization.getPrincipalName());
        pojo.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        pojo.setAuthorizedScopes(CollectionUtils.isEmpty(authorization.getAuthorizedScopes()) 
            ? null 
            : authorization.getAuthorizedScopes().stream().toList());
        pojo.setAttributes(convertAttributes(authorization.getAttributes()));
        pojo.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

        // 转换授权码
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = 
            authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            pojo.setAuthorizationCodeValue(authorizationCode.getToken().getTokenValue());
            pojo.setAuthorizationCodeIssuedAt(authorizationCode.getToken().getIssuedAt());
            pojo.setAuthorizationCodeExpiresAt(authorizationCode.getToken().getExpiresAt());
            pojo.setAuthorizationCodeMetadata(convertTokenMetadata(authorizationCode));
        }

        // 转换访问令牌
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = 
            authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            pojo.setAccessTokenValue(accessToken.getToken().getTokenValue());
            pojo.setAccessTokenIssuedAt(accessToken.getToken().getIssuedAt());
            pojo.setAccessTokenExpiresAt(accessToken.getToken().getExpiresAt());
            pojo.setAccessTokenMetadata(convertTokenMetadata(accessToken));
            pojo.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            pojo.setAccessTokenScopes(CollectionUtils.isEmpty(accessToken.getToken().getScopes()) 
                ? null 
                : accessToken.getToken().getScopes().stream().toList());
        }

        // 转换刷新令牌
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = 
            authorization.getRefreshToken();
        if (refreshToken != null) {
            pojo.setRefreshTokenValue(refreshToken.getToken().getTokenValue());
            pojo.setRefreshTokenIssuedAt(refreshToken.getToken().getIssuedAt());
            pojo.setRefreshTokenExpiresAt(refreshToken.getToken().getExpiresAt());
            pojo.setRefreshTokenMetadata(convertTokenMetadata(refreshToken));
        }

        // 转换 OIDC ID 令牌
        OAuth2Authorization.Token<OidcIdToken> idToken = 
            authorization.getToken(OidcIdToken.class);
        if (idToken != null) {
            pojo.setOidcIdTokenValue(idToken.getToken().getTokenValue());
            pojo.setOidcIdTokenIssuedAt(idToken.getToken().getIssuedAt());
            pojo.setOidcIdTokenExpiresAt(idToken.getToken().getExpiresAt());
            pojo.setOidcIdTokenMetadata(convertTokenMetadata(idToken));
            pojo.setOidcIdTokenClaims(idToken.getToken().getClaims());
        }

        return pojo;
    }

    /**
     * 将 POJO 转换为 OAuth2Authorization
     *
     * @param pojo POJO
     * @param registeredClient RegisteredClient
     * @return OAuth2Authorization
     */
    public static OAuth2Authorization fromPOJO(OAuth2AuthorizationPOJO pojo, RegisteredClient registeredClient) {
        if (pojo == null || registeredClient == null) {
            return null;
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
            .id(pojo.getId())
            .principalName(pojo.getPrincipalName())
            .authorizationGrantType(new AuthorizationGrantType(pojo.getAuthorizationGrantType()));

        // 授权范围
        if (!CollectionUtils.isEmpty(pojo.getAuthorizedScopes())) {
            builder.authorizedScopes(Set.copyOf(pojo.getAuthorizedScopes()));
        }

        // 属性
        if (!CollectionUtils.isEmpty(pojo.getAttributes())) {
            pojo.getAttributes().forEach(builder::attribute);
        }

        // State
        if (StringUtils.hasText(pojo.getState())) {
            builder.attribute(OAuth2ParameterNames.STATE, pojo.getState());
        }

        // 授权码
        if (StringUtils.hasText(pojo.getAuthorizationCodeValue())) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                pojo.getAuthorizationCodeValue(),
                pojo.getAuthorizationCodeIssuedAt(),
                pojo.getAuthorizationCodeExpiresAt()
            );
            builder.token(authorizationCode, metadata -> {
                if (!CollectionUtils.isEmpty(pojo.getAuthorizationCodeMetadata())) {
                    metadata.putAll(pojo.getAuthorizationCodeMetadata());
                }
            });
        }

        // 访问令牌
        if (StringUtils.hasText(pojo.getAccessTokenValue())) {
            OAuth2AccessToken.TokenType tokenType = OAuth2AccessToken.TokenType.BEARER;
            if (StringUtils.hasText(pojo.getAccessTokenType())) {
                tokenType = new OAuth2AccessToken.TokenType(pojo.getAccessTokenType());
            }

            Set<String> scopes = CollectionUtils.isEmpty(pojo.getAccessTokenScopes()) 
                ? null 
                : Set.copyOf(pojo.getAccessTokenScopes());

            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                tokenType,
                pojo.getAccessTokenValue(),
                pojo.getAccessTokenIssuedAt(),
                pojo.getAccessTokenExpiresAt(),
                scopes
            );
            builder.token(accessToken, metadata -> {
                if (!CollectionUtils.isEmpty(pojo.getAccessTokenMetadata())) {
                    metadata.putAll(pojo.getAccessTokenMetadata());
                }
            });
        }

        // 刷新令牌
        if (StringUtils.hasText(pojo.getRefreshTokenValue())) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                pojo.getRefreshTokenValue(),
                pojo.getRefreshTokenIssuedAt(),
                pojo.getRefreshTokenExpiresAt()
            );
            builder.token(refreshToken, metadata -> {
                if (!CollectionUtils.isEmpty(pojo.getRefreshTokenMetadata())) {
                    metadata.putAll(pojo.getRefreshTokenMetadata());
                }
            });
        }

        // OIDC ID 令牌
        if (StringUtils.hasText(pojo.getOidcIdTokenValue())) {
            OidcIdToken idToken = new OidcIdToken(
                pojo.getOidcIdTokenValue(),
                pojo.getOidcIdTokenIssuedAt(),
                pojo.getOidcIdTokenExpiresAt(),
                pojo.getOidcIdTokenClaims()
            );
            builder.token(idToken, metadata -> {
                if (!CollectionUtils.isEmpty(pojo.getOidcIdTokenMetadata())) {
                    metadata.putAll(pojo.getOidcIdTokenMetadata());
                }
            });
        }

        return builder.build();
    }

    /**
     * 转换属性 Map
     *
     * @param attributes 属性 Map
     * @return 可序列化的 Map
     */
    private static Map<String, Object> convertAttributes(Map<String, Object> attributes) {
        if (CollectionUtils.isEmpty(attributes)) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        attributes.forEach((key, value) -> {
            // 只保存可序列化的简单类型
            if (isSerializable(value)) {
                result.put(key, value);
            } else {
                log.debug("Skipping non-serializable attribute: {} = {}", key, value.getClass().getName());
            }
        });

        return result.isEmpty() ? null : result;
    }

    /**
     * 转换令牌元数据
     *
     * @param token 令牌
     * @return 元数据 Map
     */
    private static Map<String, Object> convertTokenMetadata(OAuth2Authorization.Token<?> token) {
        if (token == null) {
            return null;
        }

        Map<String, Object> metadata = new HashMap<>();
        if (token.isActive()) {
            metadata.put("active", true);
        }
        if (token.isInvalidated()) {
            metadata.put("invalidated", true);
        }
        return metadata.isEmpty() ? null : metadata;
    }

    /**
     * 检查对象是否可序列化
     *
     * @param obj 对象
     * @return true-可序列化
     */
    private static boolean isSerializable(Object obj) {
        if (obj == null) {
            return true;
        }
        return obj instanceof String
            || obj instanceof Number
            || obj instanceof Boolean
            || obj instanceof Instant
            || obj instanceof java.time.LocalDateTime
            || obj instanceof java.time.LocalDate
            || obj instanceof java.util.Date
            || obj instanceof java.util.Collection
            || obj instanceof java.util.Map
            || obj.getClass().isArray();
    }
}
