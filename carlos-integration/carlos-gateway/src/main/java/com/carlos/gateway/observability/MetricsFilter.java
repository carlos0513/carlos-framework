package com.carlos.gateway.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 指标收集过滤器
 * 基于 Micrometer 实现 Prometheus 指标暴露
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
@Component
public class MetricsFilter implements GlobalFilter, Ordered {

    private final MeterRegistry meterRegistry;

    // 指标名称
    private static final String REQUEST_COUNTER = "gateway.requests.total";
    private static final String REQUEST_LATENCY = "gateway.requests.duration";
    private static final String ACTIVE_REQUESTS = "gateway.requests.active";

    public MetricsFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant start = Instant.now();
        String routeId = Optional.ofNullable(exchange.getAttribute(
                org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_ROUTE_ATTR))
            .map(Object::toString)
            .orElse("unknown");

        // 活跃请求数增加
        meterRegistry.gauge(ACTIVE_REQUESTS, 1);

        return chain.filter(exchange)
            .doFinally(signalType -> {
                long durationMs = Duration.between(start, Instant.now()).toMillis();
                recordMetrics(exchange, routeId, durationMs);
            });
    }

    /**
     * 记录指标
     */
    private void recordMetrics(ServerWebExchange exchange, String routeId, long durationMs) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        HttpStatus statusCode = exchange.getResponse().getStatusCode();
        String status = statusCode != null ? String.valueOf(statusCode.value()) : "unknown";

        // 记录请求计数
        Counter.builder(REQUEST_COUNTER)
            .tag("route", routeId)
            .tag("path", path)
            .tag("method", method)
            .tag("status", status)
            .description("Total number of gateway requests")
            .register(meterRegistry)
            .increment();

        // 记录请求延迟
        Timer.builder(REQUEST_LATENCY)
            .tag("route", routeId)
            .tag("path", path)
            .tag("method", method)
            .tag("status", status)
            .description("Gateway request duration")
            .register(meterRegistry)
            .record(durationMs, TimeUnit.MILLISECONDS);

        // 根据状态码分类
        if (statusCode != null) {
            if (statusCode.is2xxSuccessful()) {
                Counter.builder("gateway.requests.success")
                    .tag("route", routeId)
                    .register(meterRegistry)
                    .increment();
            } else if (statusCode.is4xxClientError()) {
                Counter.builder("gateway.requests.client_error")
                    .tag("route", routeId)
                    .tag("status", status)
                    .register(meterRegistry)
                    .increment();
            } else if (statusCode.is5xxServerError()) {
                Counter.builder("gateway.requests.server_error")
                    .tag("route", routeId)
                    .tag("status", status)
                    .register(meterRegistry)
                    .increment();
            }
        }

        // 记录特定错误
        if (statusCode == HttpStatus.TOO_MANY_REQUESTS) {
            Counter.builder("gateway.requests.rate_limited")
                .tag("route", routeId)
                .register(meterRegistry)
                .increment();
        }

        if (statusCode == HttpStatus.FORBIDDEN) {
            Counter.builder("gateway.requests.blocked")
                .tag("route", routeId)
                .register(meterRegistry)
                .increment();
        }

        // 活跃请求数减少
        meterRegistry.gauge(ACTIVE_REQUESTS, 0);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200; // 在追踪过滤器之后
    }
}
