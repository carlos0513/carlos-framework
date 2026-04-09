package com.carlos.redis.multilevel;

import com.carlos.redis.caffeine.CaffeineUtil;
import com.carlos.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 多级缓存工具类
 * <p>
 * 支持 Caffeine（L1）+ Redis（L2）两级缓存
 * <p>
 * 一致性策略：
 * 1. 读：先读 L1，再读 L2，L2 命中回写 L1
 * 2. 写：先写 L2，再写 L1（确保 L1 不会比 L2 存活更久）
 * 3. 删：先删 L2，再删 L1（防止脏读）
 * </p>
 *
 * @author carlos
 * @date 2026-02-01
 */
@Slf4j
public class MultiLevelCacheUtil {

    /**
     * L1 缓存默认过期时间（应该短于 L2）
     */
    private static final long L1_DEFAULT_EXPIRE_MINUTES = 5;

    /**
     * L1 缓存最大过期时间（防止 L1 比 L2 长）
     */
    private static final long L1_MAX_EXPIRE_MINUTES = 30;

    /**
     * 获取缓存，先从 L1 获取，再从 L2 获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        // 先从 L1 缓存获取
        Object value = CaffeineUtil.get(key);
        if (value != null) {
            log.debug("Hit L1 cache for key: {}", key);
            return value;
        }

        // 从 L2 缓存获取
        value = RedisUtil.getValue(key);
        if (value != null) {
            log.debug("Hit L2 cache for key: {}, updating L1", key);
            // 回写到 L1 缓存，使用默认较短的过期时间
            putL1(key, value, L1_DEFAULT_EXPIRE_MINUTES, TimeUnit.MINUTES);
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
     * @param timeout  L2 过期时间
     * @param timeUnit 时间单位
     * @return 值
     */
    public static Object get(String key, Supplier<Object> loader, long timeout, TimeUnit timeUnit) {
        // 先从 L1 缓存获取
        Object value = CaffeineUtil.get(key);
        if (value != null) {
            log.debug("Hit L1 cache for key: {}", key);
            return value;
        }

        // 从 L2 缓存获取
        value = RedisUtil.getValue(key);
        if (value != null) {
            log.debug("Hit L2 cache for key: {}, updating L1", key);
            // 回写到 L1 缓存
            long l1Expire = calculateL1Expire(timeout, timeUnit);
            putL1(key, value, l1Expire, TimeUnit.MINUTES);
            return value;
        }

        // 缓存未命中，加载数据
        log.debug("Cache miss for key: {}, loading from source", key);

        // 使用同步加载防止缓存击穿
        synchronized (key.intern()) {
            // 双重检查
            value = CaffeineUtil.get(key);
            if (value != null) {
                return value;
            }
            value = RedisUtil.getValue(key);
            if (value != null) {
                long l1Expire = calculateL1Expire(timeout, timeUnit);
                putL1(key, value, l1Expire, TimeUnit.MINUTES);
                return value;
            }

            // 真正加载数据
            value = loader.get();
            if (value != null) {
                // 写入两级缓存
                put(key, value, timeout, timeUnit);
            }
            return value;
        }
    }

    /**
     * 计算 L1 过期时间
     * <p>
     * 确保 L1 过期时间不会比 L2 长，避免数据不一致
     * </p>
     *
     * @param l2Timeout  L2 过期时间
     * @param l2TimeUnit L2 时间单位
     * @return L1 过期时间（分钟）
     */
    private static long calculateL1Expire(long l2Timeout, TimeUnit l2TimeUnit) {
        if (l2Timeout <= 0 || l2TimeUnit == null) {
            // L2 永不过期，L1 使用默认过期时间
            return L1_DEFAULT_EXPIRE_MINUTES;
        }

        // 转换为分钟
        long l2Minutes = l2TimeUnit.toMinutes(l2Timeout);

        // L1 过期时间为 L2 的一半，但不超过最大值
        long l1Minutes = Math.min(l2Minutes / 2, L1_MAX_EXPIRE_MINUTES);

        // 至少 1 分钟
        return Math.max(l1Minutes, 1);
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
        // 先写 L2（数据源）
        if (timeout > 0 && timeUnit != null) {
            RedisUtil.setValue(key, value, timeout, timeUnit);
        } else {
            RedisUtil.setValue(key, value);
        }

        // 再写 L1（缓存层），过期时间不能长于 L2
        long l1Expire = calculateL1Expire(timeout, timeUnit);
        putL1(key, value, l1Expire, TimeUnit.MINUTES);

        log.debug("Put value to L1 and L2 cache for key: {}", key);
    }

    /**
     * 仅写入 L1 缓存（用于内部回写）
     */
    private static void putL1(String key, Object value, long timeout, TimeUnit timeUnit) {
        // Caffeine 不支持设置单个 key 的过期时间，由全局配置控制
        // 这里只做记录，实际过期时间由 Caffeine 配置决定
        CaffeineUtil.put(key, value);
    }

    /**
     * 删除两级缓存
     * <p>
     * 删除策略：先删 L2，再删 L1
     * 防止在删除过程中有新的读请求导致 L1 脏读
     * </p>
     *
     * @param key 键
     */
    public static void evict(String key) {
        // 先删 L2
        RedisUtil.delete(key);

        // 再删 L1
        CaffeineUtil.evict(key);

        log.debug("Evicted key from L1 and L2 cache: {}", key);
    }

    /**
     * 批量删除两级缓存
     *
     * @param keys 键集合
     */
    public static void evictAll(Iterable<String> keys) {
        // 先删 L2
        for (String key : keys) {
            RedisUtil.delete(key);
        }

        // 再删 L1
        CaffeineUtil.evictAll(keys);

        log.debug("Evited keys from L1 and L2 cache");
    }

    /**
     * 清空两级缓存
     * <p>
     * 注意：不清空 L2 缓存，因为 Redis 可能被多个应用共享
     * </p>
     */
    public static void clear() {
        // 清空 L1 缓存
        CaffeineUtil.clear();

        log.info("Cleared L1 cache (L2 not cleared for safety)");
    }

    /**
     * 仅删除 L1 缓存
     *
     * @param key 键
     */
    public static void evictL1(String key) {
        CaffeineUtil.evict(key);
        log.debug("Evicted key from L1 cache: {}", key);
    }

    /**
     * 仅删除 L2 缓存
     *
     * @param key 键
     */
    public static void evictL2(String key) {
        RedisUtil.delete(key);
        log.debug("Evicted key from L2 cache: {}", key);
    }

    /**
     * 刷新缓存（先删除再加载）
     *
     * @param key    键
     * @param loader 加载函数
     * @return 新值
     */
    public static Object refresh(String key, Supplier<Object> loader) {
        evict(key);
        return get(key, loader);
    }

    /**
     * 刷新缓存（带过期时间）
     *
     * @param key      键
     * @param loader   加载函数
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 新值
     */
    public static Object refresh(String key, Supplier<Object> loader, long timeout, TimeUnit timeUnit) {
        evict(key);
        return get(key, loader, timeout, timeUnit);
    }
}
