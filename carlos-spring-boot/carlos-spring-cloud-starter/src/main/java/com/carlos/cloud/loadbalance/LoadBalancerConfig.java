package com.carlos.cloud.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 负载均衡配置
 * </p>
 *
 * <p>
 * 支持多种负载均衡策略：
 * <ul>
 *   <li>轮询（RoundRobin）- 默认</li>
 *   <li>随机（Random）</li>
 * </ul>
 * </p>
 *
 * <p>
 * 配置方式：
 * <pre>
 * carlos:
 *   cloud:
 *     loadbalancer:
 *       strategy: random  # 或 roundrobin
 * </pre>
 * </p>
 *
 * @author carlos
 * @date 2021/10/12 17:33
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "carlos.cloud.loadbalancer", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoadBalancerConfig {

    /**
     * 轮询负载均衡策略
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "carlos.cloud.loadbalancer", name = "strategy", havingValue = "roundrobin", matchIfMissing = true)
    public ReactorLoadBalancer<ServiceInstance> roundRobinLoadBalancer(
        Environment environment,
        LoadBalancerClientFactory loadBalancerClientFactory) {
        log.info("使用轮询负载均衡策略");
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RoundRobinLoadBalancer(
            loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
            name
        );
    }

    /**
     * 随机负载均衡策略
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "carlos.cloud.loadbalancer", name = "strategy", havingValue = "random")
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
        Environment environment,
        LoadBalancerClientFactory loadBalancerClientFactory) {
        log.info("使用随机负载均衡策略");
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(
            loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
            name
        );
    }
}
