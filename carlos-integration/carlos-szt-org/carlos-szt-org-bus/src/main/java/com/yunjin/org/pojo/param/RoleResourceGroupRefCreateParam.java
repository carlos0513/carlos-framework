package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 角色资源组关联表 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "角色资源组关联表新增参数", description = "角色资源组关联表新增参数")
public class RoleResourceGroupRefCreateParam {
    @NotBlank(message = "角色id不能为空")
    @Schema(value = "角色id")
    private String roleId;
    @NotBlank(message = "资源组id不能为空")
    @Schema(value = "资源组id")
    private String resourceGroupId;
}
