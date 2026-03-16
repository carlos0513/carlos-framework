package com.carlos.encrypt.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DESede;
import com.carlos.encrypt.exception.EncryptException;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 3DES (Triple DES) 加密工具类
 * </p>
 *
 * <p>
 * 3DES 是 DES 的改进版本，使用三个密钥进行三次加密，密钥长度可达 168 位。
 * 安全性比 DES 高，但性能较低，建议使用 AES 替代。
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class Des3Util {

    private Des3Util() {
        // 工具类禁止实例化
    }

    // ==================== 加密方法 ====================

    /**
     * 3DES 加密（CBC 模式，PKCS5Padding）
     *
     * @param data 待加密数据
     * @param key  密钥（16 或 24 字节）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key) {
        return encrypt(data, key, null);
    }

    /**
     * 3DES 加密（CBC 模式，PKCS5Padding）
     *
     * @param data 待加密数据
     * @param key  密钥（16 或 24 字节）
     * @param iv   初始化向量（8 字节）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String key, String iv) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            DESede des3 = buildDes3(key, iv);
            return des3.encryptHex(data);
        } catch (Exception e) {
            throw new EncryptException("3DES 加密失败", e);
        }
    }

    /**
     * 3DES 加密为 Base64
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
     * 3DES 加密为 Base64
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
            DESede des3 = buildDes3(key, iv);
            return des3.encryptBase64(data);
        } catch (Exception e) {
            throw new EncryptException("3DES 加密失败", e);
        }
    }

    // ==================== 解密方法 ====================

    /**
     * 3DES 解密
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
     * 3DES 解密
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
            DESede des3 = buildDes3(key, iv);
            return des3.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("3DES 解密失败", e);
        }
    }

    /**
     * 3DES 解密 Base64 格式密文
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
     * 3DES 解密 Base64 格式密文
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
            DESede des3 = buildDes3(key, iv);
            return des3.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new EncryptException("3DES 解密失败", e);
        }
    }

    // ==================== 密钥生成 ====================

    /**
     * 生成 3DES 密钥
     *
     * @return 密钥字符串（Base64）
     */
    public static String generateKey() {
        DESede des3 = new DESede(Mode.CBC, Padding.PKCS5Padding);
        return cn.hutool.core.codec.Base64.encode(des3.getSecretKey().getEncoded());
    }

    // ==================== 辅助方法 ====================

    /**
     * 构建 3DES 对象
     *
     * @param key 密钥
     * @param iv  初始化向量
     * @return DESede 对象
     */
    private static DESede buildDes3(String key, String iv) {
        byte[] keyBytes = processKey(key);
        byte[] ivBytes = processIv(iv, keyBytes);
        return new DESede(Mode.CBC, Padding.PKCS5Padding, keyBytes, ivBytes);
    }

    /**
     * 处理密钥
     *
     * @param key 密钥字符串
     * @return 16 或 24 字节密钥
     */
    private static byte[] processKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 16 && keyBytes.length != 24) {
            // 使用 MD5 处理并扩展
            byte[] md5 = cn.hutool.crypto.digest.DigestUtil.md5(key);
            byte[] newKey = new byte[24];
            System.arraycopy(md5, 0, newKey, 0, 16);
            System.arraycopy(md5, 0, newKey, 16, 8);
            return newKey;
        }
        return keyBytes;
    }

    /**
     * 处理 IV
     *
     * @param iv       IV 字符串
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
