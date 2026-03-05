package com.carlos.audit.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-技术上下文 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogTechnicalVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "")
    private Long id;
    @Schema(description = "关联主表ID")
    private Long auditLogId;
    @Schema(description = "全链路追踪ID")
    private String traceId;
    @Schema(description = "当前Span")
    private String spanId;
    @Schema(description = "父Span")
    private String parentSpanId;
    @Schema(description = "调用链路节点")
    private String tracePath;
    @Schema(description = "数据库查询次数")
    private Integer dbQueryCount;
    @Schema(description = "数据库查询耗时")
    private Integer dbQueryTimeMs;
    @Schema(description = "外部接口调用次数")
    private Integer externalCallCount;
    @Schema(description = "外部接口耗时")
    private Integer externalCallTimeMs;
    @Schema(description = "自定义指标")
    private String customMetrics;
    @Schema(description = "请求数据存储引用(OSS/MinIO)")
    private String requestPayloadRef;
    @Schema(description = "响应数据存储引用")
    private String responsePayloadRef;
    @Schema(description = "存储类型: OSS/S3/MINIO/DB")
    private Integer payloadStorageType;
    @Schema(description = "应用名")
    private String appName;
    @Schema(description = "应用版本")
    private String appVersion;
    @Schema(description = "集群标识")
    private String cluster;
    @Schema(description = "主机名")
    private String hostName;
    @Schema(description = "")
    private LocalDateTime createdTime;

}
