package com.carlos.gateway.filter;

import com.carlos.gateway.config.GatewayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 *   网关接口路径统一前缀，给所有的接口地址添加统一前缀，允许前缀为空
 *   需要注意顺序
 * </p>
 *
 * @author Carlos
 * @date 2025-01-10 11:19
 */
@RequiredArgsConstructor
public class PathPrefixFilter implements WebFilter {

    private final GatewayProperties gatewayProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String apiPrefix = gatewayProperties.getPrefix();
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getRawPath();
        if (!path.contains(apiPrefix)) {
            return chain.filter(exchange);
        }
        // 存在前缀的情况下 截取（适配原有前端请求，不需要更改代码）
        String newPath = path.replaceFirst(apiPrefix, "");
        ServerHttpRequest newRequest = request.mutate().path(newPath).build();

        return chain.filter(exchange.mutate().request(newRequest).build());
    }
}