package com.carlos.redis.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.redis.exception.CacheException;
import com.carlos.redis.exception.CacheGetException;
import com.google.common.collect.Lists;
import io.lettuce.core.RedisNoScriptException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;
import org.springframework.scripting.ScriptSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * redis工具
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

    private static RedisTemplate redisTemplate;
    private static RedisTemplate redisMasterTemplate;
    private static ValueOperations valueOperations;
    private static HashOperations hashOperations;
    private static SetOperations setOperations;
    private static ZSetOperations zSetOperations;
    private static ListOperations listOperations;


    private static final int DEFAULT_BATCH = 5_000;

    // 与 CPU 核数相同，也可配 System.getProperty("mget.parallelism", "8")
    public static final ForkJoinPool POOL = new ForkJoinPool(
            8,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null, true);   // true=异步模式，更轻量

    public RedisUtil(@Qualifier("redisTemplate") RedisTemplate redisTemplate, @Qualifier("onlyMasterTemplate") RedisTemplate redisMasterTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
        RedisUtil.redisMasterTemplate = redisMasterTemplate;
        RedisUtil.valueOperations = redisTemplate.opsForValue();
        RedisUtil.setOperations = redisTemplate.opsForSet();
        RedisUtil.zSetOperations = redisTemplate.opsForZSet();
        RedisUtil.listOperations = redisTemplate.opsForList();
        RedisUtil.hashOperations = redisTemplate.opsForHash();
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
     * @return 是否设置成功
     */
    public static Boolean setExpire(@NonNull String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 设置缓存失效时间
     *
     * @param key    键
     * @param expire 过期时间
     * @return 是否设置成功
     */
    public static Boolean setExpire(@NonNull String key, Duration expire) {
        return setExpire(key, expire.getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 获取缓存失效时间
     *
     * @param key 键
     * @return 时间（秒） 0为永久有效
     */
    public static Long getExpire(@NonNull String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * key 是否存在
     *
     * @param key 键
     * @return {Boolean}
     */
    public static Boolean hasKey(@NonNull String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取相关key的数量
     *
     * @param pattern key模糊匹配
     * @return int
     * @author Carlos
     * @date 2024/3/8 17:06
     */
    public static int getKeyNum(@NonNull String pattern) {
        final int[] keyNum = {0};
        scanKeys(pattern, 1000, s -> keyNum[0]++);
        return keyNum[0];
    }

    /**
     * 删除单个缓存
     *
     * @param key 缓存key
     * @return java.lang.Boolean
     * @author carlos
     * @date 2022/1/5 9:43
     */
    public static boolean delete(@NonNull String key) {
        Boolean success = redisTemplate.delete(key);
        if (success == null || !success) {
            log.error("RedisUtil delete cache fail, key:{}", key);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("RedisUtil delete cache by key:{}", key);
        }
        return true;
    }


    /**
     * 安全批量删除，避免 Redis 慢查询
     * @param keys  待删 key 集合
     * @return 实际删除条数
     */

    public static long delete(Collection<?> keys) {
        if (CollUtil.isEmpty(keys)) {
            return 0L;
        }

        int batchSize = 300;
        List<? extends List<?>> partitions = CollUtil.split(keys, batchSize);

        AtomicLong total = new AtomicLong(0L);
        partitions.forEach(batch -> {
            try {
                /* 方法 3：SessionCallback + pipeline */
                List<Object> results = redisMasterTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations operations) {
                        batch.forEach(operations::delete); // 高级 API
                        return null; // 必须
                    }
                });

                long deleted = results.stream()
                        .mapToLong(r -> r instanceof Long ? (Long) r : 0L)
                        .sum();
                total.addAndGet(deleted);
            } catch (Exception e) {
                log.error("delete batch fail, batchSize={}", batch.size(), e);
            }
        });
        return total.get();
    }


    /**
     * 序列化key
     *
     * @param key 缓存key
     * @return byte[]
     */
    public static byte[] rawKey(Object key) {
        return redisTemplate.getKeySerializer().serialize(key);
    }

    /**
     * 序列化value
     *
     * @param value 只
     * @return byte[]
     */
    public static byte[] rawValue(Object value) {
        return redisTemplate.getValueSerializer().serialize(value);
    }


    /**
     * 使用通配符删除缓存
     *
     * @param pattern 带通配符的key
     * @return int
     */
    public static long deletePattern(@NonNull String pattern) {
        long deleted = 0L;
        if (StrUtil.isBlank(pattern)) {
            return deleted;
        }
        List<String> batch = new ArrayList<>(300);
        ScanOptions options = ScanOptions.scanOptions().match(ObjUtil.defaultIfNull(rawKey(pattern), pattern.getBytes())).count(300).build();
        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                batch.add(key);
                if (batch.size() == 300) {
                    deleted += delete(batch);
                    batch.clear();
                }
            }
            // 尾批
            if (!batch.isEmpty()) {
                deleted += delete(batch);
            }
        }
        return deleted;
    }

    /**
     * 匹配符合条件的key
     *
     * @param pattern key通配符匹配
     * @return 匹配到的key
     */
    public static Set<String> keys(@NonNull String pattern) {
        return redisTemplate.keys(pattern);
        // return scanKeys(pattern, 300);
    }

    /**
     * 扫描并处理符合条件的key
     *
     * @param pattern      key通配符匹配
     * @param batchSize    单次扫描条数
     * @param processor    处理函数
     */
    public static void scanKeys(String pattern, int batchSize, Consumer<String> processor) {
        if (processor == null) {
            return;
        }
        if (ObjUtil.isEmpty(pattern)) {
            return;
        }
        ScanOptions options = ScanOptions.scanOptions().match(ObjUtil.defaultIfNull(rawKey(pattern), pattern.getBytes())).count(batchSize).build();
        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                processor.accept(cursor.next());
            }
        }
    }

    /**
     * 扫描并获取符合条件的key
     *
     * @param pattern      key通配符匹配
     * @param batchSize    单次扫描条数
     * @return 匹配到的key
     */
    public static Set<String> scanKeys(@NonNull String pattern, int batchSize) {
        Set<String> sets = new HashSet<>();
        scanKeys(pattern, batchSize, sets::add);
        return sets;
    }

    /**
     * 执行Redis命令, 键值自动序列化
     * 回调接口： SessionCallback
     * 网络模型：单命令同步，每条操作立即发、立即收结果
     * 返回值： T   session  里  return  的对象（可为  String 、 Long 、自定义 DTO）
     * 场景：
     * 需要 事务（ multi()/exec() ）
     * 需要 高级 API（ opsForValue() 、 opsForHash() …）
     * 需要 立即拿到单个结果
     *
     * @param session RedisSession
     * @return java.lang.Object
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static <T> T execute(SessionCallback<T> session) {
        return (T) redisMasterTemplate.execute(session);
    }

    /**
     * 使用redisConnection执行Redis原生命令， 需要自行对键值进行序列化
     * 场景：
     * 需要 底层字节操作（发冷门指令如  OBJECT FREQ 、 BITFIELD ）
     * 需要 直接操作  RedisConnection
     * @param callback RedisCallback
     * @return java.lang.Object
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static <T> T execute(RedisCallback<T> callback) {
        return (T) redisMasterTemplate.execute(callback);
    }

    /**
     * 将所有操作使用一次RTT, 使用包装的Redis操作，不需要自行处理序列化
     *
     * @param session RedisSession
     * @return java.lang.Object
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static List<Object> executePipelined(SessionCallback<?> session) {
        return redisMasterTemplate.executePipelined(session);
    }

    /**
     *  将所有操作使用一次RTT, 使用原生命令，需要自行处理序列化
     *
     * @param callback RedisCallback
     * @return java.lang.Object
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static List<Object> executePipelined(RedisCallback<?> callback) {
        return redisMasterTemplate.executePipelined(callback);
    }
    // endregion----------------------   通用方法 end   ------------------------


    // region----------------------  字符串数据结构操作 start  ------------------------

    /**
     * 如果缓存不存在设置 (永不过期)
     *
     * @param key   键
     * @param value 值
     * @return java.lang.Boolean
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static <T> Boolean setIfAbsent(@NonNull String key, @NonNull T value) {
        return valueOperations.setIfAbsent(key, value);
    }

    /**
     * 如果缓存不存在设置
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间 小于等于0时将设为无限期
     * @param unit    时间单位
     * @return java.lang.Boolean true成功 false 失败
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static <T> Boolean setIfAbsent(
            @NonNull String key, @NonNull T value, long timeout, TimeUnit unit) {
        return valueOperations.setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 如果缓存不存在设置
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     * @return java.lang.Boolean true成功 false 失败
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static <T> Boolean setIfAbsent(
            @NonNull String key, @NonNull T value, Duration expire) {
        return setIfAbsent(key, value, expire.getSeconds());
    }

    /**
     * 如果缓存不存在设置
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（秒） 小于等于0时将设为无限期
     * @return java.lang.Boolean true成功 false 失败
     * @author carlos
     * @date 2021/12/13 10:15
     */
    public static <T> Boolean setIfAbsent(
            @NonNull String key, @NonNull T value, long timeout) {
        return valueOperations.setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置普通缓存(永不过期)
     *
     * @param key   键
     * @param value 值
     */
    public static <T> void setValue(@NonNull String key, @NonNull T value) {
        valueOperations.set(key, value);
    }

    /**
     * 设置普通缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（秒） 小于等于0时将设为无限期
     * @return true成功 false 失败
     */
    public static <T> boolean setValue(@NonNull String key, @NonNull T value, @NonNull long timeout) {
        return setValue(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置普通缓存
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间
     */
    public static <T> boolean setValue(@NonNull String key, @NonNull T value, @NonNull Duration expire) {
        return setValue(key, value, expire.toMillis(), TimeUnit.MILLISECONDS);
    }


    /**
     * 设置普通缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（秒） 小于等于0时将设为无限期
     * @param unit    时间单位
     * @return true成功 false 失败
     */
    public static <T> boolean setValue(
            @NonNull String key, @NonNull T value, long timeout, TimeUnit unit) {
        try {
            valueOperations.set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil setValue error, key:{} value:{}", key, value, e);
            return false;
        }
    }


    /**
     * 批量插入Value  如需设置过期时间使用 {@link #setValueList(Map, Duration)}
     *
     * @param values key为缓存key, value为对应的值
     * @author Carlos
     * @date 2024/3/11 11:39
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
     * @return 值
     */
    public static <T> T getValue(@NonNull String key) {
        try {
            return (T) valueOperations.get(key);
        } catch (Throwable e) {
            log.error("RedisUtil getValue fail, key:{}", key, e);
            return null;
        }
    }

    /**
     * 获取普通缓存
     *
     * @param key 键
     * @return 值
     * @deprecated 使用 {@link #getValue(String)}
     */
    @Deprecated
    public static <T> T getValue(@NonNull String key, Class<T> clazz) {
        return getValue(key);
    }

    /**
     * 模糊查找Value list
     *
     * @param pattern key通配符匹配
     * @return java.util.List<T>
     * @author Carlos
     * @date 2024/3/8 15:25
     */
    public static <T> List<T> getValueList(String pattern) {
        if (StrUtil.isEmpty(pattern)) {
            return null;
        }
        Set<String> keys = keys(pattern);
        if (CollUtil.isEmpty(keys)) {
            return null;
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
     * @param delta 要增加几（大于0）
     * @return 加上指定值之后 key 的值
     */
    public static Long incrementValue(@NonNull String key, long delta) {
        if (delta <= 0) {
            throw new CacheException("递增因子必须大于0");
        }
        return valueOperations.increment(key, delta);
    }

    /**
     * 递增
     *
     * @param key      键
     * @param delta    要增加几（大于0）
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 加上指定值之后 key 的值
     */
    public static Long incrementValue(
            @NonNull String key, long delta, long timeout, TimeUnit timeUnit) {
        if (delta <= 0) {
            throw new CacheException("递增因子必须大于0");
        }
        Long increment = valueOperations.increment(key, delta);
        setExpire(key, timeout, timeUnit);
        return increment;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 减少指定值之后 key 的值
     */
    public static Long decrementValue(@NonNull String key, long delta) {
        if (delta <= 0) {
            throw new CacheException("递减因子必须大于0");
        }
        return valueOperations.decrement(key, delta);
    }

    /**
     * 字符串追加
     *
     * @param key   键
     * @param value 需要追加的值
     */
    public static Integer append(@NonNull String key, @NonNull String value) {
        try {
            return valueOperations.append(key, value);
        } catch (Throwable e) {
            log.error("RedisUtil append error, key: {}", key, e);
        }
        return 0;
    }
    // endregion----------------------   字符串数据结构操作 end   ------------------------

    // region----------------------  Hash操作 start  ------------------------

    /**
     * 获取选项值
     *
     * @param key  键
     * @param item 项
     * @return 值
     */
    @SneakyThrows
    public static <T> T getHash(@NonNull String key, @NonNull String item) {
        return (T) hashOperations.get(key, item);
    }

    /**
     * 获取整个hash
     *
     * @param key   key
     * @param clazz hash对应的对象类型
     * @return T
     * @author carlos
     * @date 2022/2/11 18:25
     */
    @SneakyThrows
    public static <T> T getHash(@NonNull String key, Class<T> clazz) {

        Map<Object, Object> hash = getHash(key);
        if (MapUtil.isEmpty(hash)) {
            return null;
        }
        return BeanUtil.toBean(hash, clazz);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> getHash(@NonNull String key) {
        return hashOperations.entries(key);
    }

    /**
     * 批量获取多个 Hash 的部分字段，并反序列成具体对象
     * @param keys      Hash key 集合
     * @param fields    字段列表
     * @param batchSize 每页数量
     * @param clazz     目标实体类型
     * @param <T>       实体类型
     * @return key → 键值对（无数据返回 null）
     */
    public static <T> Map<String, T> hashMultiGet(List<String> keys, List<String> fields, int batchSize, Class<T> clazz) {
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyMap();
        }


        // 1. 顺序切页
        List<List<String>> pages = Lists.partition(keys, batchSize);
        Map<String, T> result = new ConcurrentHashMap<>(keys.size());

        // 2. 异步提交每批
        List<CompletableFuture<Map<String, T>>> futures = pages.stream()
                .map(page -> CompletableFuture.supplyAsync(
                        () -> hashMultiGet(page, fields, clazz), POOL))
                .collect(Collectors.toList());

        // 3. 等待全部完成 & 归并（保持原顺序）
        futures.forEach(f -> result.putAll(f.join()));
        return result;
    }


    /**
     * 批量获取多个 Hash 的全部字段，并反序列成具体对象
     * @param keys   Hash key 集合
     * @param clazz  目标实体类型
     * @param <T>    实体类型
     * @return key → 实体对象（无数据返回 null）
     */
    public static <T> Map<String, T> hashMultiGet(Collection<String> keys, List<String> fields, Class<T> clazz) {
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        // 1. pipeline 批量 HGETALL
        List<Object> rawResults = redisMasterTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) {
                for (String key : keys) {
                    // 返回 Map<Object,Object>
                    if (CollUtil.isEmpty(fields)) {
                        operations.opsForHash().entries(key);
                    } else {
                        operations.opsForHash().multiGet(key, fields);
                    }
                }
                return null; // 必须
            }
        });

        // 2. 把 Map→实体
        Map<String, T> ans = new LinkedHashMap<>(keys.size());
        int index = 0;
        for (String key : keys) {
            Object row = rawResults.get(index++);
            if (row == null) {
                continue;
            }
            Map<Object, Object> entries = Collections.emptyMap();
            if (row instanceof List) {
                entries = new HashMap<>(fields.size());
                List<Object> list = (List<Object>) row;
                for (int i = 0; i < list.size(); i++) {
                    Object o = list.get(i);
                    if (o == null) {
                        continue;
                    }
                    entries.put(fields.get(i), list.get(i));
                }
            } else if (row instanceof Map) {
                entries = (Map<Object, Object>) row;
            }
            if (CollUtil.isNotEmpty(entries)) {
                T bean = BeanUtil.toBean(entries, clazz);
                ans.put(key, bean);
            }
        }
        return ans;
    }

    /**
     * 批量获取多个 Hash 的全部字段，并反序列成具体对象
     * @param keys   Hash key 集合
     * @param clazz  目标实体类型
     * @param <T>    实体类型
     * @return key → 实体对象（无数据返回 null）
     */
    public static <T> Map<String, T> hashMultiGetAll(Collection<String> keys, Class<T> clazz) {
        return hashMultiGet(keys, null, clazz);
    }

    /**
     * 批量获取多个 Hash 的全部字段，并反序列成具体对象
     * @param keys      Hash key 集合
     * @param batchSize 每页数量
     * @param clazz     目标实体类型
     * @param <T>       实体类型
     * @return key → 键值对（无数据返回 null）
     */
    public static <T> Map<String, T> hashMultiGetAll(List<String> keys, int batchSize, Class<T> clazz) {
        return hashMultiGet(keys, null, batchSize, clazz);
    }


    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return {boolean}
     */
    public static boolean putHash(@NonNull String key, @NonNull Map<String, Object> map) {
        try {
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil putHash error, key: {} value:{}", key, map, e);
            return false;
        }
    }

    /**
     * 将对象保存为hash
     *
     * @param key  key
     * @param hash hash对象
     * @return boolean
     * @author carlos
     * @date 2022/2/11 18:28
     */
    public static boolean putHash(@NonNull String key, @NonNull Object hash) {
        Map<String, Object> map = BeanUtil.beanToMap(hash);
        return putHash(key, map);
    }

    /**
     * 将对象保存为hash
     *
     * @param key  key
     * @param hash hash对象
     * @return boolean
     * @author carlos
     * @date 2022/2/11 18:28
     */
    public static boolean putHash(@NonNull String key, @NonNull Object hash, @NonNull long timeout) {
        Map<String, Object> map = BeanUtil.beanToMap(hash);
        return putHash(key, map, timeout);
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
     * @return {boolean}
     */
    public static <T> boolean putHash(@NonNull String key, @NonNull String item, @NonNull T value) {
        try {
            hashOperations.put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil putHash error, key:{} item:{} value:{}", key, item, value, e);
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
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void deleteHash(@NonNull String key, @NonNull Object... item) {
        hashOperations.delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hasKeyHash(@NonNull String key, @NonNull String item) {
        return hashOperations.hasKey(key, item);
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
     * @param by   要增加几(大于0)
     * @return 加上指定值之后 key 的值
     */
    public static long incrementHash(@NonNull String key, @NonNull String item, @NonNull long by) {
        return hashOperations.increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return 减少指定值之后 key 的值
     */
    public static double decrementHash(@NonNull String key, @NonNull String item, @NonNull double by) {
        return incrementHash(key, item, -by);
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

    // region----------------------  集合数据结构操作 start   存储无序数据------------------------

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static <T> Long addSet(@NonNull String key, @NonNull T... values) {
        Long count = 0L;
        try {
            count = setOperations.add(key, values);
        } catch (Exception e) {
            log.error("RedisUtil addSet error, key:{} value:{}", key, values, e);
        }
        return count;
    }

    /**
     * 将集合中的数据放入set缓存
     *
     * @param key  键
     * @param list 值 可以是多个
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
     * @return Set<Object>
     */
    @SneakyThrows
    public static <T> Set<T> getSet(@NonNull String key) {
        try {
            return (Set<T>) setOperations.members(key);
        } catch (Exception e) {
            log.error("RedisUtil getSet error, key: {}", key, e);
        }
        return null;
    }

    /**
     * 根据 value 从一个 set 中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在, false 不存在
     */
    public static <T> boolean hasKeySet(@NonNull String key, @NonNull T value) {
        try {
            return Boolean.TRUE.equals(setOperations.isMember(key, value));
        } catch (Exception e) {
            log.error("RedisUtil hasKeySet error, key: {} value:{}", key, value, e);
            return false;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 缓存的长度
     */
    public static Long getSetSize(@NonNull String key) {
        try {
            return setOperations.size(key);
        } catch (Exception e) {
            log.error("RedisUtil getSetSize error, key: {}", key, e);
        }
        return 0L;
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static Long removeSet(@NonNull String key, @NonNull Object... values) {
        try {
            return setOperations.remove(key, values);
        } catch (Exception e) {
            log.error("RedisUtil removeSet error, key: {} value:{}", key, values, e);
        }
        return 0L;
    }
    // endregion----------------------   集合数据结构操作 end   ------------------------

    // region----------------------  列表数据结构操作 start  ------------------------

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否放入成功
     */
    public static <T> boolean leftPushList(@NonNull String key, @NonNull T value) {
        try {
            listOperations.leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil leftPushList error, message: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否放入成功
     */
    public static <T> boolean pushList(@NonNull String key, @NonNull T value) {
        try {
            listOperations.rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil pushList error, key:{} value: {}", key, value, e);
        }
        return false;
    }

    /**
     * 将list放入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return 是否放入成功
     */
    public static <T> boolean pushList(
            @NonNull String key, @NonNull T value, @NonNull long timeout) {
        try {
            listOperations.rightPush(key, value);
            if (timeout > 0) {
                setExpire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否放入成功
     */
    public static boolean pushList(@NonNull String key, @NonNull Collection<Object> value) {
        try {
            listOperations.rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil pushList error, message: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 将list放入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return 是否放入成功
     */
    public static boolean pushListAll(
            @NonNull String key, @NonNull List<?> value, @NonNull long timeout) {
        try {
            listOperations.rightPushAll(key, value);
            if (timeout > 0) {
                setExpire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil pushListAll error, message: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return list缓存的内容
     */
    public static <T> List<T> getList(@NonNull String key, @NonNull Long start, @NonNull Long end) {
        return (List<T>) listOperations.range(key, start, end);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return list缓存的内容
     * @deprecated 请使用 {@link #getList(String, Long, Long)}
     */
    @Deprecated
    public static <T> List<T> getList(@NonNull String key, @NonNull Long start, @NonNull Long end, Class<T> clazz) {
        return getList(key, start, end);
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key 键
     * @return list缓存的内容
     */
    public static <T> List<T> getList(@NonNull String key) {
        return (List<T>) listOperations.range(key, 0, -1);
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key 键
     * @return list缓存的内容
     * @deprecated 请使用 {@link #getList(String)}
     */
    @Deprecated
    public static <T> List<T> getList(@NonNull String key, Class<T> clazz) {
        return getList(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return list中的值
     */
    public static <T> T getListIndex(@NonNull String key, @NonNull Long index) {
        try {
            return (T) listOperations.index(key, index);
        } catch (Exception e) {
            log.error("RedisUtil getListIndex error, message: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 对list进行修剪 保留[start, end]区间的值
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return true 操作成功 false 操作失败
     */
    public static boolean listTrim(
            @NonNull String key, @NonNull Long start, @NonNull Long end) {
        try {
            listOperations.trim(key, start, end);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil listTrim error, message: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return list缓存的长度
     */
    public static Long getListSize(@NonNull String key) {
        try {
            return listOperations.size(key);
        } catch (Exception e) {
            log.error("RedisUtil getListSize error, message: {}", e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 根据索引修改 list 中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 是否修改成功
     */
    public static <T> boolean updateListIndex(@NonNull String key, @NonNull Long index, @NonNull T value) {
        try {
            listOperations.set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil updateListIndex error, message: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static <T> Long removeList(@NonNull String key, @NonNull Long count, @NonNull T value) {
        try {
            return listOperations.remove(key, count, value);
        } catch (Exception e) {
            log.error("RedisUtil removeList error, message: {}", e.getMessage(), e);
        }
        return 0L;
    }

    // endregion----------------------   列表数据结构操作 end   ------------------------

    // region----------------------  ZSET start  ------------------------
    // TODO: Carlos 2023/2/7 ZSET相关操作
    // endregion----------------------   ZSET end   ------------------------


    // region----------------------  缓存批量操作 start  ------------------------


    /**
     * 批量加入缓存 可设置过期时长
     *
     * @param values 缓存集合
     * @param expire 过期时长
     * @author Carlos
     * @date 2024/3/8 15:09
     */
    public static <T> void setValueList(@NonNull Map<String, T> values, Duration expire) {
        redisMasterTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations ops) {
                if (expire == null) {                 // ① 无过期
                    values.forEach((k, v) -> ops.opsForValue().set(k, v));
                } else {                              // ② 有过期
                    values.forEach((k, v) ->
                            ops.opsForValue().set(k, v, expire));
                }
                return null; // 必须
            }
        });
    }


    /**
     * 安全分页 + 并行 pipeline，支持 1 w+ key
     * @param keys          去重后 key 集合
     * @param batchSize     每批数量，建议 200~500
     * @return 与 key 顺序一一对应，null 表示 key 不存在
     */
    public static <T> List<T> getValueList(@NonNull Set<String> keys, int batchSize) {
        if (batchSize <= 0) {
            batchSize = 300;
        }
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> keyList = new ArrayList<>(keys);
        Map<Integer, T> idxVal = new ConcurrentHashMap<>(keyList.size());
        List<List<String>> batches = Lists.partition(keyList, batchSize);
        try {
            POOL.submit(() ->
                    batches.parallelStream().forEach(batch -> {
                        List<T> vs = (List<T>) valueOperations.multiGet(batch);
                        int offset = keyList.indexOf(batch.get(0));
                        for (int i = 0; i < batch.size(); i++) {
                            idxVal.put(offset + i, vs.get(i));
                        }
                    })
            ).get(); // 等待全部完成
        } catch (Exception e) {
            throw new CacheGetException("parallel mget error", e);
        }

        return keyList.stream()
                .map(k -> idxVal.get(keyList.indexOf(k)))
                .collect(Collectors.toList());
    }

    /**
     * 批量获取缓存（分批次并行获取）
     *
     * @param keys keys
     * @return java.util.List<T>
     * @author Carlos
     * @date 2025-12-02 10:47
     */
    public static <T> List<T> getValueList(@NonNull Set<String> keys) {
        return getValueList(keys, 300);
    }

    // endregion----------------------   缓存批量操作 end   ------------------------


    // region----------------------  数据库操作 start  ------------------------

    /**
     * 清所有库，线上慎用！
     *
     * @author Carlos
     * @date 2025-12-08 14:06
     */
    public static void flushAll() {
        redisMasterTemplate.execute((RedisCallback<Void>) conn -> {
            conn.flushAll();   //
            return null;
        });
    }

    /**
     * 清理当前DB
     *
     * @author Carlos
     * @date 2025-12-08 14:06
     */
    public static void flushDb() {
        redisMasterTemplate.execute((RedisCallback<Void>) conn -> {
            conn.flushDb();   //
            return null;
        });
    }

    /**
     * 清理当前DB
     *
     * @author Carlos
     * @date 2025-12-08 14:06
     */
    public static void flushDb(int dbIndex) {
        redisMasterTemplate.execute((RedisCallback<Void>) conn -> {
            conn.select(dbIndex);
            conn.flushDb();   //
            return null;
        });
    }

    /**
     * 删除指定前缀空间
     * @param keyPrefix 前缀，如 "region:"
     * @return 实际删除条数
     */
    public static long deleteSpace(String keyPrefix) {
        return deleteSpace(keyPrefix, DEFAULT_BATCH);
    }

    /**
     * 带限流的删除
     * @param keyPrefix 前缀
     * @param batchSize 每批 SCAN 数量
     * @return 删除条数
     */
    public static long deleteSpace(String keyPrefix, int batchSize) {
        if (!keyPrefix.endsWith(":")) {
            keyPrefix = keyPrefix + ":";
        }
        String pattern = keyPrefix + "*";

        final long[] deleted = {0};
        List<String> list = new ArrayList<>();
        scanKeys(pattern, batchSize, new Consumer<String>() {
            @Override
            public void accept(String s) {
                list.add(s);
                if (list.size() >= batchSize) {
                    deleted[0] += delete(list);
                    list.clear();
                }
            }
        });
        if (CollUtil.isNotEmpty(list)) {
            deleted[0] += delete(list);

        }
        log.info("deleteSpace finish, prefix={}, deleted={}", keyPrefix, deleted[0]);
        return deleted[0];
    }
    // endregion----------------------   数据库操作 end   ------------------------


    /* ========== 通用 Lua 工具 ========== */

    /**
     * 加载脚本
     *
     * @param script 参数0
     * @return java.lang.String
     * @author Carlos
     * @date 2025-09-15 15:03
     */
    public static String loadScripts(String script) {
        if (StrUtil.isBlank(script)) {
            return null;
        }
        String scriptSha = (String) redisTemplate.execute((RedisConnection c) ->
                c.scriptLoad(script.getBytes(StandardCharsets.UTF_8)));
        return scriptSha;
    }

    /**
     * 判断脚本是否存在（支持单机 & 集群）
     *
     * @param sha SHA1 摘要
     * @return true=已存在
     */
    public static List<Boolean> scriptExists(String... sha) {
        try (RedisConnection conn = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            if (conn instanceof RedisClusterConnection) {
                // 集群：任选一个主节点判断即可（key 带 {tag} 时）
                RedisClusterConnection cluster = (RedisClusterConnection) conn;
                for (RedisClusterNode node : cluster.clusterGetNodes()) {
                    if (node.isMaster()) {
                        return cluster.scriptExists(sha);
                    }
                }
            } else {
                // 单机
                return conn.scriptExists(sha);
            }
        }
        return Collections.singletonList(false);
    }

    /**
     * 执行Lua
     *
     * @param source   脚本源
     * @param keys     KEYS 数组
     * @param args     ARGV 数组
     * @param retType  返回 Java 类型
     * @param <T>      返回泛型
     */
    public static <T> T lua(ScriptSource source, List<String> keys, List<Object> args, Class<T> retType) {
        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setScriptSource(source); // ← 这里是 SHA
        script.setResultType(retType);
        return lua(script, keys, args.toArray(new Object[0]));
    }

    /**
     * 执行Lua
     *
     * @param script   脚本源
     * @param keys     KEYS 数组
     * @param args     ARGV 数组
     * @param <T>      返回泛型
     */
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
            return conn.evalSha(sha, returnType, numKeys, keysAndArgs);
        } catch (RedisNoScriptException e) {
            // 节点仍没有，立即回退 EVAL
            return conn.eval(sha.getBytes(StandardCharsets.UTF_8), returnType, numKeys, keysAndArgs);
        } finally {
            conn.close();
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
