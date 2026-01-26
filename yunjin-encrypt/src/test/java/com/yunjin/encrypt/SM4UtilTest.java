package com.yunjin.encrypt;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.security.Security;

public class SM4UtilTest {

    @Test
    public void sm4() {
        String key = "1234563456789076543457689";
        String realKey = DigestUtil.md5Hex16(key);
        String iv = DigestUtil.md5Hex16(StrUtil.subWithLength(key, 0, 16));
        SM4 sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding,
                realKey.getBytes(), iv.getBytes());
        // sm4 = new SM4(Mode.ECB, Padding.PKCS5Padding,
        //         realKey.getBytes());


        String encrypt = sm4.encryptBase64("511324199405134859");
        System.out.println(encrypt);
        String decrypt = sm4.decryptStr(encrypt);
        System.out.println(decrypt);


    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    @Test
    public void testgenerateKeyPair() throws GeneralSecurityException {
        Sm2KeyPair sm2KeyPair = EncryptUtil.generateSm2KeyPair();
        System.out.println("公钥：" + sm2KeyPair.getPublicKey());
        System.out.println("私钥：" + sm2KeyPair.getPrivateKey());
    }


}
