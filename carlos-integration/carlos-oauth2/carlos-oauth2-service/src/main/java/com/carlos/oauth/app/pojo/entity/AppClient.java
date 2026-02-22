package com.carlos.oauth.app.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.carlos.oauth.app.pojo.enums.ClientStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 应用信息 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
@TableName("app_client")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppClient implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 应用编号
     */
    @TableField(value = "app_key")
    private String appKey;
    /**
     * 应用名称
     */
    @TableField(value = "app_name")
    private String appName;
    /**
     * 应用logo
     */
    @TableField(value = "app_logo")
    private String appLogo;
    /**
     * 应用密钥
     */
    @TableField(value = "app_secret")
    private String appSecret;
    /**
     * 应用密钥到期时间
     */
    @TableField(value = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;
    /**
     * 应用发行时间
     */
    @TableField(value = "client_issued_at")
    private LocalDateTime clientIssuedAt;
    /**
     * 认证方式
     */
    @TableField(value = "authentication_methods")
    private String authenticationMethods;
    /**
     * grant_type
     */
    @TableField(value = "authorization_grant_types")
    private String authorizationGrantTypes;
    /**
     * scopes
     */
    @TableField(value = "scopes")
    private String scopes;
    /**
     * 重定向地址
     */
    @TableField(value = "redirect_uris")
    private String redirectUris;
    /**
     * client设置 key:value;
     */
    @TableField(value = "client_settings")
    private String clientSettings;
    /**
     * token设置 key:value;
     */
    @TableField(value = "token_settings")
    private String tokenSettings;
    /**
     * 应用状态
     */
    @TableField(value = "state")
    private ClientStateEnum state;
    /**
     * 删除状态
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private Long updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
