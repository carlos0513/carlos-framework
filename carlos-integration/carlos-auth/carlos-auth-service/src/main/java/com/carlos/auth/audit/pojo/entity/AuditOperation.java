package com.carlos.auth.audit.pojo.entity;

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
 * <p>操作审计实体</p>
 *
 * <p>记录敏感操作的前后值对比</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审计ID
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
     * 操作类型：UPDATE_PASSWORD-修改密码、DISABLE_MFA-禁用MFA、ENABLE_MFA-启用MFA等
     */
    @TableField(value = "operation_type")
    private String operationType;

    /**
     * 资源类型：USER-用户、MFA_DEVICE-MFA设备
     */
    @TableField(value = "resource_type")
    private String resourceType;

    /**
     * 资源ID
     */
    @TableField(value = "resource_id")
    private String resourceId;

    /**
     * 操作前值（JSON格式）
     */
    @TableField(value = "before_value")
    private String beforeValue;

    /**
     * 操作后值（JSON格式）
     */
    @TableField(value = "after_value")
    private String afterValue;

    /**
     * IP地址
     */
    @TableField(value = "ip_address")
    private String ipAddress;

    /**
     * User-Agent
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
