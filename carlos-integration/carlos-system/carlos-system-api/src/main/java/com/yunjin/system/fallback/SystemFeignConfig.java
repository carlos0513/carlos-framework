package com.carlos.system.fallback;


import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 组织架构feign配置类
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:24
 */
@Configuration
@ConditionalOnClass(FallbackFactory.class)
public class SystemFeignConfig {

    @Bean
    public com.carlos.system.fallback.FeignResourceFallbackFactory resourceFallbackFactory() {
        return new com.carlos.system.fallback.FeignResourceFallbackFactory();
    }

    @Bean
    public com.carlos.system.fallback.FeignDictFallbackFactory dictFallbackFactory() {
        return new com.carlos.system.fallback.FeignDictFallbackFactory();
    }

    @Bean
    public com.carlos.system.fallback.FeignMenuFallbackFactory menuFallbackFactory() {
        return new com.carlos.system.fallback.FeignMenuFallbackFactory();
    }

    @Bean
    public com.carlos.system.fallback.FeignSystemConfigFallbackFactory systemConfigFallbackFactory() {
        return new com.carlos.system.fallback.FeignSystemConfigFallbackFactory();
    }

    @Bean
    public com.carlos.system.fallback.FeignRegionFallbackFactory regionFallbackFactory() {
        return new com.carlos.system.fallback.FeignRegionFallbackFactory();
    }

    @Bean
    public com.carlos.system.fallback.FeignFileFallbackFactory fileFallbackFactory() {
        return new com.carlos.system.fallback.FeignFileFallbackFactory();
    }

    @Bean
    public com.carlos.system.fallback.ApiMenuOperateFallbackFactory apiMenuOperateFallbackFactory() {
        return new com.carlos.system.fallback.ApiMenuOperateFallbackFactory();
    }
}

