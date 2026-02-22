package com.carlos.oauth.security.ext;

import com.carlos.oauth.security.SecurityUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 扩展认证信息 用于存放请求中的信息
 * </p>
 *
 * @author Carlos
 * @date 2022/11/10 16:07
 */
@Getter
@Setter
public class ExtendAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 授权类型
     */
    private AuthorizationGrantType grantType;

    /**
     * 客户端信息
     */
    private OAuth2ClientAuthenticationToken client;

    /**
     * scope
     */
    private Set<String> scopes;

    /**
     * 账号信息： 类型可以为 用户名，邮箱，电话号码等
     */
    private Object principal;

    /**
     * 登录的凭证： 密码，验证码，人脸信息等等
     */
    private Object credentials;

    private SecurityUser securityUser;

    public ExtendAuthenticationToken(Object principal, Object credentials, AuthorizationGrantType grantType, OAuth2ClientAuthenticationToken client,
                                     Set<String> scopes) {
        super(null);
        this.grantType = grantType;
        this.client = client;
        this.scopes = scopes;
        this.principal = principal;
        this.credentials = credentials;
    }

    public ExtendAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }


    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
