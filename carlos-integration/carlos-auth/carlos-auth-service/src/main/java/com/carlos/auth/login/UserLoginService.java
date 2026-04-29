package com.carlos.auth.login;

import com.carlos.audit.api.ApiAuditLogMain;
import com.carlos.audit.api.pojo.enums.*;
import com.carlos.audit.api.pojo.param.ApiAuditLogMainParam;
import com.carlos.auth.api.enums.AuthErrorCode;
import com.carlos.auth.config.OAuth2Properties;
import com.carlos.auth.login.dto.LoginRequest;
import com.carlos.auth.login.dto.LoginResponse;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.security.manager.IpBlockManager;
import com.carlos.auth.security.manager.LoginAttemptManager;
import com.carlos.auth.util.IpLocationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户登录服务
 *
 * <p>处理用户登录逻辑，包括用户名密码验证、OAuth2 令牌颁发、登录审计等。</p>
 *
 * <p><strong>与标准 OAuth2 流程的关系：</strong></p>
 * <p>此类提供的自定义登录接口（/login）是供内部应用直接获取令牌使用的便捷接口。
 * 它内部使用了与标准 OAuth2 /oauth2/token 端点相同的 Token 生成机制，包括：</p>
 * <ul>
 *   <li>{@link OAuth2TokenGenerator} - 标准 Token 生成器</li>
 *   <li>{@link OAuth2AuthorizationService} - 授权信息存储（支持 Token 撤销）</li>
 *   <li>{@link RegisteredClientRepository} - 客户端配置</li>
 * </ul>
 *
 * <p>对于第三方应用集成，建议直接使用标准 OAuth2 端点：</p>
 * <pre>
 * POST /oauth2/token
 * Content-Type: application/x-www-form-urlencoded
 *
 * grant_type=password&username=user&password=pass&client_id=client&scope=read
 * </pre>
 *
 * @author Carlos
 * @date 2026-02-26
 * @see org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpoint
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService {

    /**
     * 用户服务
     */
    private final UserProvider userProvider;

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
     * OAuth2 Token 生成器
     */
    private final OAuth2TokenGenerator<?> tokenGenerator;

    /**
     * 授权信息服务
     */
    private final OAuth2AuthorizationService authorizationService;

    /**
     * 客户端仓库
     */
    private final RegisteredClientRepository registeredClientRepository;

    /**
     * 审计日志 Feign 接口
     */
    private final ApiAuditLogMain apiAuditLogMain;

    /**
     * OAuth2 配置属性
     */
    private final OAuth2Properties oAuth2Properties;

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
            UserInfo user = userProvider.loadUserByIdentifier(loginRequest.getUsername());

            if (user == null) {
                log.error("User not found after authentication: {}", loginRequest.getUsername());
                throw AuthErrorCode.AUTH_USER_NOT_FOUND.exception();
            }

            // 检查是否需要MFA验证
            boolean shouldTriggerMfa = true;
            boolean shouldRecommendMfa = shouldTriggerMfa && !Boolean.TRUE.equals(user.getMfaEnabled());
            boolean mfaRequired = shouldTriggerMfa && Boolean.TRUE.equals(user.getMfaEnabled());

            // 优先使用请求中的 clientId，否则使用默认客户端
            String clientId = StringUtils.hasText(loginRequest.getClientId())
                ? loginRequest.getClientId()
                : oAuth2Properties.getLogin().getDefaultClientId();

            // 构建登录响应（使用标准 OAuth2 Token 生成）
            LoginResponse response = buildLoginResponse(user, authentication, clientId);
            response.setMfaRequired(mfaRequired);
            response.setMfaRecommended(shouldRecommendMfa);

            // 异步记录登录成功审计日志
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
    public LoginResponse loginWithUserDetails(org.springframework.security.core.userdetails.UserDetails userDetails) {
        log.info("Building login response for user: {}", userDetails.getUsername());

        // 注意：此处不生成真实JWT令牌
        // 真实的JWT令牌应通过OAuth2的/oauth2/token端点获取
        // 使用authorization_code + PKCE流程（生产环境推荐）

        return LoginResponse.builder()
            .accessToken("") // 空令牌，需通过/oauth2/token获取
            .tokenType("Bearer")
            .expiresIn(oAuth2Properties.getLogin().getAccessTokenTtl())
            .build();
    }

    /**
     * 构建登录响应
     *
     * <p>使用标准 OAuth2 Token 生成机制生成访问令牌和刷新令牌。</p>
     *
     * @param user 用户信息
     * @param authentication 认证信息
     * @return 登录响应
     */
    private LoginResponse buildLoginResponse(UserInfo user, Authentication authentication, String clientId) {
        // 如果未指定客户端，使用默认客户端
        String resolvedClientId = StringUtils.hasText(clientId) ? clientId : oAuth2Properties.getLogin().getDefaultClientId();

        // 获取客户端
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(resolvedClientId);
        if (registeredClient == null) {
            log.error("Client not found: {}", resolvedClientId);
            throw AuthErrorCode.AUTH_CLIENT_NOT_FOUND.exception("客户端配置不存在: %s", resolvedClientId);
        }

        // 构建 Token 上下文
        Set<String> authorizedScopes = registeredClient.getScopes();
        if (CollectionUtils.isEmpty(authorizedScopes)) {
            authorizedScopes = new HashSet<>();
            authorizedScopes.add(OidcScopes.OPENID);
        }

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(authentication)
            .authorizedScopes(authorizedScopes)
            // 不设置 authorizationGrantType，因为这是直接登录而非 OAuth2 授权流程
            .authorizationGrant(authentication);
        // @formatter:on

        // 构建授权信息
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
            .withRegisteredClient(registeredClient)
            .principalName(authentication.getName());
        // 不设置 authorizationGrantType

        // 生成访问令牌
        OAuth2TokenContext tokenContext = tokenContextBuilder
            .tokenType(OAuth2TokenType.ACCESS_TOKEN)
            .build();
        OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);

        if (generatedAccessToken == null) {
            log.error("Failed to generate access token for user: {}", user.getUsername());
            throw AuthErrorCode.AUTH_TOKEN_GENERATE_FAILED.exception();
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.getTokenValue(),
            generatedAccessToken.getIssuedAt(),
            generatedAccessToken.getExpiresAt(),
            tokenContext.getAuthorizedScopes()
        );

        authorizationBuilder
            .id(accessToken.getTokenValue())
            .accessToken(accessToken);

        // 生成刷新令牌
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            tokenContext = tokenContextBuilder
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .build();
            OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContext);

            if (generatedRefreshToken instanceof OAuth2RefreshToken) {
                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(refreshToken);
            }
        }

        // 保存授权信息（支持 Token 撤销）
        OAuth2Authorization authorization = authorizationBuilder.build();
        authorizationService.save(authorization);

        log.info("Generated OAuth2 tokens for user: {}, accessToken expires at: {}",
            user.getUsername(), accessToken.getExpiresAt());

        // 构建用户信息
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
            .id(user.getUserId())
            .username(user.getUsername())
            .email(user.getEmail())
            .phone(user.getPhone())
            .build();

        // 计算有效期（秒）
        long expiresIn = 0;
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            expiresIn = Duration.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()).getSeconds();
        }

        return LoginResponse.builder()
            .accessToken(accessToken.getTokenValue())
            .refreshToken(refreshToken != null ? refreshToken.getTokenValue() : null)
            .tokenType(accessToken.getTokenType().getValue())
            .expiresIn(expiresIn)
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
     * <p>记录登出审计日志，将令牌加入黑名单（撤销授权）</p>
     *
     * @param accessToken 访问令牌
     * @param user 当前用户
     */
    public void logout(String accessToken, UserInfo user) {
        log.info("User logout: {}", user.getUsername());

        try {
            // 撤销授权（使 Token 失效）
            if (accessToken != null && !accessToken.isBlank()) {
                OAuth2Authorization authorization = authorizationService.findByToken(
                    accessToken, OAuth2TokenType.ACCESS_TOKEN);
                if (authorization != null) {
                    authorizationService.remove(authorization);
                    log.debug("Revoked OAuth2 authorization for user: {}", user.getUsername());
                }
            }

            // 异步记录登出审计日志
            recordLoginAudit(user, "LOGOUT", "SUCCESS", null);

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
