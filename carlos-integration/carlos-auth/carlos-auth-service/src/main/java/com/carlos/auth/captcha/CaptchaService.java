package com.carlos.auth.captcha;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.auth.util.SensitiveDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 验证码服务
 * </p>
 * <p>
 * 提供图形验证码、短信验证码的生成、发送、验证功能
 * </p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成数字验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public String generateNumericCaptcha(int length) {
        return RandomUtil.randomNumbers(length);
    }

    /**
     * 保存验证码到Redis
     *
     * @param key        Redis键
     * @param captcha    验证码
     * @param expireTime 过期时间（秒）
     */
    public void saveCaptcha(String key, String captcha, long expireTime) {
        redisTemplate.opsForValue().set(key, captcha, expireTime, TimeUnit.SECONDS);
        log.debug("Saved captcha to Redis with key: {}, expireTime: {}", key, expireTime);
    }

    /**
     * 从Redis获取验证码
     *
     * @param key Redis键
     * @return 验证码，不存在或过期返回null
     */
    public String getCaptcha(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * 删除验证码
     *
     * @param key Redis键
     */
    public void deleteCaptcha(String key) {
        redisTemplate.delete(key);
        log.debug("Deleted captcha with key: {}", key);
    }

    /**
     * 验证验证码
     *
     * @param key     Redis键
     * @param captcha 用户输入的验证码
     * @return true-验证通过，false-验证失败
     */
    public boolean verifyCaptcha(String key, String captcha) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(captcha)) {
            return false;
        }

        String storedCaptcha = getCaptcha(key);
        if (storedCaptcha == null) {
            log.warn("Captcha not found or expired, key: {}", key);
            return false;
        }

        boolean isValid = captcha.equals(storedCaptcha);
        if (isValid) {
            deleteCaptcha(key);
            log.info("Captcha verified successfully, key: {}", key);
        } else {
            log.warn("Captcha verification failed, key: {}", key);
        }

        return isValid;
    }

    /**
     * 构建验证码Redis键
     *
     * @param type   类型：sms/email
     * @param target 手机号或邮箱
     * @return Redis键
     */
    private String buildCaptchaKey(String type, String target) {
        return "auth:captcha:" + type + ":" + target;
    }

    /**
     * 发送短信验证码到指定手机
     * <p>
     * 示例：发送限制逻辑（限流规则）
     * - 60秒内只能发送1次
     * - 每日发送上限：10条
     * </p>
     *
     * 低优先级（可选）：实际短信发送功能可后续集成真实短信服务商（如阿里云、腾讯云）
     *
     * @param phone 手机号码
     * @return true-发送成功，false-发送失败
     */
    public boolean sendSmsCaptcha(String phone) {
        if (!isValidPhone(phone)) {
            log.warn("Invalid phone number: {}", phone);
            return false;
        }

        // 生成6位数字验证码（发送限制已在外层RateLimitService处理）
        String captcha = generateNumericCaptcha(6);

        // 存储到Redis（300秒过期）
        String key = buildCaptchaKey("sms", phone);
        saveCaptcha(key, captcha, 300);

        // TODO: 调用真实短信服务API发送验证码
        log.info("SMS captcha sent to {}, code: {}", SensitiveDataUtil.maskPhone(phone), captcha);

        return true;
    }

    /**
     * 发送邮箱验证码
     * <p>
     * 低优先级（可选）：邮件服务可后续集成
     *
     * @param email 邮箱地址
     * @return true-发送成功，false-发送失败
     */
    public boolean sendEmailCaptcha(String email) {
        // 生成6位数字验证码
        String captcha = generateNumericCaptcha(6);

        // 存储到Redis（300秒过期）
        String key = buildCaptchaKey("email", email);
        saveCaptcha(key, captcha, 300);

        // TODO: 调用邮件服务发送验证码
        log.info("Email captcha sent to {}, code: {}", SensitiveDataUtil.maskEmail(email), captcha);

        return true;
    }

    /**
     * 验证短信验证码
     *
     * @param phone   手机号
     * @param captcha 验证码
     * @return true-验证通过
     */
    public boolean verifySmsCaptcha(String phone, String captcha) {
        String key = buildCaptchaKey("sms", phone);
        return verifyCaptcha(key, captcha);
    }

    /**
     * 验证邮箱验证码
     *
     * @param email   邮箱
     * @param captcha 验证码
     * @return true-验证通过
     */
    public boolean verifyEmailCaptcha(String email, String captcha) {
        String key = buildCaptchaKey("email", email);
        return verifyCaptcha(key, captcha);
    }

    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 发送限制检查
     *
     * @param type   类型：sms/email
     * @param target 手机号/邮箱
     * @return true-可以发送，false-达到限制
     */
    public boolean canSendCaptcha(String type, String target) {
        String limitKey = "auth:captcha:limit:" + type + ":" + target;

        Object count = redisTemplate.opsForValue().get(limitKey);
        if (count == null) {
            return true;
        }

        int sentCount = Integer.parseInt(count.toString());
        return sentCount < 10;
    }

    /**
     * 增加发送计数
     *
     * @param type   类型：sms/email
     * @param target 手机号/邮箱
     */
    public void incrementSendCount(String type, String target) {
        String limitKey = "auth:captcha:limit:" + type + ":" + target;

        Long count = redisTemplate.opsForValue().increment(limitKey);

        if (count != null && count == 1) {
            redisTemplate.expire(limitKey, Duration.ofDays(1));
        }
    }

    /**
     * 删除发送计数（可选：用户注销时清理）
     *
     * @param type   类型：sms/email
     * @param target 手机号/邮箱
     */
    public void clearSendCount(String type, String target) {
        String limitKey = "auth:captcha:limit:" + type + ":" + target;
        redisTemplate.delete(limitKey);
    }
}
