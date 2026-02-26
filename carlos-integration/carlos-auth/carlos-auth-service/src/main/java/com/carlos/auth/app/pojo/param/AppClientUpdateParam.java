package com.carlos.auth.app.pojo.param;


import com.carlos.auth.app.pojo.dto.Oauth2ClientSettings;
import com.carlos.auth.app.pojo.dto.Oauth2TokenSettings;
import com.carlos.auth.app.pojo.enums.ClientStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Schema(name = "应用信息修改参数", description = "应用信息修改参数")
public class AppClientUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "应用名称")
    private String appName;
    @Schema(description = "应用logo")
    private String appLogo;
    @Schema(description = "应用密钥到期时间")
    private LocalDateTime clientSecretExpiresAt;
    @Schema(description = "应用发行时间")
    private LocalDateTime clientIssuedAt;
    @Schema(description = "认证方式")
    private Set<String> authenticationMethods;
    @Schema(description = "grant_type")
    private Set<String> authorizationGrantTypes;
    @Schema(description = "scopes")
    private Set<String> scopes;
    @Schema(description = "重定向地址")
    private Set<String> redirectUris;
    @Schema(description = "client设置 key:value;")
    private Oauth2ClientSettings clientSettings;
    @Schema(description = "token设置 key:value;")
    private Oauth2TokenSettings tokenSettings;
    @Schema(description = "应用状态")
    private ClientStateEnum state;
}
