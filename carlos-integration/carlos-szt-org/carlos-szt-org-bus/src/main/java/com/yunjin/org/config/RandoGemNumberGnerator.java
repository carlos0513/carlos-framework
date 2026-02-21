package com.yunjin.org.config;

import cn.hutool.captcha.generator.AbstractGenerator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 随机数字生成器
 * </p>
 *
 * @author Carlos
 * @date 2023/10/8 15:55
 */
public class RandoGemNumberGnerator extends AbstractGenerator {

    private static final long serialVersionUID = -7802758587765561876L;

    /**
     * 构造，使用字母+数字做为基础
     *
     * @param count 生成验证码长度
     */
    public RandoGemNumberGnerator(int count) {
        super("123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ123456789", count);
    }


    @Override
    public String generate() {
        return RandomUtil.randomString(this.baseStr, this.length);
    }

    @Override
    public boolean verify(String code, String userInputCode) {
        if (StrUtil.isNotBlank(userInputCode)) {
            return StrUtil.equalsIgnoreCase(code, userInputCode);
        }
        return false;
    }
}
