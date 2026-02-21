package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.linkage.BigLinkAgeUtil;
import com.yunjin.docking.linkage.result.BigLinkAgeUserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 大联动登录
 * @Date: 2023/7/12 16:08
 */
@Slf4j
public class BigLinkAgeLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("big-linkage login param:{}", param);
        String code = param.get("userid");
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("userId不能为空");
        }
        BigLinkAgeUserInfo userInfo = null;
        try {
            userInfo = BigLinkAgeUtil.getUserInfo(code);
            log.info("Get big-linkage userinfo success: code:{}  userinfo:{}", code, userInfo);
        } catch (Exception e) {
            log.error("Big-linkage login failed, code:{}", code, e);
            throw new ServiceException("登录失败！");
        }
        return userInfo.getTel();
    }
}
