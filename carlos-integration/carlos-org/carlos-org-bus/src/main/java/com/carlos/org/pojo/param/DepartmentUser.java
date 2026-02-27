package com.carlos.org.pojo.param;

import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.json.jackson.annotation.EnumField.SerializerType;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "用户Id")
    private String userId;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "管理员")
    private Boolean admin;
    @EnumField(type = SerializerType.DESC)
    @Schema(description = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
}
