package com.carlos.auth.config.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Redis OAuth2 授权服务
 * </p>
 *
 * <p>基于 Redis 实现 OAuth2AuthorizationService，支持授权信息的持久化存储。</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    /**
     * Redis Key 前缀
     */
    private static final String AUTHORIZATION_KEY_PREFIX = "auth:oauth2:authorization:";
    private static final String AUTHORIZATION_CODE_KEY_PREFIX = AUTHORIZATION_KEY_PREFIX + "code:";
    private static final String ACCESS_TOKEN_KEY_PREFIX = AUTHORIZATION_KEY_PREFIX + "access_token:";
    private static final String REFRESH_TOKEN_KEY_PREFIX = AUTHORIZATION_KEY_PREFIX + "refresh_token:";
    private static final String STATE_KEY_PREFIX = AUTHORIZATION_KEY_PREFIX + "state:";
    private static final String USER_CODE_KEY_PREFIX = AUTHORIZATION_KEY_PREFIX + "user_code:";
    private static final String DEVICE_CODE_KEY_PREFIX = AUTHORIZATION_KEY_PREFIX + "device_code:";

    /**
     * OAuth2 Token 类型
     */
    private static final OAuth2TokenType AUTHORIZATION_CODE_TOKEN_TYPE = new OAuth2TokenType("code");
    private static final OAuth2TokenType ACCESS_TOKEN_TOKEN_TYPE = new OAuth2TokenType("access_token");
    private static final OAuth2TokenType REFRESH_TOKEN_TOKEN_TYPE = new OAuth2TokenType("refresh_token");

    /**
     * RegisteredClientRepository
     */
    private final RegisteredClientRepository registeredClientRepository;

    /**
     * RedisTemplate
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * ObjectMapper（已配置 OAuth2Authorization 序列化）
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置 Jackson 序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
        // 添加其他必要的模块
    }

    /**
     * 保存 OAuth2 授权信息
     *
     * @param authorization OAuth2 授权信息
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        log.debug("Saving OAuth2 authorization: id={}", authorization.getId());

        // 序列化 OAuth2Authorization
        byte[] serialized = serialize(authorization);
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // 计算 TTL（使用 refresh_token 的有效期）
        Duration ttl = getTimeToLive(authorization);

        // 保存主键
        String authorizationKey = AUTHORIZATION_KEY_PREFIX + authorization.getId();
        ops.set(authorizationKey, serialized, ttl);

        // 保存各 token 的索引
        saveTokenIndex(authorization.getRegisteredClientId(), authorization.getPrincipalName(), authorization.getId());

        // 保存授权码索引
        if (authorization.getToken("code") != null) {
            OAuth2Token code = authorization.getToken("code").getToken();
            String codeKey = AUTHORIZATION_CODE_KEY_PREFIX + code.getTokenValue();
            ops.set(codeKey, serialized, ttl);
        }

        // 保存访问令牌索引
        if (authorization.getAccessToken() != null) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            String accessTokenKey = ACCESS_TOKEN_KEY_PREFIX + accessToken.getTokenValue();
            ops.set(accessTokenKey, serialized, ttl);
        }

        // 保存刷新令牌索引
        if (authorization.getRefreshToken() != null) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            String refreshTokenKey = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getTokenValue();
            ops.set(refreshTokenKey, serialized, ttl);
        }

        log.debug("Successfully saved OAuth2 authorization: id={}", authorization.getId());
    }

    /**
     * 根据 ID 移除 OAuth2 授权信息
     *
     * @param authorization OAuth2 授权信息
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        log.debug("Removing OAuth2 authorization: id={}", authorization.getId());

        List<String> keys = new ArrayList<>();
        keys.add(AUTHORIZATION_KEY_PREFIX + authorization.getId());

        // 添加各 token 的索引键
        if (authorization.getToken("code") != null) {
            OAuth2Token code = authorization.getToken("code").getToken();
            keys.add(AUTHORIZATION_CODE_KEY_PREFIX + code.getTokenValue());
        }

        if (authorization.getAccessToken() != null) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            keys.add(ACCESS_TOKEN_KEY_PREFIX + accessToken.getTokenValue());
        }

        if (authorization.getRefreshToken() != null) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            keys.add(REFRESH_TOKEN_KEY_PREFIX + refreshToken.getTokenValue());
        }

        // 批量删除
        redisTemplate.delete(keys);
        log.debug("Successfully removed OAuth2 authorization: id={}", authorization.getId());
    }

    /**
     * 根据 ID 查找 OAuth2 授权信息
     *
     * @param id 授权 ID
     * @return OAuth2 授权信息
     */
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        log.debug("Finding OAuth2 authorization by id: {}", id);

        String authorizationKey = AUTHORIZATION_KEY_PREFIX + id;
        byte[] serialized = (byte[]) redisTemplate.opsForValue().get(authorizationKey);

        if (serialized == null) {
            log.debug("OAuth2 authorization not found by id: {}", id);
            return null;
        }

        return deserialize(serialized);
    }

    /**
     * 根据 Token 查找 OAuth2 授权信息
     *
     * @param token Token 值
     * @param tokenType Token 类型
     * @return OAuth2 授权信息
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        log.debug("Finding OAuth2 authorization by token: tokenType={}", tokenType);

        String tokenKey;
        if (tokenType == null) {
            // 尝试所有 token 类型
            tokenKey = ACCESS_TOKEN_KEY_PREFIX + token;
            OAuth2Authorization authorization = findByTokenKey(tokenKey);
            if (authorization != null) {
                return authorization;
            }

            tokenKey = REFRESH_TOKEN_KEY_PREFIX + token;
            authorization = findByTokenKey(tokenKey);
            if (authorization != null) {
                return authorization;
            }

            tokenKey = AUTHORIZATION_CODE_KEY_PREFIX + token;
            return findByTokenKey(tokenKey);
        } else if (AUTHORIZATION_CODE_TOKEN_TYPE.equals(tokenType)) {
            tokenKey = AUTHORIZATION_CODE_KEY_PREFIX + token;
        } else if (ACCESS_TOKEN_TOKEN_TYPE.equals(tokenType)) {
            tokenKey = ACCESS_TOKEN_KEY_PREFIX + token;
        } else if (REFRESH_TOKEN_TOKEN_TYPE.equals(tokenType)) {
            tokenKey = REFRESH_TOKEN_KEY_PREFIX + token;
        } else {
            // 自定义 token 类型
            tokenKey = AUTHORIZATION_KEY_PREFIX + tokenType.getValue() + ":" + token;
        }

        return findByTokenKey(tokenKey);
    }

    /**
     * 根据 Token Key 查找授权信息
     *
     * @param tokenKey Token Key
     * @return OAuth2 授权信息
     */
    private OAuth2Authorization findByTokenKey(String tokenKey) {
        byte[] serialized = (byte[]) redisTemplate.opsForValue().get(tokenKey);

        if (serialized == null) {
            return null;
        }

        return deserialize(serialized);
    }

    /**
     * 保存 Token 索引
     *
     * @param clientId 客户端 ID
     * @param principalName 主体名称（用户名）
     * @param authorizationId 授权 ID
     */
    private void saveTokenIndex(String clientId, String principalName, String authorizationId) {
        // 可以添加 client_id + username -> authorization_id 的索引
        // 便于后续查询用户的所有授权
        log.debug("Saving token index: clientId={}, principalName={}", clientId, principalName);
    }

    /**
     * 序列化 OAuth2Authorization
     *
     * @param authorization OAuth2 授权信息
     * @return 序列化后的字节数组
     */
    private byte[] serialize(OAuth2Authorization authorization) {
        try {
            String json = objectMapper.writeValueAsString(authorization);
            return json.getBytes();
        } catch (Exception e) {
            log.error("Failed to serialize OAuth2Authorization: id={}", authorization.getId(), e);
            throw new IllegalStateException("Failed to serialize OAuth2Authorization", e);
        }
    }

    /**
     * 反序列化 OAuth2Authorization
     *
     * @param serialized 序列化后的字节数组
     * @return OAuth2Authorization
     */
    private OAuth2Authorization deserialize(byte[] serialized) {
        try {
            String json = new String(serialized);

            // TODO: 需要实现 OAuth2Authorization 的反序列化
            // 这是一个简化版本，实际需要使用 Jackson 模块或自定义序列化器
            log.warn("OAuth2Authorization deserialization not fully implemented yet");
            return null;

        } catch (Exception e) {
            log.error("Failed to deserialize OAuth2Authorization", e);
            throw new IllegalStateException("Failed to deserialize OAuth2Authorization", e);
        }
    }

    /**
     * 计算 OAuth2Authorization 的 TTL
     *
     * @param authorization OAuth2 授权信息
     * @return TTL（有效期）
     */
    private Duration getTimeToLive(OAuth2Authorization authorization) {
        // 优先使用刷新令牌的有效期
        if (authorization.getRefreshToken() != null) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            if (refreshToken.getExpiresAt() != null) {
                return Duration.ofSeconds(
                        ChronoUnit.SECONDS.between(java.time.Instant.now(), refreshToken.getExpiresAt())
                );
            }
        }

        // 其次使用访问令牌的有效期
        if (authorization.getAccessToken() != null) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            if (accessToken.getExpiresAt() != null) {
                return Duration.ofSeconds(
                        ChronoUnit.SECONDS.between(java.time.Instant.now(), accessToken.getExpiresAt())
                );
            }
        }

        // 默认使用刷新令牌的最大有效期（7天）
        return Duration.ofDays(7);
    }
}
