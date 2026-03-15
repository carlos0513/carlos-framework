package com.carlos.redis.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Redis 工具类
 * <p>
 * 注意事项：
 * 1. 所有方法都已处理空值和异常情况
 * 2. 批量操作使用 Pipeline 优化性能
 * 3. 使用线程池执行并行操作
 * </p>
 *
 * @author carlos
 * @date 2020/11/22 1:16
 */
@SuppressWarnings({"unchecked", "unused", "UnusedReturnValue"})
@Slf4j
@Component
public class RedisUtil {

    /**
     * 通配符
     */
    public static final String ALL = "*";

    /**
     * 默认批次大小
     */
    private static final int DEFAULT_BATCH = 5_000;

    /**
     * 默认每批删除大小
     */
    private static final int DEFAULT_DELETE_BATCH = 300;

    /**
     * 并行操作线程池 - 使用有界队列防止 OOM
     */
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
        4, 16, 60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(1000),
        new ThreadFactory() {
            private final AtomicLong counter = new AtomicLong(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "redis-util-" + counter.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        },
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private static RedisTemplate<String, Object> redisTemplate;
    private static RedisTemplate<String, Object> redisMasterTemplate;
    private static ValueOperations<String, Object> valueOperations;
    private static HashOperations<String, String, Object> hashOperations;
    private static SetOperations<String, Object> setOperations;
    private static ZSetOperations<String, Object> zSetOperations;
    private static ListOperations<String, Object> listOperations;

    public RedisUtil(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate,
                     @Qualifier("onlyMasterTemplate") RedisTemplate<String, Object> redisMasterTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
        RedisUtil.redisMasterTemplate = redisMasterTemplate;
        RedisUtil.valueOperations = redisTemplate.opsForValue();
        RedisUtil.setOperations = redisTemplate.opsForSet();
        RedisUtil.zSetOperations = redisTemplate.opsForZSet();
        RedisUtil.listOperations = redisTemplate.opsForList();
        RedisUtil.hashOperations = redisTemplate.opsForHash();
        log.info("RedisUtil initialized");
    }

    /**
     * 优雅关闭线程池
     */
    @PreDestroy
    public void destroy() {
        log.info("Shutting down RedisUtil executor...");
        EXECUTOR.shutdown();
        try {
            if (!EXECUTOR.awaitTermination(60, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
                if (!EXECUTOR.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("RedisUtil executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("RedisUtil executor shutdown complete");
    }

    // region----------------------  通用方法 start  ------------------------

    /**
     * 设置缓存失效时间
     *
     * @param key     键
     * @param timeout 时间（秒）
     * @return 是否设置成功
     */
    public static Boolean setExpire(@NonNull String key, long timeout) {
        return setExpire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存失效时间
     *
     * @param key      键
     * @param timeout  时间
     * @param timeUnit 时间单位
     * @return 是否设置成功，失败返回 false 不抛异常
     */
    public static Boolean setExpire(@NonNull String key, long timeout, TimeUnit timeUnit) {
        try {
            return redisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            log.error("Redis setExpire error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存失效时间
     *
     * @param key    键
     * @param expire 过期时间
     * @return 是否设置成功
     */
    public static Boolean setExpire(@NonNull String key, Duration expire) {
        if (expire == null) {
            return false;
        }
        return setExpire(key, expire.getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 获取缓存失效时间
     *
     * @param key 键
     * @return 时间（秒）-2 表示 key 不存在，-1 表示永久有效
     */
    public static Long getExpire(@NonNull String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis getExpire error, key: {}", key, e);
            return -2L;
        }
    }

    /**
     * key 是否存在
     *
     * @param key 键
     * @return true 存在，false 不存在或出错
     */
    public static Boolean hasKey(@NonNull String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis hasKey error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取相关 key 的数量
     *
     * @param pattern key 模糊匹配
     * @return key 数量
     */
    public static long getKeyNum(@NonNull String pattern) {
        AtomicLong keyNum = new AtomicLong(0);
        scanKeys(pattern, 1000, s -> keyNum.incrementAndGet());
        return keyNum.get();
    }

    /**
     * 删除单个缓存
     *
     * @param key 缓存 key
     * @return 是否删除成功
     */
    public static boolean delete(@NonNull String key) {
        try {
            Boolean success = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(success)) {
                log.debug("Redis delete success, key: {}", key);
                return true;
            } else {
                log.warn("Redis delete fail or key not exist, key: {}", key);
                return false;
            }
        } catch (Exception e) {
            log.error("Redis delete error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 安全批量删除，避免 Redis 慢查询
     *
     * @param keys 待删 key 集合
     * @return 实际删除条数
     */
    public static long delete(Collection<?> keys) {
        if (CollUtil.isEmpty(keys)) {
            return 0L;
        }

        List<? extends List<?>> partitions = CollUtil.split(keys, DEFAULT_DELETE_BATCH);
        AtomicLong total = new AtomicLong(0L);

        for (List<?> batch : partitions) {
            try {
                List<Object> results = redisMasterTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations operations) {
                        batch.forEach(operations::delete);
                        return null;
                    }
                });

                long deleted = results.stream()
                    .mapToLong(r -> r instanceof Long ? (Long) r : 0L)
                    .sum();
                total.addAndGet(deleted);
            } catch (Exception e) {
                log.error("Redis delete batch fail, batchSize={}", batch.size(), e);
            }
        }
        return total.get();
    }

    /**
     * 序列化 key
     *
     * @param key 缓存 key
     * @return 字节数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] rawKey(String key) {
        RedisSerializer<String> serializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        return serializer.serialize(key);
    }

    /**
     * 序列化 value
     *
     * @param value 值
     * @return 字节数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] rawValue(Object value) {
        RedisSerializer<Object> serializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
        return serializer.serialize(value);
    }

    /**
     * 使用通配符删除缓存
     *
     * @param pattern 带通配符的 key
     * @return 删除条数
     */
    public static long deletePattern(@NonNull String pattern) {
        long deleted = 0L;
        if (StrUtil.isBlank(pattern)) {
            return deleted;
        }

        List<String> batch = new ArrayList<>(DEFAULT_DELETE_BATCH);
        ScanOptions options = ScanOptions.scanOptions()
            .match(ObjUtil.defaultIfNull(rawKey(pattern), pattern.getBytes(StandardCharsets.UTF_8)))
            .count(DEFAULT_DELETE_BATCH)
            .build();

        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                batch.add(key);
                if (batch.size() == DEFAULT_DELETE_BATCH) {
                    deleted += delete(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                deleted += delete(batch);
            }
        } catch (Exception e) {
            log.error("Redis deletePattern error, pattern: {}", pattern, e);
        }
        return deleted;
    }

    /**
     * 匹配符合条件的 key（慎用，大数据量时使用 scanKeys）
     *
     * @param pattern key 通配符匹配
     * @return 匹配到的 key
     */
    public static Set<String> keys(@NonNull String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("Redis keys error, pattern: {}", pattern, e);
            return Collections.emptySet();
        }
    }

    /**
     * 扫描并处理符合条件的 key
     *
     * @param pattern   key 通配符匹配
     * @param batchSize 单次扫描条数
     * @param processor 处理函数
     */
    public static void scanKeys(String pattern, int batchSize, Consumer<String> processor) {
        if (processor == null || ObjUtil.isEmpty(pattern)) {
            return;
        }

        ScanOptions options = ScanOptions.scanOptions()
            .match(ObjUtil.defaultIfNull(rawKey(pattern), pattern.getBytes(StandardCharsets.UTF_8)))
            .count(batchSize)
            .build();

        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                processor.accept(cursor.next());
            }
        } catch (Exception e) {
            log.error("Redis scanKeys error, pattern: {}", pattern, e);
        }
    }

    /**
     * 扫描并获取符合条件的 key
     *
     * @param pattern   key 通配符匹配
     * @param batchSize 单次扫描条数
     * @return 匹配到的 key
     */
    public static Set<String> scanKeys(@NonNull String pattern, int batchSize) {
        Set<String> sets = new HashSet<>();
        scanKeys(pattern, batchSize, sets::add);
        return sets;
    }

    /**
     * 执行 Redis 命令（支持事务）
     *
     * @param session RedisSession
     * @return 执行结果
     */
    public static <T> T execute(SessionCallback<T> session) {
        try {
            return redisMasterTemplate.execute(session);
        } catch (Exception e) {
            log.error("Redis execute error", e);
            return null;
        }
    }

    /**
     * 使用 RedisConnection 执行原生命令
     *
     * @param callback RedisCallback
     * @return 执行结果
     */
    public static <T> T execute(RedisCallback<T> callback) {
        try {
            return redisMasterTemplate.execute(callback);
        } catch (Exception e) {
            log.error("Redis execute error", e);
            return null;
        }
    }

    /**
     * 执行 Pipeline 操作
     *
     * @param session RedisSession
     * @return 执行结果列表
     */
    public static List<Object> executePipelined(SessionCallback<?> session) {
        try {
            return redisMasterTemplate.executePipelined(session);
        } catch (Exception e) {
            log.error("Redis executePipelined error", e);
            return Collections.emptyList();
        }
    }

    /**
     * 执行 Pipeline 操作（原生）
     *
     * @param callback RedisCallback
     * @return 执行结果列表
     */
    public static List<Object> executePipelined(RedisCallback<?> callback) {
        try {
            return redisMasterTemplate.executePipelined(callback);
        } catch (Exception e) {
            log.error("Redis executePipelined error", e);
            return Collections.emptyList();
        }
    }

    // endregion----------------------   通用方法 end   ------------------------

    // region----------------------  字符串数据结构操作 start  ------------------------

    /**
     * 如果缓存不存在设置（永不过期）
     *
     * @param key   键
     * @param value 值
     * @return true 成功，false 失败或 key 已存在
     */
    public static <T> Boolean setIfAbsent(@NonNull String key, @NonNull T value) {
        try {
            return valueOperations.setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("Redis setIfAbsent error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 如果缓存不存在设置
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（小于等于 0 时设为无限期）
     * @param unit    时间单位
     * @return true 成功，false 失败或 key 已存在
     */
    public static <T> Boolean setIfAbsent(@NonNull String key, @NonNull T value, long timeout, TimeUnit unit) {
        try {
            return valueOperations.setIfAbsent(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis setIfAbsent error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 如果缓存不存在设置
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return true 成功，false 失败或 key 已存在
     */
    public static <T> Boolean setIfAbsent(@NonNull String key, @NonNull T value, Duration expire) {
        if (expire == null) {
            return setIfAbsent(key, value);
        }
        return setIfAbsent(key, value, expire.getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 设置普通缓存（永不过期）
     *
     * @param key   键
     * @param value 值
     * @return true 成功，false 失败
     */
    public static <T> boolean setValue(@NonNull String key, @NonNull T value) {
        try {
            valueOperations.set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis setValue error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置普通缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（秒，小于等于 0 时设为无限期）
     * @return true 成功，false 失败
     */
    public static <T> boolean setValue(@NonNull String key, @NonNull T value, long timeout) {
        return setValue(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置普通缓存
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return true 成功，false 失败
     */
    public static <T> boolean setValue(@NonNull String key, @NonNull T value, Duration expire) {
        if (expire == null) {
            return setValue(key, value);
        }
        return setValue(key, value, expire.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 设置普通缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（小于等于 0 时设为无限期）
     * @param unit    时间单位
     * @return true 成功，false 失败
     */
    public static <T> boolean setValue(@NonNull String key, @NonNull T value, long timeout, TimeUnit unit) {
        try {
            valueOperations.set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            log.error("Redis setValue error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 批量设置缓存
     *
     * @param values key 为缓存 key，value 为对应的值
     * @param expire 过期时长，null 表示永不过期
     */
    public static <T> void setValueList(@NonNull Map<String, T> values, Duration expire) {
        if (CollUtil.isEmpty(values)) {
            return;
        }

        try {
            redisMasterTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations ops) {
                    if (expire == null) {
                        values.forEach((k, v) -> ops.opsForValue().set(k, v));
                    } else {
                        values.forEach((k, v) -> ops.opsForValue().set(k, v, expire));
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("Redis setValueList error", e);
        }
    }

    /**
     * 批量插入 Value（永不过期）
     *
     * @param values key 为缓存 key，value 为对应的值
     */
    public static <T> void setValueList(@NonNull Map<String, T> values) {
        setValueList(values, null);
    }


    /**
     * 偏移量开始设置为指定值
     *
     * @param key    键
     * @param value  值
     * @param offset 偏移量
     * @return true成功 false 失败
     */
    public static <T> boolean setValueRang(
        @NonNull String key, @NonNull T value, long offset) {
        try {
            valueOperations.set(key, value, offset);
            return true;
        } catch (Throwable e) {
            log.error("RedisUtil setValueRang error, key:{} value:{}", key, value, e);
            return false;
        }
    }

    /**
     * 将字节串看成二进制位串，修改偏移量位offset的值为value
     *
     * @param key    键
     * @param value  值
     * @param offset 偏移量
     * @return true成功 false 失败
     */
    public static boolean setValueBit(
        @NonNull String key, long offset, @NonNull boolean value) {
        try {
            valueOperations.setBit(key, offset, value);
            return true;
        } catch (Throwable e) {
            log.error("RedisUtil setValueBit error, key:{}", key, e);
            return false;
        }
    }

    /**
     * 将字节串看成二进制位串，获取偏移量位offset的值, 超出末尾视为0
     *
     * @param key    键
     * @param offset 偏移量
     * @return 值
     */
    public static Object getValueBit(@NonNull String key, @NonNull long offset) {
        try {
            return valueOperations.getBit(key, offset);
        } catch (Throwable e) {
            log.error("RedisUtil getValueBit error, key:{}", key, e);
            return null;
        }
    }

    /**
     * 获取普通缓存
     *
     * @param key 键
     * @return 值，出错返回 null
     */
    public static <T> T getValue(@NonNull String key) {
        try {
            return (T) valueOperations.get(key);
        } catch (Exception e) {
            log.error("Redis getValue error, key: {}", key, e);
            return null;
        }
    }

    /**
     * 获取普通缓存（带类型转换）
     *
     * @param key   键
     * @param clazz 目标类型
     * @return 值
     * @deprecated 使用 {@link #getValue(String)} 即可
     */
    @Deprecated
    public static <T> T getValue(@NonNull String key, Class<T> clazz) {
        return getValue(key);
    }

    /**
     * 获取缓存，如果不存在则加载
     *
     * @param key    键
     * @param loader 加载函数
     * @return 值
     */
    public static <T> T getValue(@NonNull String key, Supplier<T> loader) {
        return getValue(key, loader, null, null);
    }

    /**
     * 获取缓存，如果不存在则加载
     *
     * @param key      键
     * @param loader   加载函数
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 值
     */
    public static <T> T getValue(@NonNull String key, Supplier<T> loader, Long timeout, TimeUnit timeUnit) {
        T value = getValue(key);
        if (value != null) {
            return value;
        }

        // 加锁防止缓存击穿
        synchronized (key.intern()) {
            // 双重检查
            value = getValue(key);
            if (value != null) {
                return value;
            }

            // 加载数据
            value = loader.get();
            if (value != null) {
                if (timeout != null && timeUnit != null) {
                    setValue(key, value, timeout, timeUnit);
                } else {
                    setValue(key, value);
                }
            }
            return value;
        }
    }

    /**
     * 批量获取缓存（分批次并行获取）
     *
     * @param keys      key 集合
     * @param batchSize 每批数量
     * @return 值列表，与 key 顺序一一对应
     */
    public static <T> List<T> getValueList(@NonNull Set<String> keys, int batchSize) {
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyList();
        }

        if (batchSize <= 0) {
            batchSize = DEFAULT_DELETE_BATCH;
        }

        List<String> keyList = new ArrayList<>(keys);
        Map<Integer, T> idxVal = new ConcurrentHashMap<>(keyList.size());
        List<List<String>> batches = Lists.partition(keyList, batchSize);

        // 使用 CompletableFuture 并行处理
        List<CompletableFuture<Void>> futures = batches.stream()
            .map(batch -> CompletableFuture.runAsync(() -> {
                try {
                    List<T> vs = (List<T>) valueOperations.multiGet(batch);
                    if (vs != null) {
                        int offset = keyList.indexOf(batch.get(0));
                        for (int i = 0; i < batch.size() && i < vs.size(); i++) {
                            idxVal.put(offset + i, vs.get(i));
                        }
                    }
                } catch (Exception e) {
                    log.error("Redis mget batch error, batchSize={}", batch.size(), e);
                }
            }, EXECUTOR))
            .collect(Collectors.toList());

        // 等待全部完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return keyList.stream()
            .map(k -> idxVal.get(keyList.indexOf(k)))
            .collect(Collectors.toList());
    }

    /**
     * 批量获取缓存（默认批次大小）
     *
     * @param keys keys
     * @return 值列表
     */
    public static <T> List<T> getValueList(@NonNull Set<String> keys) {
        return getValueList(keys, DEFAULT_DELETE_BATCH);
    }

    /**
     * 模糊查找 Value list
     *
     * @param pattern key 通配符匹配
     * @return 值列表
     */
    public static <T> List<T> getValueListByPattern(String pattern) {
        if (StrUtil.isEmpty(pattern)) {
            return Collections.emptyList();
        }
        Set<String> keys = scanKeys(pattern, 1000);
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return getValueList(keys);
    }


    /**
     * 批量获取缓存, 当key数目较大是，存在性能问题
     *
     * @param keys key集合
     * @return java.util.List<T>
     * @author Carlos
     * @date 2024/3/8 15:44
     */
    public static <T> List<T> mGet(@NonNull Set<String> keys) {
        try {
            return (List<T>) valueOperations.multiGet(keys);
        } catch (Exception e) {
            log.error("RedisUtil getValueList error,", e);
            return null;
        }
    }

    /**
     * 模糊查找 Value Set
     *
     * @param pattern key通配符
     * @return java.util.Set<T>
     * @author Carlos
     * @date 2024/3/8 15:24
     */
    public static <T> Set<T> getValueSet(String pattern) {
        if (StrUtil.isBlank(pattern)) {
            return null;
        }
        Set<String> keys = keys(pattern);
        if (CollUtil.isEmpty(keys)) {
            return null;
        }
        List<T> list = getValueList(keys);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        scanKeys(pattern, 500, key -> {

        });
        return new HashSet<>(list);
    }

    /**
     * 获取指定范围的字符串, 超出字符串末尾的数据会视为是空串
     *
     * @param key   键
     * @param start 起始索引
     * @param end   终止位置
     * @return 值
     * @deprecated 请使用 {@link #getValueRang(String, long, long)}
     */
    @Deprecated
    public static <T> T getValueRang(@NonNull String key, long start, long end, Class<T> clazz) {
        return getValueRang(key, start, end);
    }

    /**
     * 获取指定范围的字符串, 超出字符串末尾的数据会视为是空串
     *
     * @param key   键
     * @param start 起始索引
     * @param end   终止位置
     * @return 值
     */
    public static <T> T getValueRang(@NonNull String key, long start, long end) {
        return (T) valueOperations.get(key, start, end);
    }


    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几（大于 0）
     * @return 加上指定值之后 key 的值，失败返回 null
     */
    public static Long incrementValue(@NonNull String key, long delta) {
        if (delta <= 0) {
            log.warn("Increment delta must be > 0, got: {}", delta);
            return null;
        }
        try {
            return valueOperations.increment(key, delta);
        } catch (Exception e) {
            log.error("Redis incrementValue error, key: {}", key, e);
            return null;
        }
    }

    /**
     * 递增并设置过期时间
     *
     * @param key      键
     * @param delta    要增加几（大于 0）
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 加上指定值之后 key 的值
     */
    public static Long incrementValue(@NonNull String key, long delta, long timeout, TimeUnit timeUnit) {
        Long value = incrementValue(key, delta);
        if (value != null) {
            setExpire(key, timeout, timeUnit);
        }
        return value;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几（大于 0）
     * @return 减少指定值之后 key 的值，失败返回 null
     */
    public static Long decrementValue(@NonNull String key, long delta) {
        if (delta <= 0) {
            log.warn("Decrement delta must be > 0, got: {}", delta);
            return null;
        }
        try {
            return valueOperations.decrement(key, delta);
        } catch (Exception e) {
            log.error("Redis decrementValue error, key: {}", key, e);
            return null;
        }
    }

    // endregion----------------------   字符串数据结构操作 end   ------------------------

    // region----------------------  Hash 操作 start  ------------------------

    /**
     * 获取 Hash 中的值
     *
     * @param key  键
     * @param item 项
     * @return 值
     */
    public static <T> T getHash(@NonNull String key, @NonNull String item) {
        try {
            return (T) hashOperations.get(key, item);
        } catch (Exception e) {
            log.error("Redis getHash error, key: {}, item: {}", key, item, e);
            return null;
        }
    }

    /**
     * 获取整个 hash 并转换为对象
     *
     * @param key   key
     * @param clazz 目标类型
     * @return 对象
     */
    public static <T> T getHash(@NonNull String key, Class<T> clazz) {
        Map<Object, Object> hash = getHash(key);
        if (MapUtil.isEmpty(hash)) {
            return null;
        }
        try {
            return BeanUtil.toBean(hash, clazz);
        } catch (Exception e) {
            log.error("Redis getHash convert error, key: {}", key, e);
            return null;
        }
    }

    /**
     * 获取 hashKey 对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    @SuppressWarnings("unchecked")
    public static Map<Object, Object> getHash(@NonNull String key) {
        try {
            return (Map<Object, Object>) (Map<?, ?>) hashOperations.entries(key);
        } catch (Exception e) {
            log.error("Redis getHash error, key: {}", key, e);
            return Collections.emptyMap();
        }
    }

    /**
     * 批量获取多个 Hash 的部分字段
     *
     * @param keys      Hash key 集合
     * @param fields    字段列表
     * @param batchSize 每页数量
     * @param clazz     目标实体类型
     * @return key → 实体对象
     */
    public static <T> Map<String, T> hashMultiGet(List<String> keys, List<String> fields, int batchSize, Class<T> clazz) {
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        List<List<String>> pages = Lists.partition(keys, batchSize);
        Map<String, T> result = new ConcurrentHashMap<>(keys.size());

        List<CompletableFuture<Map<String, T>>> futures = pages.stream()
            .map(page -> CompletableFuture.supplyAsync(
                () -> hashMultiGet(page, fields, clazz), EXECUTOR))
            .collect(Collectors.toList());

        futures.forEach(f -> result.putAll(f.join()));
        return result;
    }

    /**
     * 批量获取多个 Hash 的部分字段
     *
     * @param keys   Hash key 集合
     * @param fields 字段列表
     * @param clazz  目标实体类型
     * @return key → 实体对象
     */
    public static <T> Map<String, T> hashMultiGet(Collection<String> keys, List<String> fields, Class<T> clazz) {
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        try {
            List<Object> rawResults = redisMasterTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations operations) {
                    for (String key : keys) {
                        if (CollUtil.isEmpty(fields)) {
                            operations.opsForHash().entries(key);
                        } else {
                            operations.opsForHash().multiGet(key, fields);
                        }
                    }
                    return null;
                }
            });

            Map<String, T> ans = new LinkedHashMap<>(keys.size());
            int index = 0;
            for (String key : keys) {
                Object row = rawResults.get(index++);
                if (row == null) {
                    continue;
                }
                Map<Object, Object> entries = parseHashResult(row, fields);
                if (CollUtil.isNotEmpty(entries)) {
                    try {
                        T bean = BeanUtil.toBean(entries, clazz);
                        ans.put(key, bean);
                    } catch (Exception e) {
                        log.error("Redis hashMultiGet convert error, key: {}", key, e);
                    }
                }
            }
            return ans;
        } catch (Exception e) {
            log.error("Redis hashMultiGet error", e);
            return Collections.emptyMap();
        }
    }

    private static Map<Object, Object> parseHashResult(Object row, List<String> fields) {
        if (row instanceof List) {
            Map<Object, Object> entries = new HashMap<>();
            List<Object> list = (List<Object>) row;
            if (fields != null) {
                for (int i = 0; i < list.size() && i < fields.size(); i++) {
                    Object o = list.get(i);
                    if (o != null) {
                        entries.put(fields.get(i), o);
                    }
                }
            }
            return entries;
        } else if (row instanceof Map) {
            return (Map<Object, Object>) row;
        }
        return Collections.emptyMap();
    }

    /**
     * 批量获取多个 Hash 的全部字段
     *
     * @param keys  Hash key 集合
     * @param clazz 目标实体类型
     * @return key → 实体对象
     */
    public static <T> Map<String, T> hashMultiGetAll(Collection<String> keys, Class<T> clazz) {
        return hashMultiGet(keys, null, clazz);
    }

    /**
     * 批量获取多个 Hash 的全部字段
     *
     * @param keys      Hash key 集合
     * @param batchSize 每页数量
     * @param clazz     目标实体类型
     * @return key → 实体对象
     */
    public static <T> Map<String, T> hashMultiGetAll(List<String> keys, int batchSize, Class<T> clazz) {
        return hashMultiGet(keys, null, batchSize, clazz);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return 是否成功
     */
    @SuppressWarnings("unchecked")
    public static boolean putHash(@NonNull String key, @NonNull Map<String, Object> map) {
        try {
            hashOperations.putAll(key, (Map<String, Object>) (Map<?, ?>) map);
            return true;
        } catch (Exception e) {
            log.error("Redis putHash error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 将对象保存为 hash
     *
     * @param key  key
     * @param hash hash 对象
     * @return 是否成功
     */
    public static boolean putHash(@NonNull String key, @NonNull Object hash) {
        try {
            Map<String, Object> map = BeanUtil.beanToMap(hash);
            return putHash(key, map);
        } catch (Exception e) {
            log.error("Redis putHash error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 将对象保存为 hash 并设置过期时间
     *
     * @param key     key
     * @param hash    hash 对象
     * @param timeout 过期时间（秒）
     * @return 是否成功
     */
    public static boolean putHash(@NonNull String key, @NonNull Object hash, long timeout) {
        if (putHash(key, hash) && timeout > 0) {
            return setExpire(key, timeout);
        }
        return false;
    }

    /**
     * HashSet 并设置时间
     *
     * @param key     键
     * @param map     对应多个键值
     * @param timeout 时间(秒)
     * @return {boolean}
     */
    public static boolean putHash(@NonNull String key, @NonNull Map<String, Object> map, @NonNull long timeout) {
        putHash(key, map);
        if (timeout > 0) {
            setExpire(key, timeout);
        }
        return true;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return 是否成功
     */
    public static <T> boolean putHash(@NonNull String key, @NonNull String item, @NonNull T value) {
        try {
            hashOperations.put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("Redis putHash error, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param item    项
     * @param value   值
     * @param timeout 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return {boolean}
     */
    public static <T> boolean putHash(@NonNull String key, @NonNull String item, @NonNull T value, @NonNull long timeout) {
        putHash(key, item, value);
        if (timeout > 0) {
            setExpire(key, timeout);
        }
        return true;
    }

    /**
     * 删除 hash 表中的值
     *
     * @param key  键
     * @param item 项，可以使多个
     */
    public static void deleteHash(@NonNull String key, @NonNull Object... item) {
        try {
            hashOperations.delete(key, item);
        } catch (Exception e) {
            log.error("Redis deleteHash error, key: {}", key, e);
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 加上指定值之后 key 的值
     */
    public static double incrementHash(@NonNull String key, @NonNull String item, @NonNull double by) {
        return hashOperations.increment(key, item, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @return true 存在，false 不存在或出错
     */
    public static boolean hasKeyHash(@NonNull String key, @NonNull String item) {
        try {
            return Boolean.TRUE.equals(hashOperations.hasKey(key, item));
        } catch (Exception e) {
            log.error("Redis hasKeyHash error, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return 减少指定值之后 key 的值
     */
    public static long decrementHash(@NonNull String key, @NonNull String item, @NonNull long by) {
        return incrementHash(key, item, -by);
    }
    // endregion----------------------   Hash操作 end   ------------------------

    // region----------------------  集合数据结构操作 start  ------------------------

    /**
     * 将 set 数据放入缓存
     *
     * @param key    键
     * @param values 值，可以是多个
     * @return 成功个数
     */
    @SafeVarargs
    public static <T> Long addSet(@NonNull String key, @NonNull T... values) {
        try {
            return setOperations.add(key, values);
        } catch (Exception e) {
            log.error("Redis addSet error, key: {}", key, e);
            return 0L;
        }
    }

    /**
     * 将集合中的数据放入 set 缓存
     *
     * @param key  键
     * @param list 值
     * @return 成功个数
     */
    public static Long addSet(@NonNull String key, @NonNull Set<?> list) {
        return addSet(key, list.toArray());
    }

    /**
     * 将集合中的数据放入set缓存
     *
     * @param key    键
     * @param list   值 可以是多个
     * @param expire 过期时间
     * @return 成功个数
     */
    public static Long addSet(@NonNull String key, @NonNull Set<?> list, Duration expire) {
        Long count = addSet(key, list.toArray());
        if (expire != null) {
            setExpire(key, expire);
        }
        return count;
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param expire 过期时间
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static <T> Long addSet(@NonNull String key, Duration expire, @NonNull T... values) {
        Long count = addSet(key, values);
        if (expire != null) {
            setExpire(key, expire);
        }
        return count;
    }

    /**
     * 根据 key 获取 Set 中的所有值
     *
     * @param key 键
     * @return Set
     */
    public static <T> Set<T> getSet(@NonNull String key) {
        try {
            return (Set<T>) setOperations.members(key);
        } catch (Exception e) {
            log.error("Redis getSet error, key: {}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * 根据 value 查询是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在
     */
    public static <T> boolean hasKeySet(@NonNull String key, @NonNull T value) {
        try {
            return Boolean.TRUE.equals(setOperations.isMember(key, value));
        } catch (Exception e) {
            log.error("Redis hasKeySet error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取 set 缓存的长度
     *
     * @param key 键
     * @return 缓存长度
     */
    public static Long getSetSize(@NonNull String key) {
        try {
            return setOperations.size(key);
        } catch (Exception e) {
            log.error("Redis getSetSize error, key: {}", key, e);
            return 0L;
        }
    }

    /**
     * 移除值为 value 的
     *
     * @param key    键
     * @param values 值，可以是多个
     * @return 移除的个数
     */
    public static Long removeSet(@NonNull String key, @NonNull Object... values) {
        try {
            return setOperations.remove(key, values);
        } catch (Exception e) {
            log.error("Redis removeSet error, key: {}", key, e);
            return 0L;
        }
    }

    // endregion----------------------   集合数据结构操作 end   ------------------------

    // region----------------------  列表数据结构操作 start  ------------------------

    /**
     * 将 list 放入缓存（左侧插入）
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public static <T> boolean leftPushList(@NonNull String key, @NonNull T value) {
        try {
            listOperations.leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis leftPushList error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 将 list 放入缓存（右侧插入）
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public static <T> boolean rightPushList(@NonNull String key, @NonNull T value) {
        try {
            listOperations.rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis rightPushList error, key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取 list 缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束（0 到 -1 代表所有值）
     * @return list 缓存内容
     */
    public static <T> List<T> getList(@NonNull String key, @NonNull Long start, @NonNull Long end) {
        try {
            return (List<T>) listOperations.range(key, start, end);
        } catch (Exception e) {
            log.error("Redis getList error, key: {}", key, e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取 list 缓存的所有内容
     *
     * @param key 键
     * @return list 缓存内容
     */
    public static <T> List<T> getList(@NonNull String key) {
        return getList(key, 0L, -1L);
    }

    // endregion----------------------   列表数据结构操作 end   ------------------------

    // region----------------------  数据库操作 start  ------------------------

    /**
     * 清所有库，线上慎用！
     */
    public static void flushAll() {
        try {
            redisMasterTemplate.execute((RedisCallback<Void>) conn -> {
                conn.flushAll();
                return null;
            });
            log.info("Redis flushAll executed");
        } catch (Exception e) {
            log.error("Redis flushAll error", e);
        }
    }

    /**
     * 清理当前 DB
     */
    public static void flushDb() {
        try {
            redisMasterTemplate.execute((RedisCallback<Void>) conn -> {
                conn.flushDb();
                return null;
            });
            log.info("Redis flushDb executed");
        } catch (Exception e) {
            log.error("Redis flushDb error", e);
        }
    }

    /**
     * 清理指定 DB
     *
     * @param dbIndex DB 索引
     */
    public static void flushDb(int dbIndex) {
        try {
            redisMasterTemplate.execute((RedisCallback<Void>) conn -> {
                conn.select(dbIndex);
                conn.flushDb();
                return null;
            });
            log.info("Redis flushDb executed for db {}", dbIndex);
        } catch (Exception e) {
            log.error("Redis flushDb error, dbIndex: {}", dbIndex, e);
        }
    }

    /**
     * 删除指定前缀空间
     *
     * @param keyPrefix 前缀，如 "region:"
     * @return 实际删除条数
     */
    public static long deleteSpace(String keyPrefix) {
        return deleteSpace(keyPrefix, DEFAULT_BATCH);
    }

    /**
     * 带限流的删除
     *
     * @param keyPrefix 前缀
     * @param batchSize 每批 SCAN 数量
     * @return 删除条数
     */
    public static long deleteSpace(String keyPrefix, int batchSize) {
        if (!keyPrefix.endsWith(":")) {
            keyPrefix = keyPrefix + ":";
        }
        String pattern = keyPrefix + "*";

        final AtomicLong deleted = new AtomicLong(0);
        List<String> list = new ArrayList<>();

        scanKeys(pattern, batchSize, key -> {
            list.add(key);
            if (list.size() >= batchSize) {
                deleted.addAndGet(delete(list));
                list.clear();
            }
        });

        if (CollUtil.isNotEmpty(list)) {
            deleted.addAndGet(delete(list));
        }

        log.info("Redis deleteSpace finish, prefix={}, deleted={}", keyPrefix, deleted.get());
        return deleted.get();
    }

    // endregion----------------------   数据库操作 end   ------------------------

    // region----------------------  Lua 脚本工具 start  ------------------------

    /**
     * 加载脚本
     *
     * @param script 脚本内容
     * @return SHA1 摘要
     */
    public static String loadScripts(String script) {
        if (StrUtil.isBlank(script)) {
            return null;
        }
        try {
            return (String) redisTemplate.execute((RedisConnection c) ->
                c.scriptLoad(script.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Redis loadScripts error", e);
            return null;
        }
    }

    /**
     * 判断脚本是否存在（支持单机 & 集群）
     *
     * @param sha SHA1 摘要
     * @return true=已存在
     */
    public static List<Boolean> scriptExists(String... sha) {
        if (sha == null || sha.length == 0) {
            return Collections.singletonList(false);
        }
        try (RedisConnection conn = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            if (conn instanceof RedisClusterConnection) {
                RedisClusterConnection cluster = (RedisClusterConnection) conn;
                for (RedisClusterNode node : cluster.clusterGetNodes()) {
                    if (node.isMaster()) {
                        return cluster.scriptExists(sha);
                    }
                }
            } else {
                return conn.scriptExists(sha);
            }
        } catch (Exception e) {
            log.error("Redis scriptExists error", e);
        }
        return Collections.singletonList(false);
    }

    /**
     * 执行 Lua 脚本
     *
     * @param script  脚本
     * @param keys    KEYS 数组
     * @param args    ARGV 数组
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T lua(RedisScript<T> script, List<String> keys, Object... args) {
        return (T) redisMasterTemplate.execute(script,
            redisTemplate.getStringSerializer(),  // Key序列化器
            redisTemplate.getStringSerializer(),
            keys,
            args);
    }

    /**
     * 执行已预加载的 Lua（SHA 模式）
     *
     * @param sha         预加载得到的 SHA1
     * @param numKeys     key个数
     * @param keysAndArgs KEY  ARGV 数组
     * @param <T>      返回泛型
     */
    public static <T> T evalSha(String sha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        RedisConnection conn = redisMasterTemplate.getConnectionFactory().getConnection();
        try {
            return redisMasterTemplate.execute(script, keys, args);
        } catch (Exception e) {
            log.error("Redis lua error", e);
            return null;
        }
    }

    /**
     * 执行一次性 Lua（整串脚本，低频用）
     *
     * @param script   完整 Lua 源码
     * @param keys     KEYS 数组
     * @param args     ARGV 数组
     * @param retType  返回 Java 类型
     * @param <T>      返回泛型
     */
    public static <T> T lua(String script, List<String> keys, List<String> args, Class<T> retType) {
        RedisScript<T> redisScript = new DefaultRedisScript<>(script, retType);
        return (T) redisMasterTemplate.execute(redisScript, keys, args);
    }

    /**
     * 执行一次性 Lua（整串脚本，低频用）
     *
     * @param script   完整 Lua 源码
     * @param retType  返回 Java 类型
     * @param <T>      返回泛型
     */
    public static <T> T lua(String script, Class<T> retType) {
        return lua(script, Collections.emptyList(), Collections.emptyList(), retType);
    }

    /**
     * 执行一次性 Lua（整串脚本，低频用）
     *
     * @param script   完整 Lua 源码
     * @param key      KEYS[1]
     * @param retType  返回 Java 类型
     * @param <T>      返回泛型
     */
    public static <T> T lua(String script, String key, Class<T> retType) {
        return lua(script, Collections.singletonList(key), Collections.emptyList(), retType);
    }

    /**
     * 执行一次性 Lua（整串脚本，低频用）
     *
     * @param script   完整 Lua 源码
     * @param key      KEYS[1]
     * @param arg      ARGV[1]
     * @param retType  返回 Java 类型
     * @param <T>      返回泛型
     */
    public static <T> T lua(String script, String key, String arg, Class<T> retType) {
        return lua(script, Collections.singletonList(key), Collections.singletonList(arg), retType);
    }


}
