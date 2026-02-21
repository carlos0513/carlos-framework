package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.json.jackson.annotation.EnumField.SerializerType;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统用户卡片信息
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserFloatCardInfoVO implements Serializable {

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
    @Schema(value = "部门(角色拼接)")
    private String roleName;

    @Schema(value = "部门信息")
    List<DepartmentRole> depts;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class DepartmentRole implements Serializable {

        @Schema(value = "部门编码")
        private String deptCode;
        @Schema(value = "部门名称")
        private String deptName;
        @Schema(value = "角色名称")
        private String roleName;
    }

}
