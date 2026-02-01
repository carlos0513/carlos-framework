package com.carlos.redis.multilevel;

import com.carlos.redis.caffeine.CaffeineUtil;
import com.carlos.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 多级缓存工具类
 * 支持Caffeine（L1）+ Redis（L2）两级缓存
 *
 * @author carlos
 * @date 2026-02-01
 */
@Slf4j
@Component
public class MultiLevelCacheUtil {

    /**
     * 获取缓存，先从L1获取，再从L2获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        // 先从L1缓存获取
        Object value = CaffeineUtil.get(key);
        if (value != null) {
            if (log.isDebugEnabled()) {
                log.debug("Hit L1 cache for key: {}", key);
            }
            return value;
        }

        // 从L2缓存获取
        value = RedisUtil.getValue(key);
        if (value != null) {
            if (log.isDebugEnabled()) {
                log.debug("Hit L2 cache for key: {}, updating L1", key);
            }
            // 回写到L1缓存
            CaffeineUtil.put(key, value);
        }

        return value;
    }

    /**
     * 获取缓存，如果不存在则加载
     *
     * @param key    键
     * @param loader 加载函数
     * @return 值
     */
    public static Object get(String key, Supplier<Object> loader) {
        return get(key, loader, 0, null);
    }

    /**
     * 获取缓存，如果不存在则加载并设置过期时间
     *
     * @param key      键
     * @param loader   加载函数
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 值
     */
    public static Object get(String key, Supplier<Object> loader, long timeout, TimeUnit timeUnit) {
        // 先从L1缓存获取
        Object value = CaffeineUtil.get(key);
        if (value != null) {
            if (log.isDebugEnabled()) {
                log.debug("Hit L1 cache for key: {}", key);
            }
            return value;
        }

        // 从L2缓存获取
        value = RedisUtil.getValue(key);
        if (value != null) {
            if (log.isDebugEnabled()) {
                log.debug("Hit L2 cache for key: {}, updating L1", key);
            }
            // 回写到L1缓存
            CaffeineUtil.put(key, value);
            return value;
        }

        // 缓存未命中，加载数据
        if (log.isDebugEnabled()) {
            log.debug("Cache miss for key: {}, loading from source", key);
        }
        value = loader.get();
        if (value != null) {
            // 写入两级缓存
            put(key, value, timeout, timeUnit);
        }

        return value;
    }

    /**
     * 设置缓存到两级缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, Object value) {
        put(key, value, 0, null);
    }

    /**
     * 设置缓存到两级缓存，并设置过期时间
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public static void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        // 写入L1缓存
        CaffeineUtil.put(key, value);

        // 写入L2缓存
        if (timeout > 0 && timeUnit != null) {
            RedisUtil.setValue(key, value, Duration.ofMillis(timeUnit.toMillis(timeout)));
        } else {
            RedisUtil.setValue(key, value);
        }

        if (log.isDebugEnabled()) {
            log.debug("Put value to L1 and L2 cache for key: {}", key);
        }
    }

    /**
     * 删除两级缓存
     *
     * @param key 键
     */
    public static void evict(String key) {
        // 删除L1缓存
        CaffeineUtil.evict(key);

        // 删除L2缓存
        RedisUtil.delete(key);

        if (log.isDebugEnabled()) {
            log.debug("Evicted key from L1 and L2 cache: {}", key);
        }
    }

    /**
     * 清空两级缓存
     */
    public static void clear() {
        // 清空L1缓存
        CaffeineUtil.clear();

        // 注意：不清空L2缓存，因为Redis可能被多个应用共享
        log.info("Cleared L1 cache");
    }

    /**
     * 仅删除L1缓存
     *
     * @param key 键
     */
    public static void evictL1(String key) {
        CaffeineUtil.evict(key);
        if (log.isDebugEnabled()) {
            log.debug("Evicted key from L1 cache: {}", key);
        }
    }

    /**
     * 仅删除L2缓存
     *
     * @param key 键
     */
    public static void evictL2(String key) {
        RedisUtil.delete(key);
        if (log.isDebugEnabled()) {
            log.debug("Evicted key from L2 cache: {}", key);
        }
    }
}
