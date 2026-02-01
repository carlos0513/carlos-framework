package com.carlos.redis.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁注解
 * 使用Redisson实现分布式锁
 *
 * @author carlos
 * @date 2020/10/27 23:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * redis锁名字
     */
    String name() default "";

    /**
     * redis锁key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 锁过期时间，-1代表一直持有锁直到释放
     */
    int expire() default 5000;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
