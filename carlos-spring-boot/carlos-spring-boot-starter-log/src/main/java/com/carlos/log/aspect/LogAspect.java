package com.carlos.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.log.annotation.Log;
import com.carlos.log.disruptor.LogEventProducer;
import com.carlos.log.entity.OperationLog;
import com.carlos.log.spel.SpelExpressionResolver;
import com.carlos.log.storage.LogStorage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志 AOP 切面
 * <p>
 * 支持 SpEL 表达式、异步记录、风险评估等功能
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    private final SpelExpressionResolver spelResolver = new SpelExpressionResolver();

    // 存储相关
    private final LogEventProducer eventProducer;
    private final LogStorage logStorage;

    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint point, Log logAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 构建 SpEL 上下文
        StandardEvaluationContext context = spelResolver.createContext(point, null);

        // 检查记录条件
        if (StringUtils.hasText(logAnnotation.condition())) {
            boolean shouldLog = spelResolver.resolveBoolean(
                logAnnotation.condition(), context, true);
            if (!shouldLog) {
                return point.proceed();
            }
        }

        // 创建日志实体
        OperationLog operationLog = new OperationLog();

        // 填充基础信息
        fillBasicInfo(operationLog, logAnnotation, context);

        // 填充主体信息
        fillPrincipalInfo(operationLog, logAnnotation);

        // 填充请求信息
        fillRequestInfo(operationLog);

        // 解析目标对象信息
        fillTargetInfo(operationLog, logAnnotation, context);

        // 记录请求参数
        if (logAnnotation.saveRequestParams()) {
            recordRequestParams(operationLog, point, logAnnotation);
        }

        // 执行目标方法
        Object result = null;
        Throwable exception = null;

        try {
            result = point.proceed();

            // 成功状态
            operationLog.setState("SUCCESS");

            // 记录响应数据
            if (logAnnotation.saveResponseData() && result != null) {
                recordResponseData(operationLog, result, logAnnotation);
            }

            // 更新上下文中的 result
            context.setVariable("result", result);

            // 重新解析可能依赖 result 的表达式
            if (StringUtils.hasText(logAnnotation.targetName())) {
                String targetName = spelResolver.resolve(logAnnotation.targetName(), context);
                operationLog.setTargetName(targetName);
            }

        } catch (Throwable e) {
            exception = e;
            operationLog.setState("FAIL");
            operationLog.setException(e.getMessage());
            operationLog.setExceptionClass(e.getClass().getName());
        } finally {
            // 计算耗时
            if (logAnnotation.recordDuration()) {
                operationLog.setDurationMs((int) (System.currentTimeMillis() - startTime));
            }

            // 设置时间信息
            LocalDateTime now = LocalDateTime.now();
            operationLog.setServerTime(now);
            operationLog.setEventTime(now);
            operationLog.setEventDate(LocalDate.now());
            operationLog.setCreatedTime(now);
            operationLog.setRetentionDeadline(LocalDate.now().plusDays(90));

            // 发送日志
            sendLog(operationLog, logAnnotation);
        }

        // 如果发生异常，继续抛出
        if (exception != null) {
            throw exception;
        }

        return result;
    }

    /**
     * 填充基础信息
     */
    private void fillBasicInfo(OperationLog operationLog, Log logAnnotation,
                               StandardEvaluationContext context) {
        // Schema 版本
        operationLog.setLogSchemaVersion(1);

        // 风险等级
        operationLog.setRiskLevel(logAnnotation.riskLevel());

        // 日志类型
        String logType = logAnnotation.logType();
        if (!StringUtils.hasText(logType) && StringUtils.hasText(logAnnotation.title())) {
            logType = logAnnotation.title() + "_" + logAnnotation.businessType().name();
        }
        operationLog.setLogType(logType);

        // 类别（从 title 推断或默认 BUSINESS）
        // TODO: Carlos 2026-03-19  
        operationLog.setCategory("BUSINESS");

        // 操作描述（支持 SpEL）
        if (StringUtils.hasText(logAnnotation.operation())) {
            String operation = spelResolver.resolve(logAnnotation.operation(), context);
            operationLog.setOperation(operation);
        } else {
            operationLog.setOperation(logAnnotation.businessType().getInfo());
        }

        // 业务渠道（支持 SpEL）
        if (StringUtils.hasText(logAnnotation.bizChannel())) {
            String bizChannel = spelResolver.resolve(logAnnotation.bizChannel(), context);
            operationLog.setBizChannel(bizChannel);
        }

        // 业务场景（支持 SpEL）
        if (StringUtils.hasText(logAnnotation.bizScene())) {
            String bizScene = spelResolver.resolve(logAnnotation.bizScene(), context);
            operationLog.setBizScene(bizScene);
        }
    }

    /**
     * 填充主体信息（操作人）
     */
    private void fillPrincipalInfo(OperationLog operationLog, Log logAnnotation) {
        try {
            UserContext userContext = ExtendInfoUtil.getUserContext();

            if (userContext != null) {
                // 主体ID
                if (userContext.getUserId() != null) {
                    String userId = String.valueOf(userContext.getUserId());
                    operationLog.setPrincipalId(userId);
                }

                // 主体名称
                String account = userContext.getAccount();
                operationLog.setPrincipalName(account);

                // 租户ID
                if (userContext.getTenantId() != null) {
                    operationLog.setTenantId(String.valueOf(userContext.getTenantId()));
                }

                // 部门ID
                if (userContext.getDepartmentId() != null) {
                    operationLog.setDeptId(String.valueOf(userContext.getDepartmentId()));
                }

                // 主体类型
                operationLog.setPrincipalType("USER");

                // 从请求头获取额外信息
                ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletResponse response = attributes.getResponse();
                    if (response != null) {
                        if (StrUtil.isBlank(operationLog.getPrincipalId())) {
                            operationLog.setPrincipalId(response.getHeader("X-User-Id"));
                        }
                    }
                }
            } else {
                // 尝试从请求头获取
                ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    operationLog.setPrincipalId(request.getHeader("X-User-Id"));
                    operationLog.setPrincipalName(request.getHeader("X-User-Account"));
                }
                operationLog.setPrincipalType("ANONYMOUS");
            }
        } catch (Exception e) {
            log.debug("获取主体信息失败", e);
            operationLog.setPrincipalType("ANONYMOUS");
        }
    }

    /**
     * 填充请求信息
     */
    private void fillRequestInfo(OperationLog operationLog) {
        try {
            RequestInfo requestInfo = RequestUtil.getRequestInfo();
            if (requestInfo != null) {
                operationLog.setUrl(requestInfo.getUrl());
                operationLog.setClientIp(requestInfo.getIp());
            }

            ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operationLog.setHttpMethod(request.getMethod());
                operationLog.setUserAgent(request.getHeader("User-Agent"));
                operationLog.setServerIp(request.getLocalAddr());
                operationLog.setClientPort(request.getRemotePort());

                // 应用信息
                String contextPath = request.getContextPath();
                operationLog.setAppName(contextPath != null ? contextPath.replace("/", "") : "");
            }

        } catch (Exception e) {
            log.debug("获取请求信息失败", e);
        }
    }

    /**
     * 填充目标对象信息
     */
    private void fillTargetInfo(OperationLog operationLog, Log logAnnotation, StandardEvaluationContext context) {
        operationLog.setTargetType(logAnnotation.targetType());

        if (StringUtils.hasText(logAnnotation.targetId())) {
            String targetId = spelResolver.resolve(logAnnotation.targetId(), context);
            operationLog.setTargetId(targetId);
        }

        if (StringUtils.hasText(logAnnotation.targetName())) {
            String targetName = spelResolver.resolve(logAnnotation.targetName(), context);
            operationLog.setTargetName(targetName);
        }
    }

    /**
     * 记录请求参数
     */
    private void recordRequestParams(OperationLog operationLog, ProceedingJoinPoint point, Log logAnnotation) {
        try {
            Object[] args = point.getArgs();
            if (args == null || args.length == 0) {
                return;
            }

            // 过滤掉 HttpServletRequest 和 HttpServletResponse
            Object[] filteredArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest)
                    && !(arg instanceof HttpServletResponse))
                .toArray();

            if (filteredArgs.length == 0) {
                return;
            }

            // 序列化参数
            String paramsJson = JSONUtil.toJsonStr(filteredArgs);

            // 长度限制
            if (paramsJson.length() > logAnnotation.maxParamLength()) {
                paramsJson = paramsJson.substring(0, logAnnotation.maxParamLength()) + "...(truncated)";
            }

            operationLog.setRequestPayloadRef(paramsJson);

        } catch (Exception e) {
            log.warn("序列化请求参数失败", e);
        }
    }

    /**
     * 记录响应数据
     */
    private void recordResponseData(OperationLog operationLog, Object result, Log logAnnotation) {
        try {
            String resultJson = JSONUtil.toJsonStr(result);

            if (resultJson.length() > logAnnotation.maxResponseLength()) {
                resultJson = resultJson.substring(0, logAnnotation.maxResponseLength()) + "...(truncated)";
            }

            operationLog.setResponsePayloadRef(resultJson);
        } catch (Exception e) {
            log.warn("序列化响应数据失败", e);
        }
    }

    /**
     * 发送日志
     */
    private void sendLog(OperationLog operationLog, Log logAnnotation) {
        try {
            if (logAnnotation.async() && eventProducer != null) {
                // 异步发送
                eventProducer.publish(operationLog);
            } else {
                // 同步发送
                if (logStorage != null) {
                    logStorage.store(operationLog);
                }
            }
        } catch (Exception e) {
            log.error("发送操作日志失败", e);
        }
    }
}
