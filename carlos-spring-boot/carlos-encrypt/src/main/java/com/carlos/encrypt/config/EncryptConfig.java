package com.carlos.encrypt.config;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.carlos.core.exception.ComponentException;
import com.carlos.encrypt.EncryptMode;
import com.carlos.encrypt.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * <p>
 * 加密配置
 * </p>
 *
 * @author Carlos
 * @date 2023/3/22 13:25
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EncryptProperties.class)
public class EncryptConfig {

    /**
     * 注册加密算法
     *
     * @return cn.hutool.crypto.symmetric.SM4
     * @author Carlos
     * @date 2023/3/22 14:11
     */
    @Bean

    @ConditionalOnProperty(prefix = "yunjin.encrypt.sm4", name = "enabled", havingValue = "true", matchIfMissing = true)
    public SM4 sm4(EncryptProperties properties) {
        EncryptProperties.SM4Properties sm4Config = properties.getSm4();
        String key = sm4Config.getKey();
        EncryptMode encryptMode = sm4Config.getEncryptMode();
        SM4 sm4 = null;
        if (key.length() != 16) {
            key = DigestUtil.md5Hex16(key);
        }
        if (encryptMode == EncryptMode.CBC) {
            String iv = sm4Config.getIv();
            if (StrUtil.isBlank(iv)) {
                iv = DigestUtil.md5Hex16(key.substring(0, 16));
            }
            log.info("SM4 key:{}", key);
            log.info("SM4 iv:{}", iv);
            sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        }
        if (encryptMode == EncryptMode.ECB) {
            sm4 = new SM4(Mode.ECB, Padding.PKCS5Padding, key.getBytes());
        }
        return sm4;
    }


    /**
     * 注册SM2
     *
     * @return cn.hutool.crypto.asymmetric.SM2
     * @author Carlos
     * @date 2023/3/22 14:11
     */
    @Bean
    @ConditionalOnProperty(prefix = "yunjin.encrypt.sm2", name = "enabled", havingValue = "true", matchIfMissing = true)
    public SM2 sm2(EncryptProperties properties) {
        EncryptProperties.SM2Properties sm2 = properties.getSm2();
        if (StrUtil.isBlank(sm2.getPrivateKey())) {
            throw new ComponentException("sm2 privateIv is null");
        }
        if (StrUtil.isBlank(sm2.getPublicKey())) {
            throw new ComponentException("sm2 publicKey is null");
        }
        return SmUtil.sm2(sm2.getPrivateKey(), sm2.getPublicKey());
    }

    /**
     * 加密工具注册
     */
    @Bean
    public EncryptUtil encryptUtil(@Nullable SM4 sm4, @Nullable SM2 sm2, EncryptProperties encryptProperties) {
        return new EncryptUtil(sm4, sm2, encryptProperties);
    }

}
