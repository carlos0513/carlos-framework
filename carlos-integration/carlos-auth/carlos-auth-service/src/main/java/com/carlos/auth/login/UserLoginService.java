package com.carlos.auth.login;

import com.carlos.audit.api.ApiAuditLogMain;
import com.carlos.audit.api.pojo.enums.*;
import com.carlos.audit.api.pojo.param.ApiAuditLogMainParam;
import com.carlos.auth.login.dto.LoginRequest;
import com.carlos.auth.login.dto.LoginResponse;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.security.manager.IpBlockManager;
import com.carlos.auth.security.manager.LoginAttemptManager;
import com.carlos.auth.security.token.JwtTokenProvider;
import com.carlos.auth.util.IpLocationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户登录服务
 *
 * <p>处理用户登录逻辑，包括用户名密码验证、JWT 令牌颁发、登录审计等。</p>
 *
 * <p><strong>与 {@link com.carlos.auth.security.service.CaptchaService} 的区别：</strong></p>
 * <ul>
 *   <li>此类：处理完整的用户登录流程，包括身份验证、Token 生成、登录审计</li>
 *   <li>CaptchaService：仅处理验证码的生成、存储和校验，不涉及用户身份验证</li>
 * </ul>
 *
 * @author Carlos
 * @date 2026-02-26
 * @see com.carlos.auth.security.service.CaptchaService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService {


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
     * IP定位工具
     */
    private final IpLocationUtil ipLocationUtil;

    /**
     * JWT 令牌提供者
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 审计日志 Feign 接口
     */
    private final ApiAuditLogMain apiAuditLogMain;

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

            // 如果未启用MFA且检测到新设备/异地登录，建议启用MFA
            boolean shouldRecommendMfa = shouldTriggerMfa && !Boolean.TRUE.equals(user.getMfaEnabled());

            // 如果已启用MFA且需要触发，返回MFA验证请求
            boolean mfaRequired = shouldTriggerMfa && Boolean.TRUE.equals(user.getMfaEnabled());

            // 构建登录响应
            LoginResponse response = buildLoginResponse(user, authentication);
            response.setMfaRequired(mfaRequired);
            response.setMfaRecommended(shouldRecommendMfa);

            // 异步记录登录成功审计日志到统一审计服务
            recordLoginAudit(user, "LOGIN", "SUCCESS", null);

            // 记录登录成功
            recordLoginSuccess(loginRequest.getUsername());

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

        // 使用 JwtTokenProvider 生成 JWT 令牌
        JwtTokenProvider.TokenResponse tokenResponse = jwtTokenProvider.generateTokens(authentication, user);

        return LoginResponse.builder()
            .accessToken(tokenResponse.getAccessToken())
            .refreshToken(tokenResponse.getRefreshToken())
            .tokenType(tokenResponse.getTokenType())
            .expiresIn(tokenResponse.getExpiresIn())
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
            // 异步记录登出审计日志到统一审计服务
            recordLoginAudit(user, "LOGOUT", "SUCCESS", null);

            // TODO: 将令牌添加到Redis黑名单（可选）
            // String jti = extractJtiFromToken(accessToken);
            // redisTemplate.opsForValue().set("auth:token:blacklist:" + jti, "revoked",
            //         Duration.ofSeconds(getTokenExpiry(accessToken)));

            log.info("User logged out successfully: {}", user.getUsername());

        } catch (Exception e) {
            log.error("Error during logout for user: {}", user.getUsername(), e);
        }
    }

    /**
     * 异步记录登录审计日志
     *
     * @param user 用户信息
     * @param eventType 事件类型：LOGIN、LOGOUT
     * @param status 状态：SUCCESS、FAILURE
     * @param errorMessage 错误消息
     */
    @Async
    public void recordLoginAudit(UserInfo user, String eventType, String status, String errorMessage) {
        try {
            ApiAuditLogMainParam param = buildAuditLogParam(user, eventType, status, errorMessage);
            apiAuditLogMain.saveAuditLog(param);
            log.debug("Login audit log sent: user={}, type={}, status={}", user.getUsername(), eventType, status);
        } catch (Exception e) {
            log.error("Failed to send login audit log", e);
        }
    }

    /**
     * 构建审计日志参数
     */
    private ApiAuditLogMainParam buildAuditLogParam(UserInfo user, String eventType,
                                                    String status, String errorMessage) {
        ApiAuditLogMainParam param = new ApiAuditLogMainParam();

        // 时间信息
        LocalDateTime now = LocalDateTime.now();
        param.setServerTime(now);
        param.setEventTime(now);
        param.setEventDate(LocalDate.now());

        // 日志分类和类型
        param.setCategory(AuditLogCategoryEnum.SECURITY);
        param.setLogType("USER_" + eventType);
        param.setOperation("用户" + getEventDesc(eventType));

        // 主体信息
        param.setPrincipalId(String.valueOf(user.getUserId()));
        param.setPrincipalName(user.getUsername());
        param.setPrincipalType(AuditLogPrincipalTypeEnum.USER);

        // 状态信息
        param.setState("SUCCESS".equals(status) ? AuditLogStateEnum.SUCCESS : AuditLogStateEnum.FAIL);
        param.setResultMessage(errorMessage);

        // 认证信息
        param.setAuthType(AuditLogAuthTypeEnum.PASSWORD);
        param.setAuthProvider(AuditLogAuthProviderEnum.LOCAL);

        // IP和位置信息
        String clientIp = ipLocationUtil.getClientIp(request);
        param.setClientIp(clientIp);
        param.setUserAgent(request.getHeader("User-Agent"));

        // 获取地理位置
        try {
            String location = ipLocationUtil.getLocation(clientIp);
            if (location != null && !location.isEmpty()) {
                String[] parts = location.split(" ");
                if (parts.length >= 1) {
                    param.setLocationCountry(parts[0]);
                }
                if (parts.length >= 2) {
                    param.setLocationProvince(parts[1]);
                }
                if (parts.length >= 3) {
                    param.setLocationCity(parts[2]);
                }
            }
        } catch (Exception e) {
            log.debug("Failed to get location for IP: {}", clientIp);
        }

        // 服务器信息
        try {
            param.setServerIp(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            log.debug("Failed to get server IP");
        }

        // Schema版本
        param.setLogSchemaVersion(1);

        return param;
    }

    /**
     * 获取事件描述
     */
    private String getEventDesc(String eventType) {
        return switch (eventType) {
            case "LOGIN" -> "登录";
            case "LOGOUT" -> "登出";
            case "REFRESH" -> "刷新令牌";
            case "LOCKED" -> "账号锁定";
            default -> eventType;
        };
    }
}
