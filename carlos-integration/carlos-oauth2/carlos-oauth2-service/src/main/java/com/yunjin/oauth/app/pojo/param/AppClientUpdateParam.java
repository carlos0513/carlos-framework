package com.carlos.oauth.app.pojo.param;


import com.carlos.oauth.app.pojo.dto.Oauth2ClientSettings;
import com.carlos.oauth.app.pojo.dto.Oauth2TokenSettings;
import com.carlos.oauth.app.pojo.enums.ClientStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(value = "应用信息修改参数", description = "应用信息修改参数")
public class AppClientUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "应用名称")
    private String appName;
    @Schema(value = "应用logo")
    private String appLogo;
    @Schema(value = "应用密钥到期时间")
    private LocalDateTime clientSecretExpiresAt;
    @Schema(value = "应用发行时间")
    private LocalDateTime clientIssuedAt;
    @Schema(value = "认证方式")
    private Set<String> authenticationMethods;
    @Schema(value = "grant_type")
    private Set<String> authorizationGrantTypes;
    @Schema(value = "scopes")
    private Set<String> scopes;
    @Schema(value = "重定向地址")
    private Set<String> redirectUris;
    @Schema(value = "client设置 key:value;")
    private Oauth2ClientSettings clientSettings;
    @Schema(value = "token设置 key:value;")
    private Oauth2TokenSettings tokenSettings;
    @Schema(value = "应用状态")
    private ClientStateEnum state;
}
