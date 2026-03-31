package com.carlos.boot.translation.cache;

import com.carlos.boot.translation.config.TranslationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 多级缓存管理器（Caffeine + Redis）
 * </p>
 *
 * @author carlos
 * @date 2025/01/20
 */
@Component
@Slf4j
public class TranslationCacheManager {

    private final Cache<String, Object> localCache;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public TranslationCacheManager(
        StringRedisTemplate redisTemplate,
        TranslationProperties properties,
        ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;

        // 构建 Caffeine 本地缓存
        this.localCache = Caffeine.newBuilder()
            .maximumSize(properties.getCache().getLocalSize())
            .expireAfterWrite(properties.getCache().getLocalExpireMinutes(), TimeUnit.MINUTES)
            .recordStats()
            .build();
    }

    /**
     * 获取缓存
     *
     * @param key  缓存Key
     * @param type 类型
     * @param <T>  泛型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        // L1: 本地缓存
        Object local = localCache.getIfPresent(key);
        if (local != null) {
            return (T) local;
        }

        // L2: Redis
        String json = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(json)) {
            try {
                T value = objectMapper.readValue(json, type);
                // 回填本地缓存
                localCache.put(key, value);
                return value;
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize cache value: {}", key, e);
            }
        }

        return null;
    }

    /**
     * 写入缓存
     *
     * @param key   缓存Key
     * @param value 缓存值
     */
    public void put(String key, Object value) {
        put(key, value, 30, TimeUnit.MINUTES);
    }

    /**
     * 写入缓存（指定过期时间）
     *
     * @param key     缓存Key
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void put(String key, Object value, long timeout, TimeUnit unit) {
        // L1
        localCache.put(key, value);

        // L2
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, timeout, unit);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize cache value: {}", key, e);
        }
    }

    /**
     * 批量获取（优化 Redis 网络往返）
     *
     * @param keys 缓存Key集合
     * @param type 类型
     * @param <T>  泛型
     * @return 缓存值Map
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> batchGet(Collection<String> keys, Class<T> type) {
        Map<String, T> result = new HashMap<>();
        List<String> missKeys = new ArrayList<>();

        // L1 批量查询
        for (String key : keys) {
            Object local = localCache.getIfPresent(key);
            if (local != null) {
                result.put(key, (T) local);
            } else {
                missKeys.add(key);
            }
        }

        // L2 批量查询（使用 Redis Pipeline）
        if (!missKeys.isEmpty()) {
            List<String> values = redisTemplate.opsForValue().multiGet(missKeys);
            for (int i = 0; i < missKeys.size(); i++) {
                String key = missKeys.get(i);
                String json = values.get(i);

                if (StringUtils.hasText(json)) {
                    try {
                        T value = objectMapper.readValue(json, type);
                        result.put(key, value);
                        // 回填 L1
                        localCache.put(key, value);
                    } catch (JsonProcessingException e) {
                        log.warn("Failed to deserialize cache value: {}", key, e);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 使缓存失效
     *
     * @param key 缓存Key
     */
    public void evict(String key) {
        localCache.invalidate(key);
        redisTemplate.delete(key);
    }

    /**
     * 批量使缓存失效
     *
     * @param keys 缓存Key集合
     */
    public void batchEvict(Collection<String> keys) {
        keys.forEach(localCache::invalidate);
        redisTemplate.delete(keys);
    }

    /**
     * 清空所有本地缓存
     */
    public void clearLocal() {
        localCache.invalidateAll();
    }

    /**
     * 获取本地缓存统计信息
     *
     * @return 统计信息
     */
    public String getStats() {
        return localCache.stats().toString();
    }
}
