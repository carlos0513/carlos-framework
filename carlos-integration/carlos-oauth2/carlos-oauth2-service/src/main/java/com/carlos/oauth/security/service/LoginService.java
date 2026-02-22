package com.carlos.oauth.security.service;

import com.carlos.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 登录相关service<p>
 *
 * @author hemx
 * @since 2023/9/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private static final Long VERIFICATION_CODE_TIME_OUT = 60 * 5L;


    /**
     * redis验证码的key
     */
    private static final String VERIFICATION_CODE_KEY = "login:verification_code";

    /**
     * redis群众端修改用户信息验证码的key值*
     */
    private static final String MASSES_VERIFICATION_CODE_KEY = "masses_update:verification_code";

    /**
     * 获取用户的验证码
     *
     * @param userId 用户id
     * @return 验证码
     */
    public String getVerificationCode(String userId) {
        return RedisUtil.getValue(VERIFICATION_CODE_KEY + userId);
    }

    /**
     * 生成6位数验证码
     *
     * @return 验证码
     */
    private static String generateCaptcha() {
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        int length = 6;
        for (int i = 0; i < length; i++) {
            captcha.append(random.nextInt(10));
        }

        return captcha.toString();
    }

    public Boolean checkVerificationCode(String key, String verificationCode) {
        String value = RedisUtil.getValue(key);
        if (StringUtils.isNotEmpty(value) && value.equals(verificationCode)) {
            return true;
        }
        return false;
    }
}
