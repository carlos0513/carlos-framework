package com.carlos.auth.oauth2.client;

import lombok.Data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 客户端配置属性
 *
 * <p>配置单个 OAuth2 客户端的信息。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Data
public class OAuth2ClientProperties {

    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 授权类型
     */
    private List<String> authorizationGrantTypes = new ArrayList<>();

    /**
     * 重定向 URI 列表
     */
    private List<String> redirectUris = new ArrayList<>();

    /**
     * 作用域
     */
    private List<String> scopes = new ArrayList<>();

    /**
     * 是否需要授权确认
     */
    private boolean requireAuthorizationConsent = false;

    /**
     * 是否需要 PKCE
     */
    private boolean requireProofKey = false;

    /**
     * 访问令牌有效期
     */
    private Duration accessTokenTimeToLive;

    /**
     * 刷新令牌有效期
     */
    private Duration refreshTokenTimeToLive;

}
