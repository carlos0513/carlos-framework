package com.carlos.auth.audit.service;

import com.carlos.auth.audit.mapper.SecurityAlertMapper;
import com.carlos.auth.audit.pojo.entity.SecurityAlert;
import com.carlos.auth.audit.pojo.enums.AlertSeverity;
import com.carlos.auth.audit.pojo.enums.AlertType;
import com.carlos.auth.provider.UserInfo;
import com.carlos.auth.util.DeviceFingerprint;
import com.carlos.auth.util.IpLocationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全告警服务实现
 * </p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAlertServiceImpl implements SecurityAlertService {

    private final SecurityAlertMapper securityAlertMapper;
    private final IpLocationUtil ipLocationUtil;
    private final DeviceFingerprint deviceFingerprint;

    @Override
    public SecurityAlert createAlert(UserInfo user, AlertType alertType, AlertSeverity severity, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);
        String userAgent = deviceFingerprint.getUserAgent(request);

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

        securityAlertMapper.insert(alert);

        log.warn("Security Alert: {} - {} - User: {} (IP: {}, Location: {})",
            alert.getTitle(), alert.getSeverity(), user.getUsername(), ip, location);

        sendNotification(alert);

        return alert;
    }

    @Override
    public SecurityAlert createUnusualLocationAlert(UserInfo user, HttpServletRequest request) {
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

        securityAlertMapper.insert(alert);

        log.warn("Security Alert [异地登录]: User: {} login from {} (IP: {})", user.getUsername(), location, ip);

        sendNotification(alert);

        return alert;
    }

    @Override
    public SecurityAlert createNewDeviceAlert(UserInfo user, HttpServletRequest request) {
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

        securityAlertMapper.insert(alert);

        log.warn("Security Alert [新设备]: User: {} login from {} (IP: {}, Device: {})",
            user.getUsername(), location, ip, deviceDesc);

        sendNotification(alert);

        return alert;
    }

    @Override
    public SecurityAlert createBruteForceAlert(String identifier, HttpServletRequest request) {
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

        securityAlertMapper.insert(alert);

        log.warn("Security Alert [暴力破解]: Account: {} may be under brute force attack from {} (IP: {})",
            identifier, location, ip);

        sendNotification(alert);

        return alert;
    }

    @Override
    public SecurityAlert createOffHoursLoginAlert(UserInfo user, HttpServletRequest request) {
        String ip = ipLocationUtil.getClientIp(request);
        String location = ipLocationUtil.getLocation(ip);
        LocalTime now = LocalTime.now();

        SecurityAlert alert = new SecurityAlert();
        alert.setUserId(user.getUserId());
        alert.setUsername(user.getUsername());
        alert.setAlertType(AlertType.OFF_HOURS_LOGIN.getCode());
        alert.setSeverity(AlertSeverity.LOW.getCode());
        alert.setTitle("非工作时间登录");
        alert.setContent("用户 " + user.getUsername() + " 在非工作时间 " + now + " 登录 (IP: " + ip + ", 位置: " + location + ")");
        alert.setIpAddress(ip);
        alert.setLocation(location);
        alert.setUserAgent(deviceFingerprint.getUserAgent(request));
        alert.setHandled(false);

        securityAlertMapper.insert(alert);

        log.info("Security Alert [非工作时间]: User: {} login at {} (IP: {}, Location: {})",
            user.getUsername(), now, ip, location);

        sendNotification(alert);

        return alert;
    }

    @Override
    public void sendNotification(SecurityAlert alert) {
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
        message.put("text", String.format("用户: %s\nIP: %s\n位置: %s\n时间: %s",
            alert.getUsername(), alert.getIpAddress(), alert.getLocation(),
            alert.getCreateTime()));

        log.info("[告警通知-钉钉] 消息: {}", message);
    }

    @Override
    public List<SecurityAlert> getUserAlerts(Long userId) {
        if (userId == null) {
            return List.of();
        }

        return securityAlertMapper.selectByUserId(userId);
    }

    @Override
    public List<SecurityAlert> getUnhandledAlerts() {
        return securityAlertMapper.selectUnhandled();
    }

    @Override
    public void handleAlert(Long alertId, String remark) {
        SecurityAlert alert = securityAlertMapper.selectById(alertId);

        if (alert == null) {
            log.warn("Alert not found: {}", alertId);
            return;
        }

        alert.setHandled(true);
        alert.setHandledTime(LocalDateTime.now());
        alert.setRemark(remark);

        securityAlertMapper.updateById(alert);

        log.info("Alert {} handled by admin with remark: {}", alertId, remark);
    }
}
