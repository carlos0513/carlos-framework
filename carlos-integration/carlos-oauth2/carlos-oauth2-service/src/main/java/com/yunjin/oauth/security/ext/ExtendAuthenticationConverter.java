package com.carlos.oauth.security.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 该转换器只针对有密码的认证 扩展认证信息转换器 从请求信息提取认证信息，可以从请求头，请求参数，请求体中获取取到对应的参数
 * </p>
 *
 * @author Carlos
 * @date 2022/11/10 16:04
 */
@Slf4j
public class ExtendAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {


        // grant_type (REQUIRED)
        Map<String, String> params = ServletUtil.getParamMap(request);
        String grantType = params.get(OAuth2ParameterNames.GRANT_TYPE);
        // 仅处理密码模式
        if (!grantType.equalsIgnoreCase(AuthorizationGrantType.PASSWORD.getValue())) {
            log.warn("AuthenticationConvert [{}] can't support grant_typ:{}", this.getClass().getSimpleName(), grantType);
            return null;
        }

        // 验证客户端是否认证通过
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null || clientPrincipal instanceof OAuth2ClientAuthenticationToken) {
            log.warn("Can't get client authentication");
            return null;
        }

        // scope (OPTIONAL)
        String scope = params.get(OAuth2ParameterNames.SCOPE);
        String username = params.get(OAuth2ParameterNames.USERNAME);
        if (StrUtil.isBlank(username)) {
            log.warn("AuthenticationConvert [{}] can't support username is blank.", this.getClass().getSimpleName());
            return null;
        }
        String password = params.get(OAuth2ParameterNames.PASSWORD);
        if (StrUtil.isBlank(password)) {
            log.warn("AuthenticationConvert [{}] can't support password is blank.", this.getClass().getSimpleName());
            return null;
        }
        return new ExtendAuthenticationToken(username, password, AuthorizationGrantType.PASSWORD, (OAuth2ClientAuthenticationToken) clientPrincipal,
                Sets.newHashSet(scope));

    }
}
