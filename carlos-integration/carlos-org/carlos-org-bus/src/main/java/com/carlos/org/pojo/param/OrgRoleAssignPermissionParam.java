package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 角色分配权限参数
 * </p>
 * <p>RM-007 配置角色权限</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgRoleAssignPermissionParam {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    @Schema(description = "角色id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Serializable roleId;

    /**
     * 权限id列表
     */
    @NotEmpty(message = "权限id列表不能为空")
    @Schema(description = "权限id列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Serializable> permissionIds;

}
