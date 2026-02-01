package com.carlos.redis.lock;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.util.SpelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁AOP切面
 *
 * @author carlos
 * @date 2020/10/27 23:46
 */
@Slf4j
@Aspect
@Component
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnProperty(prefix = "carlos.redis.lock", name = "enabled", havingValue = "true", matchIfMissing = true)
@AllArgsConstructor
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final RedisLockProperties lockProperties;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String spel = redisLock.key();
        String lockName = redisLock.name();

        // 获取完整的锁key
        String lockKey = getRedisKey(joinPoint, lockName, spel);

        // 获取锁对象
        RLock rLock = redissonClient.getLock(lockKey);

        // 上锁
        rLock.lock(redisLock.expire(), redisLock.timeUnit());

        if (log.isDebugEnabled()) {
            log.debug("Acquired distributed lock: {}", lockKey);
        }

        Object result;
        try {
            // 执行方法
            result = joinPoint.proceed();
        } finally {
            // 方法执行完成后释放锁
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                if (log.isDebugEnabled()) {
                    log.debug("Released distributed lock: {}", lockKey);
                }
            }
        }
        return result;
    }

    /**
     * 将SpEL表达式转换为字符串
     *
     * @param joinPoint 切点
     * @param lockName  锁名称
     * @param spel      SpEL表达式
     * @return redisKey
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint, String lockName, String spel) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();

        String prefix = lockProperties.getPrefix();
        String parsedKey = SpelUtil.parse(target, spel, targetMethod, arguments);

        return prefix + lockName + StrUtil.COLON + parsedKey;
    }
}
