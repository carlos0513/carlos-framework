package com.carlos.org.pojo.param;

import com.carlos.org.pojo.enums.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录参数
 *
 * @author carlos
 * @date 2019-05-15
 **/
@Data
@Schema(description = "登录参数")
public class ApiLoginParam {

    @NotBlank(message = "请输入账号")
    @Schema(description = "账号", example = "admin")
    private String account;

    @Schema(description = "密码", example = "123456")
    private String password;

    @NotBlank(message = "验证码Token不能为空")
    @Schema(description = "验证码Token")
    private String verifyToken;

    @NotBlank(message = "请输入验证码")
    @Schema(description = "验证码")
    private String code;

    @Schema(description = "登录方式，默认密码方式")
    private LoginType loginType = LoginType.PASSWORD;

}
