package com.carlos.log.config;

import com.carlos.log.aspect.LogAspect;
import com.carlos.log.disruptor.LogEvent;
import com.carlos.log.disruptor.LogEventHandler;
import com.carlos.log.disruptor.LogEventProducer;
import com.carlos.log.properties.LogProperties;
import com.carlos.log.storage.CompositeLogStorage;
import com.carlos.log.storage.LogStorage;
import com.carlos.log.storage.LoggingLogStorage;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * 日志模块自动配置
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(LogProperties.class)
@ConditionalOnProperty(prefix = "carlos.log", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogConfig {

    /**
     * 日志切面
     */
    @Bean
    @ConditionalOnMissingBean(LogAspect.class)
    public LogAspect logAspect(LogEventProducer eventProducer, LogStorage logStorage) {
        return new LogAspect(eventProducer, logStorage);
    }

    /**
     * 日志存储器
     * 如果应用没有自定义存储器，使用默认的 LoggingLogStorage
     */
    @Bean
    @ConditionalOnMissingBean(LogStorage.class)
    public LogStorage logStorage(ObjectProvider<LogStorage> logStorages) {
        List<LogStorage> storages = new ArrayList<>();

        // 获取所有自定义存储器
        for (LogStorage storage : logStorages) {
            storages.add(storage);
        }

        if (storages.isEmpty()) {
            // 没有自定义存储器，使用默认的日志存储
            log.info("未检测到自定义 LogStorage，使用默认的 LoggingLogStorage");
            return new LoggingLogStorage();
        }

        if (storages.size() == 1) {
            log.info("检测到单个 LogStorage: {}", storages.get(0).getName());
            return storages.get(0);
        }

        // 多个存储器，使用组合存储
        log.info("检测到多个 LogStorage ({}个)，使用组合存储", storages.size());
        return new CompositeLogStorage(storages);
    }

    /**
     * Disruptor RingBuffer
     */
    @Bean
    @ConditionalOnMissingBean(RingBuffer.class)
    @ConditionalOnProperty(prefix = "carlos.log", name = "async", havingValue = "true", matchIfMissing = true)
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
    @ConditionalOnProperty(prefix = "carlos.log", name = "async", havingValue = "true", matchIfMissing = true)
    public LogEventProducer logEventProducer(RingBuffer<LogEvent> ringBuffer) {
        return new LogEventProducer(ringBuffer);
    }

    /**
     * 同步模式下的占位 RingBuffer（不启用 Disruptor）
     */
    @Bean
    @ConditionalOnMissingBean(RingBuffer.class)
    @ConditionalOnProperty(prefix = "carlos.log", name = "async", havingValue = "false")
    public RingBuffer<LogEvent> noopRingBuffer() {
        // 同步模式下返回 null，Disruptor 相关组件不生效
        return null;
    }

    /**
     * 同步模式下的占位 Producer
     */
    @Bean
    @ConditionalOnMissingBean(LogEventProducer.class)
    @ConditionalOnProperty(prefix = "carlos.log", name = "async", havingValue = "false")
    public LogEventProducer noopLogEventProducer() {
        return null;
    }

    /**
     * 创建等待策略
     */
    private WaitStrategy createWaitStrategy(String type) {
        return switch (type.toUpperCase()) {
            case "BUSY_SPIN" -> new com.lmax.disruptor.BusySpinWaitStrategy();
            case "SLEEPING" -> new com.lmax.disruptor.SleepingWaitStrategy();
            case "YIELDING" -> new com.lmax.disruptor.YieldingWaitStrategy();
            default -> new BlockingWaitStrategy();
        };
    }
}
