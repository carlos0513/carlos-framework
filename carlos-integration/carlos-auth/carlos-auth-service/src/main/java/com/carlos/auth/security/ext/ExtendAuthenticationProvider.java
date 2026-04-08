package com.carlos.auth.security.ext;

import com.carlos.auth.oauth2.OAuth2ErrorCodesExpand;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.security.SecurityUser;
import com.carlos.auth.service.ExtendUserDetailsService;
import com.carlos.core.auth.LoginUserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 扩展认证提供者
 *
 * <p>支持多种认证方式的统一认证处理器，包括密码、短信、邮箱、扫码、第三方登录等。</p>
 *
 * <h3>认证流程：</h3>
 * <ol>
 *   <li>验证授权类型支持</li>
 *   <li>根据认证方式提取用户信息</li>
 *   <li>验证用户凭证</li>
 *   <li>检查用户状态（锁定、禁用等）</li>
 *   <li>构建认证成功的令牌</li>
 * </ol>
 *
 * <h3>支持的认证方式：</h3>
 * <ul>
 *   <li>password - 密码认证（使用 PasswordEncoder 验证）</li>
 *   <li>sms_code - 短信验证码认证（需接入短信服务）</li>
 *   <li>email_code - 邮箱验证码认证（需接入邮件服务）</li>
 *   <li>qr_code - 扫码认证（需接入扫码服务）</li>
 *   <li>social - 第三方登录（需接入第三方平台）</li>
 * </ul>
 *
 * <h3>扩展方式：</h3>
 * <p>如需支持新的认证方式，可继承此类并重写以下方法：</p>
 * <ul>
 *   <li>{@link #supports(Authentication)} - 判断是否支持该认证类型</li>
 *   <li>{@link #authenticateInternal(ExtendAuthenticationToken)} - 实现认证逻辑</li>
 * </ul>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see ExtendAuthenticationToken
 * @see ExtendAuthenticationConverter
 * @see UserProvider
 * @see ExtendUserDetailsService
 */
@Slf4j
public class ExtendAuthenticationProvider implements AuthenticationProvider, ApplicationEventPublisherAware {

    /**
     * 用户信息服务
     */
    @Getter
    @Setter
    private UserProvider userProvider;

    /**
     * 用户详情服务
     */
    @Getter
    @Setter
    private ExtendUserDetailsService userDetailsService;

    /**
     * 密码编码器
     */
    @Getter
    @Setter
    private PasswordEncoder passwordEncoder;

    /**
     * 事件发布器
     */
    private ApplicationEventPublisher eventPublisher;

    /**
     * 短信验证码验证器（可选）
     */
    @Getter
    @Setter
    private SmsCodeValidator smsCodeValidator;

    /**
     * 邮箱验证码验证器（可选）
     */
    @Getter
    @Setter
    private EmailCodeValidator emailCodeValidator;

    /**
     * 扫码验证器（可选）
     */
    @Getter
    @Setter
    private QrCodeValidator qrCodeValidator;

    /**
     * 第三方登录验证器（可选）
     */
    @Getter
    @Setter
    private SocialLoginValidator socialLoginValidator;

    /**
     * 构造方法
     *
     * @param userProvider 用户信息服务
     * @param userDetailsService 用户详情服务
     * @param passwordEncoder 密码编码器
     */
    public ExtendAuthenticationProvider(UserProvider userProvider,
                                        ExtendUserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {
        Assert.notNull(userProvider, "userProvider cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.userProvider = userProvider;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 执行认证
     *
     * @param authentication 认证令牌
     * @return 认证成功的令牌
     * @throws AuthenticationException 认证失败时抛出
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ExtendAuthenticationToken token = (ExtendAuthenticationToken) authentication;
        String grantType = token.getGrantType().getValue();

        log.debug("Authenticating with grant_type: {}, principal: {}", grantType, token.getPrincipal());

        try {
            // 执行具体认证
            ExtendAuthenticationToken authenticatedToken = authenticateInternal(token);

            // 发布认证成功事件
            if (eventPublisher != null) {
                // eventPublisher.publishEvent(new AuthenticationSuccessEvent(authenticatedToken));
            }

            log.info("Authentication successful: {}, grant_type: {}", token.getPrincipal(), grantType);
            return authenticatedToken;

        } catch (AuthenticationException e) {
            log.warn("Authentication failed: {}, grant_type: {}, reason: {}",
                token.getPrincipal(), grantType, e.getMessage());

            // 发布认证失败事件
            if (eventPublisher != null) {
                // eventPublisher.publishEvent(new AuthenticationFailureEvent(token, e));
            }
            throw e;
        }
    }

    /**
     * 内部认证逻辑
     *
     * <p>子类可重写此方法添加新的认证方式支持。</p>
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     * @throws AuthenticationException 认证失败时抛出
     */
    protected ExtendAuthenticationToken authenticateInternal(ExtendAuthenticationToken token)
        throws AuthenticationException {

        String grantType = token.getGrantType().getValue();

        switch (grantType) {
            case "password":
                return authenticateByPassword(token);
            case "sms_code":
                return authenticateBySmsCode(token);
            case "email_code":
                return authenticateByEmailCode(token);
            case "qr_code":
                return authenticateByQrCode(token);
            case "social":
                return authenticateBySocial(token);
            default:
                throw new BadCredentialsException("不支持的认证方式: " + grantType);
        }
    }

    /**
     * 密码认证
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     */
    protected ExtendAuthenticationToken authenticateByPassword(ExtendAuthenticationToken token) {
        String username = (String) token.getPrincipal();
        String password = (String) token.getCredentials();

        // 加载用户信息
        UserInfo userInfo = userProvider.loadUserByIdentifier(username);
        if (userInfo == null) {
            log.warn("User not found: {}", username);
            throw new UsernameNotFoundException(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND.getErrorDescription());
        }

        // 验证密码
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            log.warn("Invalid password for user: {}", username);
            throw new BadCredentialsException(OAuth2ErrorCodesExpand.BAD_CREDENTIALS.getErrorDescription());
        }

        // 检查用户状态
        checkUserStatus(userInfo);

        // 构建认证成功令牌
        return buildAuthenticatedToken(token, userInfo);
    }

    /**
     * 短信验证码认证
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     */
    protected ExtendAuthenticationToken authenticateBySmsCode(ExtendAuthenticationToken token) {
        String phone = (String) token.getPrincipal();
        String smsCode = (String) token.getCredentials();

        // 验证短信验证码
        if (smsCodeValidator != null) {
            boolean valid = smsCodeValidator.validate(phone, smsCode);
            if (!valid) {
                log.warn("Invalid SMS code for phone: {}", phone);
                throw new BadCredentialsException(OAuth2ErrorCodesExpand.VERIFICATION_CODE_ERROR.getErrorDescription());
            }
        } else {
            log.warn("SMS code validator not configured, skipping validation for phone: {}", phone);
            // 如果没有配置验证器，可以选择抛出异常或跳过验证
            // throw new BadCredentialsException("短信验证码服务未配置");
        }

        // 加载或创建用户
        UserInfo userInfo = userProvider.loadUserByIdentifier(phone);
        if (userInfo == null) {
            // 短信登录时，如果用户不存在可以自动创建
            log.info("Creating new user for phone: {}", phone);
            // userInfo = createUserByPhone(phone);
            throw new UsernameNotFoundException("用户不存在: " + phone);
        }

        // 检查用户状态
        checkUserStatus(userInfo);

        return buildAuthenticatedToken(token, userInfo);
    }

    /**
     * 邮箱验证码认证
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     */
    protected ExtendAuthenticationToken authenticateByEmailCode(ExtendAuthenticationToken token) {
        String email = (String) token.getPrincipal();
        String emailCode = (String) token.getCredentials();

        // 验证邮箱验证码
        if (emailCodeValidator != null) {
            boolean valid = emailCodeValidator.validate(email, emailCode);
            if (!valid) {
                log.warn("Invalid email code for: {}", email);
                throw new BadCredentialsException(OAuth2ErrorCodesExpand.VERIFICATION_CODE_ERROR.getErrorDescription());
            }
        } else {
            log.warn("Email code validator not configured, skipping validation for: {}", email);
        }

        // 加载用户信息
        UserInfo userInfo = userProvider.loadUserByIdentifier(email);
        if (userInfo == null) {
            log.warn("User not found by email: {}", email);
            throw new UsernameNotFoundException(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND.getErrorDescription());
        }

        // 检查用户状态
        checkUserStatus(userInfo);

        return buildAuthenticatedToken(token, userInfo);
    }

    /**
     * 扫码认证
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     */
    protected ExtendAuthenticationToken authenticateByQrCode(ExtendAuthenticationToken token) {
        String qrToken = (String) token.getPrincipal();

        // 验证扫码令牌
        if (qrCodeValidator != null) {
            UserInfo userInfo = qrCodeValidator.validateAndGetUser(qrToken);
            if (userInfo == null) {
                log.warn("Invalid or expired QR token: {}", qrToken);
                throw new BadCredentialsException("扫码令牌无效或已过期");
            }

            // 检查用户状态
            checkUserStatus(userInfo);

            return buildAuthenticatedToken(token, userInfo);
        } else {
            log.error("QR code validator not configured");
            throw new BadCredentialsException("扫码认证服务未配置");
        }
    }

    /**
     * 第三方登录认证
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     */
    protected ExtendAuthenticationToken authenticateBySocial(ExtendAuthenticationToken token) {
        String socialInfo = (String) token.getPrincipal();
        String[] parts = socialInfo.split(":", 2);
        if (parts.length != 2) {
            throw new BadCredentialsException("第三方登录信息格式错误");
        }
        String socialType = parts[0];
        String socialCode = parts[1];

        // 验证第三方授权码
        if (socialLoginValidator != null) {
            UserInfo userInfo = socialLoginValidator.validateAndGetUser(socialType, socialCode);
            if (userInfo == null) {
                log.warn("Social login failed: type={}, code={}", socialType, socialCode);
                throw new BadCredentialsException("第三方登录失败");
            }

            // 检查用户状态
            checkUserStatus(userInfo);

            return buildAuthenticatedToken(token, userInfo);
        } else {
            log.error("Social login validator not configured");
            throw new BadCredentialsException("第三方登录服务未配置");
        }
    }

    /**
     * 检查用户状态
     *
     * @param userInfo 用户信息
     * @throws AuthenticationException 状态异常时抛出
     */
    protected void checkUserStatus(UserInfo userInfo) {
        if (userInfo.isAccountLocked()) {
            log.warn("Account is locked: {}", userInfo.getUsername());
            throw new LockedException(OAuth2ErrorCodesExpand.USER_LOCKED.getErrorDescription());
        }
        if (!userInfo.isAccountEnabled()) {
            log.warn("Account is disabled: {}", userInfo.getUsername());
            throw new DisabledException(OAuth2ErrorCodesExpand.USER_DISABLE.getErrorDescription());
        }
    }

    /**
     * 构建认证成功的令牌
     *
     * @param originalToken 原始令牌
     * @param userInfo 用户信息
     * @return 认证成功的令牌
     */
    protected ExtendAuthenticationToken buildAuthenticatedToken(ExtendAuthenticationToken originalToken,
                                                                UserInfo userInfo) {
        // 加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());

        // 转换权限
        Collection<SimpleGrantedAuthority> authorities = userInfo.getRoleCodes() != null
            ? userInfo.getRoleCodes().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList())
            : null;

        // 创建 SecurityUser
        LoginUserInfo loginUserInfo = userDetailsService.loadLoginUserInfo(userInfo.getUsername());
        SecurityUser securityUser = loginUserInfo != null
            ? new SecurityUser(loginUserInfo)
            : null;

        // 构建已认证令牌
        ExtendAuthenticationToken authenticatedToken = new ExtendAuthenticationToken(
            userInfo.getUsername(),
            null, // 认证成功后清除凭证
            originalToken.getGrantType(),
            originalToken.getClient(),
            originalToken.getScopes(),
            originalToken.getAdditionalParameters(),
            authorities,
            securityUser
        );

        // 复制认证方式
        authenticatedToken.setAuthMethod(originalToken.getAuthMethod());

        return authenticatedToken;
    }

    /**
     * 判断是否支持该认证类型
     *
     * @param authentication 认证类型
     * @return true-支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return ExtendAuthenticationToken.class.isAssignableFrom(authentication);
    }

    // ==================== 验证器接口 ====================

    /**
     * 短信验证码验证器接口
     */
    @FunctionalInterface
    public interface SmsCodeValidator {
        /**
         * 验证短信验证码
         *
         * @param phone 手机号
         * @param code 验证码
         * @return true-验证通过
         */
        boolean validate(String phone, String code);
    }

    /**
     * 邮箱验证码验证器接口
     */
    @FunctionalInterface
    public interface EmailCodeValidator {
        /**
         * 验证邮箱验证码
         *
         * @param email 邮箱
         * @param code 验证码
         * @return true-验证通过
         */
        boolean validate(String email, String code);
    }

    /**
     * 扫码验证器接口
     */
    @FunctionalInterface
    public interface QrCodeValidator {
        /**
         * 验证扫码令牌并返回用户信息
         *
         * @param qrToken 扫码令牌
         * @return 用户信息，验证失败返回 null
         */
        UserInfo validateAndGetUser(String qrToken);
    }

    /**
     * 第三方登录验证器接口
     */
    @FunctionalInterface
    public interface SocialLoginValidator {
        /**
         * 验证第三方授权码并返回用户信息
         *
         * @param socialType 第三方类型（wechat, weibo, github等）
         * @param authCode 授权码
         * @return 用户信息，验证失败返回 null
         */
        UserInfo validateAndGetUser(String socialType, String authCode);
    }

}
