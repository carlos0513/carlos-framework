package com.yunjin.org.login;

import cn.hutool.extra.spring.SpringUtil;
import com.yunjin.core.base.UserInfo;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.manager.UserManager;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: 网格员二维码登录
 * @Date: 2023/7/12 16:08
 */
@Slf4j
public class GridQrLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("password login param:{}", param);
        UserManager userManager= SpringUtil.getBean(UserManager.class);

        String userId = param.get("userId");

        UserDTO user = userManager.getDtoById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        return user.getPhone();
    }
}
