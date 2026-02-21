package com.yunjin.org.pojo.ao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.enums.UserStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentUserAO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "管理员")
    private Boolean admin;
    @Schema(value = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(value = "用户创建时间")
    private LocalDateTime createTime;
    @Schema(value = "机构层级Code")
    private String departmentLevelCode;
    @Schema(value = "组织机构编码")
    private String deptCode;
    @Schema(value = "角色信息")
    private List<UserRoleAO> roleList;
    @Schema(value = "组织机构")
    private String deptNames;
    @Schema(value = "组织机构Code")
    private String departmentCode;
    @Schema(value = "组织机构")
    private String departmentName;
    @Schema(value = "组织机构id")
    private String departmentId;
    @Schema(value = "区域code")
    private String regionCode;
}
