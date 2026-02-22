package com.carlos.oauth.app.pojo.dto;


import com.carlos.oauth.app.pojo.enums.ClientStateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 应用信息 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
public class AppClientDTO {
    /** 主键 */
    private Long id;
    /** 应用编号 */
    private String appKey;
    /** 应用名称 */
    private String appName;
    /** 应用logo */
    private String appLogo;
    /** 应用密钥 */
    private String appSecret;
    /** 应用密钥到期时间 */
    private LocalDateTime clientSecretExpiresAt;
    /** 应用发行时间 */
    private LocalDateTime clientIssuedAt;
    /** 认证方式 */
    private Set<String> authenticationMethods;
    /** grant_type */
    private Set<String> authorizationGrantTypes;
    /** scopes */
    private Set<String> scopes;
    /** 重定向地址 */
    private Set<String> redirectUris;
    /** client设置 key:value; */
    private Oauth2ClientSettings clientSettings;
    /** token设置 key:value; */
    private Oauth2TokenSettings tokenSettings;
    /** 应用状态 */
    private ClientStateEnum state;
    /** 创建人 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新人 */
    private Long updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
