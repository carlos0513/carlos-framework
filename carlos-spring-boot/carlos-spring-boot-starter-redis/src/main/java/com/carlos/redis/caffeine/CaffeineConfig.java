package com.carlos.redis.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Caffeine本地缓存配置
 *
 * @author carlos
 * @date 2026-02-01
 */
@Slf4j
@Configuration
@ConditionalOnClass(Caffeine.class)
@ConditionalOnProperty(prefix = "carlos.redis.caffeine", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CaffeineProperties.class)
public class CaffeineConfig {

    /**
     * 创建Caffeine缓存实例
     */
    @Bean
    public Cache<String, Object> caffeineCache(CaffeineProperties properties) {
        log.info("Initializing Caffeine local cache with properties: {}", properties);

        Caffeine<Object, Object> builder = Caffeine.newBuilder();

        // 初始容量
        if (properties.getInitialCapacity() != null) {
            builder.initialCapacity(properties.getInitialCapacity());
        }

        // 最大容量
        if (properties.getMaximumSize() != null) {
            builder.maximumSize(properties.getMaximumSize());
        }

        // 写入后过期
        if (properties.getExpireAfterWriteDuration() != null) {
            builder.expireAfterWrite(properties.getExpireAfterWriteDuration());
        }

        // 访问后过期
        if (properties.getExpireAfterAccessDuration() != null) {
            builder.expireAfterAccess(properties.getExpireAfterAccessDuration());
        }

        // 刷新时间
        if (properties.getRefreshAfterWriteDuration() != null) {
            builder.refreshAfterWrite(properties.getRefreshAfterWriteDuration());
        }

        // 记录统计信息
        if (Boolean.TRUE.equals(properties.getRecordStats())) {
            builder.recordStats();
        }

        Cache<String, Object> cache = builder.build();
        log.info("Caffeine local cache initialized successfully");

        // 初始化 CaffeineUtil
        CaffeineUtil.init(cache);

        return cache;
    }
}
