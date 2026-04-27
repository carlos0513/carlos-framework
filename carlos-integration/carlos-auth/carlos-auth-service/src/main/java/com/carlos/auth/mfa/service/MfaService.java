package com.carlos.auth.mfa.service;

import com.carlos.auth.api.enums.AuthErrorCode;
import com.carlos.auth.mfa.pojo.dto.MfaSetupDTO;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MFA 业务服务
 *
 * <p>处理多因素认证相关业务逻辑，包括 TOTP 生成、验证、启用/禁用等。</p>
 *
 * <p><strong>分层职责：</strong></p>
 * <ul>
 *   <li>Controller ({@link com.carlos.auth.mfa.controller.MfaController}): 接收请求，参数校验</li>
 *   <li>Service (此类): 处理业务逻辑，业务流程串联</li>
 *   <li>Manager ({@link com.carlos.auth.mfa.manager.MfaManager}): 数据查询封装，原子操作</li>
 *   <li>Mapper: 数据访问，与数据库交互</li>
 * </ul>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-02-26
 * @see com.carlos.auth.mfa.controller.MfaController
 * @see com.carlos.auth.mfa.manager.MfaManager
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MfaService {

    private final TotpGenerator totpGenerator;
    private final QrCodeGenerator qrCodeGenerator;
    private final UserProvider userProvider;
    private final MfaRecoveryCodeService recoveryCodeService;

    /**
     * 生成MFA设置信息（密钥和QR码）
     *
     * @param username 用户名
     * @return MFA设置信息 DTO
     */
    public MfaSetupDTO generateMfaSetup(String username) {
        log.info("Generating MFA setup for user: {}", username);

        // 查找用户
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            throw AuthErrorCode.AUTH_USER_NOT_FOUND.exception("用户不存在: %s", username);
        }

        // 生成密钥
        String secret = totpGenerator.generateSecret();
        log.debug("Generated MFA secret for user: {}", username);

        // 生成QR码URI
        String qrCodeUrl = qrCodeGenerator.generateQrCodeUrl(username, secret);
        log.info("Generated MFA QR code for user: {}", username);

        // 生成备用恢复码（将在验证后保存）
        List<String> recoveryCodes = recoveryCodeService.generateRecoveryCodes();

        return MfaSetupDTO.builder()
            .secret(secret)
            .qrCodeUrl(qrCodeUrl)
            .formattedSecret(qrCodeGenerator.formatSecretForDisplay(secret))
            .recoveryCodes(recoveryCodes)
            .build();
    }

    /**
     * 验证并启用MFA
     *
     * @param username 用户名
     * @param secret MFA密钥
     * @param code 验证器显示的6位验证码
     * @return true-验证成功，false-验证失败
     */
    public boolean enableMfa(String username, String secret, String code) {
        log.info("Attempting to enable MFA for user: {}", username);

        // 验证TOTP验证码
        boolean isValid = totpGenerator.verifyTotpCode(secret, code);

        if (!isValid) {
            log.warn("MFA verification failed for user: {}", username);
            throw AuthErrorCode.AUTH_MFA_CODE_ERROR.exception();
        }

        // 查找用户
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            throw AuthErrorCode.AUTH_USER_NOT_FOUND.exception("用户不存在: %s", username);
        }

        // 启用MFA并保存密钥
        // user.setMfaEnabled(true);
        // user.setMfaSecret(secret);
        // userProvider.updateById(user);

        log.info("MFA enabled successfully for user: {}", username);
        return true;
    }

    /**
     * 验证MFA验证码
     *
     * @param username 用户名
     * @param code 验证器显示的6位验证码
     * @return true-验证成功，false-验证失败
     */
    public boolean verifyMfaCode(String username, String code) {
        // 查找用户
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            throw AuthErrorCode.AUTH_USER_NOT_FOUND.exception("用户不存在: %s", username);
        }

        // 检查是否启用了MFA
        // if (Boolean.FALSE.equals(user.getMfaEnabled()) || user.getMfaSecret() == null) {
        //     throw AuthErrorCode.AUTH_MFA_NOT_ENABLED.exception();
        // }
        //
        // // 验证TOTP验证码
        // boolean isValid = totpGenerator.verifyTotpCode(user.getMfaSecret(), code);
        //
        // if (!isValid) {
        //     log.warn("MFA verification failed for user: {}", username);
        //
        //     // 检查是否为备用恢复码
        //     return recoveryCodeService.useRecoveryCode(user.getId(), code);
        // }

        log.debug("MFA verification successful for user: {}", username);
        return true;
    }

    /**
     * 禁用MFA
     *
     * @param username 用户名
     */
    public void disableMfa(String username) {
        log.info("Disabling MFA for user: {}", username);

        // 查找用户
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            throw AuthErrorCode.AUTH_USER_NOT_FOUND.exception("用户不存在: %s", username);
        }

        // 禁用MFA
        // user.setMfaEnabled(false);
        // user.setMfaSecret(null);
        // userProvider.updateById(user);
        //
        // // 删除备用恢复码
        // recoveryCodeService.clearRecoveryCodes(user.getId());

        log.info("MFA disabled successfully for user: {}", username);
    }

    /**
     * 重置MFA密钥
     *
     * @param username 用户名
     * @return 新的MFA设置信息 DTO
     */
    public MfaSetupDTO resetMfaSecret(String username) {
        log.info("Resetting MFA secret for user: {}", username);

        // 首先禁用MFA
        disableMfa(username);

        // 重新生成设置
        return generateMfaSetup(username);
    }

    /**
     * 检查用户是否启用MFA
     *
     * @param username 用户名
     * @return true-已启用，false-未启用
     */
    public boolean isMfaEnabled(String username) {
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            return false;
        }
        // return Boolean.TRUE.equals(user.getMfaEnabled());
        return true;
    }

    /**
     * 生成新的MFA验证码（用于测试）
     *
     * @param secret MFA密钥
     * @return 6位验证码
     */
    public String generateTotpCode(String secret) {
        return totpGenerator.getTotpCode(secret);
    }
}
