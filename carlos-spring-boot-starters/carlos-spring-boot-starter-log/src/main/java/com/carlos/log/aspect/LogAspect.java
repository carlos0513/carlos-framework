package com.carlos.log.aspect;

import cn.hutool.core.util.StrUtil;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.boot.util.ResponseUtil;
import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.UserContext;
import com.carlos.log.annotation.Log;
import com.carlos.log.entity.SystemOperationLog;
import com.carlos.log.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    private final OperationLogService operationLogService;

    @Pointcut("@annotation(com.carlos.log.annotation.Log)")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
    }

//    private final FeignSysLog feignSysLog;
//    /** 排除敏感属性字段 */
//    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword","pwd",};

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    private void handleLog(final JoinPoint joinPoint, final Log controllerLog, final Exception e, final Object o) {
        final SystemOperationLog logInfo = new SystemOperationLog();
        // 设置方法名称
        // 设置请求方式

        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.debug("浏览器类型：{}", request.getHeader("User-Agent"));
        logInfo.setBrowser(request.getHeader("User-Agent"));
        UserContext userContext = ExtendInfoUtil.getUserContext();
        if (userContext == null) {
            log.error("获取当前用户信息失败！");

        } else {
            if (StrUtil.isBlank(userContext.getAccount()) || StrUtil.isBlank(userContext.getUserId().toString())) {
                HttpServletResponse response = ResponseUtil.getResponse();
                logInfo.setOperatorId(response.getHeader(AuthConstant.USER_ID));
                logInfo.setOperatorAccount(response.getHeader(AuthConstant.USER_ACCOUNT));
            } else {
                logInfo.setOperatorId((String) userContext.getUserId());
                logInfo.setOperatorAccount(userContext.getAccount());
            }

        }

        if (e != null) {
            logInfo.setException(e.getMessage());
        }
        logInfo.setUrl(requestInfo.getUrl());
        logInfo.setIp(requestInfo.getIp());
        logInfo.setMessage(controllerLog.businessType().getInfo());
        operationLogService.addSysLog(logInfo);
    }
    /**
     //     * 忽略敏感属性
     //     */
//    public PropertyPreExcludeFilter excludePropertyPreFilter() {
//        return new PropertyPreExcludeFilter().addExcludes(EXCLUDE_PROPERTIES);
//    }


//
}
