package com.carlos.oauth.oauth2.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Attempts to extract client credentials from POST parameters of {@link HttpServletRequest} and then converts to an
 * {@link OAuth2ClientAuthenticationToken} used for authenticating the client.
 *
 * @author Anoop Garlapati
 * @see AuthenticationConverter
 * @see OAuth2ClientAuthenticationToken
 * @see OAuth2ClientAuthenticationFilter
 */
public final class ClientSecretPostBodyAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {

        String body = ServletUtil.getBody(request);
        Map<String, Object> map = JSONUtil.toBean(body, Map.class);

        // client_id (REQUIRED)
        String clientId = (String) map.get(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)) {
            return null;
        }

        if (StrUtil.isEmpty(clientId)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // client_secret (REQUIRED)
        String clientSecret = (String) map.get(OAuth2ParameterNames.CLIENT_SECRET);
        if (!StringUtils.hasText(clientSecret)) {
            return null;
        }

        if (StrUtil.isEmpty(clientSecret)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }
        return new OAuth2ClientAuthenticationToken(clientId, ClientAuthenticationMethod.CLIENT_SECRET_POST, clientSecret, map);
    }

}
