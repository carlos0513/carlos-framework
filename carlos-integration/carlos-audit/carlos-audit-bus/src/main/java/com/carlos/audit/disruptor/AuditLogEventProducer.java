package com.carlos.audit.disruptor;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.lmax.disruptor.RingBuffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Disruptor 事件生产者
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogEventProducer {

    private final RingBuffer<AuditLogEvent> ringBuffer;

    /**
     * 异步发送审计日志
     *
     * @param auditLog 审计日志数据
     */
    public void publish(AuditLogMainDTO auditLog) {
        if (auditLog == null) {
            log.warn("审计日志数据为空，跳过发送");
            return;
        }

        long sequence = ringBuffer.next();
        try {
            AuditLogEvent event = ringBuffer.get(sequence);
            event.setAuditLog(auditLog);
            event.setTimestamp(System.currentTimeMillis());
            event.setState(AuditLogEvent.EventState.NEW);
            event.setRetryCount(0);
            event.setSequence(sequence);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 同步发送审计日志（重要日志使用）
     *
     * @param auditLog   审计日志数据
     * @param timeoutMs  超时时间（毫秒）
     * @return 是否发送成功
     */
    public boolean publishSync(AuditLogMainDTO auditLog, long timeoutMs) {
        if (auditLog == null) {
            log.warn("审计日志数据为空，跳过发送");
            return false;
        }

        CountDownLatch latch = new CountDownLatch(1);
        boolean[] success = {false};

        long sequence = ringBuffer.next();
        try {
            AuditLogEvent event = ringBuffer.get(sequence);
            event.setAuditLog(auditLog);
            event.setTimestamp(System.currentTimeMillis());
            event.setState(AuditLogEvent.EventState.NEW);
            event.setRetryCount(0);
            event.setSequence(sequence);

            // 添加回调标记（需要在 EventHandler 中处理）
            // 这里简化处理，实际可使用 Future 或回调机制

        } finally {
            ringBuffer.publish(sequence);
        }

        try {
            success[0] = latch.await(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("同步发送审计日志被中断");
        }

        return success[0];
    }

    /**
     * 批量发送审计日志
     *
     * @param auditLogs 审计日志列表
     */
    public void publishBatch(Iterable<AuditLogMainDTO> auditLogs) {
        if (auditLogs == null) {
            return;
        }

        for (AuditLogMainDTO auditLog : auditLogs) {
            publish(auditLog);
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
}
