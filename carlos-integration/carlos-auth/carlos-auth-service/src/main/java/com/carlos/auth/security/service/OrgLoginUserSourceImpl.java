package com.carlos.auth.security.service;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.core.base.LoginUserSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 自定义登录用户来源
 * </p>
 *
 * @author carlos
 * @date 2021/12/21 14:42
 */
@Slf4j
// @Component
@AllArgsConstructor
public class OrgLoginUserSourceImpl implements LoginUserSource {

    private final OrgService orgService;


    @Override
    public LoginUserInfo getLoginUserInfoByName(String username) {
        LoginUserInfo loginUserInfo = orgService.getUserByName(username);
        return loginUserInfo;
    }

}
