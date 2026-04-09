package com.carlos.gateway.filter;

import com.carlos.core.auth.UserContext;
import com.carlos.gateway.oauth2.OAuth2GatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * <p>
 * 用户上下文传递过滤器
 * </p>
 *
 * <p>将认证后的用户信息通过HTTP Header传递给下游微服务，支持：</p>
 * <ul>
 *   <li>用户ID传递</li>
 *   <li>用户名传递</li>
 *   <li>角色信息传递</li>
 *   <li>权限信息传递</li>
 *   <li>租户信息传递（多租户场景）</li>
 * </ul>
 *
 * <p>下游服务通过解析这些Header，构建SecurityContext，支持@PreAuthorize注解。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see com.carlos.core.auth.UserContext
 */
@Slf4j
@Component
public class UserContextRelayFilter implements GlobalFilter, Ordered {

    /**
     * Header常量定义
     */
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username";
    public static final String HEADER_ROLES = "X-Roles";
    public static final String HEADER_PERMISSIONS = "X-Permissions";
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_DEPT_ID = "X-Dept-Id";
    public static final String HEADER_PHONE = "X-Phone";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";

    private final OAuth2GatewayProperties properties;

    public UserContextRelayFilter(OAuth2GatewayProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从exchange属性中获取用户上下文（由OAuth2AuthenticationFilter设置）
        UserContext userContext = exchange.getAttribute("userContext");

        if (userContext == null) {
            log.debug("No user context found, skipping header relay");
            return chain.filter(exchange);
        }

        // 构建新的请求，添加用户上下文Header
        ServerHttpRequest mutatedRequest = buildRequestWithUserContext(
            exchange.getRequest(),
            userContext
        );

        log.debug("Relaying user context to downstream: userId={}, username={}",
            userContext.getUserId(), userContext.getAccount());

        return chain.filter(exchange.mutate()
            .request(mutatedRequest)
            .build());
    }

    /**
     * 构建带有用户上下文的请求
     *
     * @param originalRequest 原始请求
     * @param userContext 用户上下文
     * @return 修改后的请求
     */
    private ServerHttpRequest buildRequestWithUserContext(
        ServerHttpRequest originalRequest,
        UserContext userContext) {

        ServerHttpRequest.Builder requestBuilder = originalRequest.mutate();

        // 基础用户信息
        if (userContext.getUserId() != null) {
            requestBuilder.header(HEADER_USER_ID, userContext.getUserId());
        }
        if (userContext.getAccount() != null) {
            requestBuilder.header(HEADER_USERNAME, userContext.getAccount());
        }
        if (userContext.getPhone() != null) {
            requestBuilder.header(HEADER_PHONE, userContext.getPhone());
        }

        // 角色信息（逗号分隔）
        if (userContext.getRoleIds() != null && !userContext.getRoleIds().isEmpty()) {
            String roles = userContext.getRoleIds().stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
            requestBuilder.header(HEADER_ROLES, roles);
        }

        // 权限信息（逗号分隔）
        if (userContext.getPermissions() != null && !userContext.getPermissions().isEmpty()) {
            String permissions = String.join(",", userContext.getPermissions());
            requestBuilder.header(HEADER_PERMISSIONS, permissions);
        }

        // 租户和部门信息（多租户场景）
        if (userContext.getTenantId() != null) {
            requestBuilder.header(HEADER_TENANT_ID, String.valueOf(userContext.getTenantId()));
        }
        if (userContext.getDepartmentId() != null) {
            requestBuilder.header(HEADER_DEPT_ID, String.valueOf(userContext.getDepartmentId()));
        }

        // 请求ID（链路追踪）
        requestBuilder.header(HEADER_REQUEST_ID,
            originalRequest.getId() != null ? originalRequest.getId().toString() : java.util.UUID.randomUUID().toString());

        return requestBuilder.build();
    }

    @Override
    public int getOrder() {
        // 在OAuth2AuthenticationFilter之后执行（假设其order为-100）
        return -90;
    }
}
