package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.event.EventUtil;
import com.yunjin.docking.event.result.EventUserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 事件中枢登录
 * @Date: 2023/7/12 16:28
 */
@Slf4j
public class EventLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("event login param:{}", param);
        String token = param.get("token");
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("token不能为空");
        }
        EventUserInfo userInfo = null;
        try {
            userInfo = EventUtil.getUserInfo(token);
            log.info("Get event userinfo success: token:{}  userinfo:{}", token, userInfo);
        } catch (Exception e) {
            log.error("event login failed, token:{}", token, e);
            throw new ServiceException("登录失败！");
        }
        return userInfo.getPhone();
    }
}
