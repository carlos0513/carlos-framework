package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 重置密码参数
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@Schema(description = "重置密码参数-UM008")
public class OrgUserResetPwdParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户ID")
    private String id;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码（SM2加密后）")
    private String newPwd;

}
