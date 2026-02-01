package com.carlos.oauth2.example;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.oauth2.service.OAuth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2用户详情服务示例实现
 *
 * 在实际项目中，你需要实现这个接口来从数据库加载用户信息
 *
 * @author carlos
 * @date 2026-01-25
 */
@Service
@RequiredArgsConstructor
public class ExampleOAuth2UserDetailsService implements OAuth2UserDetailsService {

    // 注入你的UserMapper或UserService
    // private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserInfo loginUserInfo = loadLoginUserInfo(username);
        if (loginUserInfo == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return convertToUserDetails(loginUserInfo);
    }

    @Override
    public LoginUserInfo loadLoginUserInfo(String username) {
        // 示例：从数据库加载用户信息
        // User user = userMapper.selectByUsername(username);
        // if (user == null) {
        //     return null;
        // }

        // 示例数据
        LoginUserInfo userInfo = new LoginUserInfo();
        userInfo.setId(1L);
        userInfo.setAccount(username);
        // 注意：密码应该已经是加密后的
        userInfo.setPassword("$2a$10$...");  // BCrypt加密后的密码
        userInfo.setClientId("tenant-001");

        // 设置角色ID
        Set<Serializable> roleIds = new HashSet<>();
        roleIds.add(1L);
        roleIds.add(2L);
        userInfo.setRoleIds(roleIds);

        userInfo.setDepartmentId(100L);
        userInfo.setEnable(true);

        return userInfo;
    }
}
