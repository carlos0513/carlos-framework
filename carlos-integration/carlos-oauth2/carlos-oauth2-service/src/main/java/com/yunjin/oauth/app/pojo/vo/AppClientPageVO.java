package com.carlos.oauth.app.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 应用信息 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppClientPageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "应用编号")
    private String appKey;
    @Schema(value = "应用名称")
    private String appName;
    @Schema(value = "应用logo")
    private String appLogo;
    @Schema(value = "应用密钥到期时间")
    private LocalDateTime clientSecretExpiresAt;
    @Schema(value = "应用发行时间")
    private LocalDateTime clientIssuedAt;
    @Schema(value = "认证方式")
    private String authenticationMethods;
    @Schema(value = "grant_type")
    private String authorizationGrantTypes;
    @Schema(value = "scopes")
    private String scopes;
    @Schema(value = "重定向地址")
    private String redirectUris;
    @Schema(value = "应用状态")
    private String state;
    @Schema(value = "创建人")
    private Long createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "更新人")
    private Long updateBy;
    @Schema(value = "更新时间")
    private LocalDateTime updateTime;

}
