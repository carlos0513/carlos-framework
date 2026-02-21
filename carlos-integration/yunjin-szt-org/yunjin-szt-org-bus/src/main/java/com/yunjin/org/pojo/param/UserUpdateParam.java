package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 系统用户 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统用户修改参数", description = "系统用户修改参数")
public class UserUpdateParam {

    @NotBlank(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "密码")
    private String pwd;
    @Schema(value = "确认密码")
    private String confirmPwd;
    @Schema(value = "盐值")
    private String salt;
    @Schema(value = "证件号码")
    private String identify;
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "行政区域编码")
    private String regionCode;
    //    @Schema(value = "角色id")
//    private Set<String> roleIds;
//    @Schema(value = "部门id")
//    private Set<String> departmentIds;
    @Schema(value = "用户排序")
    private int sort;
    @NotEmpty(message = "用户至少有一个部门")
    @Schema(value = "部门角色")
    private List<UserDeptRoleDTO> deptRoles;
}
