package com.carlos.gateway.observability;

import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.gateway.config.GlobalFilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Gateway 访问日志过滤器
 * <p>
 * 记录所有经过 Gateway 的请求信息，包含：
 * - Request ID
 * - Trace ID
 * - 请求方法、路径
 * - 响应状态码
 * - 耗时
 *
 * @author Carlos
 * @date 2025-03-26
 */
@Slf4j
public class AccessLogFilter implements GlobalFilter, Ordered {

    /**
     * 访问日志 Logger,gateway.access与logback的logname对应
     */
    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("gateway.access");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant startTime = Instant.now();
        ServerHttpRequest request = exchange.getRequest();

        // 获取追踪信息
        String requestId = request.getHeaders().getFirst(HttpHeadersConstant.X_REQUEST_ID);
        String traceId = request.getHeaders().getFirst(HttpHeadersConstant.X_TRACE_ID);

        // 记录请求开始
        log.debug("[Gateway Access] 请求开始 - method: {}, path: {}, requestId: {}, traceId: {}",
            request.getMethod(), request.getURI().getPath(), requestId, traceId);

        return chain.filter(exchange)
            .doFinally(signalType -> {
                // 计算耗时
                long duration = Duration.between(startTime, Instant.now()).toMillis();
                ServerHttpResponse response = exchange.getResponse();

                // 记录访问日志
                recordAccessLog(request, response, requestId, traceId, duration);
            });
    }

    /**
     * 记录访问日志
     */
    private void recordAccessLog(ServerHttpRequest request, ServerHttpResponse response,
                                 String requestId, String traceId, long duration) {
        String method = request.getMethod().name();
        String path = request.getURI().getPath();
        String query = Optional.ofNullable(request.getURI().getQuery()).map(q -> "?" + q).orElse("");
        int status = response.getStatusCode() != null ? response.getStatusCode().value() : 0;

        // 格式：method|path|status|duration|requestId|traceId|clientIp
        String clientIp = Optional.ofNullable(request.getRemoteAddress())
            .map(addr -> addr.getAddress().getHostAddress())
            .orElse("-");

        String logMessage = "%s|%s%s|%d|%d|%s|%s|%s".formatted(
            method, path, query, status, duration,
            requestId != null ? requestId : "-",
            traceId != null ? traceId : "-",
            clientIp);

        // 使用独立的 Logger 输出到 access 日志文件
        ACCESS_LOG.info(logMessage);

        // 同时输出到主日志
        if (status >= 500) {
            log.error("[Gateway Access] {} {} {} - {}ms, requestId: {}, traceId: {}",
                method, path, status, duration, requestId, traceId);
        } else if (status >= 400) {
            log.warn("[Gateway Access] {} {} {} - {}ms, requestId: {}, traceId: {}",
                method, path, status, duration, requestId, traceId);
        } else {
            log.debug("[Gateway Access] {} {} {} - {}ms, requestId: {}, traceId: {}",
                method, path, status, duration, requestId, traceId);
        }
    }

    @Override
    public int getOrder() {
        // 访问日志过滤器应该最后执行，记录完整请求信息
        return GlobalFilterOrder.ACCESS_LOG_ORDER;
    }
}
