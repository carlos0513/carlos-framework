package com.carlos.auth.login;

import cn.hutool.core.util.StrUtil;
import com.carlos.auth.audit.LoginAuditEvent;
import com.carlos.auth.login.dto.LoginRequest;
import com.carlos.auth.login.dto.LoginResponse;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.security.IpBlockManager;
import com.carlos.auth.security.LoginAttemptManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录服务
 * </p>
 *
 * <p>处理用户登录逻辑，包括用户名密码验证、令牌颁发等</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {


    /**
     * 用户服务
     */
    private final UserProvider userService;

    /**
     * 认证管理器
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 登录尝试管理器
     */
    private final LoginAttemptManager loginAttemptManager;

    /**
     * IP封禁管理器
     */
    private final IpBlockManager ipBlockManager;

    /**
     * HTTP请求（用于获取客户端IP）
     */
    private final HttpServletRequest request;

    /**
     * 设备白名单服务
     */
    // private final DeviceWhitelistService deviceWhitelistService;

    /**
     * 事件发布器
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应（包含访问令牌）
     */
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("User login attempt: username={}", loginRequest.getUsername());

        try {
            // 构建认证令牌
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                );

            // 执行认证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 获取用户信息
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 从数据库获取完整用户信息
            UserInfo user = userService.loadUserByIdentifier(loginRequest.getUsername());

            if (user == null) {
                log.error("User not found after authentication: {}", loginRequest.getUsername());
                throw new BadCredentialsException("用户不存在");
            }

            // 检查是否需要MFA验证
            boolean shouldTriggerMfa = true;
            // boolean shouldTriggerMfa = deviceWhitelistService.shouldTriggerMfa(user.getId(), request);

            // 如果未启用MFA且检测到新设备/异地登录，建议启用MFA
            boolean shouldRecommendMfa = shouldTriggerMfa && !Boolean.TRUE.equals(user.getMfaEnabled());

            // 如果已启用MFA且需要触发，返回MFA验证请求
            boolean mfaRequired = shouldTriggerMfa && Boolean.TRUE.equals(user.getMfaEnabled());

            // 构建登录响应
            LoginResponse response = buildLoginResponse(user, authentication);
            response.setMfaRequired(mfaRequired);
            response.setMfaRecommended(shouldRecommendMfa);

            // 发布登录成功审计事件（异步）
            eventPublisher.publishEvent(LoginAuditEvent.loginSuccess(
                this, user, request, null));

            // 记录登录成功
            recordLoginSuccess(loginRequest.getUsername());

            // 如果是可信设备，添加到白名单
            // if (!mfaRequired && !shouldRecommendMfa) {
            //     deviceWhitelistService.addTrustedDevice(user.getId(), request);
            // }

            return response;

        } catch (AuthenticationException e) {
            log.warn("Login failed for user: {}", loginRequest.getUsername(), e);
            throw e;
        }
    }

    /**
     * 使用UserDetails登录（用于内部服务调用）
     *
     * <p><strong>注意</strong>：此方法不生成真实JWT令牌，仅返回用户信息。</p>
     * <p>真实的JWT令牌应通过OAuth2的/token端点获取：</p>
     * <pre>
     * POST /oauth2/token
     * Content-Type: application/x-www-form-urlencoded
     *
     * grant_type=password&username=user&password=pass&client_id=client
     * </pre>
     *
     * @param userDetails 用户详情
     * @return 登录响应（accessToken为空，用户信息完整）
     */
    public LoginResponse loginWithUserDetails(UserDetails userDetails) {
        log.info("Building login response for user: {}", userDetails.getUsername());

        // 注意：此处不生成真实JWT令牌
        // 真实的JWT令牌应通过OAuth2的/oauth2/token端点获取
        // 使用authorization_code + PKCE流程（生产环境推荐）

        return LoginResponse.builder()
            .accessToken("") // 空令牌，需通过/oauth2/token获取
            .tokenType("Bearer")
            .expiresIn(7200L)
            .build();
    }

    /**
     * 构建登录响应
     *
     * @param user 用户信息
     * @param authentication 认证信息
     * @return 登录响应
     */
    private LoginResponse buildLoginResponse(UserInfo user, Authentication authentication) {
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
            .id(user.getUserId())
            .username(user.getUsername())
            .email(user.getEmail())
            .phone(user.getPhone())
            .build();

        // TODO: 实现真正的JWT令牌颁发
        // 临时使用空令牌，后续集成OAuth2Server
        return LoginResponse.builder()
            .accessToken("")
            .tokenType("Bearer")
            .expiresIn(7200L)
            .userInfo(userInfo)
            .build();
    }

    /**
     * 检查账号是否被锁定
     *
     * @param username 用户名
     * @return true-被锁定，false-未锁定
     */
    public boolean isAccountLocked(String username) {
        // 检查账号锁定
        boolean accountLocked = loginAttemptManager.isAccountLocked(username);

        // 检查IP封禁
        boolean ipBlocked = ipBlockManager.isIpBlocked(request);

        return accountLocked || ipBlocked;
    }

    /**
     * 记录登录失败
     *
     * @param username 用户名
     * @param request HTTP请求
     */
    public void recordLoginFailure(String username, HttpServletRequest request) {
        log.warn("Recording login failure for user: {}", username);

        // 记录账号登录失败
        if (username != null && !username.isBlank()) {
            loginAttemptManager.recordLoginFailure(username);
        }

        // 记录IP登录失败
        ipBlockManager.recordLoginFailure(request);
    }

    /**
     * 记录登录失败（仅IP）
     *
     * @param request HTTP请求
     */
    public void recordLoginFailure(HttpServletRequest request) {
        ipBlockManager.recordLoginFailure(request);
    }

    /**
     * 记录登录成功
     *
     * @param username 用户名
     */
    public void recordLoginSuccess(String username) {
        log.info("Recording login success for user: {}", username);

        // 重置账号失败计数
        if (username != null && !username.isBlank()) {
            loginAttemptManager.recordLoginSuccess(username);
        }

        // 重置IP失败计数
        ipBlockManager.recordLoginSuccess(request);
    }

    /**
     * 用户登出
     *
     * <p>记录登出审计日志，将令牌加入黑名单（可选）</p>
     *
     * @param accessToken 访问令牌
     * @param user 当前用户
     */
    public void logout(String accessToken, UserInfo user) {
        log.info("User logout: {}", user.getUsername());

        try {
            // 发布登出审计事件
            eventPublisher.publishEvent(LoginAuditEvent.logout(
                this, user, request, extractSessionId(accessToken)));

            // TODO: 将令牌添加到Redis黑名单（可选）
            // String jti = extractJtiFromToken(accessToken);
            // redisTemplate.opsForValue().set("auth:token:blacklist:" + jti, "revoked",
            //         Duration.ofSeconds(getTokenExpiry(accessToken)));

            // TODO: 清除Redis中的授权信息（可选）

            log.info("User logged out successfully: {}", user.getUsername());

        } catch (Exception e) {
            log.error("Error during logout for user: {}", user.getUsername(), e);
        }
    }

    /**
     * 从令牌中提取会话ID（简化实现）
     */
    private String extractSessionId(String accessToken) {
        if (StrUtil.isBlank(accessToken) || accessToken.length() < 20) {
            return null;
        }
        // 实际应从JWT的jti声明中提取
        return accessToken.substring(0, 20);
    }
}
