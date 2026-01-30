package com.carlos.snowflake.cache;

import com.carlos.snowflake.SnowflakeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <p>
 *   当工程中引入了redis时，使用缓存管理
 * </p>
 *
 * @author Carlos
 * @date 2025-03-17 14:09
 */
@Configuration(proxyBeanMethods = false)
public class SnowflakeRedisCacheAutoConfiguration {
    /**
     * <p>
     *   缓存管理器
     * </p>
     *
     * @author Carlos
     * @date 2025-03-17 14:0
     */
    @Bean
    @ConditionalOnMissingBean
    public SnowflakeCacheManager snowflakeCacheManager(SnowflakeProperties properties) {
        return new SnowflakeRedisCacheManager(properties);
    }

}


