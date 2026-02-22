package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "系统用户新增参数")
public class UserCreateParam {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    // @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String pwd;
    @Schema(description = "证件号码")
    private String identify;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "管理员")
    private Boolean admin;
    //    @Schema(description = "行政区域")
//    private String regionCode;
//    @Schema(description = "角色id列表")
//    private Set<String> roleIds;
//    @Schema(description = "部门id列表")
//    private Set<String> departmentIds;
    @Schema(description = "用户排序")
    private int sort;

    @NotEmpty(message = "用户至少有一个部门")
    @Schema(description = "部门角色")
    private List<UserDeptRoleDTO> deptRoles;
}
