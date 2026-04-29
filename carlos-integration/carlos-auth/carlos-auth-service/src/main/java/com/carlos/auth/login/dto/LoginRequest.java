package com.carlos.auth.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 登录请求
 * </p>
 *
 * <p>支持用户名、邮箱或手机号登录</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    /**
     * 用户名、邮箱或手机号（三选一）
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名、邮箱或手机号", example = "admin")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "admin123")
    private String password;

    /**
     * 验证码（可选，用于安全验证）
     */
    @Schema(description = "验证码（可选）", example = "123456")
    private String captcha;

    /**
     * 客户端ID（可选）
     */
    @Schema(description = "客户端ID（可选）", example = "carlos-client")
    private String clientId;

    /**
     * 密码是否已加密（SM2 前端加密标识）
     *
     * <p>当设置为 true 时，表示 password 字段已使用 SM2 公钥加密，
     * 后端需要使用 SM2 私钥解密后再进行密码校验。</p>
     */
    @Schema(description = "密码是否已使用 SM2 加密", example = "false")
    private Boolean encrypted;
}
