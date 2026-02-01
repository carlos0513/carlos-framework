package com.carlos.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 * 基于Redisson实现
 *
 * @author carlos
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
     */
    public static boolean lock(String name, long expire, TimeUnit timeUnit) {
        try {
            if (redissonClient == null) {
                log.warn("RedissonClient is null, cannot acquire lock");
                return false;
            }
            RLock lock = redissonClient.getLock(name);
            lock.lock(expire, timeUnit);
            if (log.isDebugEnabled()) {
                log.debug("Acquired lock: {}", name);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to acquire lock: {}", name, e);
            return false;
        }
    }

    /**
     * 尝试加锁
     *
     * @param name      锁名称
     * @param waitTime  等待时间
     * @param leaseTime 锁持有时间
     * @param timeUnit  时间单位
     * @return boolean
     */
    public static boolean tryLock(String name, long waitTime, long leaseTime, TimeUnit timeUnit) {
        try {
            if (redissonClient == null) {
                log.warn("RedissonClient is null, cannot try lock");
                return false;
            }
            RLock lock = redissonClient.getLock(name);
            boolean acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (log.isDebugEnabled()) {
                log.debug("Try lock: , result: {}", name, acquired);
            }
            return acquired;
        } catch (Exception e) {
            log.error("Failed to try lock: {}", name, e);
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param name 锁名称
     * @return boolean
     */
    public static boolean unlock(String name) {
        try {
            if (redissonClient == null) {
                log.warn("RedissonClient is null, cannot release lock");
                return false;
            }

            RLock lock = redissonClient.getLock(name);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                if (log.isDebugEnabled()) {
                    log.debug("Released lock: {}", name);
                }
                return true;
            } else {
                log.warn("Lock {} is not held by current thread", name);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to release lock: {}", name, e);
            return false;
        }
    }

    /**
     * 判断锁是否被持有
     *
     * @param name 锁名称
     * @return boolean
     */
    public static boolean isLocked(String name) {
        try {
            if (redissonClient == null) {
                return false;
            }
            RLock lock = redissonClient.getLock(name);
            return lock.isLocked();
        } catch (Exception e) {
            log.error("Failed to check lock status: {}", name, e);
            return false;
        }
    }
}
