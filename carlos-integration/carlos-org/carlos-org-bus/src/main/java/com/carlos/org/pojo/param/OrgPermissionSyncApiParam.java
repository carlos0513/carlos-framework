package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * <p>
 * 同步API权限参数
 * </p>
 * <p>PM-006 同步API权限</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgPermissionSyncApiParam {

    /**
     * API路径
     */
    @NotBlank(message = "API路径不能为空")
    @Schema(description = "API路径，如/org/user/add", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apiPath;

    /**
     * HTTP方法
     */
    @NotBlank(message = "HTTP方法不能为空")
    @Schema(description = "HTTP方法，如GET/POST/PUT/DELETE", requiredMode = Schema.RequiredMode.REQUIRED)
    private String method;

    /**
     * API描述
     */
    @Schema(description = "API描述")
    private String description;

    /**
     * 父权限ID（所属模块）
     */
    @Schema(description = "父权限ID（所属模块）")
    private Long parentId;

}
