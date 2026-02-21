package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 系统角色 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统角色新增参数", description = "系统角色新增参数")
public class RoleCreateParam {

    @NotBlank(message = "角色名称不能为空")
    @Schema(value = "角色名称")
    private String name;

    @Schema(value = "用户部门权限列表")
    private List<UserDeptRoleDTO> userDeptRoles;

    @Schema(value = "表单权限")
    private List<PermissionParam> permissions;

    @Schema(value = "组织机构")
    @NotEmpty(message = "组织机构层级不能为空")
    private Set<String> departmentTypes;

    @Schema(value = "菜单id")
    private Set<String> menuIds;

    @NotEmpty(message = "pc菜单id不能为空")
    @Schema(value = "pc菜单id")
    private Set<String> pcMenuIds;

    @NotEmpty(message = "移动端菜单id不能为空")
    @Schema(value = "移动端菜单id")
    private Set<String> mobileMenuIds;

    @NotEmpty(message = "资源组id不能为空")
    @Schema(value = "资源组id")
    private String resourceGroupId;

    @Data
    @Accessors(chain = true)
    public static class PermissionParam implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(value = "表id")
        private String tableId;
        @Schema(value = "操作类型ids")
        private Set<String> operateIds;

    }

}
