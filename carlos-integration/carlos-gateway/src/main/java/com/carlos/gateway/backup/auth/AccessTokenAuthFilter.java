package com.carlos.gateway.auth;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenAuthFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClientBuilder;


    // 认证服务校验端点
    private static final String CHECK_TOKEN_URI = "http://carlos-auth/api/oauth/checkToken";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 提取access_token
        String token = extractToken(exchange.getRequest());
        if (token.isEmpty()) {
            return chain.filter(exchange);
        }

        return webClientBuilder.build().get().uri(CHECK_TOKEN_URI + "?token=" + token)
            .retrieve().bodyToMono(Map.class).flatMap(response -> {
                // 2. 验证通过后注入appId
                String appId = (String) response.get("data");
                ServerHttpRequest newRequest = exchange.getRequest().mutate()
                    .header("X-App-Id", appId)
                    .header("X-App-Key", appId)
                    .header("X-App-Name", appId)
                    .build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                return chain.filter(newExchange);
            }).onErrorResume(e -> {
                throw new ServiceException(StatusCode.NOT_PERMISSION);
            });
    }


    // 从Authorization头提取Bearer Token
    private String extractToken(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get("access_token");
        if (headers == null || headers.isEmpty()) {
            return StrUtil.EMPTY;
        }
        return headers.stream().findFirst().orElse(StrUtil.EMPTY);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
