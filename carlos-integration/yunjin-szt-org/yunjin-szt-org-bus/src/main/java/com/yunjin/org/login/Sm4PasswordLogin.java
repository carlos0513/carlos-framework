package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class Sm4PasswordLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("password login param:{}", param);
        UserService userService = SpringUtil.getBean(UserService.class);

        String key = param.get("key");
        String secret = param.get("secret");

        UserDTO user = userService.getUserByAccount(key);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (!StrUtil.equals(user.getPwd(), secret)) {
            throw new ServiceException("登录失败");
        }
        return user.getPhone();
    }
}
