package com.carlos.audit.config;

import com.carlos.audit.disruptor.AuditLogEvent;
import com.carlos.audit.disruptor.AuditLogEventFactory;
import com.carlos.audit.disruptor.AuditLogEventHandler;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Disruptor 配置类
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "carlos.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DisruptorConfig {

    private final AuditProperties auditProperties;
    private final AuditLogEventHandler eventHandler;

    @Bean
    public RingBuffer<AuditLogEvent> auditLogRingBuffer() {
        AuditProperties.Disruptor disruptorProps = auditProperties.getDisruptor();

        int bufferSize = disruptorProps.getBufferSize();
        int consumerCount = disruptorProps.getConsumerCount();
        String waitStrategyName = disruptorProps.getWaitStrategy();

        log.info("初始化 Disruptor RingBuffer，bufferSize={}, consumerCount={}, waitStrategy={}",
            bufferSize, consumerCount, waitStrategyName);

        // 创建等待策略
        WaitStrategy waitStrategy = createWaitStrategy(waitStrategyName);

        // 生产者类型
        ProducerType producerType = disruptorProps.isMultiProducer()
            ? ProducerType.MULTI
            : ProducerType.SINGLE;

        // 创建 Disruptor
        Disruptor<AuditLogEvent> disruptor = new Disruptor<>(
            new AuditLogEventFactory(),
            bufferSize,
            new NamedThreadFactory("audit-log-disruptor-"),
            producerType,
            waitStrategy
        );

        // 设置消费者（使用 EventHandler）
        disruptor.handleEventsWith(eventHandler);

        // 添加异常处理器
        disruptor.setDefaultExceptionHandler(new AuditLogExceptionHandler());

        // 启动 Disruptor
        disruptor.start();

        log.info("Disruptor 启动成功");

        return disruptor.getRingBuffer();
    }

    /**
     * 创建等待策略
     */
    private WaitStrategy createWaitStrategy(String name) {
        switch (name.toLowerCase()) {
            case "busy_spin":
                return new BusySpinWaitStrategy();
            case "lite_blocking":
                return new LiteBlockingWaitStrategy();
            case "sleeping":
                return new SleepingWaitStrategy();
            case "yielding":
                return new YieldingWaitStrategy();
            case "blocking":
            default:
                return new BlockingWaitStrategy();
        }
    }

    /**
     * 命名线程工厂
     */
    private static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger counter = new AtomicInteger(0);

        NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, prefix + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        }
    }

    /**
     * 异常处理器
     */
    private static class AuditLogExceptionHandler implements ExceptionHandler<AuditLogEvent> {
        @Override
        public void handleEventException(Throwable ex, long sequence, AuditLogEvent event) {
            log.error("处理审计日志事件时发生异常，sequence={}", sequence, ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("启动 Disruptor 时发生异常", ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("关闭 Disruptor 时发生异常", ex);
        }
    }
}
