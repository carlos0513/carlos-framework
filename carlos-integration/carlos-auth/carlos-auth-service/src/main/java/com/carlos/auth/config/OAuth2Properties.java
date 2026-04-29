package com.carlos.auth.config;

import com.carlos.auth.oauth2.client.OAuth2ClientProperties;
import com.carlos.auth.oauth2.client.config.ClientAuthProperties;
import com.carlos.auth.oauth2.server.AuthorizationServerProperties;
import com.carlos.auth.oauth2.server.JwtProperties;
import com.carlos.auth.security.SecurityProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Carlos OAuth2 统一配置属性
 *
 * <p>本配置类提供了 OAuth2 授权服务器和资源服务器的完整配置选项。
 * 各子配置按功能模块分散在对应包中，实现高内聚：</p>
 * <ul>
 *   <li>授权服务器配置 → {@link com.carlos.auth.oauth2.server.AuthorizationServerProperties}</li>
 *   <li>JWT 配置 → {@link com.carlos.auth.oauth2.server.JwtProperties}</li>
 *   <li>客户端配置 → {@link com.carlos.auth.oauth2.client.OAuth2ClientProperties}</li>
 *   <li>客户端认证配置 → {@link ClientAuthProperties}</li>
 *   <li>安全配置 → {@link com.carlos.auth.security.SecurityProperties}</li>
 * </ul>
 *
 * <p><strong>配置前缀：</strong>{@code carlos.auth}</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Data
@ConfigurationProperties(prefix = "carlos.auth")
public class OAuth2Properties {

    /**
     * 是否启用 OAuth2 功能
     */
    private boolean enabled = true;

    /**
     * 授权服务器配置
     */
    @NestedConfigurationProperty
    private AuthorizationServerProperties authorizationServer = new AuthorizationServerProperties();

    /**
     * JWT 配置
     */
    @NestedConfigurationProperty
    private JwtProperties jwt = new JwtProperties();

    /**
     * 预注册的 OAuth2 客户端列表
     */
    private List<OAuth2ClientProperties> clients = new ArrayList<>();

    /**
     * 安全配置
     */
    @NestedConfigurationProperty
    private SecurityProperties security = new SecurityProperties();

    /**
     * 客户端认证配置
     */
    @NestedConfigurationProperty
    private ClientAuthProperties client = new ClientAuthProperties();

    /**
     * 登录配置
     */
    @NestedConfigurationProperty
    private Login login = new Login();

    @Data
    public static class Login {
        /**
         * 默认客户端ID
         */
        private String defaultClientId = "carlos-client";

        /**
         * 访问令牌有效期（秒）
         */
        private long accessTokenTtl = 7200;

        /**
         * 刷新令牌有效期（秒）
         */
        private long refreshTokenTtl = 604800;
    }

}
