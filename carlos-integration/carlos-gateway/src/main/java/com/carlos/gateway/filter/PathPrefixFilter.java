package com.carlos.gateway.filter;

import com.carlos.core.util.PathMatchUtil;
import com.carlos.gateway.config.GatewayProperties;
import com.carlos.gateway.config.GlobalFilterOrder;
import com.carlos.gateway.whitelist.WhitelistContext;
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
 * 网关接口路径统一前缀处理器（GlobalFilter 实现）- 优化版
 * <br>
 * 功能说明：
 * <ul>
 *   <li>所有 API 请求必须以配置的 prefix 开头（如 /api/v1）</li>
 *   <li>网关接收到请求后，截掉 prefix，再将请求转发给后端服务</li>
 *   <li>白名单中的路径（如 Swagger 资源）不受此前缀限制</li>
 * </ul>
 * <br>
 * 优化点：
 * <ul>
 *   <li>优先使用统一白名单检查结果（UnifiedWhitelistFilter）</li>
 *   <li>避免重复进行路径匹配</li>
 *   <li>降级兼容：统一白名单未启用时，使用本地白名单</li>
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
 *   <li>在统一白名单过滤器之后执行（UnifiedWhitelistFilter = HIGHEST_PRECEDENCE + 100）</li>
 *   <li>在认证授权过滤器之前执行（先校验路径格式）</li>
 *   <li>在负载均衡过滤器之前执行（路径重写后路由）</li>
 * </ul>
 * </p>
 *
 * @author Carlos
 * @date 2025-01-10 11:19
 * @updated 2026-04-10 优化：使用统一白名单
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
     * 1. 在统一白名单过滤器之后（HIGHEST_PRECEDENCE + 100）
     * 2. 在认证授权过滤器之前（通常为 ORDER_SECOND 2000 或更高）
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

        // 1. 检查路径是否在白名单中（优先使用统一白名单检查结果）
        if (isWhitelisted(exchange, path)) {
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
     * 检查路径是否在白名单中（优化版）
     * <p>
     * 优先使用统一白名单检查结果，如果统一白名单未启用，则使用本地白名单
     *
     * @param exchange ServerWebExchange
     * @param path     请求路径
     * @return true 如果在白名单中
     */
    private boolean isWhitelisted(ServerWebExchange exchange, String path) {
        // 1. 优先使用统一白名单检查结果
        if (WhitelistContext.isPrefixStripWhitelisted(exchange)) {
            return true;
        }

        // 2. 降级兼容：统一白名单未启用或结果不存在，使用本地白名单
        Set<String> whitelist = gatewayProperties.getWhitelist();
        if (whitelist == null || whitelist.isEmpty()) {
            return false;
        }

        // 使用 PathMatchUtil 进行 Ant 风格匹配
        return PathMatchUtil.antMatchAny(whitelist, path);
    }
}
