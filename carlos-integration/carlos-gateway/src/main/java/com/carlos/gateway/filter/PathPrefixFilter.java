package com.carlos.gateway.filter;

import com.carlos.core.util.PathMatchUtil;
import com.carlos.gateway.config.GatewayProperties;
import com.carlos.gateway.config.GlobalFilterOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * <p>
 * 网关接口路径统一前缀处理器（GlobalFilter 实现）
 * <br>
 * 功能说明：
 * <ul>
 *   <li>所有 API 请求必须以配置的 prefix 开头（如 /api/v1）</li>
 *   <li>网关接收到请求后，截掉 prefix，再将请求转发给后端服务</li>
 *   <li>白名单中的路径（如 Swagger 资源）不受此前缀限制</li>
 * </ul>
 * <br>
 * 示例：
 * <pre>
 *   外部请求：/api/v1/org/users
 *   转发到 carlos-org 服务：/org/users
 * </pre>
 * </p>
 * <p>
 * 执行顺序说明：
 * <ul>
 *   <li>在链路追踪过滤器之后执行（确保日志能记录原始路径）</li>
 *   <li>在认证授权过滤器之前执行（先校验路径格式）</li>
 *   <li>在负载均衡过滤器之前执行（路径重写后路由）</li>
 * </ul>
 * </p>
 *
 * @author Carlos
 * @date 2025-01-10 11:19
 * @see GlobalFilterOrder
 */
@Slf4j
@RequiredArgsConstructor
public class PathPrefixFilter implements GlobalFilter, Ordered {

    private final GatewayProperties gatewayProperties;

    /**
     * 过滤器执行顺序
     * <p>
     * 设置为 ORDER_FIRST (1000)，确保：
     * 1. 在链路追踪之后（TRACING_FILTER_ORDER = HIGHEST_PRECEDENCE + 50）
     * 2. 在认证授权之前（通常为 ORDER_SECOND 2000 或更高）
     * 3. 在负载均衡之前（RouteToRequestUrlFilter = 10000）
     */
    @Override
    public int getOrder() {
        return GlobalFilterOrder.ORDER_FIRST;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 如果前缀校验未启用，直接放行
        if (!gatewayProperties.isPrefixEnabled()) {
            return chain.filter(exchange);
        }

        String apiPrefix = gatewayProperties.getPrefix();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getRawPath();

        // 1. 检查路径是否在白名单中
        if (isWhitelisted(path)) {
            log.debug("Path [{}] is whitelisted, skipping prefix check", path);
            return chain.filter(exchange);
        }

        // 2. 检查请求路径是否以配置的前缀开头
        if (!path.startsWith(apiPrefix)) {
            // 路径不以前缀开头，记录日志但仍放行（可根据需求改为返回错误）
            log.warn("Request path [{}] does not start with required prefix [{}]", path, apiPrefix);
            // 如果需要强制前缀，可以在这里返回错误
            // return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
            //     "Path must start with prefix: " + apiPrefix));
            return chain.filter(exchange);
        }

        // 3. 截取前缀，构造新的请求路径
        String newPath = path.substring(apiPrefix.length());
        // 确保新路径以 / 开头
        if (!newPath.startsWith("/")) {
            newPath = "/" + newPath;
        }

        log.debug("Path rewritten: {} -> {}", path, newPath);

        ServerHttpRequest newRequest = request.mutate()
            .path(newPath)
            .build();

        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    /**
     * 检查路径是否在白名单中
     *
     * @param path 请求路径
     * @return true 如果在白名单中
     */
    private boolean isWhitelisted(String path) {
        Set<String> whitelist = gatewayProperties.getWhitelist();
        if (whitelist == null || whitelist.isEmpty()) {
            return false;
        }
        for (String pattern : whitelist) {
            if (PathMatchUtil.antMatch(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
