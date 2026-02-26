package com.carlos.auth.security.service;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.core.base.LoginUserSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 自定义登录用户来源
 * </p>
 *
 * @author carlos
 * @date 2021/12/21 14:42
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserLoginUserSourceImpl implements LoginUserSource {

    // private final FeignUser feignUser;


    @Override
    public LoginUserInfo getLoginUserInfoByName(String username) {
        // Result<LoginUserInfo> result = feignUser.getUserByName(username);
        // if (!result.getSuccess()) {
        //     log.error("Get loginUser failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
        //     throw new ServiceException(result.getMessage());
        // }
        // LoginUserInfo userInfo = result.getData();
        // if (log.isDebugEnabled()) {
        //     log.debug("Get user by name:{}  user:[{}]", username, userInfo);
        // }
        // return userInfo;
        return null;
    }

}
