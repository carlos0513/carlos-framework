package com.yunjin.docking.suining;

import com.tencent.kona.crypto.CryptoUtils;
import com.tencent.kona.crypto.KonaCryptoProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author Sniper.Zhang
 * @description
 * @Date: 2023/11/28 15:18
 * @Version V1.0
 */

public class UtsSSoUtils {
    static {
        //这里在类加载的时候执行(只执行一次)，否则会报Provider故障
        if (java.security.Security.getProvider("KonaCrypto") == null) {
            java.security.Security.addProvider(new KonaCryptoProvider());

        } else {
            // 如果这里没有先remove再addprovide的话，系统可能报错，
            // 错误解释在 http://blogs.sun.com/jluehe/entry/how_a_jruby_based_web
            java.security.Security.removeProvider("KonaCrypto");
            java.security.Security.addProvider(new KonaCryptoProvider());
        }
    }

    // sm4的ecb解密
    public static String sm4_ecb_decrypt(String sm4key, String encDataBase64) throws Exception {
        if (encDataBase64 == null) {
            return "";
        }
        String plianData = "";
        byte[] sm4keyBytes = CryptoUtils.toBytes(sm4key);

        SecretKey secretKey = new SecretKeySpec(sm4keyBytes, "SM4");
        //腾讯  工具解密方法

        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] ciphertext = cipher.doFinal(decodeBase64(encDataBase64));
        plianData = new String(ciphertext, "utf-8");//这里是解密后的json字符串
        return plianData;
    }

    // sm4的ecb加密，返回数据将加密的byte进行base64编码
    public static String sm4_ecb_encrypt(String sm4key, String data) throws Exception {
        if (data == null) {
            return "";
        }
        byte[] sm4keyBytes = CryptoUtils.toBytes(sm4key);

        SecretKey secretKey = new SecretKeySpec(sm4keyBytes, "SM4");
        //腾讯  工具解密方法

        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] ciphertext = cipher.doFinal(data.getBytes("utf-8"));
        return base64(ciphertext);
    }


    /**
     * 将base64编码的字符串还原为byte数组
     *
     * @param content 字符串
     * @return 原byte数组
     */
    private static byte[] decodeBase64(String content) {
        return Base64.getDecoder().decode(content);
    }

    /**
     * 将byte数组使用base64编码
     *
     * @param data byte数组
     * @return base64字符串
     */
    private static String base64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

}
