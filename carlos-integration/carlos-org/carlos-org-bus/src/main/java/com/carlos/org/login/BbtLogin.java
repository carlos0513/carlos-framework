package com.carlos.org.login;

import com.carlos.core.exception.ServiceException;
import com.carlos.org.config.OrgConstant;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * <p>
 * 报表通登录运营端
 * </p>
 *
 * @author Carlos
 * @date 2025-05-26 10:26
 */
@Slf4j
public class BbtLogin implements ThridLogin<Map<String, String>> {
    @Override
    public String login(Map<String, String> param) {
        log.info("password login param:{}", param);
        String token = param.get("code");
        UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, token));
        if (user == null) {
            throw new ServiceException("code无效");
        }
        return user.getPhone();
    }
}
