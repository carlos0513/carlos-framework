package com.carlos.auth.event;

import com.carlos.auth.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * <p>
 * 登录审计事件
 * </p>
 *
 * <p>当用户登录、登出时发布此事件，异步记录审计日志</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Getter
public class LoginAuditEvent extends ApplicationEvent {

    /**
     * 用户
     */
    private final User user;

    /**
     * HTTP请求
     */
    private final HttpServletRequest request;

    /**
     * 事件类型：LOGIN、LOGOUT、REFRESH、LOCKED
     */
    private final String eventType;

    /**
     * 状态：SUCCESS、FAILURE
     */
    private final String status;

    /**
     * 错误消息（失败时）
     */
    private final String errorMessage;

    /**
     * 会话ID
     */
    private final String sessionId;

    public LoginAuditEvent(Object source, User user, HttpServletRequest request,
                           String eventType, String status, String errorMessage, String sessionId) {
        super(source);
        this.user = user;
        this.request = request;
        this.eventType = eventType;
        this.status = status;
        this.errorMessage = errorMessage;
        this.sessionId = sessionId;
    }

    /**
     * 构建登录成功事件
     */
    public static LoginAuditEvent loginSuccess(Object source, User user,
                                               HttpServletRequest request, String sessionId) {
        return new LoginAuditEvent(source, user, request, "LOGIN", "SUCCESS", null, sessionId);
    }

    /**
     * 构建登录失败事件
     */
    public static LoginAuditEvent loginFailure(Object source, User user,
                                               HttpServletRequest request, String errorMessage) {
        return new LoginAuditEvent(source, user, request, "LOGIN", "FAILURE", errorMessage, null);
    }

    /**
     * 构建登出事件
     */
    public static LoginAuditEvent logout(Object source, User user,
                                         HttpServletRequest request, String sessionId) {
        return new LoginAuditEvent(source, user, request, "LOGOUT", "SUCCESS", null, sessionId);
    }

    /**
     * 构建账号锁定事件
     */
    public static LoginAuditEvent accountLocked(Object source, User user,
                                                HttpServletRequest request, String errorMessage) {
        return new LoginAuditEvent(source, user, request, "LOCKED", "FAILURE", errorMessage, null);
    }
}
