package com.carlos.auth.audit.service;

import com.carlos.auth.audit.pojo.entity.SecurityAlert;
import com.carlos.auth.audit.pojo.enums.AlertSeverity;
import com.carlos.auth.audit.pojo.enums.AlertType;
import com.carlos.auth.provider.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 安全告警服务接口
 * </p>
 *
 * <p>处理安全告警的创建、查询、通知等功能</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
public interface SecurityAlertService {

    /**
     * 创建告警
     *
     * @param user 用户信息
     * @param alertType 告警类型
     * @param severity 告警级别
     * @param request HTTP请求
     * @return 创建的告警
     */
    SecurityAlert createAlert(UserInfo user, AlertType alertType, AlertSeverity severity, HttpServletRequest request);

    /**
     * 创建异地登录告警
     *
     * @param user 用户信息
     * @param request HTTP请求
     * @return 创建的告警
     */
    SecurityAlert createUnusualLocationAlert(UserInfo user, HttpServletRequest request);

    /**
     * 创建新设备登录告警
     *
     * @param user 用户信息
     * @param request HTTP请求
     * @return 创建的告警
     */
    SecurityAlert createNewDeviceAlert(UserInfo user, HttpServletRequest request);

    /**
     * 创建暴力破解告警
     *
     * @param identifier 用户名或IP
     * @param request HTTP请求
     * @return 创建的告警
     */
    SecurityAlert createBruteForceAlert(String identifier, HttpServletRequest request);

    /**
     * 创建非工作时间登录告警
     *
     * @param user 用户信息
     * @param request HTTP请求
     * @return 创建的告警
     */
    SecurityAlert createOffHoursLoginAlert(UserInfo user, HttpServletRequest request);

    /**
     * 发送告警通知
     *
     * @param alert 告警信息
     */
    void sendNotification(SecurityAlert alert);

    /**
     * 获取用户告警列表
     *
     * @param userId 用户ID
     * @return 告警列表
     */
    List<SecurityAlert> getUserAlerts(Long userId);

    /**
     * 获取未处理的告警
     *
     * @return 未处理告警列表
     */
    List<SecurityAlert> getUnhandledAlerts();

    /**
     * 标记告警为已处理
     *
     * @param alertId 告警ID
     * @param remark 备注
     */
    void handleAlert(Long alertId, String remark);
}
