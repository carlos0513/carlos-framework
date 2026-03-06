package com.carlos.audit.pojo.dto;

import com.carlos.audit.pojo.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据） 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
public class AuditLogMainDTO {
    /** 主键，雪花ID */
    private Long id;
    /** 服务器时间，毫秒精度，CK分区键 */
    private LocalDateTime serverTime;
    /** 事件日期，冗余字段，方便分区 */
    private LocalDate eventDate;
    /** 客户端时间 */
    private LocalDateTime clientTime;
    /** 事件实际发生时间 */
    private LocalDateTime eventTime;
    /** 操作耗时，毫秒 */
    private Integer durationMs;
    /** 数据保留截止日期，TTL删除用 */
    private LocalDate retentionDeadline;
    /** 日志Schema版本，用于兼容性处理 */
    private Integer logSchemaVersion;
    /** 大类：SECURITY-安全/BUSINESS-业务/SYSTEM-系统/AUDIT-审计 */
    private AuditLogCategoryEnum category;
    /** 细类：USER_LOGIN-登录/ORDER_PAY-支付/DATA_EXPORT-数据导出等 */
    private AuditLogTypeEnum logType;
    /** 风险等级 0-100，0为无风险，100为极高风险 */
    private Integer riskLevel;
    /** 操作主体ID，用户ID或服务账号 */
    private String principalId;
    /** 主体类型：USER-用户/SERVICE-服务/SYSTEM-系统/ANONYMOUS-匿名 */
    private AuditLogPrincipalTypeEnum principalType;
    /** 主体名称，冗余存储避免关联查询 */
    private String principalName;
    /** 租户ID，SaaS多租户隔离字段 */
    private String tenantId;
    /** 部门ID */
    private String deptId;
    /** 部门名称，冗余存储 */
    private String deptName;
    /** 部门层级路径，如：1/12/156/，方便层级查询 */
    private String deptPath;
    /** 对象类型：ORDER-订单/USER-用户/CONFIG-配置/DATA-数据 */
    private AuditLogTargetTypeEnum targetType;
    /** 对象唯一标识 */
    private String targetId;
    /** 对象显示名称，冗余存储 */
    private String targetName;
    /** 对象关键信息摘要，JSON格式存储关键字段 */
    private String targetSnapshot;
    /** 状态：SUCCESS-成功/FAIL-失败/PENDING-处理中/TIMEOUT-超时/PARTIAL_SUCCESS-部分成功 */
    private AuditLogStateEnum state;
    /** 业务结果码，用于细分错误类型 */
    private String resultCode;
    /** 结果描述，前500字符 */
    private String resultMessage;
    /** 操作描述，人工可读的操作说明 */
    private String operation;
    /** 审批意见，工作流用 */
    private String approvalComment;
    /** 客户端IP，支持IPv6 */
    private String clientIp;
    /** 客户端端口 */
    private Integer clientPort;
    /** 处理服务器IP */
    private String serverIp;
    /** 浏览器UA，用于设备识别 */
    private String userAgent;
    /** 设备指纹，唯一标识设备 */
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
    /** 认证方式：PASSWORD-密码/SMS-短信/OAUTH2-OAuth2/LDAP-LDAP/CERT-证书 */
    private AuditLogAuthTypeEnum authType;
    /** 认证源：LOCAL-本地/WECHAT-微信/DINGTALK-钉钉 */
    private AuditLogAuthProviderEnum authProvider;
    /** 当前角色列表，JSON数组 */
    private String roles;
    /** 当前权限列表，JSON数组 */
    private String permissions;
    /** 业务渠道：WEB-网页/APP-移动应用/MINI_PROGRAM-小程序/OPEN_API-开放接口 */
    private AuditLogBizChannelEnum bizChannel;
    /** 业务场景，如：订单创建-ORDER_CREATE */
    private String bizScene;
    /** 业务订单号，用于关联订单 */
    private String bizOrderNo;
    /** 关联业务ID列表，JSON数组 */
    private String relatedBizIds;
    /** 涉及金额，精确到分 */
    private BigDecimal monetaryAmount;
    /** 流程实例ID，工作流引擎生成 */
    private String processId;
    /** 批量操作批次号，UUID */
    private String batchId;
    /** 批次内序号，从0开始 */
    private Integer batchIndex;
    /** 批次总数 */
    private Integer batchTotal;
    /** 任务ID */
    private String taskId;
    /** 审批人ID */
    private String approverId;
    /** 是否有数据变更详情：0-否/1-是 */
    private Boolean hasDataChange;
    /** 实体类名，如：com.example.Order */
    private String entityClass;
    /** 数据库表名，如：t_order */
    private String tableName;
    /** 变更摘要，人工可读的变更说明 */
    private String changeSummary;
    /** 变更字段数量 */
    private Integer changedFieldCount;
    /** 变更前完整数据，JSON格式 */
    private String oldData;
    /** 变更后完整数据，JSON格式 */
    private String newData;
    /** 旧数据是否压缩：0-否/1-是 */
    private Boolean oldDataCompressed;
    /** 新数据是否压缩：0-否/1-是 */
    private Boolean newDataCompressed;
    /** 全链路追踪ID，如SkyWalking TraceId */
    private String traceId;
    /** 当前Span ID */
    private String spanId;
    /** 父Span ID */
    private String parentSpanId;
    /** 调用链路节点，JSON数组 */
    private String tracePath;
    /** 数据库查询次数 */
    private Integer dbQueryCount;
    /** 数据库查询耗时，毫秒 */
    private Integer dbQueryTimeMs;
    /** 外部接口调用次数 */
    private Integer externalCallCount;
    /** 外部接口耗时，毫秒 */
    private Integer externalCallTimeMs;
    /** 自定义指标，JSON格式 */
    private String customMetrics;
    /** 请求数据OSS引用 */
    private String requestPayloadRef;
    /** 响应数据OSS引用 */
    private String responsePayloadRef;
    /** 存储类型：OSS/S3/MINIO/DB */
    private AuditLogPayloadStorageTypeEnum payloadStorageType;
    /** 应用名，如：order-service */
    private String appName;
    /** 应用版本，如：1.2.3 */
    private String appVersion;
    /** 集群标识，如：prod-cluster-1 */
    private String cluster;
    /** 主机名 */
    private String hostName;
    /** 标签键数组，如：['env','module'] */
    private String tagKeys;
    /** 标签值数组，如：['prod','order'] */
    private String tagValues;
    /** 附件数量 */
    private Integer attachmentCount;
    /** 附件类型数组，如：['IMAGE','PDF'] */
    private String attachmentTypes;
    /** 附件总大小，字节 */
    private Long attachmentTotalSize;
    /** 第一个附件的OSS引用，快速预览 */
    private String firstAttachmentRef;
    /** 所有附件引用数组，JSON格式 */
    private String attachmentRefs;
    /** 业务标签，JSON格式 */
    private String dynamicTags;
    /** 业务扩展字段，JSON格式 */
    private String dynamicExtras;
    /** 记录创建时间 */
    private LocalDateTime createdTime;
    /** 记录更新时间 */
    private LocalDateTime updatedTime;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getServerTime() {
        return serverTime;
    }

    public void setServerTime(LocalDateTime serverTime) {
        this.serverTime = serverTime;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getClientTime() {
        return clientTime;
    }

    public void setClientTime(LocalDateTime clientTime) {
        this.clientTime = clientTime;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public LocalDate getRetentionDeadline() {
        return retentionDeadline;
    }

    public void setRetentionDeadline(LocalDate retentionDeadline) {
        this.retentionDeadline = retentionDeadline;
    }

    public Integer getLogSchemaVersion() {
        return logSchemaVersion;
    }

    public void setLogSchemaVersion(Integer logSchemaVersion) {
        this.logSchemaVersion = logSchemaVersion;
    }

    public AuditLogCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(AuditLogCategoryEnum category) {
        this.category = category;
    }

    public AuditLogTypeEnum getLogType() {
        return logType;
    }

    public void setLogType(AuditLogTypeEnum logType) {
        this.logType = logType;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public AuditLogPrincipalTypeEnum getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(AuditLogPrincipalTypeEnum principalType) {
        this.principalType = principalType;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }

    public AuditLogTargetTypeEnum getTargetType() {
        return targetType;
    }

    public void setTargetType(AuditLogTargetTypeEnum targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetSnapshot() {
        return targetSnapshot;
    }

    public void setTargetSnapshot(String targetSnapshot) {
        this.targetSnapshot = targetSnapshot;
    }

    public AuditLogStateEnum getState() {
        return state;
    }

    public void setState(AuditLogStateEnum state) {
        this.state = state;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationProvince() {
        return locationProvince;
    }

    public void setLocationProvince(String locationProvince) {
        this.locationProvince = locationProvince;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public BigDecimal getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(BigDecimal locationLat) {
        this.locationLat = locationLat;
    }

    public BigDecimal getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(BigDecimal locationLon) {
        this.locationLon = locationLon;
    }

    public AuditLogAuthTypeEnum getAuthType() {
        return authType;
    }

    public void setAuthType(AuditLogAuthTypeEnum authType) {
        this.authType = authType;
    }

    public AuditLogAuthProviderEnum getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuditLogAuthProviderEnum authProvider) {
        this.authProvider = authProvider;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public AuditLogBizChannelEnum getBizChannel() {
        return bizChannel;
    }

    public void setBizChannel(AuditLogBizChannelEnum bizChannel) {
        this.bizChannel = bizChannel;
    }

    public String getBizScene() {
        return bizScene;
    }

    public void setBizScene(String bizScene) {
        this.bizScene = bizScene;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getRelatedBizIds() {
        return relatedBizIds;
    }

    public void setRelatedBizIds(String relatedBizIds) {
        this.relatedBizIds = relatedBizIds;
    }

    public BigDecimal getMonetaryAmount() {
        return monetaryAmount;
    }

    public void setMonetaryAmount(BigDecimal monetaryAmount) {
        this.monetaryAmount = monetaryAmount;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getBatchIndex() {
        return batchIndex;
    }

    public void setBatchIndex(Integer batchIndex) {
        this.batchIndex = batchIndex;
    }

    public Integer getBatchTotal() {
        return batchTotal;
    }

    public void setBatchTotal(Integer batchTotal) {
        this.batchTotal = batchTotal;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public Boolean getHasDataChange() {
        return hasDataChange;
    }

    public void setHasDataChange(Boolean hasDataChange) {
        this.hasDataChange = hasDataChange;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getChangeSummary() {
        return changeSummary;
    }

    public void setChangeSummary(String changeSummary) {
        this.changeSummary = changeSummary;
    }

    public Integer getChangedFieldCount() {
        return changedFieldCount;
    }

    public void setChangedFieldCount(Integer changedFieldCount) {
        this.changedFieldCount = changedFieldCount;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public Boolean getOldDataCompressed() {
        return oldDataCompressed;
    }

    public void setOldDataCompressed(Boolean oldDataCompressed) {
        this.oldDataCompressed = oldDataCompressed;
    }

    public Boolean getNewDataCompressed() {
        return newDataCompressed;
    }

    public void setNewDataCompressed(Boolean newDataCompressed) {
        this.newDataCompressed = newDataCompressed;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public void setParentSpanId(String parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getTracePath() {
        return tracePath;
    }

    public void setTracePath(String tracePath) {
        this.tracePath = tracePath;
    }

    public Integer getDbQueryCount() {
        return dbQueryCount;
    }

    public void setDbQueryCount(Integer dbQueryCount) {
        this.dbQueryCount = dbQueryCount;
    }

    public Integer getDbQueryTimeMs() {
        return dbQueryTimeMs;
    }

    public void setDbQueryTimeMs(Integer dbQueryTimeMs) {
        this.dbQueryTimeMs = dbQueryTimeMs;
    }

    public Integer getExternalCallCount() {
        return externalCallCount;
    }

    public void setExternalCallCount(Integer externalCallCount) {
        this.externalCallCount = externalCallCount;
    }

    public Integer getExternalCallTimeMs() {
        return externalCallTimeMs;
    }

    public void setExternalCallTimeMs(Integer externalCallTimeMs) {
        this.externalCallTimeMs = externalCallTimeMs;
    }

    public String getCustomMetrics() {
        return customMetrics;
    }

    public void setCustomMetrics(String customMetrics) {
        this.customMetrics = customMetrics;
    }

    public String getRequestPayloadRef() {
        return requestPayloadRef;
    }

    public void setRequestPayloadRef(String requestPayloadRef) {
        this.requestPayloadRef = requestPayloadRef;
    }

    public String getResponsePayloadRef() {
        return responsePayloadRef;
    }

    public void setResponsePayloadRef(String responsePayloadRef) {
        this.responsePayloadRef = responsePayloadRef;
    }

    public AuditLogPayloadStorageTypeEnum getPayloadStorageType() {
        return payloadStorageType;
    }

    public void setPayloadStorageType(AuditLogPayloadStorageTypeEnum payloadStorageType) {
        this.payloadStorageType = payloadStorageType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getTagKeys() {
        return tagKeys;
    }

    public void setTagKeys(String tagKeys) {
        this.tagKeys = tagKeys;
    }

    public String getTagValues() {
        return tagValues;
    }

    public void setTagValues(String tagValues) {
        this.tagValues = tagValues;
    }

    public Integer getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public String getAttachmentTypes() {
        return attachmentTypes;
    }

    public void setAttachmentTypes(String attachmentTypes) {
        this.attachmentTypes = attachmentTypes;
    }

    public Long getAttachmentTotalSize() {
        return attachmentTotalSize;
    }

    public void setAttachmentTotalSize(Long attachmentTotalSize) {
        this.attachmentTotalSize = attachmentTotalSize;
    }

    public String getFirstAttachmentRef() {
        return firstAttachmentRef;
    }

    public void setFirstAttachmentRef(String firstAttachmentRef) {
        this.firstAttachmentRef = firstAttachmentRef;
    }

    public String getAttachmentRefs() {
        return attachmentRefs;
    }

    public void setAttachmentRefs(String attachmentRefs) {
        this.attachmentRefs = attachmentRefs;
    }

    public String getDynamicTags() {
        return dynamicTags;
    }

    public void setDynamicTags(String dynamicTags) {
        this.dynamicTags = dynamicTags;
    }

    public String getDynamicExtras() {
        return dynamicExtras;
    }

    public void setDynamicExtras(String dynamicExtras) {
        this.dynamicExtras = dynamicExtras;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
