package com.carlos.apm.config;

import com.carlos.apm.feign.TracingFeignInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * APM Feign 配置类
 * <p>
 * 自动注册链路追踪 Feign 拦截器
 *
 * @author Carlos
 * @date 2025-03-26
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "feign.RequestInterceptor")
@ConditionalOnProperty(prefix = "carlos.apm.feign", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ApmFeignConfig {

    @Bean
    public TracingFeignInterceptor tracingFeignInterceptor() {
        log.debug("[Carlos APM] Feign 链路追踪拦截器已注册");
        return new TracingFeignInterceptor();
    }
}
