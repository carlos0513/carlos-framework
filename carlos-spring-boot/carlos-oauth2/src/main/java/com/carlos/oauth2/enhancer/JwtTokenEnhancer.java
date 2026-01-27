package com.carlos.oauth2.enhancer;

import com.carlos.core.auth.LoginUserInfo;
import com.carlos.oauth2.constant.OAuth2Constant;
import com.carlos.oauth2.service.OAuth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

/**
 * JWT Token增强器
 * 向JWT中添加自定义的用户信息
 *
 * @author yunjin
 * @date 2026-01-25
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenEnhancer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final OAuth2UserDetailsService userDetailsService;

    @Override
    public void customize(JwtEncodingContext context) {
        if (OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
            // 获取用户名
            String username = context.getPrincipal().getName();

            try {
                // 加载用户业务信息
                LoginUserInfo loginUserInfo = userDetailsService.loadLoginUserInfo(username);

                if (loginUserInfo != null) {
                    // 添加用户ID
                    if (loginUserInfo.getId() != null) {
                        context.getClaims().claim(OAuth2Constant.Claims.USER_ID, loginUserInfo.getId());
                    }

                    // 添加用户名
                    context.getClaims().claim(OAuth2Constant.Claims.USER_NAME, loginUserInfo.getAccount());

                    // 添加租户ID
                    if (loginUserInfo.getClientId() != null) {
                        context.getClaims().claim(OAuth2Constant.Claims.TENANT_ID, loginUserInfo.getClientId());
                    }

                    // 添加部门ID
                    if (loginUserInfo.getDepartmentId() != null) {
                        context.getClaims().claim(OAuth2Constant.Claims.DEPT_ID, loginUserInfo.getDepartmentId());
                    }

                    // 添加角色ID列表
                    if (loginUserInfo.getRoleIds() != null && !loginUserInfo.getRoleIds().isEmpty()) {
                        // 转换为List以便JSON序列化
                        context.getClaims().claim(OAuth2Constant.Claims.ROLE_IDS,
                                new java.util.ArrayList<>(loginUserInfo.getRoleIds()));
                    }

                    // 添加权限列表
                    if (context.getPrincipal().getAuthorities() != null) {
                        String authorities = context.getPrincipal().getAuthorities().stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(","));
                        context.getClaims().claim(OAuth2Constant.Claims.AUTHORITIES, authorities);
                    }

                    log.debug("Enhanced JWT token for user: {}", username);
                }
            } catch (Exception e) {
                log.error("Failed to enhance JWT token for user: {}", username, e);
            }
        }
    }
}
