package com.carlos.auth.support.sms;

import com.carlos.auth.support.base.BaseAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * @author carlos
 * @WebSite www.carlos.cn
 * @Description: 短信登录的核心处理
 */
public class SmsAuthenticationProvider
    extends BaseAuthenticationProvider<SmsAuthenticationToken> {

    private static final Logger LOGGER = LogManager.getLogger(SmsAuthenticationProvider.class);


    public SmsAuthenticationProvider(AuthenticationManager authenticationManager,
                                     OAuth2AuthorizationService authorizationService,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, String> reqParameters) {
        String username = reqParameters.get(OAuth2ParameterNames.USERNAME);
        // 验证码是放到password中的
        String code = reqParameters.get(OAuth2ParameterNames.PASSWORD);
        return new UsernamePasswordAuthenticationToken(username, code);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = SmsAuthenticationToken.class.isAssignableFrom(authentication);
        LOGGER.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {

    }

    // @Override
    // public void checkClient(RegisteredClient registeredClient) {
    //     assert registeredClient != null;
    //     if (!registeredClient.getAuthorizationGrantTypes()
    //             .contains(new AuthorizationGrantType(SecurityConstants.APP))) {
    //         throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    //     }
    // }
    //
    // @Override
    // public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
    //     String phone = (String) reqParameters.get(SecurityConstants.SMS_PARAMETER_NAME);
    //     return new UsernamePasswordAuthenticationToken(phone, null);
    // }

}
