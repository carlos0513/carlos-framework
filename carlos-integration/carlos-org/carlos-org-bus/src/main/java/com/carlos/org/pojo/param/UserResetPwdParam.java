package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统用户 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统用户重置密码参数")
public class UserResetPwdParam {

    @NotBlank(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String pwd;
}
