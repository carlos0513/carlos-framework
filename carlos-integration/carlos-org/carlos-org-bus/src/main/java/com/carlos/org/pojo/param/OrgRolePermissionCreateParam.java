package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 角色权限 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "角色权限新增参数")
public class OrgRolePermissionCreateParam {
    @NotNull(message = "角色id不能为空")
    @Schema(description = "角色id")
    private Long roleId;
    @NotNull(message = "权限id不能为空")
    @Schema(description = "权限id")
    private Long permissionId;
    @Schema(description = "租户id")
    private Long tenantId;
}
