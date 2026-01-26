package com.yunjin.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * redis分布式锁工具
 * </p>
 *
 * @author yunjin
 * @date 2022/4/21 17:44
 */
@Slf4j
@Component
public class RedissonLockUtil {

    private static RedissonClient redissonClient;


    public RedissonLockUtil(RedissonClient redissonClient) {
        RedissonLockUtil.redissonClient = redissonClient;
    }

    /**
     * 加锁
     *
     * @param name     锁名称
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return boolean
     * @author yunjin
     * @date 2022/4/21 17:50
     */
    public static boolean addLock(String name, long expire, TimeUnit timeUnit) {

        try {
            if (redissonClient == null) {
                return false;
            }
            RLock lock = redissonClient.getLock(name);
            lock.lock(expire, timeUnit);
            if (log.isDebugEnabled()) {
                log.debug("Get lock for name:{}", name);
            }
            return true;
        } catch (Exception e) {
            log.error("Get lock fail for name:{}", name, e);
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param name 锁名称
     * @return boolean
     * @author yunjin
     * @date 2022/4/21 17:52
     */
    public static boolean releaseLock(String name) {
        try {
            if (redissonClient == null) {
                return false;
            }

            RLock lock = redissonClient.getLock(name);
            lock.unlock();
            if (log.isDebugEnabled()) {
                log.debug("Unlock for name:{}", name);
            }
            return true;
        } catch (Exception e) {
            log.error("Unlock fail for name:{}", name, e);
            return false;
        }
    }
}
