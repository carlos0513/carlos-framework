package com.carlos.auth.oauth2.handler;

import com.carlos.audit.api.ApiAuditLogMain;
import com.carlos.audit.api.pojo.enums.AuditLogCategoryEnum;
import com.carlos.audit.api.pojo.enums.AuditLogPrincipalTypeEnum;
import com.carlos.audit.api.pojo.enums.AuditLogStateEnum;
import com.carlos.audit.api.pojo.param.ApiAuditLogMainParam;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.util.IpLocationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * OAuth2 登出成功事件处理器
 * </p>
 *
 * <p>处理用户登出成功事件，包括：</p>
 * <ul>
 *   <li>撤销 OAuth2 授权（Token 失效）</li>
 *   <li>记录登出审计日志</li>
 *   <li>清理相关会话数据</li>
 * </ul>
 *
 * @author carlos
 * @date 2026-02-26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2LogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

    private final OAuth2AuthorizationService authorizationService;
    private final ApiAuditLogMain apiAuditLogMain;
    private final IpLocationUtil ipLocationUtil;

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        handleLogoutSuccess(authentication);
    }

    /**
     * 处理登出成功逻辑
     *
     * @param authentication 认证对象
     */
    public void handleLogoutSuccess(Authentication authentication) {
        String principalName = Optional.ofNullable(authentication.getName()).orElse("anonymous");
        log.info("用户登出成功: {}", principalName);

        try {
            // 从认证对象中提取用户信息
            UserInfo userInfo = extractUserInfo(authentication);

            // 撤销 Token
            revokeAccessToken(authentication);

            // 记录登出审计日志
            recordLogoutAudit(userInfo, principalName);

        } catch (Exception e) {
            log.error("处理登出成功事件失败: {}", principalName, e);
        }
    }

    /**
     * 从认证对象中提取用户信息
     *
     * @param authentication 认证对象
     * @return 用户信息，可能为 null
     */
    private UserInfo extractUserInfo(Authentication authentication) {
        if (authentication instanceof AbstractAuthenticationToken authToken) {
            Object principal = authToken.getPrincipal();
            if (principal instanceof UserInfo userInfo) {
                return userInfo;
            }
        }
        return null;
    }

    /**
     * 撤销访问令牌
     *
     * @param authentication 认证对象
     */
    private void revokeAccessToken(Authentication authentication) {
        if (authentication.getCredentials() instanceof String accessToken) {
            var authorization = authorizationService.findByToken(accessToken, OAuth2TokenType.ACCESS_TOKEN);
            if (authorization != null) {
                authorizationService.remove(authorization);
                log.debug("已撤销访问令牌，用户: {}", authentication.getName());
            }
        }
    }

    /**
     * 记录登出审计日志
     *
     * @param userInfo 用户信息
     * @param principalName 主体名称
     */
    private void recordLogoutAudit(UserInfo userInfo, String principalName) {
        try {
            ApiAuditLogMainParam param = buildLogoutAuditParam(userInfo, principalName);
            apiAuditLogMain.saveAuditLog(param);
            log.debug("登出审计日志已记录: {}", principalName);
        } catch (Exception e) {
            log.error("记录登出审计日志失败: {}", principalName, e);
        }
    }

    /**
     * 构建登出审计日志参数
     *
     * @param userInfo 用户信息
     * @param principalName 主体名称
     * @return 审计日志参数
     */
    private ApiAuditLogMainParam buildLogoutAuditParam(UserInfo userInfo, String principalName) {
        ApiAuditLogMainParam param = new ApiAuditLogMainParam();

        // 时间信息
        LocalDateTime now = LocalDateTime.now();
        param.setServerTime(now);
        param.setEventTime(now);
        param.setEventDate(LocalDate.now());

        // 日志分类和类型
        param.setCategory(AuditLogCategoryEnum.SECURITY);
        param.setLogType("USER_LOGOUT");
        param.setOperation("用户登出");

        // 主体信息
        if (userInfo != null) {
            param.setPrincipalId(String.valueOf(userInfo.getUserId()));
            param.setPrincipalName(userInfo.getUsername());
        } else {
            param.setPrincipalId(principalName);
            param.setPrincipalName(principalName);
        }
        param.setPrincipalType(AuditLogPrincipalTypeEnum.USER);

        // 状态信息
        param.setState(AuditLogStateEnum.SUCCESS);
        param.setResultMessage("登出成功");

        // IP和位置信息
        populateIpAndLocation(param);

        // 服务器信息
        populateServerInfo(param);

        // Schema版本
        param.setLogSchemaVersion(1);
        return param;
    }

    /**
     * 填充 IP 和位置信息
     *
     * @param param 审计日志参数
     */
    private void populateIpAndLocation(ApiAuditLogMainParam param) {
        try {
            HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);

            if (request == null) {
                return;
            }

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
                log.debug("获取 IP 地理位置失败: {}", clientIp);
            }
        } catch (Exception e) {
            log.debug("填充 IP 信息失败");
        }
    }

    /**
     * 填充服务器信息
     *
     * @param param 审计日志参数
     */
    private void populateServerInfo(ApiAuditLogMainParam param) {
        try {
            param.setServerIp(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            log.debug("获取服务器 IP 失败");
        }
    }
}
