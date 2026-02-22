package com.carlos.org.pojo.ao;

import com.carlos.org.pojo.enums.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @Schema(description = "主键")
    private String id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "用户id")
    private String userId;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "管理员")
    private Boolean admin;
    @Schema(description = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(description = "用户创建时间")
    private LocalDateTime createTime;
    @Schema(description = "机构层级Code")
    private String departmentLevelCode;
    @Schema(description = "组织机构编码")
    private String deptCode;
    @Schema(description = "角色信息")
    private List<UserRoleAO> roleList;
    @Schema(description = "组织机构")
    private String deptNames;
    @Schema(description = "组织机构Code")
    private String departmentCode;
    @Schema(description = "组织机构")
    private String departmentName;
    @Schema(description = "组织机构id")
    private String departmentId;
    @Schema(description = "区域code")
    private String regionCode;
}
