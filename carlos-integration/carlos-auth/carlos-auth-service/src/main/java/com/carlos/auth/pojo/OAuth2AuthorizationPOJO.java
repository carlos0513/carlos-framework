package com.carlos.auth.pojo;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 授权信息 POJO
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
public class OAuth2AuthorizationPOJO {

    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    private List<String> authorizedScopes;
    private Map<String, Object> attributes;
    private String state;

    // 授权码
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;
    private Map<String, Object> authorizationCodeMetadata;

    // 访问令牌
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    private Map<String, Object> accessTokenMetadata;
    private String accessTokenType;
    private List<String> accessTokenScopes;

    // 刷新令牌
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;
    private Map<String, Object> refreshTokenMetadata;

    // OIDC ID 令牌
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;
    private Map<String, Object> oidcIdTokenMetadata;
    private Map<String, Object> oidcIdTokenClaims;
}
