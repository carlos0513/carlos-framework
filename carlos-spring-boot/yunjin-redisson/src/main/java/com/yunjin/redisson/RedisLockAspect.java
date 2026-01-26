package com.yunjin.redisson;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.util.SpelUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>
 * 分布式锁AOP切面
 * </p>
 *
 * @author yunjin
 * @date 2020/10/27 23:46
 */
@Aspect
@Component
@ConditionalOnClass(RedissonAutoConfiguration.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@EnableConfigurationProperties(RedissonProperties.class)
@AllArgsConstructor
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    private final RedissonProperties redissonProperties;


    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String spel = redisLock.key();
        String lockName = redisLock.name();
        // 获取锁对象
        RLock rLock = redissonClient.getLock(getRedisKey(joinPoint, lockName, spel));
        // 上锁
        rLock.lock(redisLock.expire(), redisLock.timeUnit());

        Object result;
        try {
            // 执行方法
            result = joinPoint.proceed();
        } finally {
            // 方法执行完成后释放锁
            rLock.unlock();
        }
        return result;
    }

    /**
     * 将spel表达式转换为字符串
     *
     * @param joinPoint 切点
     * @return redisKey
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint, String lockName, String spel) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        // 获取方法的参数
        Object[] arguments = joinPoint.getArgs();
        return redissonProperties.getPrefix() + lockName + StrUtil.COLON + SpelUtil.parse(target, spel, targetMethod,
                arguments);
    }
}
