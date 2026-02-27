package com.carlos.auth.mfa.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.carlos.auth.mfa.entity.MfaRecoveryCode;
import com.carlos.auth.mfa.mapper.MfaRecoveryCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * MFA备用恢复码服务
 * </p>
 *
 * <p>管理MFA备用恢复码的生成、验证和使用</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MfaRecoveryCodeService {

    private final MfaRecoveryCodeMapper recoveryCodeMapper;

    /**
     * 生成备用恢复码
     *
     * @return 10组8位随机码列表
     */
    public List<String> generateRecoveryCodes() {
        List<String> codes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            // 生成8位随机码（字母数字混合）
            String code = RandomUtil.randomString(8).toUpperCase();
            codes.add(code);
        }

        log.debug("Generated 10 recovery codes");
        return codes;
    }

    /**
     * 保存用户的备用恢复码
     *
     * @param userId 用户ID
     * @param recoveryCodes 恢复码列表
     */
    public void saveRecoveryCodes(Long userId, List<String> recoveryCodes) {
        if (userId == null || recoveryCodes == null || recoveryCodes.isEmpty()) {
            return;
        }

        // 删除旧的恢复码
        clearRecoveryCodes(userId);

        // 保存新的恢复码
        List<MfaRecoveryCode> entities = recoveryCodes.stream()
                .map(code -> {
                    MfaRecoveryCode entity = new MfaRecoveryCode();
                    entity.setUserId(userId);
                    entity.setCode(code);
                    entity.setUsed(false);
                    return entity;
                })
                .collect(Collectors.toList());

        // 批量保存
        for (MfaRecoveryCode entity : entities) {
            recoveryCodeMapper.insert(entity);
        }

        log.info("Saved {} recovery codes for user: {}", recoveryCodes.size(), userId);
    }

    /**
     * 使用恢复码
     *
     * @param userId 用户ID
     * @param code 恢复码
     * @return true-使用成功，false-恢复码无效或已使用
     */
    public boolean useRecoveryCode(Long userId, String code) {
        if (userId == null || code == null) {
            return false;
        }

        // 查找未使用的恢复码
        LambdaQueryWrapper<MfaRecoveryCode> queryWrapper = new LambdaQueryWrapper<MfaRecoveryCode>()
                .eq(MfaRecoveryCode::getUserId, userId)
                .eq(MfaRecoveryCode::getCode, code.toUpperCase())
                .eq(MfaRecoveryCode::getUsed, false);

        MfaRecoveryCode recoveryCode = recoveryCodeMapper.selectOne(queryWrapper);

        if (recoveryCode == null) {
            log.warn("Invalid or already used recovery code for user: {}", userId);
            return false;
        }

        // 标记为已使用
        LambdaUpdateWrapper<MfaRecoveryCode> updateWrapper = new LambdaUpdateWrapper<MfaRecoveryCode>()
                .eq(MfaRecoveryCode::getId, recoveryCode.getId())
                .set(MfaRecoveryCode::getUsed, true)
                .set(MfaRecoveryCode::getUsedTime, LocalDateTime.now());

        recoveryCodeMapper.update(null, updateWrapper);

        log.info("Recovery code used for user: {}", userId);
        return true;
    }

    /**
     * 验证恢复码（不标记为已使用）
     *
     * @param userId 用户ID
     * @param code 恢复码
     * @return true-验证通过，false-恢复码无效或已使用
     */
    public boolean verifyRecoveryCode(Long userId, String code) {
        if (userId == null || code == null) {
            return false;
        }

        LambdaQueryWrapper<MfaRecoveryCode> queryWrapper = new LambdaQueryWrapper<MfaRecoveryCode>()
                .eq(MfaRecoveryCode::getUserId, userId)
                .eq(MfaRecoveryCode::getCode, code.toUpperCase())
                .eq(MfaRecoveryCode::getUsed, false);

        Long count = recoveryCodeMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }

    /**
     * 获取用户的未使用恢复码
     *
     * @param userId 用户ID
     * @return 未使用的恢复码列表
     */
    public List<String> getUnusedRecoveryCodes(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<MfaRecoveryCode> queryWrapper = new LambdaQueryWrapper<MfaRecoveryCode>()
                .eq(MfaRecoveryCode::getUserId, userId)
                .eq(MfaRecoveryCode::getUsed, false);

        List<MfaRecoveryCode> codes = recoveryCodeMapper.selectList(queryWrapper);

        return codes.stream()
                .map(MfaRecoveryCode::getCode)
                .collect(Collectors.toList());
    }

    /**
     * 检查恢复码是否可用
     *
     * @param userId 用户ID
     * @return true-有可用恢复码，false-没有可用恢复码
     */
    public boolean hasAvailableRecoveryCode(Long userId) {
        if (userId == null) {
            return false;
        }

        LambdaQueryWrapper<MfaRecoveryCode> queryWrapper = new LambdaQueryWrapper<MfaRecoveryCode>()
                .eq(MfaRecoveryCode::getUserId, userId)
                .eq(MfaRecoveryCode::getUsed, false);

        Long count = recoveryCodeMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }

    /**
     * 清除用户的所有恢复码
     *
     * @param userId 用户ID
     */
    public void clearRecoveryCodes(Long userId) {
        if (userId == null) {
            return;
        }

        LambdaQueryWrapper<MfaRecoveryCode> queryWrapper = new LambdaQueryWrapper<MfaRecoveryCode>()
                .eq(MfaRecoveryCode::getUserId, userId);

        recoveryCodeMapper.delete(queryWrapper);

        log.info("Cleared all recovery codes for user: {}", userId);
    }
}
