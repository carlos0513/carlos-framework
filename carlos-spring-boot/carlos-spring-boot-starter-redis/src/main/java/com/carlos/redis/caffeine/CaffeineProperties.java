package com.carlos.redis.caffeine;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Caffeine本地缓存配置属性
 *
 * @author carlos
 * @date 2026-02-01
 */
@Data
@ConfigurationProperties(prefix = "carlos.redis.caffeine")
public class CaffeineProperties {

    /**
     * 是否启用Caffeine本地缓存，默认启用
     */
    private Boolean enabled = true;

    /**
     * 初始容量
     */
    private Integer initialCapacity = 100;

    /**
     * 最大容量
     */
    private Integer maximumSize = 10000;

    /**
     * 写入后过期时间（秒），默认5分钟
     */
    private Long expireAfterWrite = 300L;

    /**
     * 访问后过期时间（秒），默认不启用
     */
    private Long expireAfterAccess;

    /**
     * 刷新时间（秒），默认不启用
     */
    private Long refreshAfterWrite;

    /**
     * 是否记录统计信息
     */
    private Boolean recordStats = false;

    /**
     * 获取写入后过期时间
     */
    public Duration getExpireAfterWriteDuration() {
        return expireAfterWrite != null ? Duration.ofSeconds(expireAfterWrite) : null;
    }

    /**
     * 获取访问后过期时间
     */
    public Duration getExpireAfterAccessDuration() {
        return expireAfterAccess != null ? Duration.ofSeconds(expireAfterAccess) : null;
    }

    /**
     * 获取刷新时间
     */
    public Duration getRefreshAfterWriteDuration() {
        return refreshAfterWrite != null ? Duration.ofSeconds(refreshAfterWrite) : null;
    }
}
