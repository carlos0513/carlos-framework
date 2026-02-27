package com.carlos.org.pojo.ao;

import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户部门 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDepartmentVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "用户id")
    private String userId;
    @Schema(description = "部门id")
    private String departmentId;
    @Schema(description = "租户id")
    private String tenantId;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "排序")
    private int sort;
    @Schema(description = "用户创建时间")
    private LocalDateTime createTime;
    @Schema(description = "是否是管理员")
    private Boolean admin;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "手机号码")
    private String phone;
    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(description = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(description = "用户角色")
    private String roleNames;
}
