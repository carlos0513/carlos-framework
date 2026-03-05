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
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-技术上下文 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_technical")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogTechnical extends Model<AuditLogTechnical> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 关联主表ID
     */
    @TableField(value = "audit_log_id")
    private Long auditLogId;
    /**
     * 全链路追踪ID
     */
    @TableField(value = "trace_id")
    private String traceId;
    /**
     * 当前Span
     */
    @TableField(value = "span_id")
    private String spanId;
    /**
     * 父Span
     */
    @TableField(value = "parent_span_id")
    private String parentSpanId;
    /**
     * 调用链路节点
     */
    @TableField(value = "trace_path")
    private String tracePath;
    /**
     * 数据库查询次数
     */
    @TableField(value = "db_query_count")
    private Integer dbQueryCount;
    /**
     * 数据库查询耗时
     */
    @TableField(value = "db_query_time_ms")
    private Integer dbQueryTimeMs;
    /**
     * 外部接口调用次数
     */
    @TableField(value = "external_call_count")
    private Integer externalCallCount;
    /**
     * 外部接口耗时
     */
    @TableField(value = "external_call_time_ms")
    private Integer externalCallTimeMs;
    /**
     * 自定义指标
     */
    @TableField(value = "custom_metrics")
    private String customMetrics;
    /**
     * 请求数据存储引用(OSS/MinIO)
     */
    @TableField(value = "request_payload_ref")
    private String requestPayloadRef;
    /**
     * 响应数据存储引用
     */
    @TableField(value = "response_payload_ref")
    private String responsePayloadRef;
    /**
     * 存储类型: OSS/S3/MINIO/DB
     */
    @TableField(value = "payload_storage_type")
    private Integer payloadStorageType;
    /**
     * 应用名
     */
    @TableField(value = "app_name")
    private String appName;
    /**
     * 应用版本
     */
    @TableField(value = "app_version")
    private String appVersion;
    /**
     * 集群标识
     */
    @TableField(value = "cluster")
    private String cluster;
    /**
     * 主机名
     */
    @TableField(value = "host_name")
    private String hostName;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

}
