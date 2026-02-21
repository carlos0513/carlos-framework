package com.yunjin.org.pojo.param;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门 更新参数封装
 * </p>
 *
 * @author yunjin
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentUserListParam implements Serializable {

    @NotBlank(message = "主键不能为空")
    @Schema(value = "部门id")
    private String id;

    @Schema(value = "人员信息")
    private List<DepartmentUserModify> users;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class DepartmentUserModify implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(value = "主键")
        private String id;
        @Schema(value = "用户名")
        private String account;
        @Schema(value = "真实姓名")
        private String realname;
        @Schema(value = "手机号码")
        private String phone;
        @Schema(value = "管理员")
        private Boolean admin;
    }


}
