package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色资源 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@Schema(value = "角色资源修改参数", description = "角色资源修改参数")
public class RoleResourceUpdateParam {

    @NotBlank(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
    @Schema(value = "角色id")
    private String roleId;
    @Schema(value = "资源id")
    private String resourceId;
    @Schema(value = "租户id")
    private String tenantId;
}
