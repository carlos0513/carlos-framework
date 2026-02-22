package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 用户导入 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户导入新增参数")
public class UserImportCreateParam {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String account;
    @NotBlank(message = "真实姓名不能为空")
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "证件号码")
    private String identify;
    @NotBlank(message = "手机号码不能为空")
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "角色名称")
    private String role;
    @Schema(description = "部门完整信息，以”-“分割部门级别")
    private String department;
    @Schema(description = "行政区域编码")
    private String regionCode;
    @Schema(description = "性别，0：保密, 1：男，2：女，默认0")
    private String gender;
    @Schema(description = "邮箱")
    private String email;
}
