package com.carlos.encrypt;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import com.carlos.encrypt.config.EncryptProperties;
import com.carlos.encrypt.enums.AesKeySize;
import com.carlos.encrypt.enums.AlgorithmType;
import com.carlos.encrypt.enums.RsaKeySize;
import com.carlos.encrypt.exception.EncryptException;
import com.carlos.encrypt.key.RsaKeyPair;
import com.carlos.encrypt.utils.*;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

/**
 * <p>
 * 加解密工具类 - 统一入口
 * </p>
 *
 * <p>
 * 提供常用的加解密算法支持，包括：
 * <ul>
 *   <li>国密算法：SM2（非对称）、SM3（哈希）、SM4（对称）</li>
 *   <li>国际算法：AES、RSA、DES、3DES</li>
 *   <li>哈希算法：MD5、SHA-1、SHA-256、SHA-512</li>
 *   <li>编码：Base64</li>
 * </ul>
 * </p>
 *
 * @author Carlos
 * @date 2023/3/31
 */
public class EncryptUtil {

    private static volatile SM4 sm4;
    private static volatile SM2 sm2;
    private static volatile EncryptProperties encryptProperties;
    private static volatile boolean initialized = false;
    private static final Object INIT_LOCK = new Object();

    public EncryptUtil(final SM4 sm4, final SM2 sm2, final EncryptProperties encryptProperties) {
        if (!initialized) {
            synchronized (INIT_LOCK) {
                if (!initialized) {
                    EncryptUtil.sm4 = sm4;
                    EncryptUtil.sm2 = sm2;
                    EncryptUtil.encryptProperties = encryptProperties;
                    initialized = true;
                }
            }
        }
    }

    /**
     * 静态初始化方法，用于手动初始化工具类
     */
    public static void init(final SM4 sm4, final SM2 sm2, final EncryptProperties encryptProperties) {
        if (!initialized) {
            synchronized (INIT_LOCK) {
                if (!initialized) {
                    EncryptUtil.sm4 = sm4;
                    EncryptUtil.sm2 = sm2;
                    EncryptUtil.encryptProperties = encryptProperties;
                    initialized = true;
                }
            }
        }
    }

    // region ====================== SM4 对称加密 ======================

    /**
     * SM4 字符加密（使用配置的默认模式）
     *
     * @param str 加密字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String str) {
        if (null == sm4) {
            return str;
        }
        if (null == encryptProperties) {
            return str;
        }
        EncryptProperties.SM4Properties sm4Properties = encryptProperties.getSm4();
        StoreType storeType = sm4Properties.getStoreType();
        String encrypt;
        try {
            switch (storeType) {
                case HEX:
                    encrypt = sm4.encryptHex(str);
                    break;
                case BASE64:
                    encrypt = sm4.encryptBase64(str);
                    break;
                default:
                    encrypt = sm4.encryptHex(str);
            }
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
        return encrypt;
    }

    /**
     * SM4 字符加密（Hex 格式）
     *
     * @param str 加密字符串
     * @return 加密后的 Hex 字符串
     */
    public static String sm4EncryptHex(String str) {
        if (null == sm4) {
            return str;
        }
        try {
            return sm4.encryptHex(str);
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
    }

    /**
     * SM4 字符加密（Base64 格式）
     *
     * @param str 加密字符串
     * @return 加密后的 Base64 字符串
     */
    public static String sm4EncryptBase64(String str) {
        if (null == sm4) {
            return str;
        }
        try {
            return sm4.encryptBase64(str);
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
    }

    /**
     * SM4 字符解密
     *
     * @param str 加密字符串
     * @return 解密后的原文
     */
    public static String decrypt(String str) {
        if (null == sm4) {
            return str;
        }
        try {
            return sm4.decryptStr(str);
        } catch (Exception e) {
            throw new EncryptException(str + " decrypt failed", e);
        }
    }
    // endregion ====================== SM4 结束 ======================


    // region ====================== SM2 非对称加密 ======================

    /**
     * SM2 加密（使用配置的默认模式）
     *
     * @param str 加密字符串
     * @return 加密后的字符串
     */
    public static String sm2Encrypt(String str) {
        if (null == sm2) {
            return str;
        }
        if (null == encryptProperties) {
            return str;
        }
        EncryptProperties.SM2Properties sm2Properties = encryptProperties.getSm2();
        StoreType storeType = sm2Properties.getStoreType();
        String encrypt;
        try {
            switch (storeType) {
                case HEX:
                    encrypt = sm2.encryptHex(str, KeyType.PublicKey);
                    break;
                case BASE64:
                    encrypt = sm2.encryptBase64(str, KeyType.PublicKey);
                    break;
                default:
                    encrypt = sm2.encryptHex(str, KeyType.PublicKey);
            }
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
        return encrypt;
    }

    /**
     * SM2 加密（Hex 格式）
     *
     * @param str 加密字符串
     * @return 加密后的 Hex 字符串
     */
    public static String sm2EncryptHex(String str) {
        if (null == sm2) {
            return str;
        }
        try {
            return sm2.encryptHex(str, KeyType.PublicKey);
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
    }

    /**
     * SM2 加密（Base64 格式）
     *
     * @param str 加密字符串
     * @return 加密后的 Base64 字符串
     */
    public static String sm2EncryptBase64(String str) {
        if (null == sm2) {
            return str;
        }
        try {
            return sm2.encryptBase64(str, KeyType.PublicKey);
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
    }

    /**
     * SM2 解密
     *
     * @param str 加密字符串
     * @return 解密后的原文
     */
    public static String sm2Decrypt(String str) {
        if (null == sm2) {
            return str;
        }
        try {
            return sm2.decryptStr(str, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new EncryptException(str + " decrypt failed", e);
        }
    }

    /**
     * 生成 SM2 密钥对
     *
     * @return SM2 密钥对
     * @throws GeneralSecurityException 生成失败时抛出
     */
    public static Sm2KeyPair generateSm2KeyPair() throws GeneralSecurityException {
        ECParameterSpec sm2p256v1 = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
        kpg.initialize(sm2p256v1, new SecureRandom());
        KeyPair kp = kpg.generateKeyPair();

        /* 公钥：未压缩点编码 -> 04+128 位 */
        ECPoint q = ((org.bouncycastle.jce.interfaces.ECPublicKey) kp.getPublic()).getQ();
        String publicKey = Hex.toHexString(q.getEncoded(false)); // 130 位

        /* 私钥：32 字节 BigInteger，去符号位补零 */
        BigInteger d = ((org.bouncycastle.jce.interfaces.ECPrivateKey) kp.getPrivate()).getD();
        byte[] dBytes = d.toByteArray();
        if (dBytes.length > 32) {          // 去掉 0x00 符号位
            dBytes = Arrays.copyOfRange(dBytes, 1, dBytes.length);
        }
        String privateKey = Hex.toHexString(dBytes); // 64 位
        return new Sm2KeyPair(publicKey, privateKey);
    }
    // endregion ====================== SM2 结束 ======================


    // region ====================== AES 对称加密 ======================

    /**
     * AES 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的 Hex 字符串
     */
    public static String aesEncrypt(String data, String key) {
        return AesUtil.encrypt(data, key);
    }

    /**
     * AES 加密（Base64）
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的 Base64 字符串
     */
    public static String aesEncryptBase64(String data, String key) {
        return AesUtil.encryptBase64(data, key);
    }

    /**
     * AES 解密
     *
     * @param encryptedData 加密数据（Hex 格式）
     * @param key           密钥
     * @return 解密后的原文
     */
    public static String aesDecrypt(String encryptedData, String key) {
        return AesUtil.decrypt(encryptedData, key);
    }

    /**
     * AES 解密（Base64）
     *
     * @param encryptedData 加密数据（Base64 格式）
     * @param key           密钥
     * @return 解密后的原文
     */
    public static String aesDecryptBase64(String encryptedData, String key) {
        return AesUtil.decryptBase64(encryptedData, key);
    }

    /**
     * 生成 AES 密钥
     *
     * @param keySize 密钥长度
     * @return 密钥字符串（Base64）
     */
    public static String generateAesKey(AesKeySize keySize) {
        return AesUtil.generateKey(keySize);
    }

    /**
     * 生成 AES 密钥（256 位）
     *
     * @return 密钥字符串（Base64）
     */
    public static String generateAesKey() {
        return AesUtil.generateKey();
    }
    // endregion ====================== AES 结束 ======================


    // region ====================== RSA 非对称加密 ======================

    /**
     * RSA 公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥（Base64 格式）
     * @return 加密后的 Hex 字符串
     */
    public static String rsaEncrypt(String data, String publicKey) {
        return RsaUtil.encrypt(data, publicKey);
    }

    /**
     * RSA 公钥加密（Base64）
     *
     * @param data      待加密数据
     * @param publicKey 公钥（Base64 格式）
     * @return 加密后的 Base64 字符串
     */
    public static String rsaEncryptBase64(String data, String publicKey) {
        return RsaUtil.encryptBase64(data, publicKey);
    }

    /**
     * RSA 私钥解密
     *
     * @param encryptedData 加密数据（Hex 格式）
     * @param privateKey    私钥（Base64 格式）
     * @return 解密后的原文
     */
    public static String rsaDecrypt(String encryptedData, String privateKey) {
        return RsaUtil.decrypt(encryptedData, privateKey);
    }

    /**
     * RSA 私钥解密（Base64）
     *
     * @param encryptedData 加密数据（Base64 格式）
     * @param privateKey    私钥（Base64 格式）
     * @return 解密后的原文
     */
    public static String rsaDecryptBase64(String encryptedData, String privateKey) {
        return RsaUtil.decryptBase64(encryptedData, privateKey);
    }

    /**
     * RSA 私钥签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥（Base64 格式）
     * @return 签名后的 Hex 字符串
     */
    public static String rsaSign(String data, String privateKey) {
        return RsaUtil.sign(data, privateKey);
    }

    /**
     * RSA 公钥验签
     *
     * @param data      原始数据
     * @param sign      签名（Hex 格式）
     * @param publicKey 公钥（Base64 格式）
     * @return 验签结果
     */
    public static boolean rsaVerify(String data, String sign, String publicKey) {
        return RsaUtil.verify(data, sign, publicKey);
    }

    /**
     * 生成 RSA 密钥对
     *
     * @param keySize 密钥长度
     * @return RSA 密钥对
     */
    public static RsaKeyPair generateRsaKeyPair(RsaKeySize keySize) {
        return RsaUtil.generateKeyPair(keySize);
    }

    /**
     * 生成 RSA 密钥对（2048 位）
     *
     * @return RSA 密钥对
     */
    public static RsaKeyPair generateRsaKeyPair() {
        return RsaUtil.generateKeyPair();
    }
    // endregion ====================== RSA 结束 ======================


    // region ====================== DES 对称加密 ======================

    /**
     * DES 加密
     *
     * @param data 待加密数据
     * @param key  密钥（8 字节）
     * @return 加密后的 Hex 字符串
     * @deprecated 建议使用 {@link #aesEncrypt(String, String)}
     */
    @Deprecated
    public static String desEncrypt(String data, String key) {
        return DesUtil.encrypt(data, key);
    }

    /**
     * DES 解密
     *
     * @param encryptedData 加密数据（Hex 格式）
     * @param key           密钥
     * @return 解密后的原文
     * @deprecated 建议使用 {@link #aesDecrypt(String, String)}
     */
    @Deprecated
    public static String desDecrypt(String encryptedData, String key) {
        return DesUtil.decrypt(encryptedData, key);
    }
    // endregion ====================== DES 结束 ======================


    // region ====================== 3DES 对称加密 ======================

    /**
     * 3DES 加密
     *
     * @param data 待加密数据
     * @param key  密钥（16 或 24 字节）
     * @return 加密后的 Hex 字符串
     */
    public static String des3Encrypt(String data, String key) {
        return Des3Util.encrypt(data, key);
    }

    /**
     * 3DES 解密
     *
     * @param encryptedData 加密数据（Hex 格式）
     * @param key           密钥
     * @return 解密后的原文
     */
    public static String des3Decrypt(String encryptedData, String key) {
        return Des3Util.decrypt(encryptedData, key);
    }
    // endregion ====================== 3DES 结束 ======================


    // region ====================== MD5 哈希 ======================

    /**
     * MD5 哈希（32 位）
     *
     * @param data 待计算字符串
     * @return MD5 值
     */
    public static String md5(String data) {
        return Md5Util.md5(data);
    }

    /**
     * MD5 哈希（16 位）
     *
     * @param data 待计算字符串
     * @return MD5 值（16 位）
     */
    public static String md5_16(String data) {
        return Md5Util.md5_16(data);
    }

    /**
     * 加盐 MD5
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 MD5 值
     */
    public static String md5WithSalt(String data, String salt) {
        return Md5Util.md5WithSalt(data, salt);
    }

    /**
     * 验证 MD5
     *
     * @param data     原始数据
     * @param expected 预期的 MD5 值
     * @return 是否匹配
     */
    public static boolean md5Verify(String data, String expected) {
        return Md5Util.verify(data, expected);
    }
    // endregion ====================== MD5 结束 ======================


    // region ====================== SHA 哈希 ======================

    /**
     * SHA-1 哈希
     *
     * @param data 待计算字符串
     * @return SHA-1 值
     */
    public static String sha1(String data) {
        return ShaUtil.sha1(data);
    }

    /**
     * SHA-256 哈希
     *
     * @param data 待计算字符串
     * @return SHA-256 值
     */
    public static String sha256(String data) {
        return ShaUtil.sha256(data);
    }

    /**
     * SHA-512 哈希
     *
     * @param data 待计算字符串
     * @return SHA-512 值
     */
    public static String sha512(String data) {
        return ShaUtil.sha512(data);
    }

    /**
     * 加盐 SHA-256
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 SHA-256 值
     */
    public static String sha256WithSalt(String data, String salt) {
        return ShaUtil.sha256WithSalt(data, salt);
    }

    /**
     * 验证 SHA-256
     *
     * @param data     原始数据
     * @param expected 预期的 SHA-256 值
     * @return 是否匹配
     */
    public static boolean sha256Verify(String data, String expected) {
        return ShaUtil.verifySha256(data, expected);
    }
    // endregion ====================== SHA 结束 ======================


    // region ====================== SM3 哈希 ======================

    /**
     * SM3 哈希
     *
     * @param data 待计算字符串
     * @return SM3 值
     */
    public static String sm3(String data) {
        return Sm3Util.sm3(data);
    }

    /**
     * 加盐 SM3
     *
     * @param data 待计算字符串
     * @param salt 盐值
     * @return 加盐 SM3 值
     */
    public static String sm3WithSalt(String data, String salt) {
        return Sm3Util.sm3WithSalt(data, salt);
    }

    /**
     * 验证 SM3
     *
     * @param data     原始数据
     * @param expected 预期的 SM3 值
     * @return 是否匹配
     */
    public static boolean sm3Verify(String data, String expected) {
        return Sm3Util.verify(data, expected);
    }
    // endregion ====================== SM3 结束 ======================


    // region ====================== Base64 编码 ======================

    /**
     * Base64 编码
     *
     * @param data 待编码字符串
     * @return Base64 编码字符串
     */
    public static String base64Encode(String data) {
        return Base64Util.encode(data);
    }

    /**
     * Base64 解码
     *
     * @param base64Data Base64 编码字符串
     * @return 解码后的字符串
     */
    public static String base64Decode(String base64Data) {
        return Base64Util.decode(base64Data);
    }

    /**
     * Base64 编码字节数组
     *
     * @param data 待编码字节数组
     * @return Base64 编码字符串
     */
    public static String base64Encode(byte[] data) {
        return Base64Util.encode(data);
    }

    /**
     * Base64 解码为字节数组
     *
     * @param base64Data Base64 编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64DecodeToBytes(String base64Data) {
        return Base64Util.decodeToBytes(base64Data);
    }
    // endregion ====================== Base64 结束 ======================


    // region ====================== 通用工具方法 ======================

    /**
     * 获取支持的算法类型
     *
     * @return 算法类型枚举数组
     */
    public static AlgorithmType[] getSupportedAlgorithms() {
        return AlgorithmType.values();
    }

    /**
     * 生成随机密钥
     *
     * @param algorithmType 算法类型
     * @return 密钥字符串
     */
    public static String generateKey(AlgorithmType algorithmType) {
        switch (algorithmType) {
            case AES:
                return AesUtil.generateKey();
            case DES:
                return DesUtil.generateKey();
            case DES3:
                return Des3Util.generateKey();
            case RSA:
                return "请使用 generateRsaKeyPair() 方法生成 RSA 密钥对";
            case SM2:
                return "请使用 generateSm2KeyPair() 方法生成 SM2 密钥对";
            default:
                throw new EncryptException("该算法不支持密钥生成: " + algorithmType.getCode());
        }
    }
    // endregion ====================== 通用工具 结束 ======================
}
