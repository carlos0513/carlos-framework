package com.yunjin.org.login.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录参数
 *
 * @author yunjin
 * @date 2023-06-29
 **/
@Data
public class EventLoginParam {

    @NotBlank(message = "token不能为空")
    @Schema(value = "token")
    private String token;


}
