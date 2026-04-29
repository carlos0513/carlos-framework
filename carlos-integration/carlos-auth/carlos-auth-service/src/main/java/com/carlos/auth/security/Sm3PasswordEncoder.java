package com.carlos.auth.security;

import cn.hutool.crypto.digest.SM3;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SM3 国密密码编码器
 *
 * <p>基于 SM3 国密哈希算法实现的密码编码器。</p>
 *
 * <p>SM3 是中国国家密码管理局发布的密码哈希函数，安全性与 SHA-256 相当。
 * 适用于有国密合规要求的场景。</p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-29
 */
public class Sm3PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return new SM3().digestHex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encodedPassword.equalsIgnoreCase(encode(rawPassword));
    }
}
