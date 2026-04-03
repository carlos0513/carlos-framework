package com.carlos.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 网关基础设施自动配置类
 * <p>
 * 负责基础设施相关的 Bean 配置：
 * - WebClient（HTTP 客户端）
 * - ReactiveStringRedisTemplate（Redis 操作）
 * - 全局异常处理器
 * - 错误属性扩展
 *
 * @author carlos
 * @date 2026/3/27
 */
@Slf4j
@Configuration
public class InfrastructureAutoConfiguration {

    /**
     * WebClient 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    /**
     * WebClient 构建器
     */
    @Bean
    @ConditionalOnMissingBean(name = "webClientBuilder")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Reactive Redis 模板
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(
        ReactiveRedisConnectionFactory connectionFactory) {
        return new ReactiveStringRedisTemplate(connectionFactory);
    }

    /**
     * 网关启动器
     */
    @Bean
    public GatewayRunnerWorker gatewayRunnerWorker() {
        return new GatewayRunnerWorker();
    }

    /**
     * 网关全局异常处理器
     * 覆盖 Spring Boot 默认的异常处理，返回统一的 JSON 格式错误响应
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler gatewayErrorWebExceptionHandler(ExceptionProperties exceptionProperties) {
        log.info("Initializing Gateway Exception Handler (devMode: {}, showStackTrace: {})",
            exceptionProperties.isDevMode(), exceptionProperties.isShowStackTrace());
        return new GatewayExceptionHandler(exceptionProperties.isDevMode(), exceptionProperties.isShowStackTrace());
    }

    /**
     * 错误属性扩展
     * 扩展 Spring Boot 默认的错误属性，添加网关特定的字段
     */
    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class)
    public DefaultErrorAttributes errorAttributes(ExceptionProperties exceptionProperties) {
        log.info("Initializing Gateway Error Attributes (devMode: {})", exceptionProperties.isDevMode());
        return new GatewayErrorAttributes(exceptionProperties.isDevMode());
    }
}
