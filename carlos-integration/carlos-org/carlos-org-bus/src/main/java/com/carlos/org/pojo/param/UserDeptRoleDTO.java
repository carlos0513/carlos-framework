package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * <p>
 * 系统用户 新增参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统用户部门角色")
public class UserDeptRoleDTO {

    @Schema(description = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @Schema(description = "部门id")
    @NotBlank(message = "部门不能为空")
    private String departmentId;
    @Schema(description = "角色id")
    @NotBlank(message = "角色id不能为空")
    private String roleId;

    @Schema(description = "全部父级,依次排序(包含自己)")
    private List<String> allParentIds;

}
