package com.yunjin.org.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(value = "个人中心用户密码修改参数", description = "个人中心用户密码修改参数")
public class UserChangePwdParam {

    @Schema(value = "主键", hidden = true)
    private String id;
    @Schema(value = "用户账号")
    private String account;
    @Schema(value = "旧密码")
    private String pwdOld;
    @Schema(value = "新密码")
    private String pwdNew;
}
