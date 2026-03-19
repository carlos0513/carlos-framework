package com.carlos.log.disruptor;

import com.carlos.log.entity.OperationLog;
import com.carlos.log.storage.LogStorage;
import com.lmax.disruptor.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Disruptor 日志事件处理器
 * <p>
 * 支持批量处理和自动重试机制
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class LogEventHandler implements EventHandler<LogEvent> {

    private final LogStorage logStorage;

    // 批量处理队列
    private final BlockingQueue<OperationLog> batchQueue = new LinkedBlockingQueue<>();

    // 批量大小
    private final int batchSize = 100;

    @Override
    public void onEvent(LogEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (event.getOperationLog() == null) {
            log.warn("收到空的日志事件，序列号: {}", sequence);
            event.setState(LogEvent.EventState.DISCARDED);
            return;
        }

        try {
            event.setState(LogEvent.EventState.PROCESSING);

            OperationLog operationLog = event.getOperationLog();

            // 数据清洗和 enrich
            enrichLog(operationLog);

            // 添加到批量队列或直接处理
            if (logStorage.supportBatch()) {
                batchQueue.offer(operationLog);
                if (batchQueue.size() >= batchSize || endOfBatch) {
                    processBatch();
                }
            } else {
                // 单条处理
                boolean success = logStorage.store(operationLog);
                if (success) {
                    event.setState(LogEvent.EventState.SUCCESS);
                } else {
                    handleError(event, new RuntimeException("存储返回失败"));
                }
            }

            // 如果是批量处理且是最后一个事件，处理剩余队列
            if (endOfBatch && logStorage.supportBatch()) {
                processBatch();
            }

        } catch (Exception e) {
            log.error("处理日志事件失败，序列号: {}", sequence, e);
            handleError(event, e);
        }
    }

    /**
     * 数据清洗和 enrich
     */
    private void enrichLog(OperationLog operationLog) {
        // 确保时间字段
        if (operationLog.getCreatedTime() == null) {
            operationLog.setCreatedTime(java.time.LocalDateTime.now());
        }

        // 确保状态
        if (operationLog.getState() == null) {
            operationLog.setState("SUCCESS");
        }

        // 确保风险等级
        if (operationLog.getRiskLevel() == null) {
            operationLog.setRiskLevel(0);
        }
    }

    /**
     * 执行批量处理
     */
    private void processBatch() {
        List<OperationLog> logs = new ArrayList<>();
        batchQueue.drainTo(logs, batchSize);

        if (logs.isEmpty()) {
            return;
        }

        try {
            boolean success = logStorage.storeBatch(logs);
            if (!success) {
                log.warn("批量日志存储失败，数量: {}", logs.size());
                // 降级为单条处理
                for (OperationLog operationLog : logs) {
                    logStorage.store(operationLog);
                }
            }
        } catch (Exception e) {
            log.error("批量日志存储异常，数量: {}", logs.size(), e);
            // 降级为单条处理
            for (OperationLog operationLog : logs) {
                try {
                    logStorage.store(operationLog);
                } catch (Exception ex) {
                    log.error("单条日志存储失败", ex);
                }
            }
        }
    }

    /**
     * 处理错误
     */
    private void handleError(LogEvent event, Exception e) {
        if (event.getRetryCount() < 3) {
            event.setRetryCount(event.getRetryCount() + 1);
            event.setState(LogEvent.EventState.NEW);
            log.warn("日志事件处理失败，将重试，序列号: {}，重试次数: {}",
                event.getSequence(), event.getRetryCount());
        } else {
            event.setState(LogEvent.EventState.FAILED);
            saveToDeadLetterQueue(event, e);
        }
    }

    /**
     * 保存到死信队列
     */
    private void saveToDeadLetterQueue(LogEvent event, Exception e) {
        log.error("日志事件最终处理失败，转入死信队列，序列号: {}, log: {}",
            event.getSequence(), event.getOperationLog(), e);
        // TODO: 可以实现本地文件备份或其他持久化方案
    }

    /**
     * 刷新所有待处理的日志
     */
    public void flush() {
        while (!batchQueue.isEmpty()) {
            processBatch();
        }
    }
}
