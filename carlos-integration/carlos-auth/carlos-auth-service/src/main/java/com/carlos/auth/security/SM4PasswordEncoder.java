package com.carlos.auth.security;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import com.carlos.core.exception.BusinessException;

/**
 * SM4 国密密码编码器
 *
 * <p>基于中国国家密码管理局发布的 SM4 分组密码算法实现的密码编码器。</p>
 *
 * <h3>算法特性：</h3>
 * <ul>
 *   <li>分组长度：128 位</li>
 *   <li>密钥长度：128 位</li>
 *   <li>迭代轮数：32 轮</li>
 *   <li>工作模式：ECB（配合随机盐值使用）</li>
 * </ul>
 *
 * <h3>加密方式：</h3>
 * <p>采用 "盐值 + SM4 加密 + Base64 编码" 的方式存储密码。</p>
 * <pre>
 * 存储格式：${salt}:${encrypted}
 * 示例：a1b2c3d4:SM4EncryptedBase64String
 * </pre>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     security:
 *       password-encoder: sm4
 *       sm4-key: ${SM4_SECRET_KEY:}  # 16字节密钥，建议从环境变量读取
 * }</pre>
 *
 * <h3>生产环境建议：</h3>
 * <ul>
 *   <li>密钥必须从安全的配置中心或环境变量读取</li>
 *   <li>定期更换密钥并迁移旧密码</li>
 *   <li>建议密钥长度恰好为 16 字节（128 位）</li>
 * </ul>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 * @see PasswordEncoder
 */
@Slf4j
public class SM4PasswordEncoder implements PasswordEncoder {

    private static final String PREFIX = "{SM4}";
    private static final int SALT_LENGTH = 16;
    private static final String SEPARATOR = ":";

    private final SymmetricCrypto sm4;

    /**
     * 使用默认密钥创建 SM4 编码器
     *
     * <p><strong>警告：</strong>仅用于测试，生产环境必须使用安全密钥</p>
     */
    public SM4PasswordEncoder() {
        this("CarlosSM4SecretKey");
        log.warn("Using default SM4 key. This is insecure for production use!");
    }

    /**
     * 使用指定密钥创建 SM4 编码器
     *
     * @param key SM4 密钥（16字节，不足会补零，超过会截断）
     */
    public SM4PasswordEncoder(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("SM4 key cannot be empty");
        }

        // 确保密钥长度为 16 字节
        byte[] keyBytes = normalizeKey(key);
        this.sm4 = SmUtil.sm4(keyBytes);

        log.info("SM4 Password Encoder initialized");
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }

        try {
            // 生成随机盐值
            String salt = generateSalt();

            // 盐值 + 密码
            String saltedPassword = salt + rawPassword.toString();

            // SM4 加密
            byte[] encrypted = sm4.encrypt(saltedPassword.getBytes(StandardCharsets.UTF_8));

            // Base64 编码
            String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

            // 存储格式：PREFIX${salt}:${encrypted}
            return PREFIX + salt + SEPARATOR + encryptedBase64;

        } catch (Exception e) {
            log.error("Failed to encode password with SM4", e);
            throw new BusinessException("Password encoding failed", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        try {
            // 检查前缀
            if (!encodedPassword.startsWith(PREFIX)) {
                log.warn("Encoded password does not have SM4 prefix, attempting BCrypt fallback");
                // 如果不是 SM4 格式，可能是旧数据，返回 false 让上层处理
                return false;
            }

            // 去除前缀
            String withoutPrefix = encodedPassword.substring(PREFIX.length());

            // 分割盐值和加密数据
            String[] parts = withoutPrefix.split(SEPARATOR, 2);
            if (parts.length != 2) {
                log.error("Invalid SM4 encoded password format");
                return false;
            }

            String salt = parts[0];
            String encryptedBase64 = parts[1];

            // 重新加密输入的密码
            String expectedEncoded = PREFIX + salt + SEPARATOR + 
                Base64.getEncoder().encodeToString(
                    sm4.encrypt((salt + rawPassword.toString()).getBytes(StandardCharsets.UTF_8))
                );

            // 时间安全的比较
            return constantTimeEquals(encodedPassword, expectedEncoded);

        } catch (Exception e) {
            log.error("Failed to match password with SM4", e);
            return false;
        }
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        // 如果密码不是 SM4 格式，建议升级
        return encodedPassword != null && !encodedPassword.startsWith(PREFIX);
    }

    /**
     * 生成随机盐值
     *
     * @return Base64 编码的盐值
     */
    private String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 标准化密钥长度为 16 字节
     *
     * @param key 原始密钥字符串
     * @return 16 字节密钥
     */
    private byte[] normalizeKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] normalized = new byte[16];

        if (keyBytes.length >= 16) {
            System.arraycopy(keyBytes, 0, normalized, 0, 16);
        } else {
            System.arraycopy(keyBytes, 0, normalized, 0, keyBytes.length);
            // 剩余部分保持为零
        }

        return normalized;
    }

    /**
     * 时间安全的字符串比较
     *
     * <p>防止定时攻击</p>
     *
     * @param a 字符串 a
     * @param b 字符串 b
     * @return true-相等
     */
    private boolean constantTimeEquals(String a, String b) {
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);

        if (aBytes.length != bBytes.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }

        return result == 0;
    }
}
