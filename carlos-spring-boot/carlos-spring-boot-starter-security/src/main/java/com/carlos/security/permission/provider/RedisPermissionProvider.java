package com.carlos.security.permission.provider;

import com.carlos.redis.util.RedisUtil;
import com.carlos.security.permission.PermissionProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 基于 Redis 的权限提供者实现
 * </p>
 *
 * <p>使用框架 {@link RedisUtil} 进行缓存操作：</p>
 * <pre>
 * Redis Key 结构：
 * ┌─────────────────────────────────────────────────────────────┐
 * │  Key: auth:permissions:{roleId}                              │
 * │  Value: Set&lt;String&gt; (权限标识集合)                             │
 * │  TTL: 可配置（默认7天）                                         │
 * └─────────────────────────────────────────────────────────────┘
 *
 * 示例：
 *   SET auth:permissions:1001 -> ["user:create", "user:read", "order:*"]
 *   EXPIRE auth:permissions:1001 604800
 * </pre>
 *
 * <h3>使用方式：</h3>
 * <pre>{@code
 * // 1. 直接创建（推荐在自动配置中使用）
 * RedisPermissionProvider provider = new RedisPermissionProvider();
 *
 * // 2. 保存权限到 Redis
 * provider.savePermissions(1001L, Set.of("user:create", "user:read"));
 *
 * // 3. 从 Redis 获取权限
 * Set<String> perms = provider.getPermissions(1001L);
 * }</pre>
 *
 * <p><b>注意：</b>此类依赖 {@code carlos-spring-boot-starter-redis} 模块，</p>
 * <p>确保已配置 Redis 连接信息。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see PermissionProvider
 * @see RedisUtil
 */
@Slf4j
public class RedisPermissionProvider implements PermissionProvider {

    /**
     * Redis Key 前缀
     */
    private static final String KEY_PREFIX = "auth:permissions:";

    /**
     * 默认缓存时间（7天，单位：秒）
     */
    private static final long DEFAULT_TTL = 7 * 24 * 60 * 60;

    /**
     * 从 Redis 获取指定角色的权限
     *
     * @param roleId 角色ID
     * @return 权限集合，缓存不存在返回空集合（不是 null）
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getPermissions(Long roleId) {
        if (roleId == null) {
            return Collections.emptySet();
        }

        String key = KEY_PREFIX + roleId;

        try {
            Object cached = RedisUtil.getValue(key);
            if (cached instanceof Set) {
                Set<String> permissions = (Set<String>) cached;
                log.debug("Cache hit for role: {}, permissions: {}", roleId, permissions.size());
                return new HashSet<>(permissions);
            }
        } catch (Exception e) {
            log.error("Failed to get permissions from Redis for role: {}", roleId, e);
        }

        log.debug("Cache miss for role: {}", roleId);
        return Collections.emptySet();
    }

    /**
     * 批量获取多个角色的权限
     *
     * @param roleIds 角色ID集合
     * @return 合并后的权限集合
     */
    @Override
    public Set<String> getPermissions(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> allPermissions = new HashSet<>();
        for (Long roleId : roleIds) {
            allPermissions.addAll(getPermissions(roleId));
        }

        return allPermissions;
    }

    /**
     * 保存权限到 Redis（使用默认 TTL）
     *
     * @param roleId      角色ID
     * @param permissions 权限集合
     */
    @Override
    public void savePermissions(Long roleId, Set<String> permissions) {
        savePermissions(roleId, permissions, DEFAULT_TTL);
    }

    /**
     * 保存权限到 Redis（指定 TTL）
     *
     * @param roleId      角色ID
     * @param permissions 权限集合
     * @param ttl         过期时间（秒）
     */
    @Override
    public void savePermissions(Long roleId, Set<String> permissions, long ttl) {
        if (roleId == null) {
            return;
        }

        String key = KEY_PREFIX + roleId;

        try {
            if (permissions == null || permissions.isEmpty()) {
                // 保存空集合，防止缓存穿透
                RedisUtil.setValue(key, Collections.emptySet(), ttl);
            } else {
                RedisUtil.setValue(key, new HashSet<>(permissions), ttl);
            }
            log.debug("Saved permissions to Redis for role: {}, ttl: {}s", roleId, ttl);
        } catch (Exception e) {
            log.error("Failed to save permissions to Redis for role: {}", roleId, e);
        }
    }

    /**
     * 清除指定角色的缓存
     *
     * @param roleId 角色ID
     */
    @Override
    public void clearCache(Long roleId) {
        if (roleId == null) {
            return;
        }

        String key = KEY_PREFIX + roleId;

        try {
            RedisUtil.delete(key);
            log.debug("Cleared Redis cache for role: {}", roleId);
        } catch (Exception e) {
            log.error("Failed to clear Redis cache for role: {}", roleId, e);
        }
    }

    /**
     * 批量清除缓存
     *
     * @param roleIds 角色ID集合
     */
    public void clearCache(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        for (Long roleId : roleIds) {
            clearCache(roleId);
        }
    }

    /**
     * 清除所有权限缓存（谨慎使用）
     */
    public void clearAllCache() {
        try {
            Set<String> keys = RedisUtil.scanKeys(KEY_PREFIX + "*", 1000);
            if (!keys.isEmpty()) {
                RedisUtil.delete(keys);
                log.info("Cleared all permission caches, count: {}", keys.size());
            }
        } catch (Exception e) {
            log.error("Failed to clear all permission caches", e);
        }
    }

    /**
     * 检查缓存是否存在
     *
     * @param roleId 角色ID
     * @return true 如果缓存存在
     */
    public boolean hasCache(Long roleId) {
        if (roleId == null) {
            return false;
        }
        return RedisUtil.hasKey(KEY_PREFIX + roleId);
    }

    /**
     * 获取缓存过期时间
     *
     * @param roleId 角色ID
     * @return 剩余秒数，-1 表示永不过期，-2 表示不存在
     */
    public long getTtl(Long roleId) {
        if (roleId == null) {
            return -2;
        }
        return RedisUtil.getExpire(KEY_PREFIX + roleId);
    }
}
