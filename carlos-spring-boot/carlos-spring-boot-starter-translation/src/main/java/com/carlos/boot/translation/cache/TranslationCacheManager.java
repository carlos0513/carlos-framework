package com.carlos.boot.translation.cache;

import com.carlos.boot.translation.config.TranslationProperties;
import com.carlos.redis.util.RedisUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class TranslationCacheManager {

    private final Cache<String, Object> localCache;

    public TranslationCacheManager(TranslationProperties properties) {
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
        try {
            T value = RedisUtil.getValue(key);
            // 回填本地缓存
            localCache.put(key, value);
            return value;
        } catch (Throwable e) {
            log.warn("Failed to deserialize cache value: {}", key, e);
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
            RedisUtil.setValue(key, value, timeout, unit);
        } catch (Throwable e) {
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
            Map<String, T> valueList = RedisUtil.getValueMap(missKeys, 300);
            result = valueList;
            for (Map.Entry<String, T> entry : valueList.entrySet()) {
                // 回填 L1
                localCache.put(entry.getKey(), entry.getValue());
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
        RedisUtil.delete(key);
    }

    /**
     * 批量使缓存失效
     *
     * @param keys 缓存Key集合
     */
    public void batchEvict(Collection<String> keys) {
        keys.forEach(localCache::invalidate);
        RedisUtil.delete(keys);
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
