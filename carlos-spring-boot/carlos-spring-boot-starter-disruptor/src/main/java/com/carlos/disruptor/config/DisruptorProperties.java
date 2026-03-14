package com.carlos.disruptor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Disruptor配置属性
 *
 * @author Carlos
 * @date 2026-01-27
 */
@Data
@ConfigurationProperties(prefix = "carlos.disruptor")
public class DisruptorProperties {

    /**
     * 是否启用Disruptor
     */
    private boolean enabled = false;

    /**
     * RingBuffer大小，必须是2的幂
     */
    private int bufferSize = 1024;

    /**
     * 等待策略: blocking, sleeping, yielding, busy-spin
     */
    private WaitStrategyType waitStrategy = WaitStrategyType.BLOCKING;

    /**
     * 线程池配置
     */
    private ThreadPoolConfig threadPool = new ThreadPoolConfig();

    @Data
    public static class ThreadPoolConfig {
        /**
         * 核心线程数
         */
        private int coreSize = 4;

        /**
         * 最大线程数
         */
        private int maxSize = 8;

        /**
         * 队列容量
         */
        private int queueCapacity = 100;

        /**
         * 线程名称前缀
         */
        private String threadNamePrefix = "disruptor-";
    }

    public enum WaitStrategyType {
        BLOCKING,
        SLEEPING,
        YIELDING,
        BUSY_SPIN;

        /**
         * 转换为 Disruptor WaitStrategy
         */
        public com.lmax.disruptor.WaitStrategy toStrategy() {
            return switch (this) {
                case BLOCKING -> new com.lmax.disruptor.BlockingWaitStrategy();
                case SLEEPING -> new com.lmax.disruptor.SleepingWaitStrategy();
                case YIELDING -> new com.lmax.disruptor.YieldingWaitStrategy();
                case BUSY_SPIN -> new com.lmax.disruptor.BusySpinWaitStrategy();
            };
        }
    }
}
