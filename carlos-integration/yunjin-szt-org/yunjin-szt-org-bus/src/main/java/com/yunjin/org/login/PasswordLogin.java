package com.yunjin.org.login;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.login.service.AuthService;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 账号密码登录
 * @Date: 2023/7/12 16:08
 */
@Slf4j
public class PasswordLogin implements ThridLogin<Map<String, String>> {
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

        AuthService authService = SpringUtil.getBean(AuthService.class);
        if (!user.getPwd().equals(authService.encodePassword(secret))) {
            log.error("Failed to authenticate since password does not match stored value");
            throw new ServiceException("账户或密码不正确");
        }

        return user.getPhone();
    }
}
