package com.carlos.org.pojo.param;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "个人中心用户密码修改参数")
public class UserChangePwdParam {

    @Schema(description = "主键")
    @Hidden
    private String id;
    @Schema(description = "用户账号")
    private String account;
    @Schema(description = "旧密码")
    private String pwdOld;
    @Schema(description = "新密码")
    private String pwdNew;
}
