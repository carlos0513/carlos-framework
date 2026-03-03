package com.carlos.org.service;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.redis.util.RedisUtil;
import com.carlos.sms.SmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCodeService {

    /**
     * 验证码缓存，手机号，验证码类型
     */
    private static final String SMS_CODE = "user:sms:%s:%s";


    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @param type  验证码类型
     * @return java.lang.String
     * @author Carlos
     * @date 2024/5/22 16:30
     */
    public String getSmsCode(String phone, SmsCodeTypeEnum type) {
        String key = String.format(SMS_CODE, phone, type.getCode());
        return RedisUtil.getValue(key);
    }

    /**
     * 生成缓存key
     *
     * @param phone 手机号
     * @param type  验证码类型
     * @return java.lang.String
     * @author Carlos
     * @date 2024/5/22 16:30
     */
    public String generateKey(String phone, SmsCodeTypeEnum type) {
        return String.format(SMS_CODE, phone, type);
    }

    /**
     * 检查验证码是否正确
     *
     * @param code  验证码
     * @param phone 手机号
     * @param type  验证码类型
     * @author Carlos
     * @date 2024/5/22 16:51
     */
    public void checkSmsCode(String code, String phone, SmsCodeTypeEnum type) {
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("验证码不能为空");
        }
        String key = generateKey(phone, type);
        String value = RedisUtil.getValue(key);
        if (value == null) {
            throw new ServiceException("验证码已过期");
        }
        if (!value.equalsIgnoreCase(code)) {
            throw new ServiceException("验证码不正确");
        }
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param type  验证码类型
     * @author Carlos
     * @date 2024/5/22 17:29
     */
    public void sendCode(String phone, SmsCodeTypeEnum type) {
        // 生成随机6位数字验证码
        String smsCode = RandomUtil.randomNumbers(6);
        // 变量map
        LinkedHashMap<String, String> map = new LinkedHashMap();
        map.put("code", smsCode);
        map.put("minutes", "5");
        try {
            SmsUtil.sendByTemplateKey(phone, "gecode", map);
        } catch (RuntimeException e) {
            log.error("send sms failed. phone:{}, verifyType:{}", phone, type, e);
            throw new ServiceException("发送验证码失败");
        }
        // 设置验证码缓存 有效期5分钟
        RedisUtil.setValue(String.format(SMS_CODE, phone, type), smsCode, 5, TimeUnit.MINUTES);
    }
}
