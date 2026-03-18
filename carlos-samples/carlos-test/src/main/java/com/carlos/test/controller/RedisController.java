package com.carlos.test.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.carlos.core.util.ExecutorUtil;
import com.carlos.redis.util.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("redis")
@Tag(name = "Redis测试")
@Slf4j
public class RedisController {

    @GetMapping("hashLuaSet")
    @Operation(summary = "hash写入")
    public Object hashLuaSet() {

        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/form_indicator.lua"));
        script.setResultType(List.class);
        Object map = RedisUtil.lua(script, Collections.singletonList("form:stat:123"), "1", String.valueOf(System.currentTimeMillis()), "2");
        return map;
    }

    @GetMapping("hashGet")
    @Operation(summary = "hash读取")
    public Object hashGet() {
        return RedisUtil.getHash("form:stat:123", Object.class);
    }

    @GetMapping("scan")
    @Operation(summary = "scan")
    public Object scan(String pattern) {
        return RedisUtil.scanKeys(pattern, 300);
    }

    @GetMapping("del")
    @Operation(summary = "del")
    public Object del(String pattern) {
        Set<String> strings = RedisUtil.scanKeys(pattern, 300);
        return RedisUtil.delete(strings);
    }

    // @Scheduled(cron = "0 0/1 * * * ?")
    public void lock() {
        log.info("-----------------------------------------------");
        ThreadPoolExecutor pool = ExecutorUtil.defaultPool();
        pool.execute(() -> {
            test1();
        });
        pool.execute(() -> {
            test2();
        });
    }

    public void test1() {
        log.info("test1开始执�?{}", Thread.currentThread().getName());
        boolean locked = false;
        String lock1 = "lock1";
        // String lock2 = "lock2";
        // Boolean b = RedisUtil.setIfAbsent(lock2, 1, 10, TimeUnit.SECONDS);
        // if (BooleanUtil.isTrue(b)) {
        //     log.info("test1111,lock2不存在，写入成功，执行业务test1");
        // } else {
        //     log.info("test1111,lock2已存在，写入失败，不执行test1");
        // }
        RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(lock1);
        try {
            log.info("test1111,尝试获取锁：{}", lock1);
            locked = lock.tryLock(0, 300, TimeUnit.SECONDS);
            log.info("test1111,获取锁是否成功：{}", locked);
            if (locked) {
                log.info("test1111,获取锁成�?成功执行业务test1");
                ThreadUtil.sleep(3000);
            } else {
                log.info("test1111,未获取到分布式锁，未执行test1");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("test1111,获取分布式锁时被中断", e);
        } finally {
            // 如果当前线程持有锁，则释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void test2() {
        log.info("test2开始执�?{}", Thread.currentThread().getName());
        boolean locked = false;
        String lock1 = "lock1";
        // String lock2 = "lock2";
        // Boolean b = RedisUtil.setIfAbsent(lock2, 1, 10, TimeUnit.SECONDS);
        // if (BooleanUtil.isTrue(b)) {
        //     log.info("test2222,lock2不存在，写入成功，执行业务test2");
        // } else {
        //     log.info("test2222,lock2已存在，写入失败，不执行test2");
        // }
        RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(lock1);
        try {
            log.info("test2222,尝试获取锁：{}", lock1);
            locked = lock.tryLock(0, 300, TimeUnit.SECONDS);
            log.info("test2222,获取锁是否成功：{}", locked);
            if (locked) {
                log.info("test2222,获取锁成�?成功执行业务test2");
                ThreadUtil.sleep(3000);
            } else {
                log.info("test2222,未获取到分布式锁，未执行test2");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("test2222,获取分布式锁时被中断", e);
        } finally {
            // 如果当前线程持有锁，则释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
