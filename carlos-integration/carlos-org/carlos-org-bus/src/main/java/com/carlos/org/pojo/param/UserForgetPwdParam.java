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
public class UserForgetPwdParam {

    @NotBlank(message = "手机号码不能为空")
    @Schema(description = "手机号码")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String code;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String pwd;

    @NotBlank(message = "再次输入密码不能为空")
    @Schema(description = "再次输入密码")
    private String pwd2;
}
