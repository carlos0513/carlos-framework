package com.carlos.oauth.app.pojo.param;


import com.carlos.oauth.app.pojo.dto.Oauth2ClientSettings;
import com.carlos.oauth.app.pojo.dto.Oauth2TokenSettings;
import com.carlos.oauth.app.pojo.enums.ClientStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 应用信息 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "应用信息修改参数", description = "应用信息修改参数")
public class AppClientUpdateParam {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "应用名称")
    private String appName;
    @ApiModelProperty(value = "应用logo")
    private String appLogo;
    @ApiModelProperty(value = "应用密钥到期时间")
    private LocalDateTime clientSecretExpiresAt;
    @ApiModelProperty(value = "应用发行时间")
    private LocalDateTime clientIssuedAt;
    @ApiModelProperty(value = "认证方式")
    private Set<String> authenticationMethods;
    @ApiModelProperty(value = "grant_type")
    private Set<String> authorizationGrantTypes;
    @ApiModelProperty(value = "scopes")
    private Set<String> scopes;
    @ApiModelProperty(value = "重定向地址")
    private Set<String> redirectUris;
    @ApiModelProperty(value = "client设置 key:value;")
    private Oauth2ClientSettings clientSettings;
    @ApiModelProperty(value = "token设置 key:value;")
    private Oauth2TokenSettings tokenSettings;
    @ApiModelProperty(value = "应用状态")
    private ClientStateEnum state;
}
