package com.carlos.auth.mfa;

import com.carlos.auth.mfa.service.MfaService;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * MFA控制器
 * </p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mfa")
@Tag(name = "多因素认证(MFA)", description = "MFA设置、验证、恢复码管理")
public class MfaController {

    private final MfaService mfaService;

    /**
     * 获取MFA设置信息（密钥和QR码）
     *
     * @param authentication 当前认证信息
     * @return MFA设置信息
     */
    @Operation(summary = "获取MFA设置信息", description = "生成MFA密钥和QR码，用于绑定Google Authenticator")
    @PostMapping("/setup")
    @PreAuthorize("isAuthenticated()")
    public Result<MfaSetupResponse> setupMfa(Authentication authentication) {
        String username = authentication.getName();
        log.info("MFA setup requested for user: {}", username);

        try {
            MfaService.MfaSetupInfo setupInfo = mfaService.generateMfaSetup(username);

            MfaSetupResponse response = MfaSetupResponse.builder()
                .secret(setupInfo.getSecret())
                .qrCodeUrl(setupInfo.getQrCodeUrl())
                .formattedSecret(setupInfo.getFormattedSecret())
                .recoveryCodes(setupInfo.getRecoveryCodes())
                .build();

            return Result.success(response, "MFA设置信息生成成功");
        } catch (Exception e) {
            log.error("Failed to generate MFA setup for user: {}", username, e);
            return Result.error("MFA设置信息生成失败: " + e.getMessage());
        }
    }

    /**
     * 验证并启用MFA
     *
     * @param authentication 当前认证信息
     * @param request 验证请求
     * @return 验证结果
     */
    @Operation(summary = "验证并启用MFA", description = "验证6位验证码，验证通过后启用MFA")
    @PostMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public Result<String> verifyMfa(Authentication authentication, @Valid @RequestBody MfaVerifyRequest request) {
        String username = authentication.getName();
        log.info("MFA verification requested for user: {}", username);

        try {
            boolean success = mfaService.enableMfa(username, request.getSecret(), request.getCode());

            if (success) {
                log.info("MFA enabled successfully for user: {}", username);
                return Result.success("MFA启用成功");
            } else {
                log.warn("MFA verification failed for user: {}", username);
                return Result.error("验证码错误，请重试");
            }
        } catch (Exception e) {
            log.error("Failed to verify MFA for user: {}", username, e);
            return Result.error("MFA验证失败: " + e.getMessage());
        }
    }

    /**
     * 禁用MFA
     *
     * @param authentication 当前认证信息
     * @return 禁用结果
     */
    @Operation(summary = "禁用MFA", description = "禁用MFA功能")
    @PostMapping("/disable")
    @PreAuthorize("isAuthenticated()")
    public Result<String> disableMfa(Authentication authentication) {
        String username = authentication.getName();
        log.info("MFA disable requested for user: {}", username);

        try {
            mfaService.disableMfa(username);
            log.info("MFA disabled successfully for user: {}", username);
            return Result.success("MFA禁用成功");
        } catch (Exception e) {
            log.error("Failed to disable MFA for user: {}", username, e);
            return Result.error("MFA禁用失败: " + e.getMessage());
        }
    }

    /**
     * 重新生成MFA密钥
     *
     * @param authentication 当前认证信息
     * @return 新的MFA设置信息
     */
    @Operation(summary = "重新生成MFA密钥", description = "重置MFA密钥，需要重新绑定验证器")
    @PostMapping("/reset")
    @PreAuthorize("isAuthenticated()")
    public Result<MfaSetupResponse> resetMfa(Authentication authentication) {
        String username = authentication.getName();
        log.info("MFA reset requested for user: {}", username);

        try {
            MfaService.MfaSetupInfo setupInfo = mfaService.resetMfaSecret(username);

            MfaSetupResponse response = MfaSetupResponse.builder()
                .secret(setupInfo.getSecret())
                .qrCodeUrl(setupInfo.getQrCodeUrl())
                .formattedSecret(setupInfo.getFormattedSecret())
                .recoveryCodes(setupInfo.getRecoveryCodes())
                .build();

            return Result.success(response, "MFA密钥重置成功");
        } catch (Exception e) {
            log.error("Failed to reset MFA for user: {}", username, e);
            return Result.error("MFA密钥重置失败: " + e.getMessage());
        }
    }

    /**
     * 检查MFA状态
     *
     * @param authentication 当前认证信息
     * @return MFA状态
     */
    @Operation(summary = "检查MFA状态", description = "检查当前用户是否启用了MFA")
    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public Result<MfaStatusResponse> getMfaStatus(Authentication authentication) {
        String username = authentication.getName();

        try {
            boolean enabled = mfaService.isMfaEnabled(username);

            MfaStatusResponse response = MfaStatusResponse.builder()
                .enabled(enabled)
                .build();

            return Result.success(response);
        } catch (Exception e) {
            log.error("Failed to get MFA status for user: {}", username, e);
            return Result.error("获取MFA状态失败: " + e.getMessage());
        }
    }

    /**
     * MFA设置响应
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MfaSetupResponse {
        private String secret;
        private String qrCodeUrl;
        private String formattedSecret;
        private java.util.List<String> recoveryCodes;
    }

    /**
     * MFA验证请求
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MfaVerifyRequest {
        @NotBlank(message = "密钥不能为空")
        private String secret;

        @NotBlank(message = "验证码不能为空")
        private String code;
    }

    /**
     * MFA状态响应
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MfaStatusResponse {
        private boolean enabled;
    }
}
