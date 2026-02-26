package com.carlos.auth.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 登录审计日志实体
 * </p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID（全局唯一）
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 客户端ID
     */
    @TableField(value = "client_id")
    private String clientId;

    /**
     * 事件类型：LOGIN-登录、LOGOUT-登出、REFRESH-刷新令牌、LOCKED-账号锁定
     */
    @TableField(value = "event_type")
    private String eventType;

    /**
     * IP地址
     */
    @TableField(value = "ip_address")
    private String ipAddress;

    /**
     * 地理位置
     */
    @TableField(value = "location")
    private String location;

    /**
     * User-Agent
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 状态：SUCCESS-成功、FAILURE-失败
     */
    @TableField(value = "status")
    private String status;

    /**
     * 错误消息
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    private LocalDateTime loginTime;

    /**
     * 会话ID
     */
    @TableField(value = "session_id")
    private String sessionId;

    /**
     * 创建时间（分区字段）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
