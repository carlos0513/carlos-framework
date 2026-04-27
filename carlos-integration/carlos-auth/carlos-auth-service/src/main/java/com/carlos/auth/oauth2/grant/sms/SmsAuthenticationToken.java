package com.carlos.auth.oauth2.grant.sms;

import com.carlos.auth.oauth2.grant.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 针对短信的授权信息
 * </p>
 *
 * @author Carlos
 * @date 2022/11/9 18:11
 */
public class SmsAuthenticationToken extends BaseAuthenticationToken {

    public SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                  Authentication clientPrincipal,
                                  Set<String> scopes,
                                  Map<String, String> params) {
        super(authorizationGrantType, clientPrincipal, scopes, params);
    }

}
