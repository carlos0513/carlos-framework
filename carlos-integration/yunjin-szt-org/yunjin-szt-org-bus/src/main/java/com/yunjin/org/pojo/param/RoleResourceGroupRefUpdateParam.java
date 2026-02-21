package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色资源组关联表 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "角色资源组关联表修改参数", description = "角色资源组关联表修改参数")
public class RoleResourceGroupRefUpdateParam {
    @NotBlank(message = "主键ID不能为空")
    @Schema(value = "主键ID")
    private String id;
    @Schema(value = "角色id")
    private String roleId;
    @Schema(value = "资源组id")
    private String resourceGroupId;
}
