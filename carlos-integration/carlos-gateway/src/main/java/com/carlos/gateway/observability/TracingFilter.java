package com.carlos.gateway.observability;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * <p>
 * 分布式链路追踪过滤器
 * 基于 Micrometer Tracing（兼容 Brave/OpenTelemetry）
 * </p>
 * <p>
 * 注意：此过滤器已被 {@link RequestTracingFilter} 替代，请使用新的过滤器。
 * 新过滤器同时处理了 Request ID 和 Trace ID。
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 * @deprecated 请使用 {@link RequestTracingFilter}
 */
@Slf4j
@Component
@Deprecated
@ConditionalOnProperty(prefix = "carlos.gateway.tracing", name = "enabled", havingValue = "false")
public class TracingFilter implements GlobalFilter, Ordered {

    private final Tracer tracer;
    private final Propagator propagator;
    private final TracingProperties properties;

    public TracingFilter(Tracer tracer, Propagator propagator, TracingProperties properties) {
        this.tracer = tracer;
        this.propagator = propagator;
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();

        // 从入站请求中提取 Trace 上下文
        Span.Builder spanBuilder = propagator.extract(request, (carrier, key) -> {
            return carrier.getHeaders().getFirst(key);
        });

        // 创建新的 Span 或使用已有的 Span
        Span span = Optional.ofNullable(spanBuilder)
            .map(builder -> builder.name("gateway.request").start())
            .orElseGet(() -> tracer.nextSpan().name("gateway.request").start());

        // 将 Span 放入当前上下文
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            // 添加标签
            tagRequest(span, request);

            // 将 Trace ID 添加到响应头
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set(com.carlos.core.constant.HttpHeadersConstant.X_TRACE_ID, span.context().traceId());
            response.getHeaders().set(com.carlos.core.constant.HttpHeadersConstant.X_SPAN_ID, span.context().spanId());

            // 将追踪上下文注入到出站请求
            ServerHttpRequest mutatedRequest = injectTracingContext(request, span);
            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

            return chain.filter(mutatedExchange)
                .doFinally(signalType -> {
                    // 记录响应信息
                    tagResponse(span, exchange.getResponse());
                    span.end();
                })
                .onErrorResume(throwable -> {
                    span.error(throwable);
                    span.end();
                    return Mono.error(throwable);
                });
        }
    }

    /**
     * 标记请求信息
     */
    private void tagRequest(Span span, ServerHttpRequest request) {
        span.tag("http.method", request.getMethod().name());
        span.tag("http.url", request.getURI().toString());
        span.tag("http.path", request.getURI().getPath());
        span.tag("http.host", request.getURI().getHost());

        // 客户端 IP
        String clientIp = Optional.ofNullable(request.getRemoteAddress())
            .map(addr -> addr.getAddress().getHostAddress())
            .orElse("unknown");
        span.tag("http.client_ip", clientIp);

        // 用户 ID（如果已认证）
        String userId = request.getHeaders().getFirst("X-User-Id");
        if (userId != null) {
            span.tag("user.id", userId);
        }

        // 添加事件
        span.event("request.received");
    }

    /**
     * 标记响应信息
     */
    private void tagResponse(Span span, ServerHttpResponse response) {
        span.tag("http.status_code", String.valueOf(
            response.getStatusCode() != null ? response.getStatusCode().value() : 0));
        span.event("response.sent");
    }

    /**
     * 将追踪上下文注入到出站请求
     */
    private ServerHttpRequest injectTracingContext(ServerHttpRequest request, Span span) {
        ServerHttpRequest.Builder builder = request.mutate();
        propagator.inject(span.context(), builder, (b, key, value) -> b.header(key, value));
        return builder.build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100; // 最先执行
    }
}
