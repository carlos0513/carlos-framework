package com.carlos.cloud.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OpenFeign 自动配置
 * </p>
 *
 * <p>
 * 提供 Feign 客户端的完整配置支持，包括：
 * <ul>
 *   <li>请求拦截器（传递请求头、上下文）</li>
 *   <li>错误解码器（异常处理）</li>
 *   <li>日志配置</li>
 *   <li>重试策略</li>
 *   <li>OkHttp 连接池</li>
 * </ul>
 * </p>
 *
 * @author carlos
 * @date 2021/12/7 15:05
 */
@Slf4j
@EnableFeignClients(basePackages = "com.carlos")
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FeignProperties.class)
public class FeignConfig {

    private final FeignProperties feignProperties;

    /**
     * Feign 请求拦截器 - 传递请求头信息
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor(feignProperties);
    }

    /**
     * Feign 错误解码器
     */
    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }

    /**
     * 配置 Feign 日志级别
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.feign.log", name = "enable", havingValue = "true", matchIfMissing = true)
    public Logger.Level feignLoggerLevel() {
        return feignProperties.getLog().getLevel();
    }

    /**
     * 配置 Feign 重试策略
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "carlos.feign.retry", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Retryer feignRetryer() {
        FeignProperties.RetryProperties retry = feignProperties.getRetry();
        return new Retryer.Default(
            retry.getPeriod(),
            retry.getMaxPeriod(),
            retry.getMaxAttempts()
        );
    }

    /**
     * 配置 OkHttp 连接池
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnProperty(prefix = "carlos.feign.pool", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ConnectionPool connectionPool() {
        FeignProperties.PoolProperties pool = feignProperties.getPool();
        return new ConnectionPool(
            pool.getMaxIdle(),
            pool.getKeepAliveDuration(),
            TimeUnit.MINUTES
        );
    }

    /**
     * 配置 OkHttp 客户端
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnProperty(prefix = "carlos.feign.pool", name = "enabled", havingValue = "true", matchIfMissing = true)
    public OkHttpClient okHttpClient(ConnectionPool connectionPool) {
        FeignProperties.PoolProperties pool = feignProperties.getPool();
        return new OkHttpClient.Builder()
            .connectTimeout(pool.getConnectTimeout(), TimeUnit.MILLISECONDS)
            .readTimeout(pool.getReadTimeout(), TimeUnit.MILLISECONDS)
            .connectionPool(connectionPool)
            .build();
    }
}
