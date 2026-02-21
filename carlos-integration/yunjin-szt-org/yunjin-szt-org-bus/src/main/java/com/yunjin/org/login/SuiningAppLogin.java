package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.suining.SuiningAuthUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>
 *   遂宁app登录
 * </p>
 *
 * @author Carlos
 * @date 2025-01-15 10:57
 */
@Slf4j
public class SuiningAppLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("Suining login param:{}", param);
        String code = param.get("code");
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("code不能为空");
        }
        String phone = null;
        try {
            phone = SuiningAuthUtil.getUserPhone(code);
            log.info("Get Suining phone success: code:{}  phone:{}", code, phone);
        } catch (Exception e) {
            log.error("Suining login failed, code:{}", code, e);
            throw new ServiceException("登录失败！");
        }

        return phone;
    }
}
