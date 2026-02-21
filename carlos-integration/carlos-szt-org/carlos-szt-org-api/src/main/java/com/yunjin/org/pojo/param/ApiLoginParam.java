package com.yunjin.org.pojo.param;

import com.yunjin.org.pojo.enums.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录参数
 *
 * @author yunjin
 * @date 2019-05-15
 **/
@Data
@Schema("登录参数")
public class ApiLoginParam {

    @NotBlank(message = "请输入账号")
    @Schema(value = "账号", example = "admin")
    private String account;

    @Schema(value = "密码", example = "123456")
    private String password;

    @NotBlank(message = "验证码Token不能为空")
    @Schema(value = "验证码Token")
    private String verifyToken;

    @NotBlank(message = "请输入验证码")
    @Schema(value = "验证码")
    private String code;

    @Schema(value = "登录方式，默认密码方式")
    private LoginType loginType = LoginType.PASSWORD;

}
