package com.carlos.encrypt.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.carlos.encrypt.enums.RsaKeySize;
import com.carlos.encrypt.exception.EncryptException;
import com.carlos.encrypt.key.RsaKeyPair;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * RSA 加密工具类
 * </p>
 *
 * <p>
 * RSA 是一种非对称加密算法，使用公钥加密、私钥解密。
 * 支持数字签名，广泛用于数据加密传输和身份验证。
 * </p>
 *
 * @author Carlos
 * @date 2025/3/16
 */
public class RsaUtil {

    private RsaUtil() {
        // 工具类禁止实例化
    }

    // ==================== 加密方法 ====================

    /**
     * RSA 公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥（Base64 格式）
     * @return 加密后的 Hex 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encrypt(String data, String publicKey) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            RSA rsa = SecureUtil.rsa(null, publicKey);
            return rsa.encryptHex(data, KeyType.PublicKey);
        } catch (Exception e) {
            throw new EncryptException("RSA 加密失败", e);
        }
    }

    /**
     * RSA 公钥加密为 Base64
     *
     * @param data      待加密数据
     * @param publicKey 公钥（Base64 格式）
     * @return 加密后的 Base64 字符串
     * @throws EncryptException 加密失败时抛出
     */
    public static String encryptBase64(String data, String publicKey) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            RSA rsa = SecureUtil.rsa(null, publicKey);
            return rsa.encryptBase64(data, KeyType.PublicKey);
        } catch (Exception e) {
            throw new EncryptException("RSA 加密失败", e);
        }
    }

    // ==================== 解密方法 ====================

    /**
     * RSA 私钥解密
     *
     * @param encryptedData 加密后的数据（Hex 格式）
     * @param privateKey    私钥（Base64 格式）
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decrypt(String encryptedData, String privateKey) {
        if (StrUtil.isBlank(encryptedData)) {
            return encryptedData;
        }
        try {
            RSA rsa = SecureUtil.rsa(privateKey, null);
            return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new EncryptException("RSA 解密失败", e);
        }
    }

    /**
     * RSA 私钥解密 Base64 格式密文
     *
     * @param encryptedData 加密后的数据（Base64 格式）
     * @param privateKey    私钥（Base64 格式）
     * @return 解密后的原文
     * @throws EncryptException 解密失败时抛出
     */
    public static String decryptBase64(String encryptedData, String privateKey) {
        if (StrUtil.isBlank(encryptedData)) {
            return encryptedData;
        }
        try {
            RSA rsa = SecureUtil.rsa(privateKey, null);
            return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new EncryptException("RSA 解密失败", e);
        }
    }

    // ==================== 字节数组方法 ====================

    /**
     * RSA 公钥加密字节数组
     *
     * @param data      待加密数据
     * @param publicKey 公钥（Base64 格式）
     * @return 加密后的字节数组
     * @throws EncryptException 加密失败时抛出
     */
    public static byte[] encrypt(byte[] data, String publicKey) {
        if (data == null || data.length == 0) {
            return data;
        }
        try {
            RSA rsa = SecureUtil.rsa(null, publicKey);
            return rsa.encrypt(data, KeyType.PublicKey);
        } catch (Exception e) {
            throw new EncryptException("RSA 加密失败", e);
        }
    }

    /**
     * RSA 私钥解密字节数组
     *
     * @param encryptedData 加密后的数据
     * @param privateKey    私钥（Base64 格式）
     * @return 解密后的字节数组
     * @throws EncryptException 解密失败时抛出
     */
    public static byte[] decrypt(byte[] encryptedData, String privateKey) {
        if (encryptedData == null || encryptedData.length == 0) {
            return encryptedData;
        }
        try {
            RSA rsa = SecureUtil.rsa(privateKey, null);
            return rsa.decrypt(encryptedData, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new EncryptException("RSA 解密失败", e);
        }
    }

    // ==================== 数字签名 ====================

    /**
     * RSA 私钥签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥（Base64 格式）
     * @return 签名后的 Hex 字符串
     * @throws EncryptException 签名失败时抛出
     */
    public static String sign(String data, String privateKey) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            RSA rsa = SecureUtil.rsa(privateKey, null);
            return rsa.signHex(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptException("RSA 签名失败", e);
        }
    }

    /**
     * RSA 私钥签名为 Base64
     *
     * @param data       待签名数据
     * @param privateKey 私钥（Base64 格式）
     * @return 签名后的 Base64 字符串
     * @throws EncryptException 签名失败时抛出
     */
    public static String signBase64(String data, String privateKey) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        try {
            RSA rsa = SecureUtil.rsa(privateKey, null);
            return rsa.sign(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptException("RSA 签名失败", e);
        }
    }

    /**
     * RSA 公钥验签
     *
     * @param data      原始数据
     * @param sign      签名（Hex 格式）
     * @param publicKey 公钥（Base64 格式）
     * @return 验签结果
     * @throws EncryptException 验签失败时抛出
     */
    public static boolean verify(String data, String sign, String publicKey) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(sign)) {
            return false;
        }
        try {
            RSA rsa = SecureUtil.rsa(null, publicKey);
            return rsa.verifyHex(data, sign);
        } catch (Exception e) {
            throw new EncryptException("RSA 验签失败", e);
        }
    }

    /**
     * RSA 公钥验签（Base64 格式签名）
     *
     * @param data      原始数据
     * @param sign      签名（Base64 格式）
     * @param publicKey 公钥（Base64 格式）
     * @return 验签结果
     * @throws EncryptException 验签失败时抛出
     */
    public static boolean verifyBase64(String data, String sign, String publicKey) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(sign)) {
            return false;
        }
        try {
            RSA rsa = SecureUtil.rsa(null, publicKey);
            return rsa.verify(data, sign, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptException("RSA 验签失败", e);
        }
    }

    // ==================== 密钥生成 ====================

    /**
     * 生成 RSA 密钥对
     *
     * @param keySize 密钥长度
     * @return RSA 密钥对
     */
    public static RsaKeyPair generateKeyPair(RsaKeySize keySize) {
        RSA rsa = SecureUtil.rsa(keySize.getBits());
        return new RsaKeyPair(rsa.getPublicKeyBase64(), rsa.getPrivateKeyBase64());
    }

    /**
     * 生成 RSA 密钥对（2048 位）
     *
     * @return RSA 密钥对
     */
    public static RsaKeyPair generateKeyPair() {
        return generateKeyPair(RsaKeySize.BITS_2048);
    }

    /**
     * 获取 RSA 对象
     *
     * @param privateKey 私钥（可为 null）
     * @param publicKey  公钥（可为 null）
     * @return RSA 对象
     */
    public static RSA getRsa(String privateKey, String publicKey) {
        return SecureUtil.rsa(privateKey, publicKey);
    }
}
