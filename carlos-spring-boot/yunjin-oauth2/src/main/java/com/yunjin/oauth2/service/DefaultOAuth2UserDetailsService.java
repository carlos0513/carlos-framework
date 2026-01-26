package com.yunjin.oauth2.service;

import com.yunjin.core.auth.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认OAuth2用户详情服务实现
 * 提供一个测试用的默认用户
 *
 * @author yunjin
 * @date 2026-01-25
 */
@Slf4j
public class DefaultOAuth2UserDetailsService implements OAuth2UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public DefaultOAuth2UserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.warn("Using default OAuth2UserDetailsService, please implement your own for production use");

        LoginUserInfo loginUserInfo = loadLoginUserInfo(username);
        if (loginUserInfo == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return convertToUserDetails(loginUserInfo);
    }

    @Override
    public LoginUserInfo loadLoginUserInfo(String username) {
        // 默认测试用户
        if ("admin".equals(username)) {
            LoginUserInfo userInfo = new LoginUserInfo();
            userInfo.setId(1L);
            userInfo.setAccount("admin");
            userInfo.setPassword(passwordEncoder.encode("admin123"));
            userInfo.setClientId("default-client");

            Set<Serializable> roleIds = new HashSet<>();
            roleIds.add(1L);
            userInfo.setRoleIds(roleIds);

            userInfo.setDepartmentId(1L);
            userInfo.setEnable(true);
            return userInfo;
        }

        return null;
    }
}
