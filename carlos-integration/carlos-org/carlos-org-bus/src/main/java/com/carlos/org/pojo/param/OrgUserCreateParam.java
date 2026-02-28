package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统用户 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统用户新增参数")
public class OrgUserCreateParam {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String account;
    @NotBlank(message = "昵称不能为空")
    @Schema(description = "昵称")
    private String nickname;
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String pwd;
    @Schema(description = "证件号码")
    private String identify;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "详细地址")
    private String address;
    @Schema(description = "性别，0：保密, 1：男，2：女，默认0")
    private Integer gender;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "状态，0：禁用，1：启用，2：锁定")
    private Integer state;
    @Schema(description = "主部门id")
    private Long mainDeptId;
    @Schema(description = "最后登录时间")
    private LocalDateTime loginTime;
    @NotBlank(message = "最后登录ip不能为空")
    @Schema(description = "最后登录ip")
    private String loginIp;
    @NotNull(message = "登录次数不能为空")
    @Schema(description = "登录次数")
    private Integer loginCount;
    @NotNull(message = "密码最后修改时间不能为空")
    @Schema(description = "密码最后修改时间")
    private LocalDateTime pwdLastModify;
    @Schema(description = "租户id")
    private Long tenantId;
}
