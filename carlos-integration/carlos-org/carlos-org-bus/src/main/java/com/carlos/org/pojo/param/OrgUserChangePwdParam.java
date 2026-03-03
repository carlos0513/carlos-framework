package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改密码参数
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@Schema(description = "修改密码参数-UM009")
public class OrgUserChangePwdParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户ID")
    private String id;

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码（SM2加密后）")
    private String oldPwd;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码（SM2加密后）")
    private String newPwd;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码（SM2加密后）")
    private String confirmPwd;

}
