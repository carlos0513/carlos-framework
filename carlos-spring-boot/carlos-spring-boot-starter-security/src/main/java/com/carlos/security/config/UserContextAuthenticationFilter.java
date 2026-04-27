package com.carlos.security.config;

import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.security.auth.UserContext;
import com.carlos.security.auth.UserContextAuthenticationToken;
import com.carlos.security.permission.PermissionProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户上下文认证过滤器
 * </p>
 *
 * <p>从 HTTP Header 提取用户信息并构建 Spring Security 上下文：</p>
 * <pre>
 * 请求流程：
 * 客户端 -> Gateway -> 下游服务
 *                    ↓
 *              [UserContextRelayFilter]
 *                    ↓
 *              添加 Header（定义在 HttpHeadersConstant）：
 *                X-User-Id: 10001
 *                X-User-Name: admin
 *                X-User-Roles: 1,2,3
 *                    ↓
 *              [UserContextAuthenticationFilter]
 *                    ↓
 *              构建 SecurityContext
 *              SecurityContextHolder.getContext().setAuthentication(auth)
 * </pre>
 *
 * <h3>Header 说明（来自 {@link HttpHeadersConstant}）：</h3>
 * <ul>
 *   <li><b>X-User-Id</b> - 用户ID（必选，存在则认证）</li>
 *   <li><b>X-User-Name</b> - 用户名（可选）</li>
 *   <li><b>X-User-Roles</b> - 角色ID列表，逗号分隔（必选，用于加载权限）</li>
 * </ul>
 *
 * <p>如果 X-User-Id 不存在，则跳过认证（由其他过滤器或匿名认证处理）。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see HttpHeadersConstant
 */
@Slf4j
public class UserContextAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 白名单路径
     */
    @Setter
    private Set<String> whitelistPaths = new HashSet<>();

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 权限提供者
     */
    private final PermissionProvider permissionProvider;

    public UserContextAuthenticationFilter(PermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 检查是否在白名单中
        String requestPath = request.getRequestURI();
        if (isWhitelisted(requestPath)) {
            log.debug("Request whitelisted: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        // 从 Header 提取用户信息（使用 HttpHeadersConstant 中的常量）
        String userIdStr = request.getHeader(HttpHeadersConstant.X_USER_ID);

        // 如果 Header 不存在，跳过（可能由其他方式认证）
        if (!StringUtils.hasText(userIdStr)) {
            log.debug("No user context found in headers, skipping");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Long userId = Long.valueOf(userIdStr);
            String username = request.getHeader(HttpHeadersConstant.X_USER_NAME);
            String rolesStr = request.getHeader(HttpHeadersConstant.X_USER_ROLES);

            // 解析角色ID
            Set<Long> roleIds = parseRoleIds(rolesStr);

            // 从缓存获取权限
            Set<String> permissions = loadPermissions(roleIds);

            // 构建用户上下文
            UserContext userContext = new UserContext();
            userContext.setUserId(userId);
            userContext.setUsername(username);
            userContext.setRoleIds(roleIds);
            userContext.setPermissions(permissions);

            // 构建认证令牌
            UserContextAuthenticationToken authentication =
                new UserContextAuthenticationToken(userContext, userContext.getAuthorities());
            authentication.setDetails(request);

            // 设置到 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("User context authenticated: userId={}, roles={}, permissions={}",
                userId, roleIds.size(), permissions.size());

        } catch (NumberFormatException e) {
            log.warn("Invalid user ID in header: {}", userIdStr);
            // 继续执行，不设置认证
        } catch (Exception e) {
            log.error("Failed to process user context authentication", e);
            // 继续执行，不设置认证
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 解析角色ID字符串
     *
     * @param rolesStr 逗号分隔的角色ID（如 "1,2,3"）
     * @return 角色ID集合
     */
    private Set<Long> parseRoleIds(String rolesStr) {
        if (!StringUtils.hasText(rolesStr)) {
            return Collections.emptySet();
        }

        return Arrays.stream(rolesStr.split(","))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .map(Long::valueOf)
            .collect(Collectors.toSet());
    }

    /**
     * 加载角色权限
     *
     * @param roleIds 角色ID集合
     * @return 权限集合
     */
    private Set<String> loadPermissions(Set<Long> roleIds) {
        if (roleIds.isEmpty() || permissionProvider == null) {
            return Collections.emptySet();
        }

        try {
            return permissionProvider.getPermissions(roleIds);
        } catch (Exception e) {
            log.error("Failed to load permissions for roles: {}", roleIds, e);
            return Collections.emptySet();
        }
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhitelisted(String requestPath) {
        for (String pattern : whitelistPaths) {
            if (pathMatcher.match(pattern, requestPath)) {
                return true;
            }
        }
        return false;
    }
}
