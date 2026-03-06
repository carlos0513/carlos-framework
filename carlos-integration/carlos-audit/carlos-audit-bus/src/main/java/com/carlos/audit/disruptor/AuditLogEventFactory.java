package com.carlos.audit.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * <p>
 * Disruptor 事件工厂
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
public class AuditLogEventFactory implements EventFactory<AuditLogEvent> {

    @Override
    public AuditLogEvent newInstance() {
        return new AuditLogEvent();
    }
}
