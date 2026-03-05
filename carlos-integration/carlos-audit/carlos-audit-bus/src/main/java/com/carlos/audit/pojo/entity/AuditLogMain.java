package com.carlos.audit.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志主表 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_main")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogMain extends Model<AuditLogMain> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 日志Schema版本
     */
    @TableField(value = "log_schema_version")
    private Integer logSchemaVersion;
    /**
     * 大类: SECURITY/BUSINESS/SYSTEM/AUDIT
     */
    @TableField(value = "category")
    private String category;
    /**
     * 细类: USER_LOGIN/ORDER_PAY等
     */
    @TableField(value = "log_type")
    private String logType;
    /**
     * 服务器时间(毫秒精度)
     */
    @TableField(value = "server_time")
    private LocalDateTime serverTime;
    /**
     * 客户端时间
     */
    @TableField(value = "client_time")
    private LocalDateTime clientTime;
    /**
     * 事件实际发生时间
     */
    @TableField(value = "event_time")
    private LocalDateTime eventTime;
    /**
     * 操作耗时(毫秒)
     */
    @TableField(value = "duration_ms")
    private Integer durationMs;
    /**
     * 数据保留截止日期(TTL)
     */
    @TableField(value = "retention_deadline")
    private LocalDate retentionDeadline;
    /**
     * 操作主体ID
     */
    @TableField(value = "principal_id")
    private String principalId;
    /**
     * 主体类型: USER/SERVICE/SYSTEM/ANONYMOUS
     */
    @TableField(value = "principal_type")
    private String principalType;
    /**
     * 主体名称(冗余)
     */
    @TableField(value = "principal_name")
    private String principalName;
    /**
     * 租户ID(SaaS隔离)
     */
    @TableField(value = "tenant_id")
    private String tenantId;
    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private String deptId;
    /**
     * 部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;
    /**
     * 部门层级路径, 如: 1/12/156/
     */
    @TableField(value = "dept_path")
    private String deptPath;
    /**
     * 对象类型: ORDER/USER/CONFIG
     */
    @TableField(value = "target_type")
    private String targetType;
    /**
     * 对象唯一标识
     */
    @TableField(value = "target_id")
    private String targetId;
    /**
     * 对象显示名称
     */
    @TableField(value = "target_name")
    private String targetName;
    /**
     * 对象关键信息摘要
     */
    @TableField(value = "target_snapshot")
    private String targetSnapshot;
    /**
     * 状态: SUCCESS/FAIL/PENDING/TIMEOUT/PARTIAL_SUCCESS
     */
    @TableField(value = "state")
    private String state;
    /**
     * 业务结果码
     */
    @TableField(value = "result_code")
    private String resultCode;
    /**
     * 结果描述
     */
    @TableField(value = "result_message")
    private String resultMessage;
    /**
     * 操作描述(人工可读)
     */
    @TableField(value = "operation")
    private String operation;
    /**
     * 风险等级 0-100
     */
    @TableField(value = "risk_level")
    private Integer riskLevel;
    /**
     * 客户端IP(支持IPv6)
     */
    @TableField(value = "client_ip")
    private String clientIp;
    /**
     * 客户端端口
     */
    @TableField(value = "client_port")
    private Integer clientPort;
    /**
     * 处理服务器IP
     */
    @TableField(value = "server_ip")
    private String serverIp;
    /**
     * 浏览器UA
     */
    @TableField(value = "user_agent")
    private String userAgent;
    /**
     * 设备指纹
     */
    @TableField(value = "device_fingerprint")
    private String deviceFingerprint;
    /**
     * 国家
     */
    @TableField(value = "location_country")
    private String locationCountry;
    /**
     * 省份
     */
    @TableField(value = "location_province")
    private String locationProvince;
    /**
     * 城市
     */
    @TableField(value = "location_city")
    private String locationCity;
    /**
     * 纬度
     */
    @TableField(value = "location_lat")
    private BigDecimal locationLat;
    /**
     * 经度
     */
    @TableField(value = "location_lon")
    private BigDecimal locationLon;
    /**
     * 认证方式: PASSWORD/SMS/OAUTH2/LDAP/CERT
     */
    @TableField(value = "auth_type")
    private String authType;
    /**
     * 认证源: LOCAL/WECHAT/DINGTALK
     */
    @TableField(value = "auth_provider")
    private String authProvider;
    /**
     * 当前角色列表
     */
    @TableField(value = "roles")
    private String roles;
    /**
     * 当前权限列表
     */
    @TableField(value = "permissions")
    private String permissions;
    /**
     * 业务渠道: WEB/APP/MINI_PROGRAM/OPEN_API
     */
    @TableField(value = "biz_channel")
    private String bizChannel;
    /**
     * 业务场景
     */
    @TableField(value = "biz_scene")
    private String bizScene;
    /**
     * 业务订单号
     */
    @TableField(value = "biz_order_no")
    private String bizOrderNo;
    /**
     * 关联业务ID列表
     */
    @TableField(value = "related_biz_ids")
    private String relatedBizIds;
    /**
     * 涉及金额
     */
    @TableField(value = "monetary_amount")
    private BigDecimal monetaryAmount;
    /**
     * 批量操作批次号
     */
    @TableField(value = "batch_id")
    private String batchId;
    /**
     * 批次内序号
     */
    @TableField(value = "batch_index")
    private Integer batchIndex;
    /**
     * 批次总数
     */
    @TableField(value = "batch_total")
    private Integer batchTotal;
    /**
     * 流程实例ID
     */
    @TableField(value = "process_id")
    private String processId;
    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    private String taskId;
    /**
     * 审批人ID
     */
    @TableField(value = "approver_id")
    private String approverId;
    /**
     * 审批意见
     */
    @TableField(value = "approval_comment")
    private String approvalComment;
    /**
     * 是否有数据变更详情(0=否,1=是)
     */
    @TableField(value = "has_data_change")
    private Boolean hasDataChange;
    /**
     * 实体类名
     */
    @TableField(value = "entity_class")
    private String entityClass;
    /**
     * 数据库表名
     */
    @TableField(value = "table_name")
    private String tableName;
    /**
     * 变更摘要
     */
    @TableField(value = "change_summary")
    private String changeSummary;
    /**
     * 关联技术上下文表ID
     */
    @TableField(value = "technical_context_id")
    private Long technicalContextId;
    /**
     * 业务标签
     */
    @TableField(value = "dynamic_tags")
    private String dynamicTags;
    /**
     * 业务扩展字段
     */
    @TableField(value = "dynamic_extras")
    private String dynamicExtras;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;
    /**
     *
     */
    @TableField(value = "updated_time")
    private LocalDateTime updatedTime;

}
