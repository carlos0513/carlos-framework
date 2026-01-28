package com.carlos.redisson;

import cn.hutool.core.date.SystemClock;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 雪花算法属性配置
 * </p>
 *
 * @author yunjin
 * @date 2020/10/25 23:45
 */
@Data
@ConfigurationProperties(prefix = "carlos.redisson")
public class RedissonProperties {

    private static final String DEFAULT_LOCK_PREFIX = "redisson_lock:";

    /**
     * 是否开启分布式锁, 默认关闭
     */
    Boolean enabled = false;

    /**
     * 分布式锁缓存前缀
     */
    String prefix = DEFAULT_LOCK_PREFIX;

    /**
     * 配置文件位置
     */
    String config;

    /**
     * 是否使用{@link SystemClock} 获取当前时间戳
     */
    Boolean isUseSystemClock = false;

}
