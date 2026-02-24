package com.carlos.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Gateway OAuth2 资源服务器配置属性
 *
 * <p>本配置类提供了 Gateway 层 OAuth2 资源服务器的配置选项。
 * 通过 application.yml 或 application.properties 文件进行配置。</p>
 *
 * <p><strong>配置前缀：</strong>{@code carlos.gateway.oauth2}</p>
 *
 * <h3>快速开始配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   gateway:
 *     oauth2:
 *       # 启用 OAuth2 资源服务器功能
 *       resource-server:
 *         enabled: true
 *         # JWK Set URI 用于验证 JWT Token
 *         jwk-set-uri: http://auth-server:9000/oauth2/jwks
 *         # 不需要认证的路径
 *         permit-all-paths:
 *           - /api/public/**
 *           - /actuator/health
 * }</pre>
 *
 * <h3>配置项说明：</h3>
 * <ul>
 *   <li>{@code resource-server.enabled} - 是否启用资源服务器</li>
 *   <li>{@code resource-server.jwk-set-uri} - 授权服务器的 JWK Set 端点</li>
 *   <li>{@code resource-server.issuer-uri} - 授权服务器的 Issuer URI</li>
 *   <li>{@code resource-server.permit-all-paths} - 白名单路径列表</li>
 * </ul>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Data
@ConfigurationProperties(prefix = "carlos.gateway.oauth2")
public class GatewayOAuth2Properties {

    /**
     * 资源服务器配置
     *
     * <p>配置资源服务器如何验证和保护 API 资源。</p>
     *
     * <h3>使用场景：</h3>
     * <ul>
     *   <li>网关作为资源服务器，统一验证请求中的 JWT Token</li>
     *   <li>配置公钥或公钥 URI 用于验证 Token 签名</li>
     *   <li>配置不需要认证的路径（白名单）</li>
     * </ul>
     */
    @NestedConfigurationProperty
    private ResourceServerProperties resourceServer = new ResourceServerProperties();

    /**
     * 资源服务器配置属性
     *
     * <p>配置资源服务器如何验证和保护 API 资源。</p>
     */
    @Data
    public static class ResourceServerProperties {

        /**
         * 是否启用资源服务器
         *
         * <p>设置为 true 时，网关将验证请求中的 JWT Token，
         * 只有合法的 Token 才能访问受保护的资源。</p>
         *
         * <p><strong>默认值：</strong>false</p>
         * <p><strong>典型场景：</strong></p>
         * <ul>
         *   <li>网关统一验证 Token，保护下游微服务</li>
         *   <li>减轻下游服务的认证压力</li>
         * </ul>
         */
        private boolean enabled = false;

        /**
         * JWK Set URI
         *
         * <p>授权服务器的 JWK Set 端点地址，资源服务器从此端点获取公钥验证 JWT。</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * jwk-set-uri: http://auth-server:9000/oauth2/jwks
         * }</pre>
         *
         * <p><strong>注意：</strong>jwk-set-uri 和 issuer-uri 至少配置一个，
         * 同时配置时优先使用 jwk-set-uri。</p>
         */
        private String jwkSetUri;

        /**
         * Issuer URI
         *
         * <p>授权服务器的 Issuer 标识，用于从发现端点获取配置。</p>
         *
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * issuer-uri: http://auth-server:9000
         * }</pre>
         *
         * <p>配置后，资源服务器会自动从以下地址获取配置：</p>
         * <pre>{issuer-uri}/.well-known/openid-configuration</pre>
         */
        private String issuerUri;

        /**
         * 不需要认证的路径列表
         *
         * <p>配置哪些路径可以匿名访问，不需要提供 Token。</p>
         *
         * <p><strong>默认值：</strong>包含常用的公共路径</p>
         * <p><strong>配置示例：</strong></p>
         * <pre>{@code
         * permit-all-paths:
         *   - /api/public/**
         *   - /actuator/health
         *   - /swagger-ui/**
         * }</pre>
         */
        private List<String> permitAllPaths = new ArrayList<>();

        /**
         * 构造函数 - 设置默认的公共路径
         */
        public ResourceServerProperties() {
            // 默认放行的路径
            permitAllPaths.add("/error");
            permitAllPaths.add("/actuator/health");
            permitAllPaths.add("/v3/api-docs/**");
            permitAllPaths.add("/swagger-ui/**");
            permitAllPaths.add("/swagger-ui.html");
            permitAllPaths.add("/doc.html");
            permitAllPaths.add("/webjars/**");
        }
    }
}
