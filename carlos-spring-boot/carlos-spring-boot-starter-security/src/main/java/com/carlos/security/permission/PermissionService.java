package com.carlos.security.permission;

import com.carlos.security.permission.cache.PermissionCacheSyncManager;
import com.carlos.security.permission.provider.CachedPermissionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 权限服务
 * </p>
 *
 * <p>提供便捷的权限操作方法，封装缓存同步逻辑：</p>
 *
 * <h3>核心功能：</h3>
 * <ul>
 *   <li><b>权限获取</b> - 从缓存获取用户权限</li>
 *   <li><b>权限更新</b> - 更新并同步缓存</li>
 *   <li><b>权限校验</b> - 检查用户是否拥有指定权限</li>
 *   <li><b>缓存管理</b> - 手动清除本地和远程缓存</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @Service
 * public class UserService {
 *
 *     @Autowired
 *     private PermissionService permissionService;
 *
 *     public void someBusinessMethod(Long userId, Set<Long> roleIds) {
 *         // 1. 获取用户权限
 *         Set<String> permissions = permissionService.getUserPermissions(roleIds);
 *
 *         // 2. 检查权限
 *         if (permissionService.hasPermission(roleIds, "user:update")) {
 *             // 执行业务逻辑
 *         }
 *
 *         // 3. 更新角色权限（自动同步到其他服务）
 *         permissionService.updateRolePermissions(roleId, newPermissions);
 *     }
 * }
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see PermissionProvider
 * @see PermissionCacheSyncManager
 */
@Slf4j
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionProvider permissionProvider;
    private final PermissionCacheSyncManager syncManager;

    /**
     * 获取用户的所有权限
     *
     * @param roleIds 用户的角色ID集合
     * @return 合并后的权限集合
     */
    public Set<String> getUserPermissions(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        return permissionProvider.getPermissions(roleIds);
    }

    /**
     * 检查用户是否有指定权限
     *
     * @param roleIds          用户的角色ID集合
     * @param requiredPermission 需要的权限（如 "user:update"）
     * @return true 如果有权限
     */
    public boolean hasPermission(Set<Long> roleIds, String requiredPermission) {
        if (roleIds == null || roleIds.isEmpty() || requiredPermission == null) {
            return false;
        }
        Set<String> permissions = getUserPermissions(roleIds);
        return permissions.contains(requiredPermission);
    }

    /**
     * 检查用户是否有任意一个权限
     *
     * @param roleIds            用户的角色ID集合
     * @param requiredPermissions 需要的权限列表
     * @return true 如果有任意一个权限
     */
    public boolean hasAnyPermission(Set<Long> roleIds, String... requiredPermissions) {
        if (roleIds == null || roleIds.isEmpty() || requiredPermissions == null || requiredPermissions.length == 0) {
            return false;
        }
        Set<String> permissions = getUserPermissions(roleIds);
        for (String required : requiredPermissions) {
            if (permissions.contains(required)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查用户是否有所有权限
     *
     * @param roleIds            用户的角色ID集合
     * @param requiredPermissions 需要的权限列表
     * @return true 如果有所有权限
     */
    public boolean hasAllPermissions(Set<Long> roleIds, String... requiredPermissions) {
        if (roleIds == null || roleIds.isEmpty() || requiredPermissions == null || requiredPermissions.length == 0) {
            return false;
        }
        Set<String> permissions = getUserPermissions(roleIds);
        for (String required : requiredPermissions) {
            if (!permissions.contains(required)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取指定角色的权限
     *
     * @param roleId 角色ID
     * @return 权限集合
     */
    public Set<String> getRolePermissions(Long roleId) {
        if (roleId == null) {
            return Collections.emptySet();
        }
        return permissionProvider.getPermissions(roleId);
    }

    /**
     * 更新角色权限（仅更新本地和 Redis 缓存，不发布同步事件）
     * <p>适用于服务启动时的缓存预热</p>
     *
     * @param roleId      角色ID
     * @param permissions 权限集合
     */
    public void updateRolePermissions(Long roleId, Set<String> permissions) {
        updateRolePermissions(roleId, permissions, false);
    }

    /**
     * 更新角色权限
     *
     * @param roleId      角色ID
     * @param permissions 权限集合
     * @param sync        是否同步到其他服务
     */
    public void updateRolePermissions(Long roleId, Set<String> permissions, boolean sync) {
        if (roleId == null) {
            return;
        }

        permissionProvider.savePermissions(roleId, permissions);
        log.info("Updated permissions for role: {}, permissions: {}", roleId, permissions != null ? permissions.size() : 0);

        if (sync && syncManager != null) {
            syncManager.publishPermissionChange(roleId);
        }
    }

    /**
     * 清除角色权限缓存
     *
     * @param roleId 角色ID
     * @param sync   是否同步到其他服务
     */
    public void clearRoleCache(Long roleId, boolean sync) {
        if (roleId == null) {
            return;
        }

        // 清除本地和 Redis 缓存
        permissionProvider.clearCache(roleId);
        log.info("Cleared cache for role: {}", roleId);

        if (sync && syncManager != null) {
            syncManager.publishPermissionChange(roleId);
        }
    }

    /**
     * 批量清除角色权限缓存
     *
     * @param roleIds 角色ID集合
     * @param sync    是否同步到其他服务
     */
    public void clearRoleCaches(Set<Long> roleIds, boolean sync) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        for (Long roleId : roleIds) {
            permissionProvider.clearCache(roleId);
        }
        log.info("Cleared cache for {} roles", roleIds.size());

        if (sync && syncManager != null) {
            syncManager.publishPermissionChanges(roleIds);
        }
    }

    /**
     * 清除所有本地缓存（仅当前服务）
     * <p>适用于 CachedPermissionProvider</p>
     */
    public void clearAllLocalCache() {
        if (permissionProvider instanceof CachedPermissionProvider cached) {
            cached.clearAllLocalCache();
            log.info("Cleared all local permission caches");
        }
    }

    /**
     * 获取缓存统计信息（仅适用于 CachedPermissionProvider）
     *
     * @return 统计信息，如果不是缓存提供者返回 null
     */
    public Map<String, Object> getCacheStats() {
        if (permissionProvider instanceof CachedPermissionProvider cached) {
            return cached.getStatsMap();
        }
        return null;
    }
}
