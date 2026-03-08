package com.carlos.auth.audit.aspect;

import com.carlos.auth.audit.annotation.AuditLog;
import com.carlos.auth.audit.mapper.AuditOperationMapper;
import com.carlos.auth.audit.pojo.entity.AuditOperation;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.util.IpLocationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作审计AOP切面
 * </p>
 *
 * <p>在标记@AuditLog注解的方法执行时，自动记录审计日志</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationAuditAspect {

    private final AuditOperationMapper auditOperationMapper;
    private final IpLocationUtil ipLocationUtil;
    private final ObjectMapper objectMapper;

    /**
     * 环绕通知：记录操作审计日志
     *
     * @param joinPoint 连接点
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Around("@annotation(com.carlos.auth.audit.annotation.AuditLog)")
    public Object auditMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AuditLog auditLog = signature.getMethod().getAnnotation(AuditLog.class);

        String operationType = auditLog.operationType();
        String resourceType = auditLog.resourceType();
        Serializable resourceId = getResourceId(joinPoint, auditLog);

        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) {
            log.warn("No current user found for audit operation: {}", operationType);
            return joinPoint.proceed();
        }

        HttpServletRequest request = getCurrentRequest();
        String beforeValue = null;
        String afterValue = null;

        try {
            Object result = joinPoint.proceed();

            if (auditLog.recordReturnValue()) {
                afterValue = serializeValue(result);
            }

            recordAuditLog(currentUser, operationType, resourceType,
                resourceId, beforeValue, afterValue, request);

            return result;
        } catch (Exception e) {
            log.error("Operation audit error: operation={}, user={}",
                operationType, currentUser.getUsername(), e);
            throw e;
        }
    }

    /**
     * 获取资源ID
     */
    private String getResourceId(ProceedingJoinPoint joinPoint, AuditLog auditLog) {
        int paramIndex = auditLog.resourceIdParamIndex();

        if (paramIndex >= 0) {
            Object[] args = joinPoint.getArgs();
            if (args.length > paramIndex && args[paramIndex] != null) {
                return args[paramIndex].toString();
            }
        }

        return null;
    }

    /**
     * 获取当前登录用户
     */
    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
            return (UserInfo) principal;
        }

        return null;
    }

    /**
     * 获取当前HTTP请求
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 序列化对象为JSON字符串
     */
    private String serializeValue(Object value) {
        if (value == null) {
            return null;
        }

        try {
            String json = objectMapper.writeValueAsString(value);
            return json;
        } catch (Exception e) {
            log.warn("Failed to serialize value for audit", e);
            return value.toString();
        }
    }

    /**
     * 记录审计日志
     */
    private void recordAuditLog(UserInfo user, String operationType, String resourceType,
                                Serializable resourceId, String beforeValue, String afterValue,
                                HttpServletRequest request) {
        try {
            AuditOperation audit = new AuditOperation();
            audit.setUserId(user.getUserId());
            audit.setUsername(user.getUsername());
            audit.setOperationType(operationType);
            audit.setResourceType(resourceType);
            audit.setResourceId((String) resourceId);
            audit.setBeforeValue(beforeValue);
            audit.setAfterValue(afterValue);

            if (request != null) {
                String ip = ipLocationUtil.getClientIp(request);
                audit.setIpAddress(ip);
                audit.setUserAgent(request.getHeader("User-Agent"));
            }

            audit.setCreateTime(LocalDateTime.now());

            auditOperationMapper.insert(audit);

            log.info("Operation audit recorded: user={}, operation={}, type={}",
                user.getUsername(), operationType, resourceType);
        } catch (Exception e) {
            log.error("Failed to record operation audit", e);
        }
    }
}
