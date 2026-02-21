package com.carlos.oauth.support.password;

import com.carlos.oauth.support.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 针对密码授权的信息
 * </p>
 *
 * @author Carlos
 * @date 2022/11/9 18:09
 */
public class PasswordAuthenticationToken extends BaseAuthenticationToken {

    public PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                       Authentication clientPrincipal, Set<String> scopes, Map<String, String> params) {
        super(authorizationGrantType, clientPrincipal, scopes, params);
    }

}
