package com.carlos.redis.config;

import com.carlos.redis.caffeine.CaffeineConfig;
import com.carlos.redis.lock.RedisLockAspect;
import com.carlos.redis.lock.RedisLockProperties;
import com.carlos.redis.lock.RedissonLockUtil;
import com.carlos.redis.ratelimit.RateLimitAspect;
import com.carlos.redis.ratelimit.RateLimitProperties;
import com.carlos.redis.ratelimit.RateLimitUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Redisson配置
 * 提供RedissonConnectionFactory和分布式锁支持
 *
 * @author carlos
 * @date 2026-02-01
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties({RedisLockProperties.class, RateLimitProperties.class})
@Import({CaffeineConfig.class})
public class RedissonConfig {

    /**
     * 创建RedissonConnectionFactory
     * 使用Redisson作为Redis连接工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        log.info("Initializing RedissonConnectionFactory");
        return new RedissonConnectionFactory(redissonClient);
    }

    /**
     * 分布式锁切面
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.redis.lock", name = "enabled", havingValue = "true")
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient, RedisLockProperties lockProperties) {
        return new RedisLockAspect(redissonClient, lockProperties);
    }

    /**
     * 限流切面
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.redis.rate-limit", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RateLimitAspect rateLimitAspect(RedissonClient redissonClient, RateLimitProperties properties) {
        return new RateLimitAspect(redissonClient, properties);
    }

    /**
     * 初始化 RedissonLockUtil
     */
    @Bean
    public RedissonLockUtil redissonLockUtilInitializer(RedissonClient redissonClient) {
        RedissonLockUtil.init(redissonClient);
        return new RedissonLockUtil();
    }

    /**
     * 初始化 RateLimitUtil
     */
    @Bean
    public RateLimitUtil rateLimitUtilInitializer(RedissonClient redissonClient, RateLimitProperties properties) {
        RateLimitUtil.init(redissonClient, properties);
        return new RateLimitUtil();
    }
}
