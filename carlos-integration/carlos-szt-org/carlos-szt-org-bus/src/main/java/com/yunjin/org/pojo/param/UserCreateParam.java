package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * <p>
 * 系统用户 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统用户新增参数", description = "系统用户新增参数")
public class UserCreateParam {

    @NotBlank(message = "用户名不能为空")
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    // @NotBlank(message = "密码不能为空")
    @Schema(value = "密码")
    private String pwd;
    @Schema(value = "证件号码")
    private String identify;
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "管理员")
    private Boolean admin;
    //    @Schema(value = "行政区域")
//    private String regionCode;
//    @Schema(value = "角色id列表")
//    private Set<String> roleIds;
//    @Schema(value = "部门id列表")
//    private Set<String> departmentIds;
    @Schema(value = "用户排序")
    private int sort;

    @NotEmpty(message = "用户至少有一个部门")
    @Schema(value = "部门角色")
    private List<UserDeptRoleDTO> deptRoles;
}
