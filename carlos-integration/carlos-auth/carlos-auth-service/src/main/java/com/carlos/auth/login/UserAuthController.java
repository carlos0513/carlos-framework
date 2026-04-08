package com.carlos.auth.login;

import com.carlos.auth.audit.service.SecurityAlertService;
import com.carlos.auth.login.dto.LoginRequest;
import com.carlos.auth.login.dto.LoginResponse;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.provider.UserProvider;
import com.carlos.auth.security.manager.IpBlockManager;
import com.carlos.auth.util.IpLocationUtil;
import com.carlos.core.response.Result;
import com.carlos.redis.ratelimit.RateLimitUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 用户认证控制器
 *
 * <p>提供用户登录、登出、获取当前用户等认证相关接口。</p>
 *
 * <p><strong>与 {@link com.carlos.auth.web.TokenController} 的区别：</strong></p>
 * <ul>
 *   <li>此类：面向最终用户，处理用户名/密码登录、Session 管理等用户级操作</li>
 *   <li>TokenController：面向 OAuth2 客户端，处理 Token 的撤销、检查等令牌级操作</li>
 * </ul>
 *
 * @author Carlos
 * @date 2026-02-26
 * @see com.carlos.auth.web.TokenController
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "用户认证", description = "用户登录、登出等认证接口")
public class UserAuthController {

    /**
     * 登录服务
     */
    private final UserLoginService loginService;

    /**
     * IP封禁管理器
     */
    private final IpBlockManager ipBlockManager;

    /**
     * IP定位工具
     */
    private final IpLocationUtil ipLocationUtil;

    /**
     * 安全告警服务
     */
    private final SecurityAlertService securityAlertService;

    /**
     * 用户服务
     */
    private final UserProvider userProvider;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    @Operation(summary = "用户登录", description = "支持用户名、邮箱或手机号登录，返回访问令牌")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) {
        log.info("Received login request for user: {}", loginRequest.getUsername());
        String clientIp = ipLocationUtil.getClientIp(httpRequest);

        try {
            // 检查IP速率限制（50次/分钟）
            if (!RateLimitUtil.tryAcquire("auth:rate:ip:" + clientIp, 50, 1, TimeUnit.MINUTES)) {
                log.warn("IP rate limit exceeded: {}", clientIp);
                return Result.error("请求过于频繁，请稍后再试");
            }

            // 检查用户速率限制（5次/分钟）
            if (!RateLimitUtil.tryAcquire("auth:rate:login:" + loginRequest.getUsername(), 5, 1, TimeUnit.MINUTES)) {
                log.warn("Login rate limit exceeded for user: {}", loginRequest.getUsername());
                return Result.error("请求过于频繁，请稍后再试");
            }

            // 检查IP是否被封禁
            if (ipBlockManager.isIpBlocked(httpRequest)) {
                log.warn("IP is blocked: {}", clientIp);
                return Result.error("登录失败次数过多，请1小时后再试");
            }

            // 检查账号是否被锁定
            if (loginService.isAccountLocked(loginRequest.getUsername())) {
                log.warn("Account is locked: {}", loginRequest.getUsername());
                return Result.error("账号已锁定，请15分钟后再试");
            }

            // 执行登录
            LoginResponse loginResponse = loginService.login(loginRequest);

            // 记录登录成功
            loginService.recordLoginSuccess(loginRequest.getUsername());

            log.info("Login successful for user: {} (IP: {}, Location: {})",
                loginRequest.getUsername(), clientIp, ipLocationUtil.getLocation(clientIp));

            return Result.success(loginResponse, "登录成功");
        } catch (BadCredentialsException e) {
            log.warn("Login failed - bad credentials for user: {}", loginRequest.getUsername());

            // 记录登录失败（账号和IP）
            loginService.recordLoginFailure(loginRequest.getUsername(), httpRequest);

            // 检查是否需要触发暴力破解告警（登录尝试次数 > 10）
            UserInfo user = userProvider.loadUserByIdentifier(loginRequest.getUsername());
            if (user != null) {
                // 检查登录失败次数
                // TODO: 从Redis获取失败次数，如果 > 10，触发告警
                if (shouldTriggerBruteForceAlert(loginRequest.getUsername(), httpRequest)) {
                    securityAlertService.createBruteForceAlert(loginRequest.getUsername(), httpRequest);
                }
            }
            return Result.error("用户名或密码错误");
        } catch (InternalAuthenticationServiceException e) {
            log.error("Login failed - internal error for user: {}", loginRequest.getUsername(), e);

            // 记录登录失败（只记录IP，不记录账号）
            ipBlockManager.recordLoginFailure(httpRequest);

            return Result.error("登录失败，请稍后重试");

        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", loginRequest.getUsername(), e);

            // 记录登录失败（只记录IP）
            ipBlockManager.recordLoginFailure(httpRequest);

            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 用户登出
     *
     * @param accessToken 访问令牌（从请求头获取）
     * @return 登出结果
     */
    @Operation(summary = "用户登出", description = "用户登出，令牌将被加入黑名单")
    @PostMapping("/logout")
    public Result<Object> logout(@RequestHeader(value = "Authorization", required = false) String accessToken) {
        log.info("Received logout request");
        try {
            // 从Authorization头中提取令牌
            String token = extractTokenFromHeader(accessToken);
            if (token != null) {
                loginService.logout(token, null);
            }

            // 清除安全上下文
            SecurityContextHolder.clearContext();
            log.info("Logout successful");
            return Result.success("登出成功");
        } catch (Exception e) {
            log.error("Error during logout", e);
            return Result.error("登出失败");
        }
    }

    /**
     * 从Authorization头中提取令牌
     *
     * @param authorizationHeader Authorization头
     * @return 令牌（不含Bearer前缀）
     */
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return authorizationHeader;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/me")
    public Result<UserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.error("未登录");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return Result.success((UserDetails) principal);
        }
        return Result.error("无法获取用户信息");
    }

    /**
     * 检查是否需要触发暴力破解告警
     *
     * @param username 用户名
     * @param request HTTP请求
     * @return true-需要触发告警
     */
    private boolean shouldTriggerBruteForceAlert(String username, HttpServletRequest request) {
        // 检查IP在5分钟内的失败次数
        String ip = ipLocationUtil.getClientIp(request);
        // TODO: 实现具体的检查逻辑
        // 这可能需要检查Redis中该IP的登录失败次数
        return false; // 暂时返回false，需要根据实际实现
    }
}
