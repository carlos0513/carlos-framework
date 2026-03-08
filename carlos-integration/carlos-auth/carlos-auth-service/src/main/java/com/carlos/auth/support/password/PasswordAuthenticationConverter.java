package com.carlos.auth.support.password;

import cn.hutool.core.util.StrUtil;
import com.carlos.auth.support.base.BaseAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 密码模式登录信息转换，获取用户的登录名和密码
 * </p>
 *
 * @author Carlos
 * @date 2022/11/9 13:05
 */
@Slf4j
public class PasswordAuthenticationConverter extends BaseAuthenticationConverter<PasswordAuthenticationToken> {

    /**
     * 支持密码模式
     *
     * @param grantType 授权类型
     */
    @Override
    public boolean support(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    @Override
    public PasswordAuthenticationToken buildToken(Authentication clientPrincipal,
                                                  Set<String> scopes, Map<String, String> params) {
        return new PasswordAuthenticationToken(AuthorizationGrantType.PASSWORD, clientPrincipal,
            scopes, params);
    }


    @Override
    public void checkParams(Map<String, String> parameters) {
        // username (REQUIRED)
        String username = parameters.get(OAuth2ParameterNames.USERNAME);
        if (StrUtil.isBlank(username)) {

        }


        // password (REQUIRED)
        String password = parameters.get(OAuth2ParameterNames.PASSWORD);
        if (StrUtil.isBlank(password)) {

        }
    }
}
