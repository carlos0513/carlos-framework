package com.carlos.snowflake.cache;

import cn.hutool.core.util.StrUtil;
import com.carlos.redis.util.RedisUtil;
import com.carlos.snowflake.SnowflakeInfo;
import com.carlos.snowflake.SnowflakeProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *缓存管理
 * 缓存设置定时过期，防止应用异常宕机，从而缓存一直存在的问题，所以需要对缓存不断续期
 * </p>
 *
 * @author Carlos
 * @date 2025-03-17 13:54 
 */
@Slf4j
public class SnowflakeRedisCacheManager implements SnowflakeCacheManager {

    private final SnowflakeProperties properties;

    private static String snowflakeRedisKey;

    public SnowflakeRedisCacheManager(SnowflakeProperties properties) {
        this.properties = properties;
        log.info("SnowflakeRedisCacheManager 初始化完成");
    }

    @Override
    public boolean putCache(SnowflakeInfo bean) {
        bean.setCreateTime(LocalDateTime.now());
        Duration redisExpire = properties.getRedisExpire();
        bean.setExpireTime(bean.getCreateTime().plusSeconds(redisExpire.getSeconds()));
        long refresh = redisExpire.getSeconds() - 30 * 60;
        snowflakeRedisKey = properties.getNamespace() + StrUtil.COLON + bean.getWorkerId() + StrUtil.UNDERLINE + bean.getDataCenterId();
        Boolean save = RedisUtil.setIfAbsent(snowflakeRedisKey, bean, properties.getRedisExpire().getSeconds());
        if (save != null || save) {
            // 执行定期更新
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("snowflake-redis-pool-%d").daemon(true).build());
            executorService.scheduleAtFixedRate(this::resetExpire, refresh, refresh, TimeUnit.SECONDS);
            return true;
        }
        // 执行定期更新
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("snowflake-redis-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(this::resetExpire, refresh, refresh, TimeUnit.SECONDS);
        return false;
    }

    @Override
    public void delCache() {
        RedisUtil.delete(snowflakeRedisKey);
        if (log.isDebugEnabled()) {
            log.debug("Delete snowflake key:{} ", snowflakeRedisKey);
        }
    }

    /**
     * 从新设置过时时间，由定时任务调用
     *
     * @author yunjin
     * @date 2021/12/13 11:16
     */
    @Override
    public void resetExpire() {
        RedisUtil.setExpire(snowflakeRedisKey, properties.getRedisExpire().getSeconds(), TimeUnit.SECONDS);
        log.info("Reset expire of snowflake key:[{}]", snowflakeRedisKey);
    }
}
