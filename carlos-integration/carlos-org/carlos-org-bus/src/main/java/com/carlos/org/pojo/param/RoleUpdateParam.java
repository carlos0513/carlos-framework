package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统角色修改参数")
public class RoleUpdateParam {

    @NotBlank(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;

//    @Schema(description = "用户部门权限列表")
//    private List<UserDeptRoleDTO> userDeptRoles;

    @Schema(description = "表单权限")
    private List<PermissionParam> permissions;

    @Schema(description = "组织机构id")
    @NotEmpty(message = "组织机构不能为空")
    private Set<String> departmentTypes;

    @Schema(description = "菜单id")
    private Set<String> menuIds;

    @NotEmpty(message = "pc菜单id不能为空")
    @Schema(description = "pc菜单id")
    private Set<String> pcMenuIds;

    @NotEmpty(message = "移动端菜单id不能为空")
    @Schema(description = "移动端菜单id")
    private Set<String> mobileMenuIds;

    @Schema(description = "管理端id")
    private Set<String> manageMenuIds;

    @NotEmpty(message = "资源组id不能为空")
    @Schema(description = "资源组id")
    private String resourceGroupId;

    @Data
    @Accessors(chain = true)
    public static class PermissionParam implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(description = "表id")
        private String tableId;
        @Schema(description = "操作类型ids")
        private Set<String> operateIds;

    }
}
