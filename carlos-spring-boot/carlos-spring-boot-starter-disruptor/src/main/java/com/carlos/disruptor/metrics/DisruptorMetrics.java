package com.carlos.disruptor.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Disruptor 监控指标
 * <p>
 * 收集和上报 Disruptor 的性能指标
 * </p>
 *
 * @author Carlos
 * @date 2026-03-14
 */
@Slf4j
public class DisruptorMetrics {

    /**
     * 指标前缀
     */
    private static final String METRICS_PREFIX = "disruptor.";

    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> publishSuccessCounters = new ConcurrentHashMap<>();
    private final Map<String, Counter> publishFailureCounters = new ConcurrentHashMap<>();
    private final Map<String, Timer> publishTimers = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> remainingCapacityGauges = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> bufferSizeGauges = new ConcurrentHashMap<>();

    public DisruptorMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 记录发布成功
     *
     * @param instanceName 实例名称
     * @param latencyMs    延迟（毫秒）
     */
    public void recordPublishSuccess(String instanceName, long latencyMs) {
        try {
            getSuccessCounter(instanceName).increment();
            getPublishTimer(instanceName).record(latencyMs, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.debug("Record publish success metrics failed: {}", e.getMessage());
        }
    }

    /**
     * 记录发布失败
     *
     * @param instanceName 实例名称
     */
    public void recordPublishFailure(String instanceName) {
        try {
            getFailureCounter(instanceName).increment();
        } catch (Exception e) {
            log.debug("Record publish failure metrics failed: {}", e.getMessage());
        }
    }

    /**
     * 注册 RingBuffer 容量监控
     *
     * @param instanceName        实例名称
     * @param remainingCapacityFn 剩余容量提供者
     * @param bufferSizeFn        总容量提供者
     */
    public void registerCapacityMetrics(String instanceName,
                                        Supplier<Number> remainingCapacityFn,
                                        Supplier<Number> bufferSizeFn) {
        try {
            String remainingKey = instanceName + ".remainingCapacity";
            AtomicLong remainingGauge = remainingCapacityGauges.computeIfAbsent(remainingKey, k -> {
                AtomicLong gauge = new AtomicLong();
                Gauge.builder(METRICS_PREFIX + "remaining.capacity", gauge, AtomicLong::get)
                    .tag("instance", instanceName)
                    .register(meterRegistry);
                return gauge;
            });

            String bufferSizeKey = instanceName + ".bufferSize";
            AtomicLong bufferSizeGauge = bufferSizeGauges.computeIfAbsent(bufferSizeKey, k -> {
                AtomicLong gauge = new AtomicLong();
                Gauge.builder(METRICS_PREFIX + "buffer.size", gauge, AtomicLong::get)
                    .tag("instance", instanceName)
                    .register(meterRegistry);
                return gauge;
            });

            // 定期更新
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory()).scheduleAtFixedRate(() -> {
                remainingGauge.set(remainingCapacityFn.get().longValue());
                bufferSizeGauge.set(bufferSizeFn.get().longValue());
            }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);

        } catch (Exception e) {
            log.debug("Register capacity metrics failed: {}", e.getMessage());
        }
    }

    /**
     * 获取成功计数器
     */
    private Counter getSuccessCounter(String instanceName) {
        return publishSuccessCounters.computeIfAbsent(instanceName, name ->
            Counter.builder(METRICS_PREFIX + "publish")
                .tag("instance", name)
                .tag("result", "success")
                .register(meterRegistry)
        );
    }

    /**
     * 获取失败计数器
     */
    private Counter getFailureCounter(String instanceName) {
        return publishFailureCounters.computeIfAbsent(instanceName, name ->
            Counter.builder(METRICS_PREFIX + "publish")
                .tag("instance", name)
                .tag("result", "failure")
                .register(meterRegistry)
        );
    }

    /**
     * 获取发布计时器
     */
    private Timer getPublishTimer(String instanceName) {
        return publishTimers.computeIfAbsent(instanceName, name ->
            Timer.builder(METRICS_PREFIX + "publish.latency")
                .tag("instance", name)
                .register(meterRegistry)
        );
    }
}
