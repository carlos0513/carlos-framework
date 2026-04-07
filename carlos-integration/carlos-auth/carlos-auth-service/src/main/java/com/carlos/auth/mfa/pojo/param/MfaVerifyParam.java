package com.carlos.auth.mfa.pojo.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MFA 验证请求参数
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MfaVerifyParam {

    @NotBlank(message = "密钥不能为空")
    private String secret;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
