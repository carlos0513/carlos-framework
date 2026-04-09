package com.carlos.security.permission.evaluator;

import com.carlos.security.auth.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * <p>
 * Spring Security 权限评估器
 * </p>
 *
 * <p>支持 SpEL 表达式的 hasPermission() 方法：</p>
 * <pre>
 * // Controller 层方法权限控制
 * @PreAuthorize("hasPermission('user', 'create')")
 * public ResponseEntity<?> createUser(...) { }
 *
 * @PreAuthorize("hasPermission('user', 'update')")
 * public ResponseEntity<?> updateUser(...) { }
 *
 * @PreAuthorize("hasPermission(#userId, 'user', 'delete')")
 * public ResponseEntity<?> deleteUser(@PathVariable Long userId) { }
 *
 * // 组合表达式
 * @PreAuthorize("hasRole('ADMIN') or hasPermission('user', 'read')")
 * public ResponseEntity<?> listUsers(...) { }
 * </pre>
 *
 * <h3>权限字符串格式：</h3>
 * <ul>
 *   <li><b>简单格式：</b>"resource:action"（如 "user:create"）</li>
 *   <li><b>通配符：</b>"resource:*"（拥有 resource 的所有权限）</li>
 *   <li><b>全通配：</b>"*"（超级管理员）</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Slf4j
public class CarlosPermissionEvaluator implements PermissionEvaluator {

    /**
     * 检查是否有指定权限
     *
     * @param authentication 当前认证信息
     * @param targetDomainObject 目标对象（通常是资源类型，如 "user"）
     * @param permission 权限操作（如 "create", "read"）
     * @return true 如果有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("Permission denied: not authenticated");
            return false;
        }

        if (targetDomainObject == null || permission == null) {
            log.debug("Permission denied: target or permission is null");
            return false;
        }

        // 从认证信息获取 UserContext
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserContext)) {
            log.debug("Permission denied: principal is not UserContext");
            return false;
        }

        UserContext userContext = (UserContext) principal;
        String requiredPermission = targetDomainObject.toString() + ":" + permission.toString();

        // 检查权限
        boolean hasPermission = userContext.hasPermission(requiredPermission);

        if (!hasPermission) {
            log.debug("Permission denied: userId={}, required={}", userContext.getUserId(), requiredPermission);
        }

        return hasPermission;
    }

    /**
     * 检查是否有指定权限（带目标ID）
     * <p>用于数据级权限控制</p>
     *
     * @param authentication 当前认证信息
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param permission 权限操作
     * @return true 如果有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // 目前先简单调用 hasPermission(targetType, permission)
        // 如需支持数据级权限，可在此扩展
        return hasPermission(authentication, targetType, permission);
    }
}
