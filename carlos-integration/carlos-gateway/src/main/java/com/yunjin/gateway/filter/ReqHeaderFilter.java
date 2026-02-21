package com.carlos.gateway.filter;

import com.carlos.util.IdUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求添加x-request-id请求头
 *
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/21 15:24
 **/
public class ReqHeaderFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求转发处理，添加新的请求头
        // 因为是使用的原始的请求对象添加了新的请求头，所以下游服务能获取到请求原有请求头和新添加的请求头
        ServerHttpRequest mutableReq = request.mutate()
                .header("x-request-id", String.format("req-%s", IdUtils.date32Id()))
                .build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    @Override
    public int getOrder() {
        return -2000;
    }
}
