package com.carlos.log.config;

import com.carlos.log.disruptor.LogEvent;
import com.carlos.log.disruptor.LogEventHandler;
import com.carlos.log.disruptor.LogEventProducer;
import com.carlos.log.properties.LogProperties;
import com.carlos.log.storage.LogStorage;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * 异步日志自动配置（Disruptor）
 * <p>
 * 仅在 carlos.log.async=true 时启用
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "carlos.log", name = "async", havingValue = "true", matchIfMissing = true)
public class LogAsyncConfig {

    /**
     * Disruptor RingBuffer
     */
    @Bean(name = "logEventRingBuffer")
    @ConditionalOnMissingBean(name = "logEventRingBuffer")
    public RingBuffer<LogEvent> logEventRingBuffer(LogProperties properties, LogStorage logStorage) {
        LogProperties.Disruptor disruptorProps = properties.getDisruptor();

        // 创建事件工厂
        LogEvent.LogEventFactory eventFactory = new LogEvent.LogEventFactory();

        // 创建等待策略
        WaitStrategy waitStrategy = createWaitStrategy(disruptorProps.getWaitStrategy());

        // 创建 Disruptor
        Disruptor<LogEvent> disruptor = new Disruptor<>(
            eventFactory,
            disruptorProps.getRingBufferSize(),
            Executors.defaultThreadFactory(),
            ProducerType.MULTI,
            waitStrategy
        );

        // 创建消费者处理器
        LogEventHandler handler = new LogEventHandler(logStorage);

        // 设置消费者
        disruptor.handleEventsWith(handler);

        // 启动 Disruptor
        disruptor.start();

        log.info("Disruptor 启动成功，RingBuffer大小: {}", disruptorProps.getRingBufferSize());
        return disruptor.getRingBuffer();
    }

    /**
     * 日志事件生产者
     */
    @Bean
    @ConditionalOnMissingBean(LogEventProducer.class)
    public LogEventProducer logEventProducer(RingBuffer<LogEvent> logEventRingBuffer) {
        // 注意：由于类型擦除，需要确保 logEventRingBuffer bean 已创建
        // 使用 @ConditionalOnMissingBean(name = "logEventRingBuffer") 避免与其他 RingBuffer 冲突
        return new LogEventProducer(logEventRingBuffer);
    }

    /**
     * 创建等待策略
     */
    private WaitStrategy createWaitStrategy(LogProperties.Disruptor.WaitStrategyType type) {
        return switch (type) {
            case BUSY_SPIN -> new BusySpinWaitStrategy();
            case SLEEPING -> new SleepingWaitStrategy();
            case YIELDING -> new YieldingWaitStrategy();
            default -> new BlockingWaitStrategy();
        };
    }
}
