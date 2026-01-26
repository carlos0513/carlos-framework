package com.yunjin.snowflake;

import cn.hutool.core.lang.Snowflake;
import com.yunjin.snowflake.cache.SnowflakeCacheManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;

/**
 * <p>
 * 分布式雪花算法配置
 * </p>
 *
 * @author yunjin
 * @date 2020/10/25 23:44
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeConfig {

    private final SnowflakeProperties properties;


    @Bean
    @DependsOn("redisUtil")
    public SnowflakeInitiator snowflakeInitiator(@Nullable SnowflakeCacheManager cacheManager) {
        return new SnowflakeInitiator(properties, cacheManager);
    }


    @Bean
    public Snowflake snowflake(SnowflakeInitiator initiator) {
        SnowflakeInfo dto = initiator.init();
        Long dataCenterId = dto.getDataCenterId();
        Long workerId = dto.getWorkerId();
        Snowflake snowflake = new Snowflake(workerId, dataCenterId, properties.isUseSystemClock());

        if (log.isDebugEnabled()) {
            log.debug("Snowflake 分布式ID算法已注册, workerId:{} dataCenterId:{}", workerId, dataCenterId);
        }
        return snowflake;
    }
}
