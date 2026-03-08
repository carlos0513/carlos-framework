package com.carlos.auth.audit.service;

import com.carlos.auth.audit.LoginAuditEvent;
import com.carlos.auth.audit.mapper.AuditLoginMapper;
import com.carlos.auth.audit.pojo.entity.AuditLogin;
import com.carlos.auth.util.IpLocationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 登录审计任务服务
 * </p>
 *
 * <p>异步记录登录审计日志</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAuditTaskService {

    private final AuditLoginMapper auditLoginMapper;
    private final IpLocationUtil ipLocationUtil;

    /**
     * 异步记录登录审计日志
     *
     * @param event 登录审计事件
     */
    public void recordAsync(LoginAuditEvent event) {
        try {
            AuditLogin auditLogin = new AuditLogin();
            auditLogin.setUserId(event.getUser().getUserId());
            auditLogin.setUsername(event.getUser().getUsername());
            auditLogin.setEventType(event.getEventType());
            auditLogin.setStatus(event.getStatus());
            auditLogin.setErrorMessage(event.getErrorMessage());
            auditLogin.setSessionId(event.getSessionId());

            String clientIp = ipLocationUtil.getClientIp(event.getRequest());
            auditLogin.setIpAddress(clientIp);
            auditLogin.setLocation(ipLocationUtil.getLocation(clientIp));
            auditLogin.setUserAgent(event.getRequest().getHeader("User-Agent"));

            auditLogin.setLoginTime(LocalDateTime.now());
            auditLogin.setCreateTime(LocalDateTime.now());

            auditLoginMapper.insert(auditLogin);

            log.info("Login audit log recorded: user={}, type={}, status={}",
                auditLogin.getUsername(), auditLogin.getEventType(), auditLogin.getStatus());
        } catch (Exception e) {
            log.error("Failed to record login audit log", e);
        }
    }
}
