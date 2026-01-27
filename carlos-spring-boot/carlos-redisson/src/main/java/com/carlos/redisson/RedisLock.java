package com.carlos.redisson;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 使用redis进行分布式锁
 * </p>
 *
 * @author yunjin
 * @date 2020/10/27 23:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * redis锁 名字
     */
    String name() default "";

    /**
     * redis锁 key 支持spel表达式
     */
    String key() default "";

    /**
     * 锁过期时间 -1代表一直持有锁，知道释放锁
     */
    int expire() default 5000;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
