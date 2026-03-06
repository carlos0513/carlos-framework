package com.carlos.audit.disruptor;

import com.carlos.audit.clickhouse.ClickHouseBatchWriter;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.enums.AuditLogStateEnum;
import com.lmax.disruptor.WorkHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * Disruptor 事件处理器（WorkPool 模式）
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogEventHandler implements WorkHandler<AuditLogEvent> {

    private final ClickHouseBatchWriter batchWriter;

    @Override
    public void onEvent(AuditLogEvent event) throws Exception {
        if (event.getAuditLog() == null) {
            log.warn("收到空的审计日志事件，序列号: {}", event.getSequence());
            event.setState(AuditLogEvent.EventState.DISCARDED);
            return;
        }

        try {
            event.setState(AuditLogEvent.EventState.PROCESSING);

            AuditLogMainDTO auditLog = event.getAuditLog();

            // 1. 数据清洗与 enrich
            enrichAuditLog(auditLog);

            // 2. 风险评级
            calculateRiskLevel(auditLog);

            // 3. 加入批量写入队列
            batchWriter.add(auditLog);

            event.setState(AuditLogEvent.EventState.SUCCESS);

            if (log.isDebugEnabled()) {
                log.debug("审计日志事件处理成功，序列号: {}, 类型: {}",
                    event.getSequence(), auditLog.getLogType());
            }

        } catch (Exception e) {
            log.error("处理审计日志事件失败，序列号: {}", event.getSequence(), e);
            handleError(event, e);
        }
    }

    /**
     * 数据清洗与 enrich
     */
    private void enrichAuditLog(AuditLogMainDTO auditLog) {
        // 设置服务器时间（如果未设置）
        if (auditLog.getServerTime() == null) {
            auditLog.setServerTime(LocalDateTime.now());
        }

        // 设置事件日期（如果未设置）
        if (auditLog.getEventDate() == null && auditLog.getServerTime() != null) {
            auditLog.setEventDate(auditLog.getServerTime().toLocalDate());
        }

        // 确保有日志 Schema 版本
        if (auditLog.getLogSchemaVersion() == null) {
            auditLog.setLogSchemaVersion(3);
        }

        // 确保有状态
        if (auditLog.getState() == null) {
            auditLog.setState(AuditLogStateEnum.SUCCESS);
        }

        // 设置创建时间
        if (auditLog.getCreatedTime() == null) {
            auditLog.setCreatedTime(LocalDateTime.now());
        }
    }

    /**
     * 计算风险等级
     */
    private void calculateRiskLevel(AuditLogMainDTO auditLog) {
        int riskLevel = 0;

        // 根据操作类型判断风险
        if (auditLog.getLogType() != null) {
            switch (auditLog.getLogType()) {
                case USER_LOGIN:
                case USER_LOGOUT:
                    riskLevel = 30;
                    break;
                case ORDER_PAY:
                case DATA_DELETE:
                    riskLevel = 70;
                    break;
                case PERMISSION_CHANGE:
                case CONFIG_CHANGE:
                    riskLevel = 80;
                    break;
                case DATA_EXPORT:
                case DATA_IMPORT:
                    riskLevel = 60;
                    break;
                default:
                    riskLevel = 10;
            }
        }

        // 如果操作失败，提高风险等级
        if (auditLog.getState() == AuditLogStateEnum.FAIL) {
            riskLevel = Math.min(100, riskLevel + 20);
        }

        // 如果指定了风险等级，取最大值
        if (auditLog.getRiskLevel() != null && auditLog.getRiskLevel() > riskLevel) {
            riskLevel = auditLog.getRiskLevel();
        }

        auditLog.setRiskLevel(riskLevel);
    }

    /**
     * 错误处理
     */
    private void handleError(AuditLogEvent event, Exception e) {
        if (event.getRetryCount() < 3) {
            event.setRetryCount(event.getRetryCount() + 1);
            event.setState(AuditLogEvent.EventState.NEW);
            // 短暂延迟后重试
            try {
                Thread.sleep(100 * event.getRetryCount());
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            // 注意：这里需要重新发布到 RingBuffer，简化处理
            log.warn("审计日志事件处理失败，将重试，序列号: {}，重试次数: {}",
                event.getSequence(), event.getRetryCount());
        } else {
            event.setState(AuditLogEvent.EventState.FAILED);
            // 转入死信队列或本地文件备份
            saveToDeadLetterQueue(event, e);
        }
    }

    /**
     * 保存到死信队列
     */
    private void saveToDeadLetterQueue(AuditLogEvent event, Exception e) {
        log.error("审计日志事件最终处理失败，转入死信队列，序列号: {}", event.getSequence(), e);
        // TODO: 实现死信队列持久化（本地文件或备用存储）
    }
}
