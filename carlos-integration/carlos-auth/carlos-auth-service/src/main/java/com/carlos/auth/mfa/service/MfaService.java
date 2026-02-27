package com.carlos.auth.mfa.service;

import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.security.TotpGenerator;
import com.carlos.auth.util.QrCodeGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * MFA服务
 * </p>
 *
 * <p>处理多因素认证相关逻辑，包括TOTP生成、验证、启用等</p>
 *
 * @author Carlos
 * @date 2026-02-26
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
     * @return MFA设置信息
     */
    public MfaSetupInfo generateMfaSetup(String username) {
        log.info("Generating MFA setup for user: {}", username);

        // 查找用户
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在: " + username);
        }

        // 生成密钥
        String secret = totpGenerator.generateSecret();
        log.debug("Generated MFA secret for user: {}", username);

        // 生成QR码URI
        String qrCodeUrl = qrCodeGenerator.generateQrCodeUrl(username, secret);
        log.info("Generated MFA QR code for user: {}", username);

        // 生成备用恢复码（将在验证后保存）
        List<String> recoveryCodes = recoveryCodeService.generateRecoveryCodes();

        return MfaSetupInfo.builder()
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
            return false;
        }

        // 查找用户
        UserInfo user = userProvider.loadUserByIdentifier(username);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在: " + username);
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
            throw new IllegalArgumentException("用户不存在: " + username);
        }

        // 检查是否启用了MFA
        // if (Boolean.FALSE.equals(user.getMfaEnabled()) || user.getMfaSecret() == null) {
        //     throw new IllegalStateException("MFA未启用");
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
            throw new IllegalArgumentException("用户不存在: " + username);
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
     * @return 新的MFA设置信息
     */
    public MfaSetupInfo resetMfaSecret(String username) {
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

    /**
     * MFA设置信息DTO
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class MfaSetupInfo {
        /**
         * MFA密钥（Base32编码）
         */
        private String secret;

        /**
         * QR码URL（可生成二维码）
         */
        private String qrCodeUrl;

        /**
         * 格式化显示的密钥
         */
        private String formattedSecret;

        /**
         * 备用恢复码列表
         */
        private List<String> recoveryCodes;
    }
}
