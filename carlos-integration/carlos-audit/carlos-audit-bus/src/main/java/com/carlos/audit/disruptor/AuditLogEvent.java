package com.carlos.audit.disruptor;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;

/**
 * <p>
 * Disruptor 审计日志事件
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
public class AuditLogEvent {

    /**
     * 事件序列号（由 Disruptor 分配）
     */
    private long sequence;

    /**
     * 审计日志数据
     */
    private AuditLogMainDTO auditLog;

    /**
     * 事件状态
     */
    private EventState state;

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 创建时间戳
     */
    private long timestamp;

    /**
     * 事件状态枚举
     */
    public enum EventState {
        /** 新事件 */
        NEW,
        /** 处理中 */
        PROCESSING,
        /** 成功 */
        SUCCESS,
        /** 失败 */
        FAILED,
        /** 丢弃 */
        DISCARDED
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public AuditLogMainDTO getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(AuditLogMainDTO auditLog) {
        this.auditLog = auditLog;
    }

    public EventState getState() {
        return state;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 重置事件（对象池复用）
     */
    public void reset() {
        this.sequence = 0L;
        this.auditLog = null;
        this.state = EventState.NEW;
        this.retryCount = 0;
        this.timestamp = 0L;
    }
}
