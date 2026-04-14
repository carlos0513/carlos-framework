package com.carlos.auth.security.ext;

import com.carlos.auth.idp.IdentityProvider;
import com.carlos.auth.idp.IdentityProviderRegistry;
import com.carlos.auth.idp.IdentityProviderRequest;
import com.carlos.auth.idp.UserIdentity;
import com.carlos.auth.oauth2.OAuth2ErrorCodesExpand;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.security.SecurityUser;
import com.carlos.auth.security.service.ExtendUserDetailsService;
import com.carlos.core.auth.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 扩展认证提供者
 *
 * <p>支持多种认证方式的统一认证处理器。基于 {@link IdentityProvider} 架构，
 * 将具体认证逻辑委托给对应身份源适配器。</p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see ExtendAuthenticationToken
 * @see ExtendAuthenticationConverter
 * @see IdentityProviderRegistry
 */
@Slf4j
public class ExtendAuthenticationProvider implements AuthenticationProvider, ApplicationEventPublisherAware {

    /**
     * 身份源注册中心
     */
    private final IdentityProviderRegistry providerRegistry;

    /**
     * 用户详情服务
     */
    private final ExtendUserDetailsService userDetailsService;

    /**
     * 事件发布器
     */
    private ApplicationEventPublisher eventPublisher;

    /**
     * 构造方法
     *
     * @param providerRegistry   身份源注册中心
     * @param userDetailsService 用户详情服务
     */
    public ExtendAuthenticationProvider(IdentityProviderRegistry providerRegistry,
                                        ExtendUserDetailsService userDetailsService) {
        Assert.notNull(providerRegistry, "providerRegistry cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.providerRegistry = providerRegistry;
        this.userDetailsService = userDetailsService;
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
            ExtendAuthenticationToken authenticatedToken = authenticateInternal(token);

            if (eventPublisher != null) {
                // eventPublisher.publishEvent(new AuthenticationSuccessEvent(authenticatedToken));
            }

            log.info("Authentication successful: {}, grant_type: {}", token.getPrincipal(), grantType);
            return authenticatedToken;

        } catch (AuthenticationException e) {
            log.warn("Authentication failed: {}, grant_type: {}, reason: {}",
                token.getPrincipal(), grantType, e.getMessage());

            if (eventPublisher != null) {
                // eventPublisher.publishEvent(new AuthenticationFailureEvent(token, e));
            }
            throw e;
        }
    }

    /**
     * 内部认证逻辑
     *
     * <p>统一路由到 {@link IdentityProvider} 进行认证。</p>
     *
     * @param token 认证令牌
     * @return 认证成功的令牌
     * @throws AuthenticationException 认证失败时抛出
     */
    protected ExtendAuthenticationToken authenticateInternal(ExtendAuthenticationToken token)
        throws AuthenticationException {

        String grantType = token.getGrantType().getValue();

        // 1. 查找对应身份源提供者
        IdentityProvider provider = providerRegistry.findByGrantType(grantType);

        // 2. 构建认证请求
        IdentityProviderRequest request = IdentityProviderRequest.builder()
            .principal(token.getPrincipal())
            .credentials(token.getCredentials())
            .clientId(token.getClient() != null ? token.getClient().getName() : null)
            .additionalParameters(token.getAdditionalParameters())
            .build();

        // 3. 执行身份源认证
        UserIdentity userIdentity = provider.authenticate(request);

        // 4. 转换为内部 UserInfo 并检查状态
        UserInfo userInfo = convertToUserInfo(userIdentity);
        checkUserStatus(userInfo);

        // 5. 构建认证令牌
        return buildAuthenticatedToken(token, userInfo);
    }

    /**
     * 将统一用户身份转换为内部 UserInfo
     *
     * @param identity 统一用户身份
     * @return 内部 UserInfo
     */
    protected UserInfo convertToUserInfo(UserIdentity identity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(identity.getProviderUserId() != null
            ? Long.valueOf(identity.getProviderUserId()) : null);
        userInfo.setUsername(identity.getUsername());
        userInfo.setEmail(identity.getEmail());
        userInfo.setPhone(identity.getPhone());
        userInfo.setRoleCodes(identity.getRoleCodes() != null
            ? new ArrayList<>(identity.getRoleCodes()) : null);
        userInfo.setStatus("ENABLE"); // 默认启用，具体状态可由 IdentityProvider 扩展
        userInfo.setMfaEnabled(false);
        return userInfo;
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
        UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());

        Collection<SimpleGrantedAuthority> authorities = userInfo.getRoleCodes() != null
            ? userInfo.getRoleCodes().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList())
            : null;

        LoginUserInfo loginUserInfo = userDetailsService.loadLoginUserInfo(userInfo.getUsername());
        SecurityUser securityUser = loginUserInfo != null
            ? new SecurityUser(loginUserInfo)
            : null;

        ExtendAuthenticationToken authenticatedToken = new ExtendAuthenticationToken(
            userInfo.getUsername(),
            null,
            originalToken.getGrantType(),
            originalToken.getClient(),
            originalToken.getScopes(),
            originalToken.getAdditionalParameters(),
            authorities,
            securityUser
        );

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
}
