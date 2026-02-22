package com.carlos.org.pojo.vo;

import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.json.jackson.annotation.EnumField.SerializerType;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.enums.UserGenderEnum;
import com.carlos.org.pojo.enums.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统用户 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "性别")
    @EnumField(type = SerializerType.FULL)
    private UserGenderEnum gender;
    @Schema(description = "头像")
    private String head;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "所属部门信息")
    private List<DepartmentDTO> departments;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "管理员")
    private Boolean admin;
    @Schema(description = "电子签名")
    private String signature;
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLogin;
    @EnumField(type = SerializerType.DESC)
    @Schema(description = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
}
