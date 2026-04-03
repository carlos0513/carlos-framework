package com.carlos.auth.config.repository;

import com.carlos.auth.pojo.OAuth2AuthorizationPOJO;
import com.carlos.auth.config.converter.OAuth2AuthorizationConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * JDBC OAuth2 授权服务实现
 *
 * <p>基于数据库存储 OAuth2 授权信息，包括访问令牌、刷新令牌、授权码等。</p>
 *
 * <h3>表结构：</h3>
 * <pre>{@code
 * CREATE TABLE oauth2_authorization (
 *     id VARCHAR(100) NOT NULL PRIMARY KEY,
 *     registered_client_id VARCHAR(100) NOT NULL,
 *     principal_name VARCHAR(200) NOT NULL,
 *     authorization_grant_type VARCHAR(100) NOT NULL,
 *     authorized_scopes VARCHAR(1000),
 *     attributes VARCHAR(4000),
 *     state VARCHAR(500),
 *     authorization_code_value VARCHAR(4000),
 *     authorization_code_issued_at TIMESTAMP,
 *     authorization_code_expires_at TIMESTAMP,
 *     authorization_code_metadata VARCHAR(2000),
 *     access_token_value VARCHAR(4000),
 *     access_token_issued_at TIMESTAMP,
 *     access_token_expires_at TIMESTAMP,
 *     access_token_metadata VARCHAR(2000),
 *     access_token_type VARCHAR(100),
 *     access_token_scopes VARCHAR(1000),
 *     refresh_token_value VARCHAR(4000),
 *     refresh_token_issued_at TIMESTAMP,
 *     refresh_token_expires_at TIMESTAMP,
 *     refresh_token_metadata VARCHAR(2000),
 *     oidc_id_token_value VARCHAR(4000),
 *     oidc_id_token_issued_at TIMESTAMP,
 *     oidc_id_token_expires_at TIMESTAMP,
 *     oidc_id_token_metadata VARCHAR(2000),
 *     oidc_id_token_claims VARCHAR(2000),
 *     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 *     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 * );
 * }</pre>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 * @see OAuth2AuthorizationService
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "carlos.oauth2.security", name = "token-storage", havingValue = "jdbc")
public class JdbcOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final JdbcTemplate jdbcTemplate;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper objectMapper;

    private static final String TABLE_NAME = "oauth2_authorization";

    // ============ SQL 语句 ============

    private static final String SAVE_SQL = """
        INSERT INTO %s (
            id, registered_client_id, principal_name, authorization_grant_type,
            authorized_scopes, attributes, state,
            authorization_code_value, authorization_code_issued_at, authorization_code_expires_at, authorization_code_metadata,
            access_token_value, access_token_issued_at, access_token_expires_at, access_token_metadata,
            access_token_type, access_token_scopes,
            refresh_token_value, refresh_token_issued_at, refresh_token_expires_at, refresh_token_metadata,
            oidc_id_token_value, oidc_id_token_issued_at, oidc_id_token_expires_at, oidc_id_token_metadata, oidc_id_token_claims
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            registered_client_id = VALUES(registered_client_id),
            principal_name = VALUES(principal_name),
            authorization_grant_type = VALUES(authorization_grant_type),
            authorized_scopes = VALUES(authorized_scopes),
            attributes = VALUES(attributes),
            state = VALUES(state),
            authorization_code_value = VALUES(authorization_code_value),
            authorization_code_issued_at = VALUES(authorization_code_issued_at),
            authorization_code_expires_at = VALUES(authorization_code_expires_at),
            authorization_code_metadata = VALUES(authorization_code_metadata),
            access_token_value = VALUES(access_token_value),
            access_token_issued_at = VALUES(access_token_issued_at),
            access_token_expires_at = VALUES(access_token_expires_at),
            access_token_metadata = VALUES(access_token_metadata),
            access_token_type = VALUES(access_token_type),
            access_token_scopes = VALUES(access_token_scopes),
            refresh_token_value = VALUES(refresh_token_value),
            refresh_token_issued_at = VALUES(refresh_token_issued_at),
            refresh_token_expires_at = VALUES(refresh_token_expires_at),
            refresh_token_metadata = VALUES(refresh_token_metadata),
            oidc_id_token_value = VALUES(oidc_id_token_value),
            oidc_id_token_issued_at = VALUES(oidc_id_token_issued_at),
            oidc_id_token_expires_at = VALUES(oidc_id_token_expires_at),
            oidc_id_token_metadata = VALUES(oidc_id_token_metadata),
            oidc_id_token_claims = VALUES(oidc_id_token_claims),
            update_time = CURRENT_TIMESTAMP
        """.formatted(TABLE_NAME);

    private static final String REMOVE_SQL = "DELETE FROM %s WHERE id = ?".formatted(TABLE_NAME);

    private static final String FIND_BY_ID_SQL = "SELECT * FROM %s WHERE id = ?".formatted(TABLE_NAME);

    private static final String FIND_BY_TOKEN_SQL = """
        SELECT * FROM %s WHERE 
        state = ? OR
        authorization_code_value = ? OR
        access_token_value = ? OR
        refresh_token_value = ?
        """.formatted(TABLE_NAME);

    // ============ 核心方法 ============

    @Override
    @Transactional
    public void save(OAuth2Authorization authorization) {
        if (authorization == null) {
            throw new IllegalArgumentException("authorization cannot be null");
        }

        try {
            OAuth2AuthorizationPOJO pojo = OAuth2AuthorizationConverter.toPOJO(authorization);
            jdbcTemplate.update(SAVE_SQL,
                pojo.getId(),
                pojo.getRegisteredClientId(),
                pojo.getPrincipalName(),
                pojo.getAuthorizationGrantType(),
                toJson(pojo.getAuthorizedScopes()),
                toJson(pojo.getAttributes()),
                pojo.getState(),
                // 授权码
                pojo.getAuthorizationCodeValue(),
                toTimestamp(pojo.getAuthorizationCodeIssuedAt()),
                toTimestamp(pojo.getAuthorizationCodeExpiresAt()),
                toJson(pojo.getAuthorizationCodeMetadata()),
                // 访问令牌
                pojo.getAccessTokenValue(),
                toTimestamp(pojo.getAccessTokenIssuedAt()),
                toTimestamp(pojo.getAccessTokenExpiresAt()),
                toJson(pojo.getAccessTokenMetadata()),
                pojo.getAccessTokenType(),
                toJson(pojo.getAccessTokenScopes()),
                // 刷新令牌
                pojo.getRefreshTokenValue(),
                toTimestamp(pojo.getRefreshTokenIssuedAt()),
                toTimestamp(pojo.getRefreshTokenExpiresAt()),
                toJson(pojo.getRefreshTokenMetadata()),
                // OIDC ID 令牌
                pojo.getOidcIdTokenValue(),
                toTimestamp(pojo.getOidcIdTokenIssuedAt()),
                toTimestamp(pojo.getOidcIdTokenExpiresAt()),
                toJson(pojo.getOidcIdTokenMetadata()),
                toJson(pojo.getOidcIdTokenClaims())
            );

            log.debug("Saved OAuth2Authorization: id={}, principal={}", 
                authorization.getId(), authorization.getPrincipalName());

        } catch (Exception e) {
            log.error("Failed to save OAuth2Authorization: id={}", authorization.getId(), e);
            throw new DataRetrievalFailureException("Failed to save authorization", e);
        }
    }

    @Override
    @Transactional
    public void remove(OAuth2Authorization authorization) {
        if (authorization == null) {
            return;
        }

        int deleted = jdbcTemplate.update(REMOVE_SQL, authorization.getId());
        
        if (deleted > 0) {
            log.debug("Removed OAuth2Authorization: id={}, principal={}", 
                authorization.getId(), authorization.getPrincipalName());
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }

        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new OAuth2AuthorizationRowMapper(), id);
        } catch (Exception e) {
            log.debug("OAuth2Authorization not found by id: {}", id);
            return null;
        }
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        try {
            // 根据令牌类型优化查询
            String sql = buildFindByTokenSql(tokenType);
            List<OAuth2Authorization> results = jdbcTemplate.query(sql, new OAuth2AuthorizationRowMapper(), token);
            
            if (results.isEmpty()) {
                return null;
            }
            
            if (results.size() > 1) {
                log.warn("Multiple authorizations found for token type: {}", tokenType);
            }
            
            return results.get(0);
            
        } catch (Exception e) {
            log.debug("OAuth2Authorization not found by token: type={}", tokenType, e);
            return null;
        }
    }

    // ============ 辅助方法 ============

    private String buildFindByTokenSql(OAuth2TokenType tokenType) {
        if (tokenType == null) {
            return FIND_BY_TOKEN_SQL;
        }

        String column = switch (tokenType.getValue()) {
            case OAuth2ParameterNames.STATE -> "state";
            case OAuth2ParameterNames.CODE -> "authorization_code_value";
            case OAuth2ParameterNames.ACCESS_TOKEN -> "access_token_value";
            case OAuth2ParameterNames.REFRESH_TOKEN -> "refresh_token_value";
            default -> throw new IllegalArgumentException("Unsupported token type: " + tokenType.getValue());
        };

        return "SELECT * FROM %s WHERE %s = ?".formatted(TABLE_NAME, column);
    }

    private String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Failed to convert to JSON: {}", obj.getClass().getSimpleName(), e);
            return null;
        }
    }

    private Timestamp toTimestamp(Instant instant) {
        if (instant == null) {
            return null;
        }
        return Timestamp.from(instant);
    }

    // ============ RowMapper ============

    private class OAuth2AuthorizationRowMapper implements RowMapper<OAuth2Authorization> {

        @Override
        public OAuth2Authorization mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                String registeredClientId = rs.getString("registered_client_id");
                RegisteredClient registeredClient = registeredClientRepository.findById(registeredClientId);
                
                if (registeredClient == null) {
                    throw new DataRetrievalFailureException(
                        "Registered client not found: " + registeredClientId);
                }

                OAuth2AuthorizationPOJO pojo = new OAuth2AuthorizationPOJO();
                pojo.setId(rs.getString("id"));
                pojo.setRegisteredClientId(registeredClientId);
                pojo.setPrincipalName(rs.getString("principal_name"));
                pojo.setAuthorizationGrantType(rs.getString("authorization_grant_type"));
                pojo.setAuthorizedScopes(fromJson(rs.getString("authorized_scopes"), new TypeReference<List<String>>() {}));
                pojo.setAttributes(fromJson(rs.getString("attributes"), new TypeReference<Map<String, Object>>() {}));
                pojo.setState(rs.getString("state"));
                
                // 授权码
                pojo.setAuthorizationCodeValue(rs.getString("authorization_code_value"));
                pojo.setAuthorizationCodeIssuedAt(toInstant(rs.getTimestamp("authorization_code_issued_at")));
                pojo.setAuthorizationCodeExpiresAt(toInstant(rs.getTimestamp("authorization_code_expires_at")));
                pojo.setAuthorizationCodeMetadata(fromJson(rs.getString("authorization_code_metadata"), new TypeReference<Map<String, Object>>() {}));
                
                // 访问令牌
                pojo.setAccessTokenValue(rs.getString("access_token_value"));
                pojo.setAccessTokenIssuedAt(toInstant(rs.getTimestamp("access_token_issued_at")));
                pojo.setAccessTokenExpiresAt(toInstant(rs.getTimestamp("access_token_expires_at")));
                pojo.setAccessTokenMetadata(fromJson(rs.getString("access_token_metadata"), new TypeReference<Map<String, Object>>() {}));
                pojo.setAccessTokenType(rs.getString("access_token_type"));
                pojo.setAccessTokenScopes(fromJson(rs.getString("access_token_scopes"), new TypeReference<List<String>>() {}));
                
                // 刷新令牌
                pojo.setRefreshTokenValue(rs.getString("refresh_token_value"));
                pojo.setRefreshTokenIssuedAt(toInstant(rs.getTimestamp("refresh_token_issued_at")));
                pojo.setRefreshTokenExpiresAt(toInstant(rs.getTimestamp("refresh_token_expires_at")));
                pojo.setRefreshTokenMetadata(fromJson(rs.getString("refresh_token_metadata"), new TypeReference<Map<String, Object>>() {}));
                
                // OIDC ID 令牌
                pojo.setOidcIdTokenValue(rs.getString("oidc_id_token_value"));
                pojo.setOidcIdTokenIssuedAt(toInstant(rs.getTimestamp("oidc_id_token_issued_at")));
                pojo.setOidcIdTokenExpiresAt(toInstant(rs.getTimestamp("oidc_id_token_expires_at")));
                pojo.setOidcIdTokenMetadata(fromJson(rs.getString("oidc_id_token_metadata"), new TypeReference<Map<String, Object>>() {}));
                pojo.setOidcIdTokenClaims(fromJson(rs.getString("oidc_id_token_claims"), new TypeReference<Map<String, Object>>() {}));

                return OAuth2AuthorizationConverter.fromPOJO(pojo, registeredClient);

            } catch (Exception e) {
                log.error("Failed to map OAuth2Authorization from ResultSet", e);
                throw new SQLException("Failed to map OAuth2Authorization", e);
            }
        }

        private Instant toInstant(Timestamp timestamp) {
            if (timestamp == null) {
                return null;
            }
            return timestamp.toInstant();
        }

        private <T> T fromJson(String json, TypeReference<T> typeRef) {
            if (!StringUtils.hasText(json)) {
                return null;
            }
            try {
                return objectMapper.readValue(json, typeRef);
            } catch (Exception e) {
                log.warn("Failed to parse JSON: {}", json, e);
                return null;
            }
        }
    }
}
