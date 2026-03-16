package com.carlos.sample.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Carlos Gateway 示例应用启动类
 *
 * <p>
 * 本示例演示了 carlos-spring-boot-starter-gateway 的使用，包括：
 * - 路由配置
 * - 自定义过滤器
 * - 负载均衡
 * - OAuth2 资源服务器（JWT Token 验证）
 * </p>
 *
 * @author carlos
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewaySampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewaySampleApplication.class, args);
    }

}
