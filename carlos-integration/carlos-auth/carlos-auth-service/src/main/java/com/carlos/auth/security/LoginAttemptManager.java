package com.carlos.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 登录尝试管理器
 * </p>
 *
 * <p>管理用户登录尝试次数，防止暴力破解密码</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginAttemptManager {

    /**
     * Redis Key 前缀
     */
    private static final String LOGIN_ATTEMPT_KEY_PREFIX = "auth:lock:user:";
    private static final String LOGIN_ATTEMPT_COUNT_KEY_PREFIX = "auth:attempt:user:";

    /**
     * 默认失败阈值（5次）
     */
    private static final int DEFAULT_MAX_ATTEMPTS = 5;

    /**
     * 默认锁定时长（15分钟）
     */
    private static final Duration DEFAULT_LOCK_DURATION = Duration.ofMinutes(15);

    /**
     * RedisTemplate
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 记录登录失败
     *
     * @param username 用户名
     */
    public void recordLoginFailure(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        String countKey = LOGIN_ATTEMPT_COUNT_KEY_PREFIX + username;
        String lockKey = LOGIN_ATTEMPT_KEY_PREFIX + username;

        // 获取当前失败次数
        Long attempts = redisTemplate.opsForValue().increment(countKey);

        if (attempts == 1) {
            // 第一次失败，设置过期时间（24小时）
            redisTemplate.expire(countKey, Duration.ofHours(24));
            log.debug("First login failure for user: {}", username);
        }

        log.warn("Login failure recorded for user: {}, attempts={}", username, attempts);

        // 检查是否达到锁定阈值
        if (attempts >= DEFAULT_MAX_ATTEMPTS) {
            // 锁定账号
            lockAccount(username);
            log.warn("Account locked due to too many failed attempts: {}", username);
        }
    }

    /**
     * 记录登录成功
     *
     * @param username 用户名
     */
    public void recordLoginSuccess(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        String countKey = LOGIN_ATTEMPT_COUNT_KEY_PREFIX + username;
        String lockKey = LOGIN_ATTEMPT_KEY_PREFIX + username;

        // 删除失败次数记录
        redisTemplate.delete(countKey);

        // 解除锁定（如果之前被锁定）
        if (Boolean.TRUE.equals(redisTemplate.hasKey(lockKey))) {
            redisTemplate.delete(lockKey);
            log.info("Account unlocked after successful login: {}", username);
        }

        log.debug("Login success recorded for user: {}", username);
    }

    /**
     * 检查账号是否被锁定
     *
     * @param username 用户名
     * @return true-被锁定，false-未锁定
     */
    public boolean isAccountLocked(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }

        String lockKey = LOGIN_ATTEMPT_KEY_PREFIX + username;
        Boolean isLocked = redisTemplate.hasKey(lockKey);

        if (Boolean.TRUE.equals(isLocked)) {
            Long ttl = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
            log.warn("Account is locked: {}, TTL={} seconds", username, ttl);
            return true;
        }

        return false;
    }

    /**
     * 锁定账号
     *
     * @param username 用户名
     */
    public void lockAccount(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        String lockKey = LOGIN_ATTEMPT_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(lockKey, "LOCKED", DEFAULT_LOCK_DURATION);

        log.info("Account locked: {}, duration={} minutes", username, DEFAULT_LOCK_DURATION.toMinutes());
    }

    /**
     * 获取剩余锁定时间（秒）
     *
     * @param username 用户名
     * @return 剩余锁定时间（秒），如果未锁定返回 0
     */
    public long getRemainingLockTime(String username) {
        if (username == null || username.isBlank()) {
            return 0;
        }

        String lockKey = LOGIN_ATTEMPT_KEY_PREFIX + username;
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(lockKey))) {
            return 0;
        }

        Long ttl = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
        return ttl != null && ttl > 0 ? ttl : 0;
    }

    /**
     * 获取当前失败次数
     *
     * @param username 用户名
     * @return 失败次数
     */
    public int getCurrentAttempts(String username) {
        if (username == null || username.isBlank()) {
            return 0;
        }

        String countKey = LOGIN_ATTEMPT_COUNT_KEY_PREFIX + username;
        Object value = redisTemplate.opsForValue().get(countKey);

        if (value == null) {
            return 0;
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            log.error("Failed to parse login attempts for user: {}", username, e);
            return 0;
        }
    }

    /**
     * 重置失败次数（手动解锁）
     *
     * @param username 用户名
     */
    public void resetAttempts(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        String countKey = LOGIN_ATTEMPT_COUNT_KEY_PREFIX + username;
        String lockKey = LOGIN_ATTEMPT_KEY_PREFIX + username;

        redisTemplate.delete(countKey);
        redisTemplate.delete(lockKey);

        log.info("Login attempts reset for user: {}", username);
    }
}
