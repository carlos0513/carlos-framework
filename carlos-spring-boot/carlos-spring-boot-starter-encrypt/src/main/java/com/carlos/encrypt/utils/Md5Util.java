package com.carlos.encrypt.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.carlos.encrypt.exception.EncryptException;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * MD5 哈希工具类
 * </p>
 *
 * <p>
 * MD5 (Message-Digest Algorithm 5) 是一种广泛使用的哈希算法，产生 128 位（16 字节）哈希值。
 * <strong>注意：MD5 已被证明不安全，不建议用于密码存储或安全敏感场景，建议使用 SHA-256 或 SM3。</strong>
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class Md5Util {

    private Md5Util() {
        // 工具类禁止实例化
    }

    // ==================== 字符串 MD5 ====================

    /**
     * 计算字符串的 MD5 值（32 位小写 Hex）
     *
     * @param data 待计算字符串
     * @return MD5 值（32 位小写 Hex）
     */
    public static String md5(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.md5Hex(data);
    }

    /**
     * 计算字符串的 MD5 值（16 位小写 Hex）
     *
     * @param data 待计算字符串
     * @return MD5 值（16 位小写 Hex）
     */
    public static String md5_16(String data) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.md5Hex16(data);
    }

    /**
     * 计算字节数组的 MD5 值
     *
     * @param data 待计算字节数组
     * @return MD5 值（32 位小写 Hex）
     */
    public static String md5(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return DigestUtil.md5Hex(data);
    }

    // ==================== 加盐 MD5 ====================

    /**
     * 计算加盐 MD5
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 MD5 值
     */
    public static String md5WithSalt(String data, String salt) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.md5Hex(data + salt);
    }

    /**
     * 计算加盐 MD5（盐值在前后各加一次）
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 MD5 值
     */
    public static String md5WithSaltDouble(String data, String salt) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.md5Hex(salt + data + salt);
    }

    // ==================== 文件 MD5 ====================

    /**
     * 计算文件的 MD5 值
     *
     * @param file 文件
     * @return MD5 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String md5(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        try {
            return DigestUtil.md5Hex(file);
        } catch (Exception e) {
            throw new EncryptException("文件 MD5 计算失败", e);
        }
    }

    /**
     * 计算输入流的 MD5 值
     *
     * @param inputStream 输入流
     * @return MD5 值
     * @throws EncryptException 计算失败时抛出
     */
    public static String md5(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            return DigestUtil.md5Hex(inputStream);
        } catch (Exception e) {
            throw new EncryptException("流 MD5 计算失败", e);
        }
    }

    // ==================== HMAC-MD5 ====================

    /**
     * 计算 HMAC-MD5
     *
     * @param data 待计算字符串
     * @param key  密钥
     * @return HMAC-MD5 值
     */
    public static String hmacMd5(String data, String key) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return DigestUtil.hmacMd5Hex(data, key.getBytes(StandardCharsets.UTF_8));
    }

    // ==================== MD5 对象 ====================

    /**
     * 创建 MD5 对象（用于分块计算）
     *
     * @return MD5 对象
     */
    public static MD5 createMd5() {
        return new MD5();
    }

    /**
     * 验证 MD5
     *
     * @param data     原始数据
     * @param expected 预期的 MD5 值
     * @return 是否匹配
     */
    public static boolean verify(String data, String expected) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(expected)) {
            return false;
        }
        return md5(data).equalsIgnoreCase(expected);
    }

    /**
     * 验证加盐 MD5
     *
     * @param data     原始数据
     * @param salt     盐值
     * @param expected 预期的 MD5 值
     * @return 是否匹配
     */
    public static boolean verifyWithSalt(String data, String salt, String expected) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(expected)) {
            return false;
        }
        return md5WithSalt(data, salt).equalsIgnoreCase(expected);
    }
}
