package com.carlos.gateway.oauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.UserContext;
import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.core.response.CommonErrorCode;
import com.carlos.core.util.PathMatchUtil;
import com.carlos.gateway.exception.ErrorResponse;
import com.carlos.gateway.oauth2.validator.TokenValidator;
import com.carlos.gateway.whitelist.WhitelistContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * <p>
 * OAuth2 认证过滤器 - 优化版
 * 统一处理 JWT 和 Opaque Token 认证
 * </p>
 * <p>
 * 优化点：
 * <ul>
 *   <li>优先使用统一白名单检查结果（UnifiedWhitelistFilter）</li>
 *   <li>避免重复进行路径匹配</li>
 *   <li>降级兼容：统一白名单未启用时，使用本地白名单</li>
 * </ul>
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 * @updated 2026-04-10 优化：使用统一白名单
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

        // 白名单路径直接放行（优先使用统一白名单检查结果）
        if (isWhitelisted(exchange, path)) {
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
            return writeUnauthorizedResponse(exchange, CommonErrorCode.UNAUTHORIZED, "未提供认证凭证");
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
                // 认证失败，返回 401，禁止穿透到下游服务
                return writeUnauthorizedResponse(exchange, CommonErrorCode.AUTH_TOKEN_INVALID, "登录凭证无效或已过期");
            });
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
        if (WhitelistContext.isAuthWhitelisted(exchange)) {
            return true;
        }

        // 2. 降级兼容：统一白名单未启用或结果不存在，使用本地白名单
        return PathMatchUtil.antMatchAny(properties.getWhitelist(), path);
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

    /**
     * 写入 401 未授权响应
     */
    private Mono<Void> writeUnauthorizedResponse(ServerWebExchange exchange, CommonErrorCode errorCode, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .code(errorCode.getCode())
            .msg(message)
            .path(exchange.getRequest().getURI().getPath())
            .method(exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN")
            .build();

        byte[] bytes = JSONUtil.toJsonStr(errorResponse).getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
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
