package com.carlos.gateway.config;

import com.carlos.core.exception.GlobalException;
import com.carlos.gateway.exception.*;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 网关错误属性扩展
 * 扩展 Spring Boot 默认的错误属性，添加自定义字段
 * </p>
 *
 * @author carlos
 * @date 2021/3/3 23:56
 * @updated 2026/3/24 重构优化，支持更多异常类型
 */
public class GatewayErrorAttributes extends DefaultErrorAttributes {

    /**
     * 是否开发模式
     */
    private boolean devMode = false;

    public GatewayErrorAttributes() {
        super();
    }

    public GatewayErrorAttributes(boolean devMode) {
        super();
        this.devMode = devMode;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable error = this.getError(request);

        // 基础字段
        errorAttributes.put("success", false);
        errorAttributes.put("timestamp", System.currentTimeMillis());
        errorAttributes.put("path", request.path());

        // 根据异常类型提取属性
        if (error instanceof GatewayException) {
            populateGatewayExceptionAttributes(errorAttributes, (GatewayException) error);
        } else if (error instanceof GlobalException) {
            populateGlobalExceptionAttributes(errorAttributes, (GlobalException) error);
        } else if (error instanceof ResponseStatusException) {
            populateResponseStatusExceptionAttributes(errorAttributes, (ResponseStatusException) error);
        } else {
            populateDefaultAttributes(errorAttributes, error, options);
        }

        // 开发模式添加额外信息
        if (devMode && options.isIncluded(ErrorAttributeOptions.Include.MESSAGE)) {
            errorAttributes.put("detail", error != null ? error.getMessage() : null);
        }

        // 包含异常信息
        if (options.isIncluded(ErrorAttributeOptions.Include.EXCEPTION) && error != null) {
            errorAttributes.put("exception", error.getClass().getName());
        }

        return errorAttributes;
    }

    /**
     * 填充网关异常属性
     */
    private void populateGatewayExceptionAttributes(Map<String, Object> attributes, GatewayException ex) {
        attributes.put("status", ex.getHttpStatus());
        attributes.put("code", ex.getErrorCode());
        attributes.put("msg", ex.getMessage());

        // 添加特定异常类型的额外信息
        Map<String, Object> extra = new HashMap<>();

        if (ex instanceof RateLimitException) {
            RateLimitException rateLimitEx = (RateLimitException) ex;
            extra.put("limitDimension", rateLimitEx.getLimitDimension());
            extra.put("retryAfter", rateLimitEx.getRetryAfter());
        } else if (ex instanceof CircuitBreakerException) {
            CircuitBreakerException cbEx = (CircuitBreakerException) ex;
            extra.put("circuitBreakerName", cbEx.getCircuitBreakerName());
            extra.put("circuitBreakerState", cbEx.getCircuitBreakerState());
        } else if (ex instanceof AuthenticationException) {
            AuthenticationException authEx = (AuthenticationException) ex;
            extra.put("failureType", authEx.getFailureType().name());
        } else if (ex instanceof WafBlockException) {
            WafBlockException wafEx = (WafBlockException) ex;
            extra.put("ruleType", wafEx.getRuleType());
            extra.put("ruleName", wafEx.getRuleName());
        } else if (ex instanceof ServiceNotFoundException) {
            ServiceNotFoundException snfEx = (ServiceNotFoundException) ex;
            extra.put("serviceName", snfEx.getServiceName());
        } else if (ex instanceof RequestTimeoutException) {
            RequestTimeoutException timeoutEx = (RequestTimeoutException) ex;
            extra.put("timeoutMs", timeoutEx.getTimeoutMs());
            extra.put("targetService", timeoutEx.getTargetService());
        } else if (ex instanceof RequestValidationException) {
            RequestValidationException validationEx = (RequestValidationException) ex;
            extra.put("fieldErrors", validationEx.getFieldErrors());
        } else if (ex instanceof ReplayAttackException) {
            ReplayAttackException replayEx = (ReplayAttackException) ex;
            extra.put("attackType", replayEx.getAttackType().name());
            extra.put("nonce", replayEx.getRequestId());
        }

        if (!extra.isEmpty()) {
            attributes.put("extra", extra);
        }
    }

    /**
     * 填充全局异常属性
     */
    private void populateGlobalExceptionAttributes(Map<String, Object> attributes, GlobalException ex) {
        String errorCode = ex.getErrorCode();
        int httpStatus = mapErrorCodeToHttpStatus(errorCode);

        attributes.put("status", httpStatus);
        attributes.put("code", errorCode != null ? errorCode : 5000);
        attributes.put("message", ex.getMessage());
    }

    /**
     * 填充 ResponseStatusException 属性
     */
    private void populateResponseStatusExceptionAttributes(Map<String, Object> attributes, ResponseStatusException ex) {
        int statusCode = ex.getStatusCode().value();
        attributes.put("status", statusCode);
        attributes.put("code", mapHttpStatusToCode(statusCode));
        attributes.put("message", ex.getReason() != null ? ex.getReason() : "Request error");
    }

    /**
     * 填充默认属性
     */
    private void populateDefaultAttributes(Map<String, Object> attributes, Throwable ex, ErrorAttributeOptions options) {
        attributes.put("status", 500);
        attributes.put("code", 5000);
        attributes.put("message", ex != null ? ex.getMessage() : "Internal Server Error");
    }

    /**
     * 错误码映射为 HTTP 状态码
     */
    private int mapErrorCodeToHttpStatus(String errorCode) {
        if (errorCode == null) {
            return 500;
        }
        switch (errorCode) {
            case "4001":
                return 401;  // 非法访问
            case "4003":
                return 403;  // 没有权限
            case "4004":
                return 404;  // 资源不存在
            case "5001":
                return 400;  // 参数校验异常
            case "5104":
                return 401;  // 登录授权异常
            case "5105":
            case "5106":
                return 403;  // 没有访问权限
            case "5107":
                return 401;  // Token 解析异常
            case "5108":
                return 405;  // 请求方式不合法
            default:
                return 500;
        }
    }

    /**
     * HTTP 状态码映射为业务错误码
     */
    private int mapHttpStatusToCode(int status) {
        switch (status) {
            case 400:
                return 5001;
            case 401:
                return 4001;
            case 403:
                return 4003;
            case 404:
                return 4004;
            case 405:
                return 5108;
            case 429:
                return 5429;
            case 502:
                return 5502;
            case 503:
                return 5503;
            case 504:
                return 5504;
            default:
                return 5000;
        }
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
