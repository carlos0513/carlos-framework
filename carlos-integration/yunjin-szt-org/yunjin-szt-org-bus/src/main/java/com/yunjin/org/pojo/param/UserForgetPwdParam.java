package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 系统用户 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class UserForgetPwdParam {

    @NotBlank(message = "手机号码不能为空")
    @Schema(value = "手机号码")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @Schema(value = "验证码")
    private String code;

    @NotBlank(message = "密码不能为空")
    @Schema(value = "密码")
    private String pwd;

    @NotBlank(message = "再次输入密码不能为空")
    @Schema(value = "再次输入密码")
    private String pwd2;
}
