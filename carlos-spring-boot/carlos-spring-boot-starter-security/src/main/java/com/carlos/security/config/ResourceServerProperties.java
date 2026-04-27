package com.carlos.security.config;

import com.carlos.core.constant.HttpHeadersConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 资源服务器配置属性
 * </p>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   resource-server:
 *     enabled: true
 *     auth:
 *       user-id-header: X-User-Id
 *       username-header: X-User-Name
 *       roles-header: X-User-Roles
 *       whitelist-paths:
 *         - /actuator/**
 *         - /public/**
 *         - /swagger-ui/**
 *     permission:
 *       cache:
 *         enabled: true
 *         local-ttl: 600
 *         max-size: 10000
 *       redis-ttl: 604800
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see HttpHeadersConstant
 */
@Data
@ConfigurationProperties(prefix = "carlos.resource-server")
public class ResourceServerProperties {

    /**
     * 是否启用资源服务器功能
     */
    private boolean enabled = true;

    /**
     * 认证相关配置
     */
    private AuthProperties auth = new AuthProperties();

    /**
     * 权限缓存配置
     */
    private PermissionProperties permission = new PermissionProperties();

    /**
     * 认证配置
     */
    @Data
    public static class AuthProperties {

        /**
         * 用户ID Header 名称（默认使用 HttpHeadersConstant.X_USER_ID）
         */
        private String userIdHeader = HttpHeadersConstant.X_USER_ID;

        /**
         * 用户名 Header 名称（默认使用 HttpHeadersConstant.X_USER_NAME）
         */
        private String usernameHeader = HttpHeadersConstant.X_USER_NAME;

        /**
         * 角色ID Header 名称（默认使用 HttpHeadersConstant.X_USER_ROLES）
         */
        private String rolesHeader = HttpHeadersConstant.X_USER_ROLES;

        /**
         * 白名单路径（不需要认证）
         */
        private Set<String> whitelistPaths = new HashSet<>() {{
            add("/actuator/**");
            add("/swagger-ui/**");
            add("/v3/api-docs/**");
            add("/doc.html");
            add("/webjars/**");
            add("/error");
        }};
    }

    /**
     * 权限配置
     */
    @Data
    public static class PermissionProperties {

        /**
         * 本地缓存配置
         */
        private CacheProperties cache = new CacheProperties();

        /**
         * Redis 缓存过期时间（秒，默认7天）
         */
        private long redisTtl = 7 * 24 * 60 * 60;

        /**
         * 缓存配置
         */
        @Data
        public static class CacheProperties {

            /**
             * 是否启用本地缓存
             */
            private boolean enabled = true;

            /**
             * 本地缓存过期时间（秒，默认10分钟）
             */
            private long localTtl = 10 * 60;

            /**
             * 本地缓存最大条目数
             */
            private int maxSize = 10000;
        }
    }
}
