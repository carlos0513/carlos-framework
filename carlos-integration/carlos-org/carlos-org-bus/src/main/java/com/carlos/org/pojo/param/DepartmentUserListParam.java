package com.carlos.org.pojo.param;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门 更新参数封装
 * </p>
 *
 * @author carlos
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentUserListParam implements Serializable {

    @NotBlank(message = "主键不能为空")
    @Schema(description = "部门id")
    private String id;

    @Schema(description = "人员信息")
    private List<DepartmentUserModify> users;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class DepartmentUserModify implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(description = "主键")
        private String id;
        @Schema(description = "用户名")
        private String account;
        @Schema(description = "真实姓名")
        private String realname;
        @Schema(description = "手机号码")
        private String phone;
        @Schema(description = "管理员")
        private Boolean admin;
    }


}
