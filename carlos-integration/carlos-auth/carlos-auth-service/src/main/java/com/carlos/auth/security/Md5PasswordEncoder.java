package com.carlos.auth.security;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * MD5 密码编码器
 *
 * <p><strong>警告：</strong>MD5 已被证明不安全，不适合生产环境使用。
 * 仅用于兼容旧系统迁移场景，新项目请使用 BCrypt。</p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-29
 * @deprecated 生产环境请使用 {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}
 */
@Deprecated
public class Md5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return DigestUtil.md5Hex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encodedPassword.equalsIgnoreCase(encode(rawPassword));
    }
}
