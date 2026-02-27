package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 系统用户 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统用户修改参数")
public class UserUpdateParam {

    @NotBlank(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "密码")
    private String pwd;
    @Schema(description = "确认密码")
    private String confirmPwd;
    @Schema(description = "盐值")
    private String salt;
    @Schema(description = "证件号码")
    private String identify;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "行政区域编码")
    private String regionCode;
    //    @Schema(description = "角色id")
//    private Set<String> roleIds;
//    @Schema(description = "部门id")
//    private Set<String> departmentIds;
    @Schema(description = "用户排序")
    private int sort;
    @NotEmpty(message = "用户至少有一个部门")
    @Schema(description = "部门角色")
    private List<UserDeptRoleDTO> deptRoles;
}
