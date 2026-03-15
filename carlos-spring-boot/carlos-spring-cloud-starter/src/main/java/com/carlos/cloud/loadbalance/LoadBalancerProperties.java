package com.carlos.cloud.loadbalance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 负载均衡配置属性
 * </p>
 *
 * @author carlos
 * @date 2024/01/15
 */
@Data
@ConfigurationProperties(prefix = "carlos.cloud.loadbalancer")
public class LoadBalancerProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 负载均衡策略：roundrobin（轮询）、random（随机）
     */
    private String strategy = "roundrobin";

    /**
     * 缓存配置
     */
    private CacheProperties cache = new CacheProperties();

    /**
     * 健康检查配置
     */
    private HealthCheckProperties healthCheck = new HealthCheckProperties();

    @Data
    public static class CacheProperties {
        /**
         * 是否启用缓存
         */
        private boolean enabled = true;

        /**
         * 缓存过期时间（秒）
         */
        private long ttl = 30;

        /**
         * 缓存最大大小
         */
        private int maxSize = 1000;
    }

    @Data
    public static class HealthCheckProperties {
        /**
         * 是否启用健康检查
         */
        private boolean enabled = true;

        /**
         * 检查间隔（毫秒）
         */
        private long interval = 30000;

        /**
         * 初始延迟（毫秒）
         */
        private long initialDelay = 0;
    }
}
