package com.carlos.auth.security.ext;

import com.carlos.auth.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * 扩展信息认证
 * </p>
 *
 * @author Carlos
 * @date 2022/11/10 16:33
 */
@Slf4j
@RequiredArgsConstructor
public class ExtendAuthenticationProvider implements AuthenticationProvider {


    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ExtendAuthenticationToken token = (ExtendAuthenticationToken) authentication;
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername((String) token.getPrincipal());
        if (securityUser.getPassword().equals(token.getCredentials())) {
            token.setSecurityUser(securityUser);
            return token;
        }
        throw new BadCredentialsException("用户信息验证失败");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = ExtendAuthenticationToken.class.isAssignableFrom(authentication);
        log.warn("AuthenticationProvider [{}] can't support authentication [{}]", this.getClass().getSimpleName(), authentication.getSimpleName());
        return supports;
    }
}
