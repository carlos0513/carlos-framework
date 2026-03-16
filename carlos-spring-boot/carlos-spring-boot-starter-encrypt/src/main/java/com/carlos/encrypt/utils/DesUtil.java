package com.carlos.encrypt.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.carlos.encrypt.exception.EncryptException;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * DES 加密工具类
 * </p>
 *
 * <p>
 * DES (Data Encryption Standard) 是一种对称加密算法，密钥长度为 56 位。
 * <strong>注意：DES 已被认为不安全，建议使用 AES 或 3DES 替代。</strong>
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 * @deprecated 建议使用 {@link AesUtil} 替代
 */
@Deprecated
public class DesUtil {

    private DesUtil() {
        // 工具类禁止实例化
    }

    // ==================== 加密方法 ====================

    /**
     * DES 加密（CBC 模式，PKCS5Padding）
     *
     * @param data 待加密数据
     * @param key  密钥（8 字节）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key) {
        return encrypt(data, key, null);
    }

    /**
     * DES 加密（CBC 模式，PKCS5Padding）
     *
     * @param data 待加密数据
     * @param key  密钥（8 字节）
     * @param iv   初始化向量（8 字节）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key, String iv) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            DES des = buildDes(key, iv);
            return des.encryptHex(data);
        } catch (Exception e) {
            throw new EncryptException("DES 加密失败", e);
        }
    }

    /**
     * DES 加密为 Base64
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的 Base64 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encryptBase64(String data, String key) {
        return encryptBase64(data, key, null);
    }

    /**
     * DES 加密为 Base64
     *
     * @param data 待加密数据
     * @param key  密钥
     * @param iv   初始化向量
     * @return 加密后的 Base64 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encryptBase64(String data, String key, String iv) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            DES des = buildDes(key, iv);
            return des.encryptBase64(data);
        } catch (Exception e) {
            throw new EncryptException("DES 加密失败", e);
        }
    }

    // ==================== 解密方法 ====================

    /**
     * DES 解密
     *
     * @param encryptedData 加密后的数据（Hex 格式）
     * @param key           密钥
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decrypt(String encryptedData, String key) {
        return decrypt(encryptedData, key, null);
    }

    /**
     * DES 解密
     *
     * @param encryptedData 加密后的数据（Hex 格式）
     * @param key           密钥
     * @param iv            初始化向量
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decrypt(String encryptedData, String key, String iv) {
        if (StrUtil.isBlank(encryptedData)) {
            return encryptedData;
        }
        try {
            DES des = buildDes(key, iv);
            return des.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("DES 解密失败", e);
        }
    }

    /**
     * DES 解密 Base64 格式密文
     *
     * @param encryptedData 加密后的数据（Base64 格式）
     * @param key           密钥
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decryptBase64(String encryptedData, String key) {
        return decryptBase64(encryptedData, key, null);
    }

    /**
     * DES 解密 Base64 格式密文
     *
     * @param encryptedData 加密后的数据（Base64 格式）
     * @param key           密钥
     * @param iv            初始化向量
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decryptBase64(String encryptedData, String key, String iv) {
        if (StrUtil.isBlank(encryptedData)) {
            return encryptedData;
        }
        try {
            DES des = buildDes(key, iv);
            return des.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("DES 解密失败", e);
        }
    }

    // ==================== 密钥生成 ====================

    /**
     * 生成 DES 密钥
     *
     * @return 密钥字符串（Base64）
     */
    public static String generateKey() {
        DES des = new DES(Mode.CBC, Padding.PKCS5Padding);
        return cn.hutool.core.codec.Base64.encode(des.getSecretKey().getEncoded());
    }

    // ==================== 辅助方法 ====================

    /**
     * 构建 DES 对象
     *
     * @param key 密钥
     * @param iv  初始化向量
     * @return DES 对象
     */
    private static DES buildDes(String key, String iv) {
        byte[] keyBytes = processKey(key);
        byte[] ivBytes = processIv(iv, keyBytes);
        return new DES(Mode.CBC, Padding.PKCS5Padding, keyBytes, ivBytes);
    }

    /**
     * 处理密钥
     *
     * @param key 密钥字符串
     * @return 8 字节密钥
     */
    private static byte[] processKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 8) {
            // 使用 MD5 处理并取前 8 字节
            byte[] md5 = cn.hutool.crypto.digest.DigestUtil.md5(key);
            byte[] newKey = new byte[8];
            System.arraycopy(md5, 0, newKey, 0, 8);
            return newKey;
        }
        return keyBytes;
    }

    /**
     * 处理 IV
     *
     * @param iv      IV 字符串
     * @param keyBytes 密钥字节数组
     * @return 8 字节 IV
     */
    private static byte[] processIv(String iv, byte[] keyBytes) {
        if (StrUtil.isBlank(iv)) {
            // 使用密钥前 8 字节作为 IV
            byte[] newIv = new byte[8];
            System.arraycopy(keyBytes, 0, newIv, 0, 8);
            return newIv;
        }
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        if (ivBytes.length != 8) {
            byte[] newIv = new byte[8];
            System.arraycopy(ivBytes, 0, newIv, 0, Math.min(ivBytes.length, 8));
            return newIv;
        }
        return ivBytes;
    }
}
