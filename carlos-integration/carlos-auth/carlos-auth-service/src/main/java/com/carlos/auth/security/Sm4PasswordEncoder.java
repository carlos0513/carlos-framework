package com.carlos.auth.security;

import com.carlos.encrypt.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 *   s使用SM4自定义加解密
 * </p>
 *
 * @author Carlos
 * @date 2025-03-18 16:15
 */
@Slf4j
public class Sm4PasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return EncryptUtil.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            log.warn("Empty encoded password");
            return false;
        }
        String decrypted = EncryptUtil.decrypt(encodedPassword);
        return rawPassword.toString().equals(decrypted);
    }
}
