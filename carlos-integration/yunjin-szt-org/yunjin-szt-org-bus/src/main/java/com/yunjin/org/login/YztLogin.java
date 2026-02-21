package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.yzt.YztAuthUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 云政通登录
 * @Date: 2025/9/8 15:33
 */
@Slf4j
public class YztLogin implements ThridLogin<Map<String, String>> {

    @Override
    public String login(Map<String, String> param) {
        log.info("yzt login param:{}", param);
        String code = param.get("code");
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("code不能为空");
        }
        String sourceType = param.get("source_type");
        if (StrUtil.isBlank(sourceType)) {
            throw new ServiceException("source_type不能为空");
        }

        return YztAuthUtil.auth(sourceType, code);
    }

}

