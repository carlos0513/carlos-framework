package com.carlos.sample.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Carlos Spring Cloud 示例应用启动类
 *
 * <p>演示 Spring Cloud Alibaba 微服务治理能力，包括：</p>
 * <ul>
 *     <li>Nacos 服务注册与发现</li>
 *     <li>OpenFeign 声明式 HTTP 客户端</li>
 *     <li>Spring Cloud LoadBalancer 负载均衡</li>
 *     <li>Sentinel 流量控制（可选）</li>
 * </ul>
 *
 * @author carlos
 * @since 3.0.0
 */
@EnableFeignClients(basePackages = "com.carlos.sample.cloud.client")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.carlos.sample.cloud")
public class CloudSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSampleApplication.class, args);
    }

}
