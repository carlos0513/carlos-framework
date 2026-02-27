package com.carlos.auth.audit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>
 * 审计日志切面
 * </p>
 *
 * <p>自动记录方法调用日志，并对敏感参数进行脱敏</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Aspect
@Component
public class AuditLogAspect {

    /**
     * 定义切点：所有Controller的方法
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointcut() {
    }

    /**
     * 定义切点：所有Service的方法
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void servicePointcut() {
    }

    /**
     * 环绕通知：记录方法调用日志
     *
     * @param joinPoint 连接点
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Around("controllerPointcut() || servicePointcut()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        String argsStr = maskArguments(args);

        log.info("[{}] {}() called with args: {}", className, methodName, argsStr);

        long startTime = System.currentTimeMillis();

        try {
            // 执行方法
            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;
            log.info("[{}] {}() completed in {}ms", className, methodName, duration);

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[{}] {}() failed after {}ms: {}", className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    /**
     * 脱敏参数
     *
     * @param args 参数数组
     * @return 脱敏后的参数字符串
     */
    private String maskArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                sb.append("null");
            } else {
                String argStr = arg.toString();
                // 检查是否为敏感数据
                if (argStr.length() > 100) {
                    // 如果参数过长，截断显示
                    sb.append(argStr.substring(0, 100)).append("...");
                } else {
                    sb.append(arg);
                }
            }

            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
