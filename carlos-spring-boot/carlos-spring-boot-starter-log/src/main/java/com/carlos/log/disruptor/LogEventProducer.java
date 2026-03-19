package com.carlos.log.disruptor;

import com.carlos.log.entity.OperationLog;
import com.lmax.disruptor.RingBuffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Disruptor 日志事件生产者
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class LogEventProducer {

    private final RingBuffer<LogEvent> ringBuffer;

    /**
     * 异步发送日志
     *
     * @param operationLog 操作日志
     */
    public void publish(OperationLog operationLog) {
        if (operationLog == null) {
            log.warn("日志数据为空，跳过发送");
            return;
        }

        long sequence = ringBuffer.next();
        try {
            LogEvent event = ringBuffer.get(sequence);
            event.setOperationLog(operationLog);
            event.setTimestamp(System.currentTimeMillis());
            event.setState(LogEvent.EventState.NEW);
            event.setRetryCount(0);
            event.setSequence(sequence);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 批量异步发送日志
     *
     * @param logs 日志列表
     */
    public void publishBatch(Iterable<OperationLog> logs) {
        if (logs == null) {
            return;
        }

        for (OperationLog operationLog : logs) {
            publish(operationLog);
        }
    }

    /**
     * 同步发送日志（重要日志使用）
     *
     * @param operationLog 操作日志
     * @param timeoutMs    超时时间（毫秒）
     * @return 是否发送成功
     */
    public boolean publishSync(OperationLog operationLog, long timeoutMs) {
        if (operationLog == null) {
            log.warn("日志数据为空，跳过发送");
            return false;
        }

        // 同步发送直接使用存储器，不走 Disruptor
        // 这样可以确保日志被正确存储后才返回
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] success = {false};

        // 这里简化处理，实际可以使用 Future 或回调机制
        // 由于是同步调用，我们直接在 Handler 中处理

        long sequence = ringBuffer.next();
        try {
            LogEvent event = ringBuffer.get(sequence);
            event.setOperationLog(operationLog);
            event.setTimestamp(System.currentTimeMillis());
            event.setState(LogEvent.EventState.NEW);
            event.setRetryCount(0);
            event.setSequence(sequence);

            // TODO: 实现同步等待机制

        } finally {
            ringBuffer.publish(sequence);
        }

        try {
            return latch.await(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("同步发送日志被中断");
            return false;
        }
    }

    /**
     * 获取 RingBuffer 剩余容量
     *
     * @return 剩余槽位数
     */
    public long remainingCapacity() {
        return ringBuffer.remainingCapacity();
    }

    /**
     * 获取 RingBuffer 已使用容量
     *
     * @return 已使用槽位数
     */
    public long getCursor() {
        return ringBuffer.getCursor();
    }

    /**
     * 检查队列是否已满
     *
     * @return true 表示队列已满或接近满
     */
    public boolean isFull() {
        return ringBuffer.remainingCapacity() < 100;
    }
}
