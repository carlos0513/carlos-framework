package com.carlos.auth.service;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * <p>
 * 速率限制服务
 * </p>
 *
 * <p>使用Redis令牌桶算法实现API限流</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Lua脚本实现令牌桶算法
    private static final String TOKEN_BUCKET_SCRIPT =
            "local key = KEYS[1]\n" +
                    "local now = tonumber(ARGV[1])\n" +
                    "local capacity = tonumber(ARGV[2])\n" +
                    "local refill_rate = tonumber(ARGV[3])\n" +
                    "local refill_period = tonumber(ARGV[4])\n" +
                    "local requested_tokens = tonumber(ARGV[5])\n" +
                    "\n" +
                    "-- 获取当前桶状态\n" +
                    "local tokens = redis.call('GET', key)\n" +
                    "local last_refill = redis.call('GET', key .. ':last_refill')\n" +
                    "\n" +
                    "-- 初始化桶\n" +
                    "if tokens == false then\n" +
                    "    tokens = capacity\n" +
                    "    last_refill = now\n" +
                    "else\n" +
                    "    tokens = tonumber(tokens)\n" +
                    "    last_refill = tonumber(last_refill)\n" +
                    "end\n" +
                    "\n" +
                    "-- 计算时间间隔\n" +
                    "local elapsed = now - last_refill\n" +
                    "\n" +
                    "-- 补充令牌\n" +
                    "if elapsed >= refill_period then\n" +
                    "    local refill_times = math.floor(elapsed / refill_period)\n" +
                    "    tokens = math.min(capacity, tokens + (refill_times * refill_rate))\n" +
                    "    last_refill = last_refill + (refill_times * refill_period)\n" +
                    "end\n" +
                    "\n" +
                    "-- 消费令牌\n" +
                    "if tokens >= requested_tokens then\n" +
                    "    tokens = tokens - requested_tokens\n" +
                    "    redis.call('SET', key, tokens)\n" +
                    "    redis.call('SET', key .. ':last_refill', last_refill)\n" +
                    "    redis.call('EXPIRE', key, 3600)\n" +
                    "    redis.call('EXPIRE', key .. ':last_refill', 3600)\n" +
                    "    return 1 -- 允许请求\n" +
                    "else\n" +
                    "    redis.call('SET', key, tokens)\n" +
                    "    redis.call('SET', key .. ':last_refill', last_refill)\n" +
                    "    redis.call('EXPIRE', key, 3600)\n" +
                    "    redis.call('EXPIRE', key .. ':last_refill', 3600)\n" +
                    "    return 0 -- 拒绝请求\n" +
                    "end\n";

    /**
     * 检查请求是否被允许（令牌桶算法）
     *
     * @param key 限流键（如：auth:rate:login:{userId}）
     * @param capacity 桶容量（最大令牌数）
     * @param refillRate 补充速率（每周期补充令牌数）
     * @param refillPeriod 补充周期（毫秒）
     * @param requestedTokens 请求消耗的令牌数
     * @return true-允许请求，false-拒绝请求
     */
    public boolean tryConsumeToken(String key, int capacity, int refillRate, long refillPeriod, int requestedTokens) {
        if (StrUtil.isBlank(key)) {
            return true;
        }

        long now = Instant.now().toEpochMilli();

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(TOKEN_BUCKET_SCRIPT);
        redisScript.setResultType(Long.class);

        List<String> keys = List.of(key);
        Object[] args = new Object[]{now, capacity, refillRate, refillPeriod, requestedTokens};

        try {
            Long result = redisTemplate.execute(redisScript, keys, args);
            return result != null && result == 1;
        } catch (Exception e) {
            log.error("Rate limiting error for key: {}", key, e);
            // 限流服务异常时，默认允许请求（降级）
            return true;
        }
    }

    /**
     * 简单的登录限流（5次/分钟）
     *
     * @param identifier 标识符（username或IP）
     * @return true-允许登录，false-拒绝登录
     */
    public boolean tryLogin(String identifier) {
        String key = "auth:rate:login:" + identifier;
        // 容量5，每分钟补充5个令牌
        return tryConsumeToken(key, 5, 5, 60000, 1);
    }

    /**
     * API限流（100次/分钟）
     *
     * @param userId 用户ID
     * @return true-允许请求，false-拒绝请求
     */
    public boolean tryApiAccess(Long userId) {
        if (userId == null) {
            return true;
        }

        String key = "auth:rate:api:" + userId;
        // 容量100，每分钟补充100个令牌
        return tryConsumeToken(key, 100, 100, 60000, 1);
    }

    /**
     * IP限流（针对未登录用户）
     *
     * @param ip IP地址
     * @return true-允许请求，false-拒绝请求
     */
    public boolean tryIpAccess(String ip) {
        if (StrUtil.isBlank(ip)) {
            return true;
        }

        String key = "auth:rate:ip:" + ip;
        // 容量50，每分钟补充50个令牌
        return tryConsumeToken(key, 50, 50, 60000, 1);
    }

    /**
     * 验证码发送限流
     *
     * @param target 手机号或邮箱
     * @param type 类型：sms/email
     * @return true-允许发送，false-拒绝发送
     */
    public boolean trySendCaptcha(String target, String type) {
        if (StrUtil.isBlank(target) || StrUtil.isBlank(type)) {
            return true;
        }

        String key = "auth:rate:captcha:" + type + ":" + target;
        // 容量3，每60秒补充1个令牌
        return tryConsumeToken(key, 3, 1, 60000, 1);
    }

    /**
     * 令牌颁发限流
     *
     * @param clientId 客户端ID
     * @return true-允许颁发，false-拒绝
     */
    public boolean tryTokenGeneration(String clientId) {
        if (StrUtil.isBlank(clientId)) {
            return true;
        }

        String key = "auth:rate:token:" + clientId;
        // 容量1000，每小时补充1000个令牌
        return tryConsumeToken(key, 1000, 1000, 3600000, 1);
    }
}
