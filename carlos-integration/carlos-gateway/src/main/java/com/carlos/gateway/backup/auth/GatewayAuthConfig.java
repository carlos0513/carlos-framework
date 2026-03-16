package com.carlos.gateway.auth;

import com.carlos.gateway.config.GlobalFilterOrder;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


/**
 * <p>
 * 网关配置
 * </p>
 *
 * @author carlos
 * @date 2021/11/4 9:43
 */
@Configuration
@EnableConfigurationProperties(com.carlos.gateway.auth.GatewayAuthProperties.class)
@AllArgsConstructor
public class GatewayAuthConfig {

    private final com.carlos.gateway.auth.GatewayAuthProperties authProperties;

    /**
     * 注册全局认证过滤器
     */
    @Bean
    @Order(GlobalFilterOrder.ORDER_FIRST)
    public GlobalFilter authGlobalFilter() {
        return new com.carlos.gateway.auth.AuthGlobalFilter(authProperties);
    }

    /**
     * token去除过滤器
     */
    @Bean
    public com.carlos.gateway.auth.RemoveJwtFilter removeJwtFilter() {
        return new com.carlos.gateway.auth.RemoveJwtFilter(authProperties);
    }
}
