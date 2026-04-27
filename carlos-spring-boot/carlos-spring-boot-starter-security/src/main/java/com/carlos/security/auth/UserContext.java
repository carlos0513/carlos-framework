package com.carlos.security.auth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户上下文
 * </p>
 *
 * <p>封装当前请求的用户信息和权限，存储在 SecurityContext 中。</p>
 * <p>数据来源于网关传递的 HTTP Header（定义在 {@link com.carlos.core.constant.HttpHeadersConstant}）：</p>
 * <pre>
 * X-User-Id: 10001
 * X-User-Name: admin
 * X-User-Roles: 1,2,3
 * </pre>
 *
 * <p>该对象作为 Spring Security Authentication 的 principal，支持：</p>
 * <ul>
 *   <li>{@link org.springframework.security.core.annotation.AuthenticationPrincipal} 注入</li>
 *   <li>{@code @PreAuthorize("hasPermission('user', 'read')")} 权限表达式</li>
 *   <li>{@code @PreAuthorize("@permissionEvaluator.isOwner(#id, 'user')")} 数据权限</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Data
public class UserContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色ID集合
     */
    private Set<Long> roleIds = new HashSet<>();

    /**
     * 权限集合（如：["user:create", "order:read"]）
     */
    private Set<String> permissions = new HashSet<>();

    /**
     * 获取 Spring Security 需要的 GrantedAuthority 集合
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 从权限生成 GrantedAuthority
        Set<SimpleGrantedAuthority> authorities = permissions.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        // 同时添加角色权限（ROLE_前缀）
        roleIds.forEach(roleId ->
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleId)));

        return authorities;
    }

    /**
     * 检查是否有指定权限
     *
     * @param permission 权限标识（如 "user:create"）
     * @return true 如果有权限
     */
    public boolean hasPermission(String permission) {
        if (permission == null) {
            return false;
        }

        // 超级管理员权限
        if (permissions.contains("*")) {
            return true;
        }

        // 精确匹配
        if (permissions.contains(permission)) {
            return true;
        }

        // 通配符匹配（如 "user:*" 匹配 "user:create"）
        String[] parts = permission.split(":");
        if (parts.length == 2) {
            String wildcard = parts[0] + ":*";
            if (permissions.contains(wildcard)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查是否有指定角色
     *
     * @param roleId 角色ID
     * @return true 如果有该角色
     */
    public boolean hasRole(Long roleId) {
        return roleIds != null && roleIds.contains(roleId);
    }

    /**
     * 检查是否有任意一个角色
     *
     * @param roleIds 角色ID集合
     * @return true 如果有任意一个角色
     */
    public boolean hasAnyRole(Set<Long> roleIds) {
        if (this.roleIds == null || roleIds == null) {
            return false;
        }
        for (Long roleId : roleIds) {
            if (this.roleIds.contains(roleId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否是超级管理员
     * <p>拥有 "*" 权限或角色ID为1</p>
     */
    public boolean isSuperAdmin() {
        return permissions.contains("*") || roleIds.contains(1L);
    }
}
