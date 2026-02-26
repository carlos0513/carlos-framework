package com.carlos.auth.oauth2.client;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p>
 * 自定义token信息,对token的信息进行增强
 * </p>
 *
 * @author carlos
 * @date 2022/11/4 11:03
 */
public class CustomizeClientOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {


    @Override
    public void customize(OAuth2TokenClaimsContext context) {

        // TODO: Carlos 2025-03-14 此处需要针对不同的grant_type处理，client模式不涉及到用户信息
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        String clientId = context.getAuthorizationGrant().getName();
        // claims.claim("clientId", clientId);
        // SecurityUser loginUser = (SecurityUser) context.getPrincipal().getPrincipal();
        // claims.claim("user", loginUser);
        // claims.claim("code", HttpStatus.OK.value());
    }

}
