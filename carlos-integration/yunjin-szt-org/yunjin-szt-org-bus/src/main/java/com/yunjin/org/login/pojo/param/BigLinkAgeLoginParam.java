package com.yunjin.org.login.pojo.param;

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
public class BigLinkAgeLoginParam {

    @NotBlank(message = "userId不能为空")
    @Schema(value = "userid")
    private String userid;


}
