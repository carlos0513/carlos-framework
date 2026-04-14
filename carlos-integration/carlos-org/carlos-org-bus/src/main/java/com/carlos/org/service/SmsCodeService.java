package com.carlos.org.service;


import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 验证码服务
 * </p>
 *
 * @author Carlos
 * @date 2024/5/22 16:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 验证短信验证码
     *
     * <p>读取 carlos-auth 模块存储的短信验证码 Redis key 进行校验。</p>
     *
     * @param phone   手机号
     * @param smsCode 短信验证码
     * @return true-验证通过，false-验证失败或已过期
     */
    public boolean verifySmsCode(String phone, String smsCode) {
        if (StrUtil.isBlank(phone) || StrUtil.isBlank(smsCode)) {
            return false;
        }

        // 兼容 carlos-auth 的 CaptchaService 存储的 key 格式
        String key = "auth:captcha:sms:" + phone;
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            log.warn("SMS code not found or expired for phone: {}", phone);
            return false;
        }

        boolean matched = smsCode.equals(value.toString());
        if (matched) {
            // 验证成功后删除，防止重复使用
            redisTemplate.delete(key);
            log.info("SMS code verified successfully for phone: {}", phone);
        } else {
            log.warn("SMS code verification failed for phone: {}", phone);
        }

        return matched;
    }
}
