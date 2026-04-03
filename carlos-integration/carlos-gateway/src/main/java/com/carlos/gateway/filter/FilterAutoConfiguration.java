package com.carlos.gateway.filter;

import com.carlos.gateway.config.GatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关过滤器自动配置类
 * <p>
 * 负责过滤器相关的 Bean 配置：
 * - 路径前缀过滤器
 *
 * @author carlos
 * @date 2026/3/27
 */
@Slf4j
@Configuration
public class FilterAutoConfiguration {

    /**
     * 路径前缀过滤器
     * 统一处理网关接口路径前缀
     */
    @Bean
    @ConditionalOnProperty(name = "carlos.gateway.prefix-enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public PathPrefixFilter pathPrefixFilter(GatewayProperties gatewayProperties) {
        log.info("Initializing Path Prefix Filter with prefix: {}", gatewayProperties.getPrefix());
        return new PathPrefixFilter(gatewayProperties);
    }
}
