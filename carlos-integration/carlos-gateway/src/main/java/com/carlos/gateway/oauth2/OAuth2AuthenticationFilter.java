package com.carlos.gateway.oauth2;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.UserContext;
import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.core.util.PathMatchUtil;
import com.carlos.gateway.oauth2.validator.TokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

/**
 * <p>
 * OAuth2 认证过滤器
 * 统一处理 JWT 和 Opaque Token 认证
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
public class OAuth2AuthenticationFilter implements GlobalFilter, Ordered {

    private final OAuth2GatewayProperties properties;
    private final TokenValidator tokenValidator;

    public OAuth2AuthenticationFilter(OAuth2GatewayProperties properties,
                                      TokenValidator tokenValidator) {
        this.properties = properties;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // OAuth2 未启用，直接放行
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();

        // 白名单路径直接放行
        if (PathMatchUtil.antMatchAny(properties.getWhitelist(), path)) {
            log.debug("Path {} is in whitelist, skipping authentication", path);
            return chain.filter(exchange);
        }

        // 跨域预检请求放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // 提取 Token
        String token = extractToken(request);
        if (StrUtil.isBlank(token)) {
            log.warn("No token found for path: {}", path);
            return chain.filter(exchange);
        }

        // 验证 Token 并构建用户上下文
        return tokenValidator.validate(token)
            .flatMap(userContext -> {
                log.debug("Token validated successfully for user: {}", userContext.getAccount());
                // 将用户信息注入请求头
                ServerHttpRequest mutatedRequest = injectUserContext(request, userContext);
                ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

                // 将用户上下文存入 exchange 属性，供后续过滤器使用
                mutatedExchange.getAttributes().put(AuthConstant.USER_CONTEXT, userContext);

                return chain.filter(mutatedExchange);
            })
            .onErrorResume(e -> {
                log.error("Authentication failed for path: {}", path, e);
                // 认证失败，继续执行（后续的鉴权过滤器会处理）
                return chain.filter(exchange);
            });
    }

    /**
     * 从请求中提取 Token
     */
    private String extractToken(ServerHttpRequest request) {
        // 1. 从 Authorization 头提取 Bearer Token
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(authorization) && authorization.startsWith(HttpHeadersConstant.BEARER_PREFIX)) {
            return authorization.substring(HttpHeadersConstant.BEARER_PREFIX.length());
        }

        // 2. 从自定义 Token 头提取
        String tokenHeader = request.getHeaders().getFirst(AuthConstant.TOKEN_HEADER);
        if (StrUtil.isNotBlank(tokenHeader)) {
            return tokenHeader;
        }

        // 3. 从 Query 参数提取（用于 WebSocket 等场景）
        String tokenParam = request.getQueryParams().getFirst(HttpHeadersConstant.ACCESS_TOKEN_PARAM);
        if (StrUtil.isNotBlank(tokenParam)) {
            return tokenParam;
        }

        return null;
    }

    /**
     * 将用户信息注入请求头
     */
    private ServerHttpRequest injectUserContext(ServerHttpRequest request, UserContext context) {
        ServerHttpRequest.Builder builder = request.mutate();

        Optional.ofNullable(context.getToken()).ifPresent(v -> builder.header(AuthConstant.USER_TOKEN, v));
        Optional.ofNullable(context.getAccount()).ifPresent(v -> builder.header(AuthConstant.USER_ACCOUNT, v));
        Optional.ofNullable(context.getUserId()).ifPresent(v -> builder.header(AuthConstant.USER_ID, String.valueOf(v)));
        Optional.ofNullable(context.getDepartmentId()).ifPresent(v -> builder.header(AuthConstant.DEPT_ID, String.valueOf(v)));
        Optional.ofNullable(context.getRoleId()).ifPresent(v -> builder.header(AuthConstant.ROLE_ID, String.valueOf(v)));
        Optional.ofNullable(context.getTenantId()).ifPresent(v -> builder.header(AuthConstant.TENANT_ID, String.valueOf(v)));
        Optional.ofNullable(context.getPhone()).ifPresent(v -> builder.header(AuthConstant.USER_PHONE, v));
        Optional.ofNullable(context.getRoleIds()).ifPresent(v -> builder.header(AuthConstant.ROLE_IDS, StrUtil.join(",", v)));
        Optional.ofNullable(context.getDepartmentIds()).ifPresent(v -> builder.header(AuthConstant.DEPT_IDS, StrUtil.join(",", v)));

        return builder.build();
    }

    @Override
    public int getOrder() {
        // 认证过滤器优先级较高，但要在请求追踪之后
        return OAuth2FilterOrder.AUTHENTICATION;
    }

    /**
     * OAuth2 过滤器顺序常量
     */
    public interface OAuth2FilterOrder {
        int TRACE = -3000;
        int AUTHENTICATION = -2000;
        int AUTHORIZATION = -1500;
        int RATE_LIMIT = -1000;
        int CIRCUIT_BREAKER = -500;
        int CACHE = 0;
        int TRANSFORM = 1000;
    }
}
