package com.carlos.auth.config.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * <p>
 * Redis OAuth2 授权许可服务
 * </p>
 *
 * <p>基于 Redis 实现 OAuth2AuthorizationConsentService，存储用户对客户端的授权许可信息。\u003c/p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    /**
     * Redis Key 前缀
     */
    private static final String AUTHORIZATION_CONSENT_KEY_PREFIX = "auth:oauth2:consent:";

    /**
     * 默认 TTL（7天）
     */
    private static final Duration DEFAULT_TTL = Duration.ofDays(7);

    /**
     * RedisTemplate
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 保存授权许可
     *
     * @param authorizationConsent 授权许可
     */
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        log.debug("Saving OAuth2 authorization consent: clientId={}, principalName={}",
            authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());

        String key = buildAuthorizationConsentKey(
            authorizationConsent.getRegisteredClientId(),
            authorizationConsent.getPrincipalName()
        );

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, authorizationConsent, DEFAULT_TTL);

        log.debug("Successfully saved OAuth2 authorization consent");
    }

    /**
     * 移除授权许可
     *
     * @param authorizationConsent 授权许可
     */
    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        log.debug("Removing OAuth2 authorization consent: clientId={}, principalName={}",
            authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());

        String key = buildAuthorizationConsentKey(
            authorizationConsent.getRegisteredClientId(),
            authorizationConsent.getPrincipalName()
        );

        redisTemplate.delete(key);

        log.debug("Successfully removed OAuth2 authorization consent");
    }

    /**
     * 查找授权许可
     *
     * @param clientId 客户端 ID
     * @param principalName 主体名称（用户名）
     * @return 授权许可，不存在返回 null
     */
    @Override
    @Nullable
    public OAuth2AuthorizationConsent findById(String clientId, String principalName) {
        log.debug("Finding OAuth2 authorization consent: clientId={}, principalName={}", clientId, principalName);

        String key = buildAuthorizationConsentKey(clientId, principalName);
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(key);
    }

    /**
     * 构建授权许可的 Redis Key
     *
     * @param clientId 客户端 ID
     * @param principalName 主体名称
     * @return Redis Key
     */
    private String buildAuthorizationConsentKey(String clientId, String principalName) {
        return AUTHORIZATION_CONSENT_KEY_PREFIX + clientId + ":" + principalName;
    }
}
