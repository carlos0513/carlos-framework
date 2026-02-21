package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.rzt.RztUtil;
import com.yunjin.docking.rzt.config.RztProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 蓉政通登录
 * @Date: 2023/7/12 16:08
 */
@Slf4j
public class RztLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("rzt login param:{}", param);
        String code = param.get("code");
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("code不能为空");
        }
        String userId = null;
        RztProperties rztProperties = SpringUtil.getBean(RztProperties.class);
        if (rztProperties.getDebug()) {
            // 开启debug模式
            userId = code;
        } else {
            try {
                userId = RztUtil.getUserId(code);
                log.info("Get rzt userid success: code:{}  userid:{}", code, userId);
            } catch (Exception e) {
                log.error("Rzt login failed, code:{}", code, e);
                throw new ServiceException("登录失败！");
            }
        }
        return userId;
    }
}
