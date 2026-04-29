package com.carlos.auth.audit.service;

import com.carlos.audit.api.ApiAuditLogMain;
import com.carlos.audit.api.pojo.enums.AuditLogCategoryEnum;
import com.carlos.audit.api.pojo.enums.AuditLogPrincipalTypeEnum;
import com.carlos.audit.api.pojo.enums.AuditLogStateEnum;
import com.carlos.audit.api.pojo.param.ApiAuditLogMainParam;
import com.carlos.auth.audit.convert.SecurityAlertConvert;
import com.carlos.auth.audit.manager.SecurityAlertManager;
import com.carlos.auth.audit.pojo.dto.SecurityAlertDTO;
import com.carlos.auth.audit.pojo.entity.SecurityAlert;
import com.carlos.auth.audit.pojo.enums.AlertSeverity;
import com.carlos.auth.audit.pojo.enums.AlertType;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.util.DeviceFingerprint;
import com.carlos.auth.util.IpLocationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全告警服务
 * </p>
 *
 * <p>处理安全告警的创建、查询、通知等功能</p>
 *
 * <p>业务逻辑服务层，通过 Manager 层进行数据获取</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAlertService {

    private final SecurityAlertManager securityAlertManager;
    private final IpLocationUtil ipLocationUtil;
    private final DeviceFingerprint deviceFingerprint;
    private final ApiAuditLogMain apiAuditLogMain;
    private final SecurityAlertConvert securityAlertConvert;

    /**
     * 创建告警
     *
     * @param user 用户信息
     * @param alertType 告警类型
     * @param severity 告警级别
     * @param request HTTP请求
     * @return 创建的告警 DTO
     */
    public SecurityAlertDTO createAlert(UserInfo user, AlertType alertType, AlertSeverity severity, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);
        String userAgent = deviceFingerprint.getUserAgent(request);

        // 构建实体
        SecurityAlert alert = new SecurityAlert();
        alert.setUserId(user.getUserId());
        alert.setUsername(user.getUsername());
        alert.setAlertType(alertType.getCode());
        alert.setSeverity(severity.getCode());
        alert.setTitle(alertType.getName());
        alert.setContent(alertType.getDescription() + " (IP: " + ip + ", 位置: " + location + ")");
        alert.setIpAddress(ip);
        alert.setLocation(location);
        alert.setUserAgent(userAgent);
        alert.setHandled(false);

        // 1. 通过 Manager 层保存到本地数据库
        securityAlertManager.save(alert);

        log.warn("Security Alert: {} - {} - User: {} (IP: {}, Location: {})",
            alert.getTitle(), alert.getSeverity(), user.getUsername(), ip, location);

        // 2. 发送通知
        sendNotification(alert);

        // 3. 同时记录到统一审计服务
        recordSecurityAlertAudit(user.getUserId(), user.getUsername(), alertType.getCode(),
            severity.getCode(), alertType.getName(), alert.getContent(), request);

        // 4. 转换为 DTO 返回
        return securityAlertConvert.entityToDto(alert);
    }

    /**
     * 创建异地登录告警
     *
     * @param user 用户信息
     * @param request HTTP请求
     * @return 创建的告警 DTO
     */
    public SecurityAlertDTO createUnusualLocationAlert(UserInfo user, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);

        SecurityAlert alert = new SecurityAlert();
        alert.setUserId(user.getUserId());
        alert.setUsername(user.getUsername());
        alert.setAlertType(AlertType.UNUSUAL_LOCATION.getCode());
        alert.setSeverity(AlertSeverity.HIGH.getCode());
        alert.setTitle("异地登录告警");
        alert.setContent("用户 " + user.getUsername() + " 从异地登录 (IP: " + ip + ", 位置: " + location + ")，请确认是否为本人操作");
        alert.setIpAddress(ip);
        alert.setLocation(location);
        alert.setUserAgent(deviceFingerprint.getUserAgent(request));
        alert.setHandled(false);

        // 1. 通过 Manager 层保存到本地数据库
        securityAlertManager.save(alert);

        log.warn("Security Alert [异地登录]: User: {} login from {} (IP: {})", user.getUsername(), location, ip);

        // 2. 发送通知
        sendNotification(alert);

        // 3. 同时记录到统一审计服务
        recordSecurityAlertAudit(user.getUserId(), user.getUsername(),
            AlertType.UNUSUAL_LOCATION.getCode(),
            AlertSeverity.HIGH.getCode(),
            "异地登录告警",
            alert.getContent(),
            request);

        return securityAlertConvert.entityToDto(alert);
    }

    /**
     * 创建新设备登录告警
     *
     * @param user 用户信息
     * @param request HTTP请求
     * @return 创建的告警 DTO
     */
    public SecurityAlertDTO createNewDeviceAlert(UserInfo user, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);
        String deviceDesc = deviceFingerprint.getDeviceDescription(request);

        SecurityAlert alert = new SecurityAlert();
        alert.setUserId(user.getUserId());
        alert.setUsername(user.getUsername());
        alert.setAlertType(AlertType.NEW_DEVICE.getCode());
        alert.setSeverity(AlertSeverity.MEDIUM.getCode());
        alert.setTitle("新设备登录告警");
        alert.setContent("用户 " + user.getUsername() + " 从新设备登录 (设备: " + deviceDesc + ", IP: " + ip + ", 位置: " + location + ")");
        alert.setIpAddress(ip);
        alert.setLocation(location);
        alert.setUserAgent(deviceFingerprint.getUserAgent(request));
        alert.setHandled(false);

        // 1. 通过 Manager 层保存到本地数据库
        securityAlertManager.save(alert);

        log.warn("Security Alert [新设备]: User: {} login from {} (IP: {}, Device: {})",
            user.getUsername(), location, ip, deviceDesc);

        // 2. 发送通知
        sendNotification(alert);

        // 3. 同时记录到统一审计服务
        recordSecurityAlertAudit(user.getUserId(), user.getUsername(),
            AlertType.NEW_DEVICE.getCode(),
            AlertSeverity.MEDIUM.getCode(),
            "新设备登录告警",
            alert.getContent(),
            request);

        return securityAlertConvert.entityToDto(alert);
    }

    /**
     * 创建暴力破解告警
     *
     * @param identifier 用户名或IP
     * @param request HTTP请求
     * @return 创建的告警 DTO
     */
    public SecurityAlertDTO createBruteForceAlert(String identifier, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);

        SecurityAlert alert = new SecurityAlert();
        alert.setUserId(null);
        alert.setUsername(identifier);
        alert.setAlertType(AlertType.BRUTE_FORCE.getCode());
        alert.setSeverity(AlertSeverity.CRITICAL.getCode());
        alert.setTitle("暴力破解告警");
        alert.setContent("检测到账号 " + identifier + " 可能被暴力破解 (IP: " + ip + ", 位置: " + location + ")");
        alert.setIpAddress(ip);
        alert.setLocation(location);
        alert.setUserAgent(deviceFingerprint.getUserAgent(request));
        alert.setHandled(false);

        // 1. 通过 Manager 层保存到本地数据库
        securityAlertManager.save(alert);

        log.warn("Security Alert [暴力破解]: Account: {} may be under brute force attack from {} (IP: {})",
            identifier, location, ip);

        // 2. 发送通知
        sendNotification(alert);

        // 3. 同时记录到统一审计服务（使用IP作为主体）
        recordSecurityAlertAudit(null, identifier,
            AlertType.BRUTE_FORCE.getCode(),
            AlertSeverity.CRITICAL.getCode(),
            "暴力破解告警",
            alert.getContent(),
            request);

        return securityAlertConvert.entityToDto(alert);
    }

    /**
     * 创建非工作时间登录告警
     *
     * @param user 用户信息
     * @param request HTTP请求
     * @return 创建的告警 DTO
     */
    public SecurityAlertDTO createOffHoursLoginAlert(UserInfo user, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);

        SecurityAlert alert = new SecurityAlert();
        alert.setUserId(user.getUserId());
        alert.setUsername(user.getUsername());
        alert.setAlertType(AlertType.OFF_HOURS_LOGIN.getCode());
        alert.setSeverity(AlertSeverity.LOW.getCode());
        alert.setTitle("非工作时间登录");
        alert.setContent("用户 " + user.getUsername() + " 在非工作时间登录 (IP: " + ip + ", 位置: " + location + ")");
        alert.setIpAddress(ip);
        alert.setLocation(location);
        alert.setUserAgent(deviceFingerprint.getUserAgent(request));
        alert.setHandled(false);

        // 1. 通过 Manager 层保存到本地数据库
        securityAlertManager.save(alert);

        log.info("Security Alert [非工作时间]: User: {} login at {} (IP: {}, Location: {})",
            user.getUsername(), LocalDateTime.now(), ip, location);

        // 2. 发送通知
        sendNotification(alert);

        // 3. 同时记录到统一审计服务
        recordSecurityAlertAudit(user.getUserId(), user.getUsername(),
            AlertType.OFF_HOURS_LOGIN.getCode(),
            AlertSeverity.LOW.getCode(),
            "非工作时间登录",
            alert.getContent(),
            request);

        return securityAlertConvert.entityToDto(alert);
    }

    /**
     * 获取用户告警列表
     *
     * @param userId 用户ID
     * @return 告警 DTO 列表
     */
    public List<SecurityAlertDTO> getUserAlerts(Long userId) {
        // 通过 Manager 层查询数据
        List<SecurityAlert> alerts = securityAlertManager.selectByUserId(userId);
        // 转换为 DTO 列表
        return alerts.stream()
            .map(securityAlertConvert::entityToDto)
            .toList();
    }

    /**
     * 获取未处理的告警
     *
     * @return 告警 DTO 列表
     */
    public List<SecurityAlertDTO> getUnhandledAlerts() {
        // 通过 Manager 层查询数据
        List<SecurityAlert> alerts = securityAlertManager.selectUnhandled();
        // 转换为 DTO 列表
        return alerts.stream()
            .map(securityAlertConvert::entityToDto)
            .toList();
    }

    /**
     * 标记告警为已处理
     *
     * @param alertId 告警ID
     * @param remark 备注
     */
    public void handleAlert(Long alertId, String remark) {
        // 通过 Manager 层查询
        SecurityAlert alert = securityAlertManager.getById(alertId);

        if (alert == null) {
            log.warn("Alert not found: {}", alertId);
            return;
        }

        alert.setHandled(true);
        alert.setHandledTime(LocalDateTime.now());
        alert.setRemark(remark);

        // 通过 Manager 层更新
        securityAlertManager.updateById(alert);

        log.info("Alert {} handled by admin with remark: {}", alertId, remark);
    }

    /**
     * 发送告警通知
     */
    private void sendNotification(SecurityAlert alert) {
        // 发送邮件通知
        sendEmailNotification(alert);

        // 高级别告警发送短信
        if (AlertSeverity.HIGH.getCode().equals(alert.getSeverity()) ||
            AlertSeverity.CRITICAL.getCode().equals(alert.getSeverity())) {
            sendSmsNotification(alert);
        }

        // 发送钉钉Webhook通知（企业场景）
        sendDingTalkNotification(alert);
    }

    private void sendEmailNotification(SecurityAlert alert) {
        // TODO: 集成邮件发送服务
        log.info("[告警通知-邮件] 标题: {}, 内容: {}, 级别: {}",
            alert.getTitle(), alert.getContent(), alert.getSeverity());
    }

    private void sendSmsNotification(SecurityAlert alert) {
        // TODO: 集成短信发送服务
        log.info("[告警通知-短信] 标题: {}, 级别: {}", alert.getTitle(), alert.getSeverity());
    }

    private void sendDingTalkNotification(SecurityAlert alert) {
        // TODO: 集成钉钉Webhook
        Map<String, Object> message = new HashMap<>();
        message.put("title", "安全告警: " + alert.getTitle());
        message.put("text", """
            用户: %s
            IP: %s
            位置: %s
            时间: %s""".formatted(
            alert.getUsername(), alert.getIpAddress(), alert.getLocation(),
            alert.getCreateTime()));

        log.info("[告警通知-钉钉] 消息: {}", message);
    }

    /**
     * 异步记录安全告警审计日志
     */
    @Async("virtualTaskExecutor")
    public void recordSecurityAlertAudit(Long userId, String username, String alertType,
                                         String severity, String title, String content,
                                         HttpServletRequest request) {
        try {
            ApiAuditLogMainParam param = buildAuditLogParam(userId, username, alertType,
                severity, title, content, request);
            apiAuditLogMain.saveAuditLog(param);
            log.debug("Security alert audit log sent: user={}, alertType={}", username, alertType);
        } catch (Exception e) {
            log.error("Failed to send security alert audit log", e);
        }
    }

    /**
     * 构建审计日志参数
     */
    private ApiAuditLogMainParam buildAuditLogParam(Long userId, String username, String alertType,
                                                    String severity, String title, String content,
                                                    HttpServletRequest request) {
        ApiAuditLogMainParam param = new ApiAuditLogMainParam();

        // 时间信息
        LocalDateTime now = LocalDateTime.now();
        param.setServerTime(now);
        param.setEventTime(now);
        param.setEventDate(LocalDate.now());

        // 日志分类和类型
        param.setCategory(AuditLogCategoryEnum.SECURITY);
        param.setLogType("SECURITY_ALERT_" + alertType);
        param.setOperation(title);

        // 主体信息
        param.setPrincipalId(userId != null ? String.valueOf(userId) : null);
        param.setPrincipalName(username);
        param.setPrincipalType(AuditLogPrincipalTypeEnum.USER);

        // 状态信息
        param.setState(AuditLogStateEnum.SUCCESS);
        param.setResultMessage(content);

        // 风险等级
        param.setRiskLevel(getRiskLevel(severity));

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
     * 获取风险等级数值
     */
    private Integer getRiskLevel(String severity) {
        return switch (severity) {
            case "critical" -> 100;
            case "high" -> 75;
            case "medium" -> 50;
            case "low" -> 25;
            default -> 0;
        };
    }
}
