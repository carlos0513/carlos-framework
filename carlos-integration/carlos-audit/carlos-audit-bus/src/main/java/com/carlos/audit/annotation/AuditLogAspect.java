package com.carlos.audit.annotation;

import cn.hutool.json.JSONUtil;
import com.carlos.audit.disruptor.AuditLogEventProducer;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.enums.AuditLogBizChannelEnum;
import com.carlos.audit.pojo.enums.AuditLogPrincipalTypeEnum;
import com.carlos.audit.pojo.enums.AuditLogStateEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志 AOP 切面
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogEventProducer producer;

    // SpEL 表达式解析器
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint point, AuditLog auditLog) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 创建审计日志 DTO
        AuditLogMainDTO auditLogDTO = new AuditLogMainDTO();

        // 设置基本信息
        auditLogDTO.setCategory(auditLog.category());
        auditLogDTO.setLogType(auditLog.type());
        auditLogDTO.setTargetType(auditLog.targetType());
        auditLogDTO.setRiskLevel(auditLog.riskLevel());

        // 解析 SpEL 表达式
        StandardEvaluationContext context = buildSpelContext(point);

        // 操作描述
        if (StringUtils.hasText(auditLog.operation())) {
            String operation = parser.parseExpression(auditLog.operation()).getValue(context, String.class);
            auditLogDTO.setOperation(operation);
        } else {
            auditLogDTO.setOperation(auditLog.type());
        }

        // 目标对象ID
        if (StringUtils.hasText(auditLog.targetId())) {
            String targetId = parser.parseExpression(auditLog.targetId()).getValue(context, String.class);
            auditLogDTO.setTargetId(targetId);
        }

        // 业务渠道
        if (StringUtils.hasText(auditLog.bizChannel())) {
            String channel = parser.parseExpression(auditLog.bizChannel()).getValue(context, String.class);
            try {
                auditLogDTO.setBizChannel(AuditLogBizChannelEnum.valueOf(channel));
            } catch (IllegalArgumentException e) {
                log.warn("无效的业务渠道: {}", channel);
            }
        }

        // 业务场景
        if (StringUtils.hasText(auditLog.bizScene())) {
            String scene = parser.parseExpression(auditLog.bizScene()).getValue(context, String.class);
            auditLogDTO.setBizScene(scene);
        }

        // 填充主体信息
        fillPrincipalInfo(auditLogDTO);

        // 填充请求信息
        fillRequestInfo(auditLogDTO);

        // 记录请求参数
        if (auditLog.recordParams()) {
            try {
                String params = JSONUtil.toJsonStr(point.getArgs());
                // 限制长度，避免过大
                if (params.length() > 4000) {
                    params = params.substring(0, 4000) + "...(truncated)";
                }
                auditLogDTO.setRequestPayloadRef(params);
            } catch (Exception e) {
                log.warn("序列化请求参数失败", e);
            }
        }

        // 执行目标方法
        Object result = null;
        try {
            result = point.proceed();

            // 成功状态
            auditLogDTO.setState(AuditLogStateEnum.SUCCESS);
            auditLogDTO.setDurationMs((int) (System.currentTimeMillis() - startTime));

            // 记录返回值
            if (auditLog.recordResult() && result != null) {
                try {
                    String resultStr = JSONUtil.toJsonStr(result);
                    if (resultStr.length() > 4000) {
                        resultStr = resultStr.substring(0, 4000) + "...(truncated)";
                    }
                    auditLogDTO.setResponsePayloadRef(resultStr);
                } catch (Exception e) {
                    log.warn("序列化返回值失败", e);
                }
            }

        } catch (Exception e) {
            // 失败状态
            auditLogDTO.setState(AuditLogStateEnum.FAIL);
            auditLogDTO.setDurationMs((int) (System.currentTimeMillis() - startTime));
            auditLogDTO.setResultCode(e.getClass().getSimpleName());
            String message = e.getMessage();
            if (message != null && message.length() > 500) {
                message = message.substring(0, 500);
            }
            auditLogDTO.setResultMessage(message);
            throw e;
        } finally {
            // 设置事件时间和保留期限
            auditLogDTO.setEventTime(LocalDateTime.now());
            auditLogDTO.setRetentionDeadline(LocalDate.now().plusDays(90));

            // 发送审计日志
            if (auditLog.async()) {
                producer.publish(auditLogDTO);
            } else {
                producer.publishSync(auditLogDTO, 5000);
            }
        }

        return result;
    }

    /**
     * 构建 SpEL 上下文
     */
    private StandardEvaluationContext buildSpelContext(ProceedingJoinPoint point) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 获取方法参数名和值
        MethodSignature signature = (MethodSignature) point.getSignature();
        String[] paramNames = discoverer.getParameterNames(signature.getMethod());
        Object[] args = point.getArgs();

        if (paramNames != null && args != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        // 添加常用变量
        context.setVariable("request", getCurrentRequest());

        return context;
    }

    /**
     * 填充主体信息
     */
    private void fillPrincipalInfo(AuditLogMainDTO auditLogDTO) {
        try {
            // 从 SecurityContext 获取当前用户
            // 这里简化处理，实际应根据具体认证框架获取
            // Long userId = WebFrameworkUtils.getLoginUserId();
            // if (userId != null) {
            //     auditLogDTO.setPrincipalId(String.valueOf(userId));
            //     auditLogDTO.setPrincipalType(AuditLogPrincipalTypeEnum.USER);
            //     // TODO: 获取用户名
            //     auditLogDTO.setPrincipalName("User-" + userId);
            // } else {
            //     auditLogDTO.setPrincipalType(AuditLogPrincipalTypeEnum.ANONYMOUS);
            // }

            // 获取租户ID
            // Long tenantId = WebFrameworkUtils.getTenantId();
            // auditLogDTO.setTenantId(tenantId != null ? String.valueOf(tenantId) : "0");

        } catch (Exception e) {
            log.warn("获取主体信息失败", e);
            auditLogDTO.setPrincipalType(AuditLogPrincipalTypeEnum.ANONYMOUS);
            auditLogDTO.setTenantId("0");
        }
    }

    /**
     * 填充请求信息
     */
    private void fillRequestInfo(AuditLogMainDTO auditLogDTO) {
        try {
            HttpServletRequest request = getCurrentRequest();
            if (request == null) {
                return;
            }

            // 客户端 IP
            // String clientIp = IpUtil.getClientIP(request);
            // auditLogDTO.setClientIp(clientIp);

            // User-Agent
            String userAgent = request.getHeader("User-Agent");
            if (StringUtils.hasText(userAgent)) {
                auditLogDTO.setUserAgent(userAgent.substring(0, Math.min(userAgent.length(), 500)));
            }

            // 服务器 IP
            // auditLogDTO.setServerIp(IpUtils.getLocalHostExactAddress());

            // 应用名
            String appName = request.getContextPath();
            if (StringUtils.hasText(appName)) {
                auditLogDTO.setAppName(appName.replace("/", ""));
            }

        } catch (Exception e) {
            log.warn("获取请求信息失败", e);
        }
    }

    /**
     * 获取当前请求
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception e) {
            log.debug("获取当前请求失败", e);
        }
        return null;
    }
}
