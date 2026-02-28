package com.carlos.org.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统用户 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统用户列表查询参数")
public class OrgUserPageParam extends ParamPage {
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "昵称")
    private String nickname;
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
    @Schema(description = "最后登录ip")
    private String loginIp;
    @Schema(description = "登录次数")
    private Integer loginCount;
    @Schema(description = "密码最后修改时间")
    private LocalDateTime pwdLastModify;
    @Schema(description = "租户id")
    private Long tenantId;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
