package com.carlos.auth.audit.listener;

import com.carlos.auth.audit.LoginAuditEvent;
import com.carlos.auth.audit.service.LoginAuditTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 登录审计事件监听器
 * </p>
 *
 * <p>异步处理登录审计日志记录</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginAuditListener {

    private final LoginAuditTaskService loginAuditTaskService;

    /**
     * 处理登录审计事件
     *
     * @param event 登录审计事件
     */
    @Async
    @EventListener
    public void handleLoginAuditEvent(LoginAuditEvent event) {
        try {
            log.debug("Handling login audit event: user={}, type={}, status={}",
                    event.getUser().getUsername(), event.getEventType(), event.getStatus());

            loginAuditTaskService.recordAsync(event);

            log.debug("Login audit event handled successfully");
        } catch (Exception e) {
            log.error("Error handling login audit event", e);
        }
    }
}
