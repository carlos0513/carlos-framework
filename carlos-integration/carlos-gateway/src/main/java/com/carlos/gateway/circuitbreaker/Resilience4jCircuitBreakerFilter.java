package com.carlos.gateway.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 熔断降级过滤器（基于 Resilience4j）
 * 支持服务级别的熔断保护
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
@Component
public class Resilience4jCircuitBreakerFilter extends
    AbstractGatewayFilterFactory<Resilience4jCircuitBreakerFilter.Config> {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final ConcurrentHashMap<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();

    public Resilience4jCircuitBreakerFilter() {
        super(Config.class);
        // 默认配置
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)                    // 失败率阈值
            .slowCallRateThreshold(80)                   // 慢调用阈值
            .slowCallDurationThreshold(Duration.ofSeconds(2))  // 慢调用时间阈值
            .permittedNumberOfCallsInHalfOpenState(10)   // 半开状态允许的调用数
            .slidingWindowSize(100)                      // 滑动窗口大小
            .minimumNumberOfCalls(10)                    // 最小调用数
            .waitDurationInOpenState(Duration.ofSeconds(30))  // 熔断持续时间
            .automaticTransitionFromOpenToHalfOpenEnabled(true)  // 自动转换
            .recordException(throwable -> {
                // 记录哪些异常算失败
                if (throwable instanceof java.util.concurrent.TimeoutException) {
                    return true;
                }
                if (throwable instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                    int statusCode = ((org.springframework.web.reactive.function.client.WebClientResponseException) throwable)
                        .getStatusCode().value();
                    return statusCode >= 500;
                }
                return throwable instanceof Exception;
            })
            .build();

        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(defaultConfig);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String routeId = exchange.getAttribute(org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_ROUTE_ATTR);
            String circuitBreakerName = config.getName() != null ? config.getName() : (routeId != null ? routeId : "default");

            CircuitBreaker circuitBreaker = circuitBreakers.computeIfAbsent(circuitBreakerName,
                name -> circuitBreakerRegistry.circuitBreaker(name));

            return chain.filter(exchange)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .onErrorResume(throwable -> {
                    log.error("Circuit breaker {} triggered for error: {}",
                        circuitBreakerName, throwable.getMessage());

                    // 触发熔断后的降级处理
                    if (config.getFallbackUri() != null) {
                        // 转发到降级接口
                        exchange.getAttributes().put(
                            org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR,
                            config.getFallbackUri()
                        );
                        return Mono.empty();
                    } else {
                        // 返回默认降级响应
                        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                        return exchange.getResponse().writeWith(
                            Mono.just(exchange.getResponse().bufferFactory().wrap(
                                "{\"code\":503,\"message\":\"Service temporarily unavailable\"}"
                                    .getBytes()
                            ))
                        );
                    }
                });
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name", "fallbackUri");
    }

    public static class Config {

        private String name;           // 熔断器名称
        private String fallbackUri;    // 降级接口地址

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFallbackUri() {
            return fallbackUri;
        }

        public void setFallbackUri(String fallbackUri) {
            this.fallbackUri = fallbackUri;
        }
    }
}
