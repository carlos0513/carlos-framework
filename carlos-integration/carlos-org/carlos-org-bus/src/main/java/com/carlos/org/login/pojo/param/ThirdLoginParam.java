package com.carlos.org.login.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

/**
 * 第三方登录参数
 *
 * @author yunjin
 * @date 2019-05-15
 **/
@Data
public class ThirdLoginParam {

    @NotBlank(message = "登录方式不能为空")
    @Schema(description = "loginType,登录方式")
    private String loginType;
    //@NotBlank(message = "第三方登录参数不能为空")
    @Schema(description = "param第三方登录参数")
    private Map<String, Object> param;


}
