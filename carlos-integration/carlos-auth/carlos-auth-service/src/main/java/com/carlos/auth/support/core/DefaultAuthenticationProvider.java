package com.carlos.auth.support.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.carlos.auth.enums.GrantType;
import com.carlos.auth.exception.UserNotFoundException;
import com.carlos.auth.security.service.LoginService;
import com.carlos.auth.security.service.UserDetailServiceImpl;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>
 * 自定义密码校验
 * </p>
 *
 * @author Carlos
 * @date 2022/11/9 23:24
 */
public class DefaultAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private final static BasicAuthenticationConverter BASIC_CONVERT = new BasicAuthenticationConverter();

    private static final String VERIFICATION_CODE_KEY = "login:verification_code";

    private PasswordEncoder passwordEncoder;

    private volatile String userNotFoundEncodedPassword;

    private UserDetailsService userDetailsService;

    private UserDetailsPasswordService userDetailsPasswordService;

    public DefaultAuthenticationProvider() {
        setPasswordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {
        Map<String, String> params = JakartaServletUtil.getParamMap(RequestUtil.getRequest());
        String grantType = params.get(OAuth2ParameterNames.GRANT_TYPE);
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        if (StrUtil.equals(GrantType.APP.getCode(), grantType)) {
            LoginService loginService = SpringUtil.getBean(LoginService.class);
            // TODO 暂时取固定值
//            String userVerificationCode="123456";
//            String userVerificationCode = loginService.getVerificationCode(authentication.getPrincipal().toString());
//            Boolean isSuccess = loginService.checkVerificationCode(VERIFICATION_CODE_KEY+userDetails.getUsername(), userDetails.getPassword());
//            if (userVerificationCode == null) {
//                throw new VerificationCodeException("验证码已失效");
//            }
//            String inputVerificationCode=authentication.getCredentials().toString();
//            if(!userVerificationCode.equals(inputVerificationCode)){
//                throw new VerificationCodeException("验证码错误");
//            }
//            if (!isSuccess) {
//                throw new VerificationCodeException("验证码错误");
//            }
        } else if (StrUtil.equals(GrantType.PASSWORD.getCode(), grantType)) {
            // 密码登录
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Failed to authenticate since password does not match stored value");
                throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        prepareTimingAttackProtection();
        HttpServletRequest request = RequestUtil.getRequest();

        Map<String, String> paramMap = JakartaServletUtil.getParamMap(request);
        String grantType = paramMap.get(OAuth2ParameterNames.GRANT_TYPE);
        String clientId = paramMap.get(OAuth2ParameterNames.CLIENT_ID);

        if (StrUtil.isBlank(clientId)) {
            clientId = BASIC_CONVERT.convert(request).getName();
        }
        UserDetailServiceImpl detailService = SpringUtil.getBean(UserDetailServiceImpl.class);
        if (GrantType.PASSWORD.getCode().equals(grantType)) {


            try {
                UserDetails loadedUser = detailService.loadUserByUsername(username);
                if (loadedUser == null) {
                    throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
                }
                return loadedUser;
            } catch (UsernameNotFoundException ex) {
                mitigateAgainstTimingAttack(authentication);
                throw ex;
            } catch (InternalAuthenticationServiceException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
            }
        } else if (GrantType.APP.getCode().equals(grantType)) {
            try {
                UserDetails loadedUser = detailService.loadUserByUsername(username);
                if (loadedUser == null) {
                    throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
                }
                return loadedUser;
            } catch (UsernameNotFoundException ex) {
                throw new UserNotFoundException(ex.getMessage());
            }
        } else {
            throw new BusinessException("不存在的登录方式");
        }

    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If not set, the password will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder} types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }
}
