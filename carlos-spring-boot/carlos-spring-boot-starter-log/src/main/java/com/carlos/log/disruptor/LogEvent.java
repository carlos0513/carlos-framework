package com.carlos.log.disruptor;

import com.carlos.log.entity.OperationLog;
import com.lmax.disruptor.EventFactory;
import lombok.Data;

/**
 * Disruptor 日志事件
 *
 * @author carlos
 * @since 3.0.0
 */
@Data
public class LogEvent {

    /**
     * 事件状态
     */
    public enum EventState {
        /** 新建 */
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

    /** 操作日志数据 */
    private OperationLog operationLog;

    /** 事件时间戳 */
    private long timestamp;

    /** 事件状态 */
    private EventState state = EventState.NEW;

    /** 重试次数 */
    private int retryCount = 0;

    /** 序列号 */
    private long sequence;

    /**
     * 事件工厂
     */
    public static class LogEventFactory implements EventFactory<LogEvent> {
        @Override
        public LogEvent newInstance() {
            return new LogEvent();
        }
    }

    /**
     * 重置事件（用于对象复用）
     */
    public void reset() {
        this.operationLog = null;
        this.timestamp = 0;
        this.state = EventState.NEW;
        this.retryCount = 0;
        this.sequence = 0;
    }
}
