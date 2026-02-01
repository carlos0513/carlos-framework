package com.carlos.oauth2.service;

import com.carlos.core.auth.LoginUserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2用户详情服务
 * 应用可以通过实现此接口来提供自定义的用户加载逻辑
 *
 * @author carlos
 * @date 2026-01-25
 */
public interface OAuth2UserDetailsService extends UserDetailsService {

    /**
     * 根据用户名加载用户信息
     *
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在异常
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 加载用户业务信息
     *
     * @param username 用户名
     * @return 登录用户信息
     */
    LoginUserInfo loadLoginUserInfo(String username);

    /**
     * 默认实现：将LoginUserInfo转换为UserDetails
     *
     * @param loginUserInfo 登录用户信息
     * @return UserDetails
     */
    default UserDetails convertToUserDetails(LoginUserInfo loginUserInfo) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (loginUserInfo.getRoleIds() != null) {
            for (java.io.Serializable roleId : loginUserInfo.getRoleIds()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleId));
            }
        }

        return User.builder()
                .username(loginUserInfo.getAccount())
                .password(loginUserInfo.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!loginUserInfo.getEnable())
                .credentialsExpired(false)
                .disabled(!loginUserInfo.getEnable())
                .build();
    }
}
