package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.json.jackson.annotation.EnumField.SerializerType;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import com.yunjin.org.pojo.enums.UserStateEnum;
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
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "性别")
    @EnumField(type = SerializerType.FULL)
    private UserGenderEnum gender;
    @Schema(value = "头像")
    private String head;
    @Schema(value = "邮箱")
    private String email;
    @Schema(value = "所属部门信息")
    private List<DepartmentDTO> departments;
    @Schema(value = "角色名称")
    private String roleName;
    @Schema(value = "管理员")
    private Boolean admin;
    @Schema(value = "电子签名")
    private String signature;
    @Schema(value = "最后登录时间")
    private LocalDateTime lastLogin;
    @EnumField(type = SerializerType.DESC)
    @Schema(value = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
}
