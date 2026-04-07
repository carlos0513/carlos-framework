package com.carlos.auth.oauth2.grant.sms;

import com.carlos.auth.oauth2.grant.BaseAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author carlos
 * @WebSite www.carlos.cn
 * @Description: 短信登录转换器
 */
public class SmsAuthenticationConverter extends BaseAuthenticationConverter<SmsAuthenticationToken> {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     */
    @Override
    public boolean support(String grantType) {
        return "APP".equals(grantType);
    }

    @Override
    public SmsAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, String> additionalParameters) {
        return new SmsAuthenticationToken(new AuthorizationGrantType("APP"),
            clientPrincipal, requestedScopes, additionalParameters);
    }


    @Override
    public void checkParams(Map<String, String> request) {

    }


}
