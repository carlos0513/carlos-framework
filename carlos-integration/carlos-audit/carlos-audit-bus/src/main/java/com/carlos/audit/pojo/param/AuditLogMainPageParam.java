package com.carlos.audit.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志主表 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计日志主表列表查询参数")
public class AuditLogMainPageParam extends ParamPage {
    @Schema(description = "日志Schema版本")
    private Integer logSchemaVersion;
    @Schema(description = "大类: SECURITY/BUSINESS/SYSTEM/AUDIT")
    private String category;
    @Schema(description = "细类: USER_LOGIN/ORDER_PAY等")
    private String logType;
    @Schema(description = "服务器时间(毫秒精度)")
    private LocalDateTime serverTime;
    @Schema(description = "客户端时间")
    private LocalDateTime clientTime;
    @Schema(description = "事件实际发生时间")
    private LocalDateTime eventTime;
    @Schema(description = "操作耗时(毫秒)")
    private Integer durationMs;
    @Schema(description = "数据保留截止日期(TTL)")
    private LocalDate retentionDeadline;
    @Schema(description = "操作主体ID")
    private String principalId;
    @Schema(description = "主体类型: USER/SERVICE/SYSTEM/ANONYMOUS")
    private String principalType;
    @Schema(description = "主体名称(冗余)")
    private String principalName;
    @Schema(description = "租户ID(SaaS隔离)")
    private String tenantId;
    @Schema(description = "部门ID")
    private String deptId;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "部门层级路径, 如: 1/12/156/")
    private String deptPath;
    @Schema(description = "对象类型: ORDER/USER/CONFIG")
    private String targetType;
    @Schema(description = "对象唯一标识")
    private String targetId;
    @Schema(description = "对象显示名称")
    private String targetName;
    @Schema(description = "对象关键信息摘要")
    private String targetSnapshot;
    @Schema(description = "状态: SUCCESS/FAIL/PENDING/TIMEOUT/PARTIAL_SUCCESS")
    private String state;
    @Schema(description = "业务结果码")
    private String resultCode;
    @Schema(description = "结果描述")
    private String resultMessage;
    @Schema(description = "操作描述(人工可读)")
    private String operation;
    @Schema(description = "风险等级 0-100")
    private Integer riskLevel;
    @Schema(description = "客户端IP(支持IPv6)")
    private String clientIp;
    @Schema(description = "客户端端口")
    private Integer clientPort;
    @Schema(description = "处理服务器IP")
    private String serverIp;
    @Schema(description = "浏览器UA")
    private String userAgent;
    @Schema(description = "设备指纹")
    private String deviceFingerprint;
    @Schema(description = "国家")
    private String locationCountry;
    @Schema(description = "省份")
    private String locationProvince;
    @Schema(description = "城市")
    private String locationCity;
    @Schema(description = "纬度")
    private BigDecimal locationLat;
    @Schema(description = "经度")
    private BigDecimal locationLon;
    @Schema(description = "认证方式: PASSWORD/SMS/OAUTH2/LDAP/CERT")
    private String authType;
    @Schema(description = "认证源: LOCAL/WECHAT/DINGTALK")
    private String authProvider;
    @Schema(description = "当前角色列表")
    private String roles;
    @Schema(description = "当前权限列表")
    private String permissions;
    @Schema(description = "业务渠道: WEB/APP/MINI_PROGRAM/OPEN_API")
    private String bizChannel;
    @Schema(description = "业务场景")
    private String bizScene;
    @Schema(description = "业务订单号")
    private String bizOrderNo;
    @Schema(description = "关联业务ID列表")
    private String relatedBizIds;
    @Schema(description = "涉及金额")
    private BigDecimal monetaryAmount;
    @Schema(description = "批量操作批次号")
    private String batchId;
    @Schema(description = "批次内序号")
    private Integer batchIndex;
    @Schema(description = "批次总数")
    private Integer batchTotal;
    @Schema(description = "流程实例ID")
    private String processId;
    @Schema(description = "任务ID")
    private String taskId;
    @Schema(description = "审批人ID")
    private String approverId;
    @Schema(description = "审批意见")
    private String approvalComment;
    @Schema(description = "是否有数据变更详情(0=否,1=是)")
    private Boolean hasDataChange;
    @Schema(description = "实体类名")
    private String entityClass;
    @Schema(description = "数据库表名")
    private String tableName;
    @Schema(description = "变更摘要")
    private String changeSummary;
    @Schema(description = "关联技术上下文表ID")
    private Long technicalContextId;
    @Schema(description = "业务标签")
    private String dynamicTags;
    @Schema(description = "业务扩展字段")
    private String dynamicExtras;
    @Schema(description = "")
    private LocalDateTime createdTime;
    @Schema(description = "")
    private LocalDateTime updatedTime;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
