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

    /**
     * 选择性路径前缀剥离过滤器工厂
     * 用于处理 Swagger/Knife4j 文档路径的前缀剥离
     */
    @Bean
    @ConditionalOnMissingBean
    public SelectStripPrefixGatewayFilterFactory selectStripPrefixGatewayFilterFactory() {
        log.info("Initializing Select Strip Prefix Gateway Filter Factory");
        return new SelectStripPrefixGatewayFilterFactory();
    }
}
