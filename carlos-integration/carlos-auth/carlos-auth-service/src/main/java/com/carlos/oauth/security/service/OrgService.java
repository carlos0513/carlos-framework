package com.carlos.oauth.security.service;

import com.carlos.core.auth.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 部门角色 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2021-12-20 14:07:16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgService {

    //
    // private final FeignUser feignUser;


    public LoginUserInfo getUserByName(String username) {
        // if (StringUtils.isBlank(username)) {
        //     return null;
        // }
        //
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
