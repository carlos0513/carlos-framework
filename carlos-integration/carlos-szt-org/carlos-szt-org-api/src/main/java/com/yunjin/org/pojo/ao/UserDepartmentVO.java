package com.yunjin.org.pojo.ao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.org.pojo.enums.UserStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户部门 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDepartmentVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "部门id")
    private String departmentId;
    @Schema(value = "租户id")
    private String tenantId;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "排序")
    private int sort;
    @Schema(value = "用户创建时间")
    private LocalDateTime createTime;
    @Schema(value = "是否是管理员")
    private Boolean admin;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "手机号码")
    private String phone;
    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(value = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(value = "用户角色")
    private String roleNames;
}
