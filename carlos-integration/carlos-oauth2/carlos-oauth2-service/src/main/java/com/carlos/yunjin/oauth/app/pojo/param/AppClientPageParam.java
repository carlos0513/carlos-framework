package com.carlos.oauth.app.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 应用信息 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "应用信息列表查询参数", description = "应用信息列表查询参数")
public class AppClientPageParam extends ParamPage {
    @Schema(description = "应用编号")
    private String appKey;
    @Schema(description = "应用名称")
    private String appName;
    @Schema(description = "应用密钥到期时间")
    private LocalDateTime clientSecretExpiresAt;
    @Schema(description = "应用发行时间")
    private LocalDateTime clientIssuedAt;
    @Schema(description = "应用状态")
    private String state;
}
