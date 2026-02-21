package com.yunjin.org.login.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 选择角色参数
 *
 * @author yunjin
 * @date 2019-05-15
 **/
@Data
@Schema("选择角色参数")
public class SelectRoleParam {

    @NotBlank(message = "请选择登录角色")
    @Schema(value = "角色")
    private String roleId;

}
