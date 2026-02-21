package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.json.jackson.annotation.EnumField.SerializerType;
import com.yunjin.org.pojo.enums.UserStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentUserVO implements Serializable {

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
    @EnumField(type = SerializerType.DESC)
    @Schema(value = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(value = "部门用户排序")
    private int sort;
}
