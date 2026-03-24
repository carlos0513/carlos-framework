package com.carlos.gateway.config;

import com.carlos.core.exception.GlobalException;
import com.carlos.core.response.StatusCode;
import com.carlos.gateway.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 网关全局异常处理器
 * 统一处理网关层面的所有异常，返回标准化的 JSON 错误响应
 * </p>
 *
 * @author carlos
 * @date 2022/4/11 11:45
 * @updated 2026/3/24 重构优化，支持更多异常类型
 */
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * 是否开发模式（显示详细错误信息）
     */
    private boolean devMode = false;

    /**
     * 是否显示堆栈信息
     */
    private boolean showStackTrace = false;

    private final ObjectMapper objectMapper;

    public GatewayExceptionHandler() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public GatewayExceptionHandler(boolean devMode, boolean showStackTrace) {
        this();
        this.devMode = devMode;
        this.showStackTrace = showStackTrace;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // 如果响应已提交，无法修改
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethodValue();
        String requestId = exchange.getRequest().getId();

        // 记录异常日志
        logException(ex, path, method, requestId);

        // 构建错误响应
        ErrorResponse errorResponse = buildErrorResponse(ex, path, method, requestId);

        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.valueOf(errorResponse.getStatus()));

        // 写入响应体
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
                return bufferFactory.wrap(bytes);
            } catch (Exception e) {
                log.error("Failed to write error response", e);
                String fallback = "{\"success\":false,\"code\":500,\"message\":\"Internal Server Error\"}";
                return bufferFactory.wrap(fallback.getBytes(StandardCharsets.UTF_8));
            }
        }));
    }

    /**
     * 构建错误响应对象
     */
    private ErrorResponse buildErrorResponse(Throwable ex, String path, String method, String requestId) {
        ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
            .path(path)
            .method(method)
            .requestId(requestId);

        // 根据异常类型处理
        if (ex instanceof GatewayException) {
            handleGatewayException((GatewayException) ex, builder);
        } else if (ex instanceof GlobalException) {
            handleGlobalException((GlobalException) ex, builder);
        } else if (ex instanceof NotFoundException) {
            handleNotFoundException((NotFoundException) ex, builder);
        } else if (ex instanceof ResponseStatusException) {
            handleResponseStatusException((ResponseStatusException) ex, builder);
        } else if (ex instanceof WebClientResponseException) {
            handleWebClientResponseException((WebClientResponseException) ex, builder);
        } else if (ex instanceof TimeoutException) {
            handleTimeoutException(ex, builder);
        } else if (ex instanceof ConnectException) {
            handleConnectException(ex, builder);
        } else {
            handleGenericException(ex, builder);
        }

        // 开发模式添加额外信息
        if (devMode || showStackTrace) {
            builder.detail(ex.getMessage());
            if (showStackTrace) {
                builder.stack(getStackTraceString(ex));
            }
        }

        return builder.build();
    }

    /**
     * 处理网关特定异常
     */
    private void handleGatewayException(GatewayException ex, ErrorResponse.ErrorResponseBuilder builder) {
        builder.status(ex.getHttpStatus())
            .code(ex.getErrorCode())
            .message(ex.getMessage());

        // 添加特定异常的扩展信息
        Map<String, Object> extra = new HashMap<>();

        if (ex instanceof RateLimitException rateLimitEx) {
            extra.put("limitDimension", rateLimitEx.getLimitDimension());
            extra.put("limitRate", rateLimitEx.getLimitRate());
            extra.put("retryAfter", rateLimitEx.getRetryAfter());
        } else if (ex instanceof CircuitBreakerException cbEx) {
            extra.put("circuitBreakerName", cbEx.getCircuitBreakerName());
            extra.put("circuitBreakerState", cbEx.getCircuitBreakerState());
            extra.put("failureRate", cbEx.getFailureRate());
        } else if (ex instanceof AuthenticationException authEx) {
            extra.put("failureType", authEx.getFailureType().name());
        } else if (ex instanceof WafBlockException wafEx) {
            extra.put("ruleType", wafEx.getRuleType());
            extra.put("ruleName", wafEx.getRuleName());
        } else if (ex instanceof ServiceNotFoundException snfEx) {
            extra.put("serviceName", snfEx.getServiceName());
        } else if (ex instanceof RequestTimeoutException timeoutEx) {
            extra.put("timeoutMs", timeoutEx.getTimeoutMs());
            extra.put("targetService", timeoutEx.getTargetService());
        } else if (ex instanceof RequestValidationException validationEx) {
            extra.put("fieldErrors", validationEx.getFieldErrors());
        } else if (ex instanceof ReplayAttackException replayEx) {
            extra.put("attackType", replayEx.getAttackType().name());
            extra.put("requestId", replayEx.getRequestId());
        }

        if (!extra.isEmpty()) {
            builder.extra(extra);
        }
    }

    /**
     * 处理核心模块全局异常
     */
    private void handleGlobalException(GlobalException ex, ErrorResponse.ErrorResponseBuilder builder) {
        Integer errorCode = ex.getErrorCode();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        // 根据错误码映射 HTTP 状态
        if (errorCode != null) {
            httpStatus = switch (errorCode) {
                case 4001 -> HttpStatus.UNAUTHORIZED;      // 非法访问
                case 4003 -> HttpStatus.FORBIDDEN;         // 没有权限
                case 4004 -> HttpStatus.NOT_FOUND;         // 资源不存在
                case 5001 -> HttpStatus.BAD_REQUEST;       // 参数校验异常
                case 5104 -> HttpStatus.UNAUTHORIZED;      // 登录授权异常
                case 5105, 5106 -> HttpStatus.FORBIDDEN;   // 没有访问权限
                case 5107 -> HttpStatus.UNAUTHORIZED;      // Token 解析异常
                case 5108 -> HttpStatus.METHOD_NOT_ALLOWED;// 请求方式不合法
                default -> HttpStatus.INTERNAL_SERVER_ERROR;
            };
        }

        builder.status(httpStatus.value())
            .code(errorCode != null ? errorCode : StatusCode.FAIL.getCode())
            .message(ex.getMessage());
    }

    /**
     * 处理 NotFoundException（Spring Cloud Gateway）
     */
    private void handleNotFoundException(NotFoundException ex, ErrorResponse.ErrorResponseBuilder builder) {
        String message = "服务未找到";
        if (ex.getMessage() != null && ex.getMessage().contains("Unable to find instance")) {
            String serviceName = extractServiceName(ex.getMessage());
            message = String.format("服务 [%s] 暂不可用，请稍后重试", serviceName);
        }

        builder.status(HttpStatus.NOT_FOUND.value())
            .code(StatusCode.NOT_FOUND.getCode())
            .message(message);
    }

    /**
     * 处理 ResponseStatusException
     */
    private void handleResponseStatusException(ResponseStatusException ex, ErrorResponse.ErrorResponseBuilder builder) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message = ex.getReason();
        if (message == null) {
            message = status.getReasonPhrase();
        }

        builder.status(status.value())
            .code(mapHttpStatusToCode(status))
            .message(message);
    }

    /**
     * 处理 WebClientResponseException（下游服务返回错误）
     */
    private void handleWebClientResponseException(WebClientResponseException ex, ErrorResponse.ErrorResponseBuilder builder) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }

        String message = String.format("下游服务返回错误 [%d]: %s", status.value(), status.getReasonPhrase());

        builder.status(status.value())
            .code(mapHttpStatusToCode(status))
            .message(message);

        // 尝试解析下游服务的错误响应
        try {
            String responseBody = ex.getResponseBodyAsString();
            if (responseBody != null && !responseBody.isEmpty()) {
                Map<String, Object> extra = new HashMap<>();
                extra.put("upstreamResponse", responseBody);
                builder.extra(extra);
            }
        } catch (Exception ignored) {
            // 忽略解析失败
        }
    }

    /**
     * 处理超时异常
     */
    private void handleTimeoutException(Throwable ex, ErrorResponse.ErrorResponseBuilder builder) {
        builder.status(HttpStatus.GATEWAY_TIMEOUT.value())
            .code(5504)
            .message("请求超时，请稍后重试");
    }

    /**
     * 处理连接异常
     */
    private void handleConnectException(Throwable ex, ErrorResponse.ErrorResponseBuilder builder) {
        builder.status(HttpStatus.BAD_GATEWAY.value())
            .code(5502)
            .message("服务连接失败，请稍后重试");
    }

    /**
     * 处理通用异常
     */
    private void handleGenericException(Throwable ex, ErrorResponse.ErrorResponseBuilder builder) {
        builder.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(StatusCode.FAIL.getCode())
            .message("服务暂不可用，请稍后重试");
    }

    /**
     * 记录异常日志
     */
    private void logException(Throwable ex, String path, String method, String requestId) {
        String logMessage = String.format("[%s] %s %s - %s: %s",
            requestId, method, path, ex.getClass().getSimpleName(), ex.getMessage());

        // 根据异常类型调整日志级别
        if (ex instanceof GatewayException
            || ex instanceof NotFoundException
            || ex instanceof WebClientResponseException) {
            // 业务异常或已知异常，记录为 WARN
            log.warn(logMessage);
        } else if (ex instanceof ResponseStatusException
            && ((ResponseStatusException) ex).getStatusCode().is4xxClientError()) {
            // 客户端错误，记录为 WARN
            log.warn(logMessage);
        } else {
            // 其他异常，记录为 ERROR 并打印堆栈
            log.error(logMessage, ex);
        }
    }

    /**
     * 从 NotFoundException 消息中提取服务名
     */
    private String extractServiceName(String message) {
        try {
            // 格式: "Unable to find instance for xxx"
            if (message.contains("Unable to find instance for ")) {
                return message.substring(message.lastIndexOf(" ") + 1);
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }

    /**
     * HTTP 状态码映射为业务错误码
     */
    private int mapHttpStatusToCode(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> StatusCode.PARAMETER_EXCEPTION.getCode();
            case UNAUTHORIZED -> StatusCode.AUTHENTICATION_EXCEPTION.getCode();
            case FORBIDDEN -> StatusCode.UNAUTHORIZED_EXCEPTION.getCode();
            case NOT_FOUND -> StatusCode.NOT_FOUND.getCode();
            case METHOD_NOT_ALLOWED -> StatusCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION.getCode();
            case TOO_MANY_REQUESTS -> 5429;
            case INTERNAL_SERVER_ERROR -> StatusCode.FAIL.getCode();
            case BAD_GATEWAY -> 5502;
            case SERVICE_UNAVAILABLE -> 5503;
            case GATEWAY_TIMEOUT -> 5504;
            default -> StatusCode.FAIL.getCode();
        };
    }

    /**
     * 获取异常堆栈字符串
     */
    private String getStackTraceString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\n");
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
            // 限制堆栈深度
            if (sb.length() > 5000) {
                sb.append("\t... (truncated)");
                break;
            }
        }
        return sb.toString();
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void setShowStackTrace(boolean showStackTrace) {
        this.showStackTrace = showStackTrace;
    }
}
