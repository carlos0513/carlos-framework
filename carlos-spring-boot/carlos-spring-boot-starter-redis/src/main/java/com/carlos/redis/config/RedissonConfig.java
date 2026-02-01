package com.carlos.redis.config;

import com.carlos.redis.caffeine.CaffeineConfig;
import com.carlos.redis.lock.RedisLockAspect;
import com.carlos.redis.lock.RedisLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@EnableConfigurationProperties(RedisLockProperties.class)
@Import({RedisLockAspect.class, CaffeineConfig.class})
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
}
