package com.carlos.org.pojo.vo;

import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.json.jackson.annotation.EnumField.SerializerType;
import com.carlos.org.pojo.enums.UserGenderEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @Schema(description = "部门(角色拼接)")
    private String roleName;

    @Schema(description = "部门信息")
    List<DepartmentRole> depts;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class DepartmentRole implements Serializable {

        @Schema(description = "部门编码")
        private String deptCode;
        @Schema(description = "部门名称")
        private String deptName;
        @Schema(description = "角色名称")
        private String roleName;
    }

}
