package com.carlos.org.login.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 选择角色参数
 *
 * @author carlos
 * @date 2019-05-15
 **/
@Data
public class SelectRoleParam {

    @NotBlank(message = "请选择登录角色")
    @Schema(description = "角色")
    private String roleId;

}
