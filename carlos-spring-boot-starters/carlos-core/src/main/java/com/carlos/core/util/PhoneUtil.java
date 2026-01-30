package com.carlos.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 手机号码工具类
 * </p>
 *
 * @author carlos
 * @date 2020/4/10 15:49
 */
@Slf4j
public class PhoneUtil {

    /**
     * 手机号码长度
     */
    private static final int PHONE_LENGTH = 11;

    /**
     * 脱敏*号
     */
    private static final String ASTERISK = "****";

    /**
     * 手机号码脱敏 截取手机号码前三位，后4为，中间4位使用*代替 13032820513 130****0513
     *
     * @param phone 原始手机号
     * @return 处理后的手机号
     */
    public static String desensitize(String phone) {
        return desensitize(phone, null);
    }

    /**
     * 手机号码脱敏 截取手机号码前三位，后4为，中间4位使用 replace 代替
     *
     * @param phone   原始手机号
     * @param replace 替换字符串
     * @return 脱敏后的结果
     */
    public static String desensitize(String phone, String replace) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        if (phone.length() != PHONE_LENGTH) {
            log.error("手机号码不合法：" + phone);
            return phone;
        }

        String before = phone.substring(0, 3);
        String after = phone.substring(7, 11);
        if (StringUtils.isEmpty(replace)) {
            replace = ASTERISK;
        }
        return before + replace + after;
    }


}
