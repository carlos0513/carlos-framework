package com.carlos.encrypt;


import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import com.carlos.encrypt.config.EncryptProperties;
import com.carlos.encrypt.exception.EncryptException;
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
 * 加解密工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/3/31 20:57
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

    // region----------------------  sm4 start  ------------------------

    /**
     * 字符加密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
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
     * 字符加密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
     */
    public static String sm4EncryptHex(String str) {
        if (null == sm4) {
            return str;
        }

        String encrypt;
        try {
            encrypt = sm4.encryptHex(str);
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
        return encrypt;
    }

    /**
     * 字符加密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
     */
    public static String sm4EncryptBase64(String str) {
        if (null == sm4) {
            return str;
        }
        String encrypt;
        try {
            encrypt = sm4.encryptBase64(str);
        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
        return encrypt;
    }

    /**
     * 字符解密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
     */
    public static String decrypt(String str) {
        if (null == sm4) {
            return str;
        }
        String encrypt;
        try {
            encrypt = sm4.decryptStr(str);
        } catch (Exception e) {
            throw new EncryptException(str + " decrypt failed", e);
        }
        return encrypt;
    }
    // endregion----------------------   sm4 end   ------------------------


    // region----------------------  sm2 start  ------------------------

    /**
     * sm2 加密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
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
     * sm2 加密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
     */
    public static String sm2EncryptHex(String str) {
        if (null == sm2) {
            return str;
        }
        String encrypt;
        try {
            encrypt = sm2.encryptHex(str, KeyType.PublicKey);

        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
        return encrypt;
    }

    /**
     * sm2 加密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
     */
    public static String sm2EncryptBase64(String str) {
        if (null == sm2) {
            return str;
        }
        String encrypt;
        try {
            encrypt = sm2.encryptBase64(str, KeyType.PublicKey);

        } catch (Exception e) {
            throw new EncryptException(str + " encrypt failed", e);
        }
        return encrypt;
    }

    /**
     * sm2 解密
     *
     * @param str 加密字符串
     * @return java.lang.String
     * @author Carlos
     * @date 2023/3/31 21:04
     */
    public static String sm2Decrypt(String str) {
        if (null == sm2) {
            return str;
        }
        String encrypt;
        try {
            encrypt = sm2.decryptStr(str, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new EncryptException(str + " decrypt failed", e);
        }
        return encrypt;
    }


    /**
     * 生成密钥对
     *
     * @return com.carlos.encrypt.Sm2KeyPair
     * @throws
     * @author Carlos
     * @date 2025-09-07 22:54
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
    // endregion----------------------   sm2 end   ------------------------
}
