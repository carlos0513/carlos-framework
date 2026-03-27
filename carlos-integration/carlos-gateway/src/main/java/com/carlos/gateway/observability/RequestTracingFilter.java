package com.carlos.gateway.observability;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import cn.hutool.core.util.IdUtil;
import com.carlos.core.constant.HttpHeadersConstant;
import io.micrometer.tracing.propagation.Propagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Optional;

/**
 * Gateway 统一链路追踪过滤器
 * <p>
 * 整合 Request ID 和 Trace ID 处理：
 * 1. 从请求头提取或生成 Request ID（业务追踪）
 * 2. 从请求头提取或生成 Trace ID（分布式链路追踪）
 * 3. 将 Request ID 和 Trace ID 传递到下游服务
 * 4. 在响应头中返回这两个 ID
 *
 * @author Carlos
 * @date 2025-03-26
 */
@Slf4j
public class RequestTracingFilter implements GlobalFilter, Ordered {


    private final Tracer tracer;
    private final Propagator propagator;
    private final TracingProperties properties;

    public RequestTracingFilter(Tracer tracer, Propagator propagator, TracingProperties properties) {
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

        // 1. 处理 Request ID（业务追踪）
        String requestId = extractOrGenerateRequestId(request);

        // 2. 处理 Trace ID（分布式链路追踪）
        Span span = createOrContinueSpan(request, requestId);

        // 3. 构建新的请求（注入追踪信息）
        ServerHttpRequest mutatedRequest = buildMutatedRequest(request, span, requestId);
        ServerHttpResponse response = exchange.getResponse();

        // 4. 设置响应头
        response.getHeaders().set(HttpHeadersConstant.X_REQUEST_ID, requestId);
        response.getHeaders().set(HttpHeadersConstant.X_TRACE_ID, span.context().traceIdString());
        response.getHeaders().set(HttpHeadersConstant.X_SPAN_ID, span.context().spanIdString());

        // 5. 构建新的 Exchange
        ServerWebExchange mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .build();

        // 6. 将追踪信息放入 Reactor Context，供后续使用
        return chain.filter(mutatedExchange)
            .contextWrite(Context.of(
                "requestId", requestId,
                "traceId", span.context().traceIdString(),
                "spanId", span.context().spanIdString()
            ))
            .doFinally(signalType -> {
                // 记录响应信息
                tagResponse(span, exchange.getResponse());
                span.finish();
                log.debug("[Gateway Tracing] 请求完成 - requestId: {}, traceId: {}",
                    requestId, span.context().traceIdString());
            })
            .onErrorResume(throwable -> {
                span.error(throwable);
                span.finish();
                return Mono.error(throwable);
            });
    }

    /**
     * 从请求头提取或生成 Request ID
     */
    private String extractOrGenerateRequestId(ServerHttpRequest request) {
        // 1. 尝试从请求头获取
        String requestId = request.getHeaders().getFirst(HttpHeadersConstant.X_REQUEST_ID);

        // 2. 如果没有，尝试从小写 header 获取（HTTP 头不区分大小写，但某些代理可能有问题）
        if (requestId == null) {
            requestId = request.getHeaders().getFirst("x-request-id");
        }

        // 3. 如果还没有，生成新的
        if (requestId == null || requestId.isEmpty()) {
            requestId = IdUtil.fastSimpleUUID();
            log.debug("[Gateway Tracing] 生成新的 Request ID: {}", requestId);
        } else {
            log.debug("[Gateway Tracing] 使用传入的 Request ID: {}", requestId);
        }

        return requestId;
    }


    /**
     * 创建新的 Span 或继续已有的追踪
     */
    private Span createOrContinueSpan(ServerHttpRequest request, String requestId) {
        // 尝试从 B3 头提取 Trace 上下文
        TraceContextOrSamplingFlags extracted = extractB3Context(request);

        Span span;
        if (extracted != null) {
            // 继续已有追踪
            span = tracer.nextSpan(extracted);
            log.debug("[Gateway Tracing] 继续已有追踪: {}", span.context().traceIdString());
        } else {
            // 创建新的追踪
            span = tracer.nextSpan();
            log.debug("[Gateway Tracing] 创建新的追踪: {}", span.context().traceIdString());
        }

        // 设置 Span 名称和标签
        span.name("gateway.request")
            .tag("http.method", request.getMethod().name())
            .tag("http.url", request.getURI().toString())
            .tag("http.path", request.getURI().getPath())
            .tag("request.id", requestId)
            .start();

        // 添加客户端 IP
        Optional.ofNullable(request.getRemoteAddress())
            .map(addr -> addr.getAddress().getHostAddress())
            .ifPresent(ip -> span.tag("http.client_ip", ip));

        // 添加用户 ID（如果已认证）
        Optional.ofNullable(request.getHeaders().getFirst("X-User-Id"))
            .ifPresent(userId -> span.tag("user.id", userId));

        return span;
    }

    /**
     * 从 B3 头提取 Trace 上下文
     */
    private TraceContextOrSamplingFlags extractB3Context(ServerHttpRequest request) {
        try {
            // 尝试单头格式: b3=traceId-spanId-sampled
            String b3Header = request.getHeaders().getFirst(HttpHeadersConstant.B3);
            if (b3Header != null && !b3Header.isEmpty()) {
                String[] parts = b3Header.split("-");
                if (parts.length >= 2) {
                    TraceContext context = buildTraceContext(parts[0], parts[1],
                        parts.length > 2 ? parts[2] : null);
                    if (context != null) {
                        return TraceContextOrSamplingFlags.create(context);
                    }
                }
            }

            // 尝试多头部格式
            String traceId = request.getHeaders().getFirst(HttpHeadersConstant.X_B3_TRACE_ID);
            String spanId = request.getHeaders().getFirst(HttpHeadersConstant.X_B3_SPAN_ID);
            String sampled = request.getHeaders().getFirst(HttpHeadersConstant.X_B3_SAMPLED);

            if (traceId != null && spanId != null) {
                TraceContext context = buildTraceContext(traceId, spanId, sampled);
                if (context != null) {
                    return TraceContextOrSamplingFlags.create(context);
                }
            }
        } catch (Exception e) {
            log.warn("[Gateway Tracing] 解析 B3 头失败: {}", e.getMessage());
        }

        return null;
    }

    /**
     * 构建 TraceContext
     */
    private TraceContext buildTraceContext(String traceIdHex, String spanIdHex, String sampled) {
        try {
            // 将 16 进制字符串转换为 long
            long traceIdHigh = 0L;
            long traceIdLow;

            // TraceId 可能是 16 位或 32 位 16 进制
            if (traceIdHex.length() == 32) {
                // 128-bit traceId: high-low
                traceIdHigh = Long.parseUnsignedLong(traceIdHex.substring(0, 16), 16);
                traceIdLow = Long.parseUnsignedLong(traceIdHex.substring(16), 16);
            } else {
                // 64-bit traceId
                traceIdLow = Long.parseUnsignedLong(traceIdHex, 16);
            }

            long spanId = Long.parseUnsignedLong(spanIdHex, 16);
            Boolean sampledFlag = sampled != null ? "1".equals(sampled) : null;

            TraceContext.Builder builder = TraceContext.newBuilder()
                .traceIdHigh(traceIdHigh)
                .traceId(traceIdLow)
                .spanId(spanId);

            if (sampledFlag != null) {
                builder.sampled(sampledFlag);
            }

            return builder.build();
        } catch (NumberFormatException e) {
            log.warn("[Gateway Tracing] 解析 Trace ID 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 构建修改后的请求（注入追踪信息到下游）
     */
    private ServerHttpRequest buildMutatedRequest(ServerHttpRequest request, Span span, String requestId) {
        ServerHttpRequest.Builder builder = request.mutate();

        // 1. 注入 Request ID
        builder.header(HttpHeadersConstant.X_REQUEST_ID, requestId);

        // 2. 注入 B3 追踪头（供下游服务使用）
        TraceContext context = span.context();
        builder.header(HttpHeadersConstant.B3, String.format("%s-%s-1",
            context.traceIdString(),
            context.spanIdString()));

        // 3. 注入多头部格式（兼容旧版）
        builder.header(HttpHeadersConstant.X_B3_TRACE_ID, context.traceIdString());
        builder.header(HttpHeadersConstant.X_B3_SPAN_ID, context.spanIdString());
        builder.header(HttpHeadersConstant.X_B3_SAMPLED, "1");

        // 4. 注入 SkyWalking 上下文（如果可用）
        try {
            String sw8 = request.getHeaders().getFirst("sw8");
            if (sw8 != null) {
                builder.header("sw8", sw8);
            }
        } catch (Exception e) {
            // SkyWalking 可能未启用
        }

        return builder.build();
    }

    /**
     * 记录响应信息到 Span
     */
    private void tagResponse(Span span, ServerHttpResponse response) {
        if (response.getStatusCode() != null) {
            span.tag("http.status_code", String.valueOf(response.getStatusCode().value()));
        }
    }

    @Override
    public int getOrder() {
        // 确保最先执行，优先级高于其他过滤器
        return Ordered.HIGHEST_PRECEDENCE + 50;
    }
}
