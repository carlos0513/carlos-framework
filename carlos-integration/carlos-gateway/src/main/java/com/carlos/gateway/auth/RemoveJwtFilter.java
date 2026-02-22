package com.carlos.gateway.auth;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.util.PathMatchUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

/**
 * <p>
 * 白名单请求移除请求头中得token
 * </p>
 *
 * @author carlos
 * @date 2021/11/3 18:00
 */
@Slf4j
@AllArgsConstructor
public class RemoveJwtFilter implements WebFilter {

    private final GatewayAuthProperties authProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();

        Set<String> whitelist = authProperties.getWhitelist();
        if (PathMatchUtil.matchAny(whitelist, path)) {
            request = exchange.getRequest().mutate().header(authProperties.getTokenName(), StrUtil.EMPTY).build();
            exchange = exchange.mutate().request(request).build();
        }
        return chain.filter(exchange);
    }
}
