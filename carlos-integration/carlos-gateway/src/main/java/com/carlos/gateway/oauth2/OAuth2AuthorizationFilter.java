package com.carlos.gateway.oauth2;

import com.carlos.core.auth.AuthConstant;
import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.BusinessException;
import com.carlos.core.response.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * OAuth2 鉴权过滤器
 * 基于 RBAC 或 ABAC 的权限控制
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
public class OAuth2AuthorizationFilter implements GlobalFilter, Ordered {

    private final OAuth2GatewayProperties properties;
    private final PermissionProvider permissionProvider;

    public OAuth2AuthorizationFilter(OAuth2GatewayProperties properties,
                                     PermissionProvider permissionProvider) {
        this.properties = properties;
        this.permissionProvider = permissionProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 未启用授权或 OAuth2，直接放行
        if (!properties.isEnabled() || !properties.isAuthorizationEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        // 获取用户上下文
        UserContext userContext = exchange.getAttribute(AuthConstant.USER_CONTEXT);
        if (userContext == null) {
            log.warn("No user context found for path: {}", path);
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "Authentication required");
        }

        // 根据授权模式进行权限校验
        Mono<Boolean> permissionMono = switch (properties.getAuthorizationMode()) {
            case RBAC -> checkRbacPermission(userContext, path, method);
            case ABAC -> checkAbacPermission(userContext, path, method, exchange);
        };

        return permissionMono.flatMap(hasPermission -> {
            if (Boolean.TRUE.equals(hasPermission)) {
                return chain.filter(exchange);
            }
            log.warn("User {} has no permission for {} {}",
                userContext.getAccount(), method, path);
            throw new BusinessException(CommonErrorCode.FORBIDDEN, "Access denied");
        });
    }

    /**
     * RBAC 权限校验（基于角色）
     */
    private Mono<Boolean> checkRbacPermission(UserContext userContext, String path, HttpMethod method) {
        // 从 Redis 或配置中心获取用户的角色权限映射
        return permissionProvider.getUserPermissions(String.valueOf(userContext.getUserId()))
            .map(permissions -> {
                // 检查是否拥有访问该资源的权限
                String requiredPermission = method + ":" + path;
                return permissions.stream()
                    .anyMatch(permission -> matchPermission(permission, requiredPermission));
            });
    }

    /**
     * ABAC 权限校验（基于属性）
     */
    private Mono<Boolean> checkAbacPermission(UserContext userContext, String path,
                                              HttpMethod method, ServerWebExchange exchange) {
        // 构建属性集合
        Map<String, Object> attributes = Map.of(
            "userId", userContext.getUserId(),
            "roleIds", userContext.getRoleIds(),
            "deptId", userContext.getDepartmentId(),
            "tenantId", userContext.getTenantId(),
            "path", path,
            "method", method,
            "time", System.currentTimeMillis()
        );

        // 执行 ABAC 策略
        return permissionProvider.evaluateAbacPolicy(attributes);
    }

    /**
     * 权限匹配（支持 Ant 风格通配符）
     */
    private boolean matchPermission(String permission, String required) {
        // 精确匹配
        if (permission.equals(required)) {
            return true;
        }
        // Ant 风格匹配
        if (required.matches(permission.replace("**", ".*").replace("*", "[^:]*"))) {
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return OAuth2AuthenticationFilter.OAuth2FilterOrder.AUTHORIZATION;
    }

    /**
     * 权限提供者接口
     */
    public interface PermissionProvider {

        /**
         * 获取用户权限列表
         */
        Mono<Set<String>> getUserPermissions(String userId);

        /**
         * 评估 ABAC 策略
         */
        Mono<Boolean> evaluateAbacPolicy(Map<String, Object> attributes);
    }
}
