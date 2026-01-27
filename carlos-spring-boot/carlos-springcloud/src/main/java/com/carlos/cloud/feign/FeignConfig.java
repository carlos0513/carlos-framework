package com.carlos.cloud.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * openfeign相关配置
 * </p>
 *
 * @author yunjin
 * @date 2021/12/7 15:05
 */
@Slf4j
@EnableFeignClients(basePackages = "com.carlos")
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FeignProperties.class)
public class FeignConfig {

    private final FeignProperties feignProperties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }

    /**
     * 配置feign日志级别
     */
    @Bean
    @ConditionalOnProperty(prefix = "yunjin.feign.log", name = "enable", matchIfMissing = true)
    public Logger.Level feignLoggerLevel() {
        return feignProperties.getLog().getLevel();
    }

}
