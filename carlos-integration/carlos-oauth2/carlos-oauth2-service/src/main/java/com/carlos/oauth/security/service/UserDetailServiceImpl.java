package com.carlos.oauth.security.service;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.core.base.LoginUserSource;
import com.carlos.oauth.security.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * springsecurity用户信息获取
 * </p>
 *
 * @author yunjin
 * @date 2021/11/4 11:51
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {


    private final LoginUserSource loginUserSource;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserInfo loginUserInfo = loginUserSource.getLoginUserInfoByName(username);
        if (loginUserInfo == null) {
            // throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        return new SecurityUser(loginUserInfo);
    }

}
