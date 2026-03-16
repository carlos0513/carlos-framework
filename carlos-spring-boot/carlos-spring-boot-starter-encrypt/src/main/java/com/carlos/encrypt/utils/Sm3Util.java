package com.carlos.encrypt.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.carlos.encrypt.exception.EncryptException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * SM3 国密哈希工具类
 * </p>
 *
 * <p>
 * SM3 是中国国家密码管理局发布的密码学哈希函数，产生 256 位（32 字节）哈希值。
 * 安全性与 SHA-256 相当，适用于数字签名、数据完整性校验等场景。
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class Sm3Util {

    private Sm3Util() {
        // 工具类禁止实例化
    }

    // ==================== 基础哈希 ====================

    /**
     * 计算字符串的 SM3 值
     *
     * @param data 待计算字符串
     * @return SM3 值（64 位 Hex）
     */
    public static String sm3(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return SmUtil.sm3(data);
    }

    /**
     * 计算字节数组的 SM3 值
     *
     * @param data 待计算字节数组
     * @return SM3 值（64 位 Hex）
     */
    public static String sm3(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        SM3Digest digest = new SM3Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return Hex.toHexString(hash);
    }

    // ==================== 文件哈希 ====================

    /**
     * 计算文件的 SM3 值
     *
     * @param file 文件
     * @return SM3 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String sm3(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        try {
            return DigestUtil.digestHex(file, "SM3");
        } catch (Exception e) {
            throw new EncryptException("文件 SM3 计算失败", e);
        }
    }

    /**
     * 计算输入流的 SM3 值
     *
     * @param inputStream 输入流
     * @return SM3 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String sm3(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            return DigestUtil.digestHex(inputStream, "SM3");
        } catch (Exception e) {
            throw new EncryptException("流 SM3 计算失败", e);
        }
    }

    // ==================== 加盐哈希 ====================

    /**
     * 计算加盐 SM3
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 SM3 值
     */
    public static String sm3WithSalt(String data, String salt) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return sm3(data + salt);
    }

    /**
     * 计算加盐 SM3（盐值在前后各加一次）
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 SM3 值
     */
    public static String sm3WithSaltDouble(String data, String salt) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return sm3(salt + data + salt);
    }

    // ==================== HMAC-SM3 ====================

    /**
     * 计算 HMAC-SM3
     *
     * @param data 待计算字符串
     * @param key  密钥
     * @return HMAC-SM3 值
     */
    public static String hmacSm3(String data, String key) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        try {
            return DigestUtil.hmacHex(data, key.getBytes(StandardCharsets.UTF_8), "SM3");
        } catch (Exception e) {
            throw new EncryptException("HMAC-SM3 计算失败", e);
        }
    }

    // ==================== 验证方法 ====================

    /**
     * 验证 SM3
     *
     * @param data     原始数据
     * @param expected 预期的 SM3 值
     * @return 是否匹配
     */
    public static boolean verify(String data, String expected) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(expected)) {
            return false;
        }
        return sm3(data).equalsIgnoreCase(expected);
    }

    /**
     * 验证加盐 SM3
     *
     * @param data     原始数据
     * @param salt     盐值
     * @param expected 预期的 SM3 值
     * @return 是否匹配
     */
    public static boolean verifyWithSalt(String data, String salt, String expected) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(expected)) {
            return false;
        }
        return sm3WithSalt(data, salt).equalsIgnoreCase(expected);
    }
}
