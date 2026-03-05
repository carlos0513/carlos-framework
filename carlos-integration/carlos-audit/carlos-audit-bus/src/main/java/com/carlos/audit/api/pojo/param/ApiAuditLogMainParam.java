package com.carlos.audit.api.pojo.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志主表 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class ApiAuditLogMainParam implements Serializable {
    /** 主键 */
    private Long id;
    /** 日志Schema版本 */
    private Integer logSchemaVersion;
    /** 大类: SECURITY/BUSINESS/SYSTEM/AUDIT */
    private String category;
    /** 细类: USER_LOGIN/ORDER_PAY等 */
    private String logType;
    /** 服务器时间(毫秒精度) */
    private LocalDateTime serverTime;
    /** 客户端时间 */
    private LocalDateTime clientTime;
    /** 事件实际发生时间 */
    private LocalDateTime eventTime;
    /** 操作耗时(毫秒) */
    private Integer durationMs;
    /** 数据保留截止日期(TTL) */
    private LocalDate retentionDeadline;
    /** 操作主体ID */
    private String principalId;
    /** 主体类型: USER/SERVICE/SYSTEM/ANONYMOUS */
    private String principalType;
    /** 主体名称(冗余) */
    private String principalName;
    /** 租户ID(SaaS隔离) */
    private String tenantId;
    /** 部门ID */
    private String deptId;
    /** 部门名称 */
    private String deptName;
    /** 部门层级路径, 如: 1/12/156/ */
    private String deptPath;
    /** 对象类型: ORDER/USER/CONFIG */
    private String targetType;
    /** 对象唯一标识 */
    private String targetId;
    /** 对象显示名称 */
    private String targetName;
    /** 对象关键信息摘要 */
    private String targetSnapshot;
    /** 状态: SUCCESS/FAIL/PENDING/TIMEOUT/PARTIAL_SUCCESS */
    private String state;
    /** 业务结果码 */
    private String resultCode;
    /** 结果描述 */
    private String resultMessage;
    /** 操作描述(人工可读) */
    private String operation;
    /** 风险等级 0-100 */
    private Integer riskLevel;
    /** 客户端IP(支持IPv6) */
    private String clientIp;
    /** 客户端端口 */
    private Integer clientPort;
    /** 处理服务器IP */
    private String serverIp;
    /** 浏览器UA */
    private String userAgent;
    /** 设备指纹 */
    private String deviceFingerprint;
    /** 国家 */
    private String locationCountry;
    /** 省份 */
    private String locationProvince;
    /** 城市 */
    private String locationCity;
    /** 纬度 */
    private BigDecimal locationLat;
    /** 经度 */
    private BigDecimal locationLon;
    /** 认证方式: PASSWORD/SMS/OAUTH2/LDAP/CERT */
    private String authType;
    /** 认证源: LOCAL/WECHAT/DINGTALK */
    private String authProvider;
    /** 当前角色列表 */
    private String roles;
    /** 当前权限列表 */
    private String permissions;
    /** 业务渠道: WEB/APP/MINI_PROGRAM/OPEN_API */
    private String bizChannel;
    /** 业务场景 */
    private String bizScene;
    /** 业务订单号 */
    private String bizOrderNo;
    /** 关联业务ID列表 */
    private String relatedBizIds;
    /** 涉及金额 */
    private BigDecimal monetaryAmount;
    /** 批量操作批次号 */
    private String batchId;
    /** 批次内序号 */
    private Integer batchIndex;
    /** 批次总数 */
    private Integer batchTotal;
    /** 流程实例ID */
    private String processId;
    /** 任务ID */
    private String taskId;
    /** 审批人ID */
    private String approverId;
    /** 审批意见 */
    private String approvalComment;
    /** 是否有数据变更详情(0=否,1=是) */
    private Boolean hasDataChange;
    /** 实体类名 */
    private String entityClass;
    /** 数据库表名 */
    private String tableName;
    /** 变更摘要 */
    private String changeSummary;
    /** 关联技术上下文表ID */
    private Long technicalContextId;
    /** 业务标签 */
    private String dynamicTags;
    /** 业务扩展字段 */
    private String dynamicExtras;
    /**  */
    private LocalDateTime createdTime;
    /**  */
    private LocalDateTime updatedTime;
}
