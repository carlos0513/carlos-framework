package com.carlos.encrypt.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import com.carlos.encrypt.exception.EncryptException;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * SHA 哈希工具类
 * </p>
 *
 * <p>
 * SHA (Secure Hash Algorithm) 是一系列密码学哈希函数，包括 SHA-1、SHA-256、SHA-384、SHA-512 等。
 * 广泛应用于数据完整性校验、数字签名等场景。
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class ShaUtil {

    private ShaUtil() {
        // 工具类禁止实例化
    }

    // ==================== SHA-1 ====================

    /**
     * 计算字符串的 SHA-1 值
     *
     * @param data 待计算字符串
     * @return SHA-1 值（40 位 Hex）
     */
    public static String sha1(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.sha1Hex(data);
    }

    /**
     * 计算字节数组的 SHA-1 值
     *
     * @param data 待计算字节数组
     * @return SHA-1 值（40 位 Hex）
     */
    public static String sha1(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return DigestUtil.sha1Hex(data);
    }

    /**
     * 计算文件的 SHA-1 值
     *
     * @param file 文件
     * @return SHA-1 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String sha1(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        try {
            return DigestUtil.sha1Hex(file);
        } catch (Exception e) {
            throw new EncryptException("文件 SHA-1 计算失败", e);
        }
    }

    // ==================== SHA-256 ====================

    /**
     * 计算字符串的 SHA-256 值
     *
     * @param data 待计算字符串
     * @return SHA-256 值（64 位 Hex）
     */
    public static String sha256(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.sha256Hex(data);
    }

    /**
     * 计算字节数组的 SHA-256 值
     *
     * @param data 待计算字节数组
     * @return SHA-256 值（64 位 Hex）
     */
    public static String sha256(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return DigestUtil.sha256Hex(data);
    }

    /**
     * 计算文件的 SHA-256 值
     *
     * @param file 文件
     * @return SHA-256 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String sha256(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        try {
            return DigestUtil.sha256Hex(file);
        } catch (Exception e) {
            throw new EncryptException("文件 SHA-256 计算失败", e);
        }
    }

    /**
     * 计算输入流的 SHA-256 值
     *
     * @param inputStream 输入流
     * @return SHA-256 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String sha256(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            return DigestUtil.sha256Hex(inputStream);
        } catch (Exception e) {
            throw new EncryptException("流 SHA-256 计算失败", e);
        }
    }

    // ==================== SHA-512 ====================

    /**
     * 计算字符串的 SHA-512 值
     *
     * @param data 待计算字符串
     * @return SHA-512 值（128 位 Hex）
     */
    public static String sha512(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.sha512Hex(data);
    }

    /**
     * 计算字节数组的 SHA-512 值
     *
     * @param data 待计算字节数组
     * @return SHA-512 值（128 位 Hex）
     */
    public static String sha512(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return DigestUtil.sha512Hex(data);
    }

    /**
     * 计算文件的 SHA-512 值
     *
     * @param file 文件
     * @return SHA-512 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String sha512(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        try {
            return DigestUtil.sha512Hex(file);
        } catch (Exception e) {
            throw new EncryptException("文件 SHA-512 计算失败", e);
        }
    }

    // ==================== 加盐 SHA ====================

    /**
     * 计算加盐 SHA-256
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 SHA-256 值
     */
    public static String sha256WithSalt(String data, String salt) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return sha256(data + salt);
    }

    /**
     * 计算加盐 SHA-512
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 SHA-512 值
     */
    public static String sha512WithSalt(String data, String salt) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return sha512(data + salt);
    }

    // ==================== HMAC-SHA ====================

    /**
     * 计算 HMAC-SHA-256
     *
     * @param data 待计算字符串
     * @param key  密钥
     * @return HMAC-SHA-256 值
     */
    public static String hmacSha256(String data, String key) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.hmacHex(data, key.getBytes(StandardCharsets.UTF_8), DigestAlgorithm.SHA256);
    }

    /**
     * 计算 HMAC-SHA-512
     *
     * @param data 待计算字符串
     * @param key  密钥
     * @return HMAC-SHA-512 值
     */
    public static String hmacSha512(String data, String key) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.hmacHex(data, key.getBytes(StandardCharsets.UTF_8), DigestAlgorithm.SHA512);
    }

    // ==================== 验证方法 ====================

    /**
     * 验证 SHA-256
     *
     * @param data     原始数据
     * @param expected 预期的 SHA-256 值
     * @return 是否匹配
     */
    public static boolean verifySha256(String data, String expected) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(expected)) {
            return false;
        }
        return sha256(data).equalsIgnoreCase(expected);
    }

    /**
     * 验证 SHA-512
     *
     * @param data     原始数据
     * @param expected 预期的 SHA-512 值
     * @return 是否匹配
     */
    public static boolean verifySha512(String data, String expected) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(expected)) {
            return false;
        }
        return sha512(data).equalsIgnoreCase(expected);
    }
}
