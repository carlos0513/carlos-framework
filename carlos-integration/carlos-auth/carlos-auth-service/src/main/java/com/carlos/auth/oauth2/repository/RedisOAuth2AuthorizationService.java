package com.carlos.auth.oauth2.repository;

import com.carlos.redis.util.RedisUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 自定义 OAuth2 授权服务 - Redis 实现
 * </p>
 *
 * <p>基于 Redis 存储 OAuth2 授权信息，支持：</p>
 * <ul>
 *   <li>授权码（Authorization Code）存储</li>
 *   <li>访问令牌（Access Token）存储</li>
 *   <li>刷新令牌（Refresh Token）存储</li>
 *   <li>状态码（State）存储</li>
 *   <li>自动 TTL 过期清理</li>
 *   <li>多类型 Token 查找</li>
 * </ul>
 *
 * <p>Redis Key 设计：</p>
 * <ul>
 *   <li>{@code oauth2:state:{state}} - 状态码</li>
 *   <li>{@code oauth2:code:{code}} - 授权码</li>
 *   <li>{@code oauth2:access_token:{token}} - 访问令牌</li>
 *   <li>{@code oauth2:refresh_token:{token}} - 刷新令牌</li>
 *   <li>{@code oauth2:id:{id}} - 授权 ID 索引</li>
 * </ul>
 *
 * @author carlos
 * @date 2022/11/4 14:35
 * @since 3.0.0
 */
@Slf4j
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    /**
     * Redis Key 前缀
     */
    private static final String AUTHORIZATION_PREFIX = "oauth2";
    private static final String STATE_PREFIX = OAuth2ParameterNames.STATE;
    private static final String CODE_PREFIX = OAuth2ParameterNames.CODE;
    private static final String ACCESS_TOKEN_PREFIX = OAuth2ParameterNames.ACCESS_TOKEN;
    private static final String REFRESH_TOKEN_PREFIX = OAuth2ParameterNames.REFRESH_TOKEN;
    private static final String ID_PREFIX = "id";

    /**
     * 默认超时时间（分钟）- 用于 state
     */
    private static final Long DEFAULT_TIMEOUT = 10L;

    /**
     * 默认最大有效期（天）- 兜底 TTL
     */
    private static final Duration DEFAULT_MAX_TTL = Duration.ofDays(7);

    /**
     * ObjectMapper 实例（配置序列化）
     */
    private final ObjectMapper objectMapper;

    public RedisOAuth2AuthorizationService() {
        this.objectMapper = new ObjectMapper();
        configureObjectMapper();
    }

    /**
     * 配置 ObjectMapper
     */
    @PostConstruct
    private void configureObjectMapper() {
        // 配置可见性
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 注册 JavaTimeModule 支持 JDK8 日期时间
        objectMapper.registerModule(new JavaTimeModule());
        // 注册 Spring Security Jackson2 Modules（关键：支持 Spring Security 内部类的序列化/反序列化）
        ClassLoader classLoader = getClass().getClassLoader();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
        // 注册 OAuth2 Authorization Server Jackson2 Module（支持 OAuth2Authorization 等内部类）
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        // 添加 AuthorizationGrantType 混合注解处理
        objectMapper.addMixIn(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        log.info("RedisOAuth2AuthorizationService initialized with SecurityJackson2Modules");
    }

    /**
     * 保存 OAuth2 授权信息
     *
     * @param authorization OAuth2 授权信息
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        log.debug("Saving OAuth2 authorization: id={}, clientId={}, principalName={}",
            authorization.getId(), authorization.getRegisteredClientId(), authorization.getPrincipalName());

        String json = serialize(authorization);
        Duration ttl = calculateTtl(authorization);

        // 保存授权 ID 索引（便于后续查找和清理）
        saveAuthorizationIdIndex(authorization, json, ttl);

        // 保存 state 索引
        if (isState(authorization)) {
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            RedisUtil.setValue(buildKey(STATE_PREFIX, state), json, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
            log.debug("Saved state index: state={}", state);
        }

        // 保存授权码索引
        if (isCode(authorization)) {
            OAuth2AuthorizationCode authorizationCode = getAuthorizationCode(authorization);
            if (authorizationCode != null) {
                long codeTtl = calculateTokenTtl(authorizationCode.getIssuedAt(), authorizationCode.getExpiresAt());
                RedisUtil.setValue(buildKey(CODE_PREFIX, authorizationCode.getTokenValue()),
                    json, codeTtl, TimeUnit.SECONDS);
                log.debug("Saved authorization code index: code={}", maskToken(authorizationCode.getTokenValue()));
            }
        }

        // 保存访问令牌索引
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            long accessTokenTtl = calculateTokenTtl(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            RedisUtil.setValue(buildKey(ACCESS_TOKEN_PREFIX, accessToken.getTokenValue()),
                json, accessTokenTtl, TimeUnit.SECONDS);
            log.debug("Saved access token index: token={}", maskToken(accessToken.getTokenValue()));
        }

        // 保存刷新令牌索引
        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            long refreshTokenTtl = calculateTokenTtl(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            RedisUtil.setValue(buildKey(REFRESH_TOKEN_PREFIX, refreshToken.getTokenValue()),
                json, refreshTokenTtl, TimeUnit.SECONDS);
            log.debug("Saved refresh token index: token={}", maskToken(refreshToken.getTokenValue()));
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

        // 添加授权 ID 索引
        keys.add(buildKey(ID_PREFIX, authorization.getId()));

        // 添加 state 索引
        if (isState(authorization)) {
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            keys.add(buildKey(STATE_PREFIX, state));
        }

        // 添加授权码索引
        if (isCode(authorization)) {
            OAuth2AuthorizationCode authorizationCode = getAuthorizationCode(authorization);
            if (authorizationCode != null) {
                keys.add(buildKey(CODE_PREFIX, authorizationCode.getTokenValue()));
            }
        }

        // 添加刷新令牌索引
        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            keys.add(buildKey(REFRESH_TOKEN_PREFIX, refreshToken.getTokenValue()));
        }

        // 添加访问令牌索引
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            keys.add(buildKey(ACCESS_TOKEN_PREFIX, accessToken.getTokenValue()));
        }

        // 批量删除
        if (!keys.isEmpty()) {
            RedisUtil.delete(keys);
            log.debug("Deleted {} keys for authorization: id={}", keys.size(), authorization.getId());
        }

        log.debug("Successfully removed OAuth2 authorization: id={}", authorization.getId());
    }

    /**
     * 根据 ID 查找 OAuth2 授权信息
     *
     * @param id 授权 ID
     * @return OAuth2 授权信息，不存在返回 null
     */
    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        log.debug("Finding OAuth2 authorization by id: {}", id);

        String json = RedisUtil.getValue(buildKey(ID_PREFIX, id));
        if (json == null) {
            log.debug("OAuth2 authorization not found by id: {}", id);
            return null;
        }

        return deserialize(json);
    }

    /**
     * 根据 Token 查找 OAuth2 授权信息
     *
     * @param token     Token 值
     * @param tokenType Token 类型
     * @return OAuth2 授权信息，不存在返回 null
     */
    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        log.debug("Finding OAuth2 authorization by token: tokenType={}", tokenType);

        // 如果 tokenType 为 null，尝试所有类型
        if (tokenType == null) {
            return findByTokenWithoutType(token);
        }

        String key = buildKey(tokenType.getValue(), token);
        String json = RedisUtil.getValue(key);

        if (json == null) {
            log.debug("OAuth2 authorization not found by token: tokenType={}", tokenType);
            return null;
        }

        return deserialize(json);
    }

    /**
     * 不指定 Token 类型，尝试所有类型查找
     *
     * @param token Token 值
     * @return OAuth2 授权信息
     */
    private OAuth2Authorization findByTokenWithoutType(String token) {
        // 按优先级尝试：access_token -> refresh_token -> code -> state
        String[] types = {ACCESS_TOKEN_PREFIX, REFRESH_TOKEN_PREFIX, CODE_PREFIX, STATE_PREFIX};

        for (String type : types) {
            String key = buildKey(type, token);
            String json = RedisUtil.getValue(key);
            if (json != null) {
                log.debug("Found OAuth2 authorization by token with type: {}", type);
                return deserialize(json);
            }
        }

        log.debug("OAuth2 authorization not found by token for any type");
        return null;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 保存授权 ID 索引
     */
    private void saveAuthorizationIdIndex(OAuth2Authorization authorization, String json, Duration ttl) {
        RedisUtil.setValue(buildKey(ID_PREFIX, authorization.getId()),
            json, ttl.getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 计算授权信息的 TTL
     * 优先使用刷新令牌有效期，其次是访问令牌，最后是默认值
     */
    private Duration calculateTtl(OAuth2Authorization authorization) {
        // 优先使用刷新令牌的有效期
        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            if (refreshToken.getExpiresAt() != null) {
                return Duration.ofSeconds(
                    ChronoUnit.SECONDS.between(java.time.Instant.now(), refreshToken.getExpiresAt())
                );
            }
        }

        // 其次使用访问令牌的有效期
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            if (accessToken.getExpiresAt() != null) {
                return Duration.ofSeconds(
                    ChronoUnit.SECONDS.between(java.time.Instant.now(), accessToken.getExpiresAt())
                );
            }
        }

        // 最后使用授权码的有效期
        if (isCode(authorization)) {
            OAuth2AuthorizationCode authorizationCode = getAuthorizationCode(authorization);
            if (authorizationCode != null && authorizationCode.getExpiresAt() != null) {
                return Duration.ofSeconds(
                    ChronoUnit.SECONDS.between(java.time.Instant.now(), authorizationCode.getExpiresAt())
                );
            }
        }

        // 兜底使用默认最大有效期
        return DEFAULT_MAX_TTL;
    }

    /**
     * 计算 Token 的 TTL（秒）
     */
    private long calculateTokenTtl(java.time.Instant issuedAt, java.time.Instant expiresAt) {
        if (expiresAt == null) {
            return DEFAULT_MAX_TTL.getSeconds();
        }
        long seconds = ChronoUnit.SECONDS.between(java.time.Instant.now(), expiresAt);
        return Math.max(seconds, 1); // 至少 1 秒
    }

    /**
     * 构建 Redis Key
     */
    private String buildKey(String type, String id) {
        return String.format("%s:%s:%s", AUTHORIZATION_PREFIX, type, id);
    }

    /**
     * 序列化 OAuth2Authorization
     */
    private String serialize(OAuth2Authorization authorization) {
        try {
            return objectMapper.writeValueAsString(authorization);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize OAuth2Authorization: id={}", authorization.getId(), e);
            throw new IllegalStateException("Failed to serialize OAuth2Authorization", e);
        }
    }

    /**
     * 反序列化 OAuth2Authorization
     */
    private OAuth2Authorization deserialize(String json) {
        try {
            return objectMapper.readValue(json, OAuth2Authorization.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize OAuth2Authorization", e);
            throw new IllegalStateException("Failed to deserialize OAuth2Authorization", e);
        }
    }

    /**
     * 获取授权码
     */
    private OAuth2AuthorizationCode getAuthorizationCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> token = authorization
            .getToken(OAuth2AuthorizationCode.class);
        return token != null ? token.getToken() : null;
    }

    // ==================== 判断方法 ====================

    private static boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean isCode(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getToken(OAuth2AuthorizationCode.class));
    }

    private static boolean isRefreshToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getRefreshToken());
    }

    private static boolean isAccessToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAccessToken());
    }

    /**
     * 脱敏 Token（用于日志）
     */
    private static String maskToken(String token) {
        if (token == null || token.length() < 10) {
            return "***";
        }
        return token.substring(0, 6) + "****" + token.substring(token.length() - 4);
    }
}
