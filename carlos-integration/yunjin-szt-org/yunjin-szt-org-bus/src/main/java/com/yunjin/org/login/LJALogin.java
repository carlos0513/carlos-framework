package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.jct.LJAppAggrUtil;
import com.yunjin.docking.jct.result.LJAppAggrUser;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>
 *   黑龙江一体化平台
 * </p>
 *
 * @author Carlos
 * @date 2025-02-25 15:15
 */
@Slf4j
public class LJALogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("lja login param:{}", param);
        String token = param.get("openToken");
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("token不能为空");
        }
        String route = param.get("redirectUrl");
        if (StrUtil.isNotBlank(route)) {
            LoginThreadLocal.setRoute(route);
        }

        LJAppAggrUser userInfo = null;
        try {
            userInfo = LJAppAggrUtil.getUserInfo(token);
            log.info("Get lja userinfo success: token:{}  userinfo:{}", token, userInfo);
        } catch (Exception e) {
            log.error("lja login failed, token:{}", token, e);
            throw new ServiceException("登录失败！");
        }
        if (userInfo == null) {
            throw new ServiceException("用户信息获取失败！");
        }
        return userInfo.getPhoneNumber();
    }
}
