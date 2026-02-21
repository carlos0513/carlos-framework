package com.yunjin.org.login;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.enums.UserStateEnum;
import com.yunjin.org.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 市级系统单点登录
 * @Date: 2023/7/12 16:08
 */
@Slf4j
public class CitySSOLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("city sso login param:{}", param);
        String userId = param.get("userId");
        String token = param.get("accessToken");
        if (CharSequenceUtil.isBlank(token)) {
            throw new ServiceException("token不能为空");
        }
        if (CharSequenceUtil.isBlank(userId)) {
            throw new ServiceException("userId不能为空");
        }
        // TODO 校验TOKEN
        UserService userService = SpringUtil.getBean(UserService.class);
        UserDTO user = userService.getUserById(userId, false);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (!UserStateEnum.ENABLE.equals(user.getState())) {
            throw new ServiceException("用户状态不可用!");
        }
        return user.getPhone();
    }
}
