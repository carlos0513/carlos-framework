package com.carlos.audit.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-技术上下文 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class AuditLogTechnicalDTO {
    /**  */
    private Long id;
    /** 关联主表ID */
    private Long auditLogId;
    /** 全链路追踪ID */
    private String traceId;
    /** 当前Span */
    private String spanId;
    /** 父Span */
    private String parentSpanId;
    /** 调用链路节点 */
    private String tracePath;
    /** 数据库查询次数 */
    private Integer dbQueryCount;
    /** 数据库查询耗时 */
    private Integer dbQueryTimeMs;
    /** 外部接口调用次数 */
    private Integer externalCallCount;
    /** 外部接口耗时 */
    private Integer externalCallTimeMs;
    /** 自定义指标 */
    private String customMetrics;
    /** 请求数据存储引用(OSS/MinIO) */
    private String requestPayloadRef;
    /** 响应数据存储引用 */
    private String responsePayloadRef;
    /** 存储类型: OSS/S3/MINIO/DB */
    private Integer payloadStorageType;
    /** 应用名 */
    private String appName;
    /** 应用版本 */
    private String appVersion;
    /** 集群标识 */
    private String cluster;
    /** 主机名 */
    private String hostName;
    /**  */
    private LocalDateTime createdTime;
}
