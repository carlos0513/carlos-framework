package com.carlos.auth.mfa.controller;

import com.carlos.auth.mfa.convert.MfaConvert;
import com.carlos.auth.mfa.pojo.dto.MfaSetupDTO;
import com.carlos.auth.mfa.pojo.param.MfaVerifyParam;
import com.carlos.auth.mfa.pojo.vo.MfaSetupVO;
import com.carlos.auth.mfa.pojo.vo.MfaStatusVO;
import com.carlos.auth.mfa.service.MfaService;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * MFA 控制器
 *
 * <p>提供多因素认证相关接口，包括 MFA 设置、验证、启用/禁用等。</p>
 *
 * <p><strong>分层职责：</strong></p>
 * <ul>
 *   <li>Controller (此类): 接收 Web 请求，参数校验，调用 Service</li>
 *   <li>Service ({@link com.carlos.auth.mfa.service.MfaService}): 处理业务逻辑</li>
 *   <li>Manager ({@link com.carlos.auth.mfa.manager.MfaManager}): 数据查询封装</li>
 *   <li>Mapper: 数据访问</li>
 * </ul>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-02-26
 * @see com.carlos.auth.mfa.service.MfaService
 * @see com.carlos.auth.mfa.manager.MfaManager
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/mfa")
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
    public Result<MfaSetupVO> setupMfa(Authentication authentication) {
        String username = authentication.getName();
        log.info("MFA setup requested for user: {}", username);

        try {
            MfaSetupDTO setupDTO = mfaService.generateMfaSetup(username);
            MfaSetupVO response = MfaConvert.INSTANCE.dtoToVo(setupDTO);

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
     * @param param 验证请求参数
     * @return 验证结果
     */
    @Operation(summary = "验证并启用MFA", description = "验证6位验证码，验证通过后启用MFA")
    @PostMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public Result<String> verifyMfa(Authentication authentication, @Valid @RequestBody MfaVerifyParam param) {
        String username = authentication.getName();
        log.info("MFA verification requested for user: {}", username);

        try {
            boolean success = mfaService.enableMfa(username, param.getSecret(), param.getCode());

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
    public Result<MfaSetupVO> resetMfa(Authentication authentication) {
        String username = authentication.getName();
        log.info("MFA reset requested for user: {}", username);

        try {
            MfaSetupDTO setupDTO = mfaService.resetMfaSecret(username);
            MfaSetupVO response = MfaConvert.INSTANCE.dtoToVo(setupDTO);

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
    public Result<MfaStatusVO> getMfaStatus(Authentication authentication) {
        String username = authentication.getName();

        try {
            boolean enabled = mfaService.isMfaEnabled(username);
            MfaStatusVO response = MfaConvert.INSTANCE.buildStatusVO(enabled);

            return Result.success(response);
        } catch (Exception e) {
            log.error("Failed to get MFA status for user: {}", username, e);
            return Result.error("获取MFA状态失败: " + e.getMessage());
        }
    }
}
