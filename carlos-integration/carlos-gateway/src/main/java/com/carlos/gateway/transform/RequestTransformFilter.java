package com.carlos.gateway.transform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * 请求转换过滤器
 * 支持：API 版本控制、路径重写、Header 转换
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
@Component
public class RequestTransformFilter implements GlobalFilter, Ordered {

    private final TransformProperties properties;

    // API 版本模式匹配
    private static final Pattern VERSION_PATTERN = Pattern.compile("/v(\\d+)/");

    public RequestTransformFilter(TransformProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest mutatedRequest = request;

        // 1. API 版本控制
        if (properties.isApiVersionEnabled()) {
            mutatedRequest = handleApiVersion(mutatedRequest);
        }

        // 2. 路径重写
        if (properties.getPathRewrites() != null) {
            mutatedRequest = rewritePath(mutatedRequest);
        }

        // 3. Header 转换
        if (properties.getHeaderTransforms() != null) {
            mutatedRequest = transformHeaders(mutatedRequest);
        }

        // 4. 添加标准 Header
        mutatedRequest = addStandardHeaders(mutatedRequest);

        // 如果请求被修改，创建新的 exchange
        if (mutatedRequest != request) {
            exchange = exchange.mutate().request(mutatedRequest).build();
        }

        return chain.filter(exchange);
    }

    /**
     * 处理 API 版本
     */
    private ServerHttpRequest handleApiVersion(ServerHttpRequest request) {
        String path = request.getURI().getPath();

        // 从 Header 中获取版本
        String versionHeader = request.getHeaders().getFirst("X-API-Version");
        if (versionHeader != null) {
            return request.mutate()
                .header("X-Routed-Version", versionHeader)
                .build();
        }

        // 从 URL 路径中提取版本
        java.util.regex.Matcher matcher = VERSION_PATTERN.matcher(path);
        if (matcher.find()) {
            String version = matcher.group(1);
            return request.mutate()
                .header("X-Routed-Version", version)
                .build();
        }

        return request;
    }

    /**
     * 重写路径
     */
    private ServerHttpRequest rewritePath(ServerHttpRequest request) {
        String path = request.getURI().getPath();

        for (Map.Entry<String, String> entry : properties.getPathRewrites().entrySet()) {
            if (path.matches(entry.getKey())) {
                String newPath = path.replaceAll(entry.getKey(), entry.getValue());
                log.debug("Path rewritten: {} -> {}", path, newPath);
                return request.mutate().path(newPath).build();
            }
        }

        return request;
    }

    /**
     * 转换 Header
     */
    private ServerHttpRequest transformHeaders(ServerHttpRequest request) {
        ServerHttpRequest.Builder builder = request.mutate();

        for (TransformProperties.HeaderTransform transform : properties.getHeaderTransforms()) {
            String value = request.getHeaders().getFirst(transform.getSource());
            if (value != null) {
                switch (transform.getOperation()) {
                    case ADD:
                        builder.header(transform.getTarget(), value);
                        break;
                    case REPLACE:
                        builder.header(transform.getTarget(), transformValue(value, transform));
                        if (!transform.getSource().equals(transform.getTarget())) {
                            // 移除原 Header
                            builder.headers(headers -> headers.remove(transform.getSource()));
                        }
                        break;
                    case REMOVE:
                        builder.headers(headers -> headers.remove(transform.getSource()));
                        break;
                }
            }
        }

        return builder.build();
    }

    /**
     * 转换值
     */
    private String transformValue(String value, TransformProperties.HeaderTransform transform) {
        if (transform.getPrefix() != null) {
            value = transform.getPrefix() + value;
        }
        if (transform.getSuffix() != null) {
            value = value + transform.getSuffix();
        }
        if (transform.getMapping() != null && transform.getMapping().containsKey(value)) {
            value = transform.getMapping().get(value);
        }
        return value;
    }

    /**
     * 添加标准 Header
     */
    private ServerHttpRequest addStandardHeaders(ServerHttpRequest request) {
        ServerHttpRequest.Builder builder = request.mutate();

        // 添加网关标识
        builder.header("X-Gateway-Id", properties.getGatewayId());

        // 添加转发时间戳
        builder.header("X-Gateway-Timestamp", String.valueOf(System.currentTimeMillis()));

        // 如果客户端没有传递 Request-ID，生成一个
        if (request.getHeaders().getFirst(com.carlos.core.constant.HttpHeadersConstant.X_REQUEST_ID) == null) {
            builder.header(com.carlos.core.constant.HttpHeadersConstant.X_REQUEST_ID,
                java.util.UUID.randomUUID().toString().replace("-", ""));
        }

        return builder.build();
    }

    @Override
    public int getOrder() {
        return 500; // 在认证之后，路由之前
    }
}
