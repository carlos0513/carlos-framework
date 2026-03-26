package com.carlos.gateway.filter;

import cn.hutool.core.util.IdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求添加 x-request-id 请求头
 * <p>
 * 注意：此过滤器在启用了 RequestTracingFilter 时会自动禁用，
 * 因为 RequestTracingFilter 已经包含了 Request ID 的处理逻辑。
 *
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/21 15:24
 * @deprecated 请使用 {@link com.carlos.gateway.observability.RequestTracingFilter}
 **/
@Component
@Deprecated
@ConditionalOnProperty(prefix = "carlos.gateway.tracing", name = "enabled", havingValue = "false")
public class ReqHeaderFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求转发处理，添加新的请求头
        // 因为是使用的原始的请求对象添加了新的请求头，所以下游服务能获取到请求原有请求头和新添加的请求头
        ServerHttpRequest mutableReq = request.mutate()
            .header(com.carlos.core.constant.HttpHeadersConstant.X_REQUEST_ID,
                String.format("req-%s", IdUtil.fastSimpleUUID()))
                .build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    @Override
    public int getOrder() {
        return -2000;
    }
}
