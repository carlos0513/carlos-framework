package com.carlos.boot.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 错误处理配置
 * </p>
 *
 * @author carlos
 * @date 2021/3/3 23:56
 */
@Slf4j
@Configuration
public class ErrorConfig {

    /**
     * 全局错误控制器
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.boot.error.controller", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GlobalErrorController globalErrorController() {
        return new GlobalErrorController();
    }

    /**
     * 自定义错误属性
     */
    @Bean
    public CustomizeErrorAttributes customizeErrorAttributes() {
        return new CustomizeErrorAttributes();
    }
}
