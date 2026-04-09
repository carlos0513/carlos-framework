package com.carlos.security.permission;

import com.carlos.security.permission.provider.CachedPermissionProvider;
import com.carlos.security.permission.provider.RedisPermissionProvider;

import java.util.Set;

/**
 * <p>
 * 权限提供者接口
 * </p>
 *
 * <p>定义角色权限加载的统一抽象，支持多种实现：</p>
 * <ul>
 *   <li>{@link RedisPermissionProvider} - Redis 缓存实现</li>
 *   <li>{@link CachedPermissionProvider} - 本地缓存包装</li>
 *   <li>自定义实现 - 从数据库或其他数据源加载</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
public interface PermissionProvider {

    /**
     * 获取指定角色的权限集合
     *
     * @param roleId 角色ID
     * @return 权限标识集合（如：["user:create", "order:read"]），
     *         如果角色无权限或不存在返回空集合（不是 null）
     */
    Set<String> getPermissions(Long roleId);

    /**
     * 批量获取多个角色的权限集合
     * <p>合并所有角色的权限，自动去重</p>
     *
     * @param roleIds 角色ID集合
     * @return 合并后的权限集合
     */
    Set<String> getPermissions(Set<Long> roleIds);

    /**
     * 保存权限到缓存
     *
     * @param roleId      角色ID
     * @param permissions 权限集合
     */
    void savePermissions(Long roleId, Set<String> permissions);

    /**
     * 保存权限到缓存（指定过期时间）
     *
     * @param roleId      角色ID
     * @param permissions 权限集合
     * @param ttl         过期时间（秒）
     */
    void savePermissions(Long roleId, Set<String> permissions, long ttl);

    /**
     * 清除指定角色的缓存
     *
     * @param roleId 角色ID
     */
    void clearCache(Long roleId);
}
