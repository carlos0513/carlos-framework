package com.carlos.encrypt.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import com.carlos.encrypt.enums.AesKeySize;
import com.carlos.encrypt.exception.EncryptException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * AES 加密工具类
 * </p>
 *
 * <p>
 * AES (Advanced Encryption Standard) 是一种对称加密算法，使用相同的密钥进行加密和解密。
 * 支持 128/192/256 位密钥长度，推荐使用 256 位密钥以获得更高的安全性。
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class AesUtil {

    private AesUtil() {
        // 工具类禁止实例化
    }

    // ==================== 基础加密方法 ====================

    /**
     * AES 加密（CBC 模式，PKCS5Padding）
     *
     * @param data 待加密数据
     * @param key  密钥（16/24/32 字节）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key) {
        return encrypt(data, key, null, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 加密（CBC 模式，PKCS5Padding）
     *
     * @param data 待加密数据
     * @param key  密钥（16/24/32 字节）
     * @param iv   初始化向量（16 字节）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key, String iv) {
        return encrypt(data, key, iv, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 加密
     *
     * @param data    待加密数据
     * @param key     密钥
     * @param iv      初始化向量（CBC 模式需要）
     * @param mode    加密模式
     * @param padding 填充方式
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key, String iv, Mode mode, Padding padding) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            AES aes = buildAes(key, iv, mode, padding);
            return aes.encryptHex(data);
        } catch (Exception e) {
            throw new EncryptException("AES 加密失败", e);
        }
    }

    /**
     * AES 加密为 Base64
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的 Base64 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encryptBase64(String data, String key) {
        return encryptBase64(data, key, null, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 加密为 Base64
     *
     * @param data 待加密数据
     * @param key  密钥
     * @param iv   初始化向量
     * @return 加密后的 Base64 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encryptBase64(String data, String key, String iv) {
        return encryptBase64(data, key, iv, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 加密为 Base64
     *
     * @param data    待加密数据
     * @param key     密钥
     * @param iv      初始化向量
     * @param mode    加密模式
     * @param padding 填充方式
     * @return 加密后的 Base64 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encryptBase64(String data, String key, String iv, Mode mode, Padding padding) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            AES aes = buildAes(key, iv, mode, padding);
            return aes.encryptBase64(data);
        } catch (Exception e) {
            throw new EncryptException("AES 加密失败", e);
        }
    }

    // ==================== 解密方法 ====================

    /**
     * AES 解密（CBC 模式，PKCS5Padding）
     *
     * @param encryptedData 加密后的数据（Hex 格式）
     * @param key           密钥
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decrypt(String encryptedData, String key) {
        return decrypt(encryptedData, key, null, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 解密（CBC 模式，PKCS5Padding）
     *
     * @param encryptedData 加密后的数据（Hex 格式）
     * @param key           密钥
     * @param iv            初始化向量
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decrypt(String encryptedData, String key, String iv) {
        return decrypt(encryptedData, key, iv, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 解密
     *
     * @param encryptedData 加密后的数据
     * @param key           密钥
     * @param iv            初始化向量
     * @param mode          加密模式
     * @param padding       填充方式
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decrypt(String encryptedData, String key, String iv, Mode mode, Padding padding) {
        if (StrUtil.isBlank(encryptedData)) {
            return encryptedData;
        }
        try {
            AES aes = buildAes(key, iv, mode, padding);
            return aes.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("AES 解密失败", e);
        }
    }

    /**
     * AES 解密 Base64 格式密文
     *
     * @param encryptedData 加密后的数据（Base64 格式）
     * @param key           密钥
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decryptBase64(String encryptedData, String key) {
        return decryptBase64(encryptedData, key, null, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 解密 Base64 格式密文
     *
     * @param encryptedData 加密后的数据（Base64 格式）
     * @param key           密钥
     * @param iv            初始化向量
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decryptBase64(String encryptedData, String key, String iv) {
        return decryptBase64(encryptedData, key, iv, Mode.CBC, Padding.PKCS5Padding);
    }

    /**
     * AES 解密 Base64 格式密文
     *
     * @param encryptedData 加密后的数据（Base64 格式）
     * @param key           密钥
     * @param iv            初始化向量
     * @param mode          加密模式
     * @param padding       填充方式
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decryptBase64(String encryptedData, String key, String iv, Mode mode, Padding padding) {
        if (StrUtil.isBlank(encryptedData)) {
            return encryptedData;
        }
        try {
            AES aes = buildAes(key, iv, mode, padding);
            return aes.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("AES 解密失败", e);
        }
    }

    // ==================== 字节数组方法 ====================

    /**
     * AES 加密字节数组
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的字节数组
     * @throws EncryptException 加密失败时抛出
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        return encrypt(data, key, null);
    }

    /**
     * AES 加密字节数组（CBC 模式）
     *
     * @param data 待加密数据
     * @param key  密钥
     * @param iv   初始化向量
     * @return 加密后的字节数组
     * @throws EncryptException 加密失败时抛出
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        if (data == null || data.length == 0) {
            return data;
        }
        try {
            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key, iv);
            return aes.encrypt(data);
        } catch (Exception e) {
            throw new EncryptException("AES 加密失败", e);
        }
    }

    /**
     * AES 解密字节数组
     *
     * @param encryptedData 加密后的数据
     * @param key           密钥
     * @return 解密后的字节数组
     * @throws EncryptException 解密失败时抛出
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key) {
        return decrypt(encryptedData, key, null);
    }

    /**
     * AES 解密字节数组（CBC 模式）
     *
     * @param encryptedData 加密后的数据
     * @param key           密钥
     * @param iv            初始化向量
     * @return 解密后的字节数组
     * @throws EncryptException 解密失败时抛出
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) {
        if (encryptedData == null || encryptedData.length == 0) {
            return encryptedData;
        }
        try {
            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key, iv);
            return aes.decrypt(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("AES 解密失败", e);
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 生成 AES 密钥
     *
     * @param keySize 密钥长度
     * @return 密钥字符串（Base64）
     */
    public static String generateKey(AesKeySize keySize) {
        SecretKey aes = SecureUtil.generateKey("AES", keySize.getBits());

        return Base64.encode(aes.getEncoded());
    }

    /**
     * 生成 AES 密钥（256 位）
     *
     * @return 密钥字符串（Base64）
     */
    public static String generateKey() {
        return generateKey(AesKeySize.BITS_256);
    }

    /**
     * 生成初始化向量（IV）
     *
     * @return IV 字符串（Base64）
     */
    public static String generateIv() {
        byte[] iv = new byte[16];
        java.security.SecureRandom random = new java.security.SecureRandom();
        random.nextBytes(iv);
        return Base64.encode(iv);
    }

    /**
     * 构建 AES 对象
     *
     * @param key     密钥
     * @param iv      初始化向量
     * @param mode    加密模式
     * @param padding 填充方式
     * @return AES 对象
     */
    private static AES buildAes(String key, String iv, Mode mode, Padding padding) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = StrUtil.isNotBlank(iv) ? iv.getBytes(StandardCharsets.UTF_8) : null;

        // 处理密钥长度
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            // 使用 MD5 处理为 16 字节
            keyBytes = DigestUtil.md5(key);
        }

        // 处理 IV
        if (ivBytes != null && ivBytes.length != 16) {
            byte[] newIv = new byte[16];
            System.arraycopy(ivBytes, 0, newIv, 0, Math.min(ivBytes.length, 16));
            ivBytes = newIv;
        }

        if (mode == Mode.CBC) {
            if (ivBytes == null) {
                // 如果未提供 IV，使用密钥前 16 字节
                ivBytes = new byte[16];
                System.arraycopy(keyBytes, 0, ivBytes, 0, Math.min(keyBytes.length, 16));
            }
            return new AES(mode, padding, keyBytes, ivBytes);
        } else {
            return new AES(mode, padding, keyBytes);
        }
    }
}
