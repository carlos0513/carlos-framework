package com.carlos.audit.clickhouse;

import com.carlos.audit.api.pojo.enums.AuditLogCategoryEnum;
import com.carlos.audit.api.pojo.enums.AuditLogPrincipalTypeEnum;
import com.carlos.audit.api.pojo.enums.AuditLogStateEnum;
import com.carlos.audit.config.AuditProperties;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.json.JsonUtils;
import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.data.ClickHouseFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * ClickHouse 批量写入器（双缓冲 + ClickHouseClient 原生批量插入）
 * </p>
 *
 * <p>优化要点：</p>
 * <ul>
 *   <li>修复双缓冲 clear() 污染异步数据的致命 Bug，每次 flush 后创建新 ArrayList 实例</li>
 *   <li>使用 ClickHouse 官方 Java Client 批量插入 API（FORMAT JSONEachRow），彻底消除 SQL 字符串拼接与注入风险</li>
 *   <li>循环重试替代递归重试，避免栈溢出风险</li>
 *   <li>destroy() 优雅关闭，awaitTermination 等待异步任务完成</li>
 *   <li>本地磁盘备份兜底，配置 localBackupOnFailure 即可启用</li>
 *   <li>统一在 Java 端生成 id 和时间戳，避免客户端与服务端时间不一致</li>
 *   <li>防御性背压：缓冲区异常溢出时丢弃最旧数据，防止 OOM</li>
 * </ul>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClickHouseBatchWriter {

    private final ClickHouseClient clickHouseClient;
    private final ClickHouseNode clickHouseNode;
    private final AuditProperties auditProperties;

    // 双缓冲队列（每次 flush 后创建新实例，避免 clear() 污染异步数据）
    private List<AuditLogMainDTO> buffer1 = new ArrayList<>();
    private List<AuditLogMainDTO> buffer2 = new ArrayList<>();
    private volatile List<AuditLogMainDTO> activeBuffer = buffer1;

    // 异步执行器
    private ExecutorService flushExecutor;

    // 统计指标
    private final AtomicLong totalWritten = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);
    private final AtomicLong bufferOverflow = new AtomicLong(0);
    private final AtomicLong totalDiscarded = new AtomicLong(0);
    private final AtomicLong idSequence = new AtomicLong(ThreadLocalRandom.current().nextInt(1 << 20));

    private static final DateTimeFormatter CK_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter BACKUP_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        log.info("初始化 ClickHouse 批量写入器，batchSize={}, flushIntervalMs={}",
            auditProperties.getBatchWriter().getBatchSize(),
            auditProperties.getBatchWriter().getFlushInterval());
        flushExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @PreDestroy
    public void destroy() {
        log.info("关闭 ClickHouse 批量写入器，执行最终刷新");
        flush();
        if (flushExecutor != null) {
            flushExecutor.shutdown();
            try {
                if (!flushExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn("刷新任务未在 30 秒内完成，强制关闭");
                    flushExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                flushExecutor.shutdownNow();
            }
        }
    }

    /**
     * 添加日志到缓冲区
     */
    public synchronized void add(AuditLogMainDTO dto) {
        if (dto == null) {
            return;
        }

        ensureRequiredFields(dto);
        activeBuffer.add(dto);

        int size = activeBuffer.size();
        int batchSize = auditProperties.getBatchWriter().getBatchSize();

        // 达到批次大小，立即刷新
        if (size >= batchSize) {
            flush();
        }

        // 防御性背压：若缓冲区异常增长（写入严重滞后），丢弃最旧数据保护内存
        int maxBufferSize = auditProperties.getBatchWriter().getMaxBufferSize();
        if (activeBuffer.size() >= maxBufferSize) {
            bufferOverflow.incrementAndGet();
            int discardCount = activeBuffer.size() - maxBufferSize / 2;
            log.warn("审计日志缓冲区异常溢出，丢弃 {} 条最旧数据", discardCount);
            activeBuffer = new ArrayList<>(activeBuffer.subList(discardCount, activeBuffer.size()));
            totalDiscarded.addAndGet(discardCount);
        }
    }

    /**
     * 定时刷新（兜底机制）
     */
    @Scheduled(fixedDelayString = "${carlos.audit.batch-writer.flush-interval:1000}")
    public void scheduledFlush() {
        flush();
    }

    /**
     * 执行刷新（双缓冲切换）
     */
    private synchronized void flush() {
        List<AuditLogMainDTO> toFlush = activeBuffer;
        if (toFlush.isEmpty()) {
            return;
        }

        // 切换缓冲区并创建新实例（避免 clear() 污染正在异步处理的数据）
        if (activeBuffer == buffer1) {
            activeBuffer = buffer2;
            buffer1 = new ArrayList<>();
        } else {
            activeBuffer = buffer1;
            buffer2 = new ArrayList<>();
        }

        final int size = toFlush.size();
        flushExecutor.submit(() -> doFlush(toFlush, size));
    }

    /**
     * 执行写入（带指数退避循环重试）
     */
    private void doFlush(List<AuditLogMainDTO> logs, int size) {
        int maxRetries = auditProperties.getBatchWriter().getRetryTimes();

        for (int retry = 0; retry <= maxRetries; retry++) {
            try {
                if (retry > 0) {
                    long backoffMs = 1000L * retry;
                    Thread.sleep(backoffMs);
                }
                executeBatchInsert(logs);
                totalWritten.addAndGet(size);
                log.debug("批量写入 ClickHouse 成功，条数: {}", size);
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("批量写入 ClickHouse 被中断，数据条数: {}", size);
                break;
            } catch (Exception e) {
                log.error("批量写入 ClickHouse 失败，重试次数: {}/{}，数据条数: {}", retry, maxRetries, size, e);
            }
        }

        totalFailed.addAndGet(size);
        log.error("批量写入 ClickHouse 最终失败，已耗尽重试次数，数据条数: {}", size);

        if (auditProperties.getBatchWriter().isLocalBackupOnFailure()) {
            backupToLocal(logs);
        }
    }

    /**
     * 使用 ClickHouse 官方 Java Client 批量插入 API（FORMAT JSONEachRow）
     */
    private void executeBatchInsert(List<AuditLogMainDTO> logs) throws Exception {
        byte[] data = buildJsonEachRow(logs);
        try (ClickHouseResponse response = clickHouseClient.read(clickHouseNode)
            .write()
            .table("audit_log_main_local")
            .format(ClickHouseFormat.JSONEachRow)
            .data(new ByteArrayInputStream(data))
            .executeAndWait()
        ) {
            // 写入操作，无需处理响应体

        }
    }

    /**
     * 将日志列表构建为 JSONEachRow 字节流
     */
    private byte[] buildJsonEachRow(List<AuditLogMainDTO> logs) {
        StringBuilder sb = new StringBuilder();
        for (AuditLogMainDTO log : logs) {
            sb.append(toJsonLine(log)).append('\n');
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 将单条审计日志转换为 JSONEachRow 格式的一行 JSON
     */
    private String toJsonLine(AuditLogMainDTO log) {
        ClickHouseRow row = new ClickHouseRow();
        // 复制同类型字段（String / Integer / Long / BigDecimal / Boolean）
        BeanUtils.copyProperties(log, row);
        // 手动处理类型不一致的字段：时间格式化、枚举转字符串
        row.setServerTime(formatDateTime(log.getServerTime()));
        row.setEventDate(formatDate(log.getEventDate()));
        row.setClientTime(formatDateTime(log.getClientTime()));
        row.setEventTime(formatDateTime(log.getEventTime()));
        row.setRetentionDeadline(formatDate(log.getRetentionDeadline()));
        row.setCreatedTime(formatDateTime(log.getCreatedTime()));
        row.setUpdatedTime(formatDateTime(log.getUpdatedTime()));
        row.setCategory(log.getCategory() != null ? log.getCategory().name() : null);
        row.setPrincipalType(log.getPrincipalType() != null ? log.getPrincipalType().name() : null);
        row.setState(log.getState() != null ? log.getState().name() : null);
        row.setAuthType(log.getAuthType() != null ? log.getAuthType().name() : null);
        row.setAuthProvider(log.getAuthProvider() != null ? log.getAuthProvider().name() : null);
        row.setBizChannel(log.getBizChannel() != null ? log.getBizChannel().name() : null);
        row.setPayloadStorageType(log.getPayloadStorageType() != null ? log.getPayloadStorageType().name() : null);
        return JsonUtils.toJson(row);
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(CK_DATETIME_FORMATTER) : null;
    }

    private String formatDate(LocalDate d) {
        return d != null ? d.toString() : null;
    }

    /**
     * 确保 DTO 包含所有非空必填字段
     */
    private void ensureRequiredFields(AuditLogMainDTO dto) {
        if (dto.getServerTime() == null) {
            dto.setServerTime(LocalDateTime.now());
        }
        if (dto.getEventDate() == null) {
            dto.setEventDate(dto.getServerTime().toLocalDate());
        }
        if (dto.getId() == null) {
            dto.setId((System.currentTimeMillis() << 20) | (idSequence.incrementAndGet() & 0xFFFFF));
        }
        if (dto.getRetentionDeadline() == null) {
            dto.setRetentionDeadline(dto.getEventDate());
        }
        if (dto.getLogSchemaVersion() == null) {
            dto.setLogSchemaVersion(3);
        }
        if (dto.getCategory() == null) {
            dto.setCategory(AuditLogCategoryEnum.SYSTEM);
        }
        if (dto.getLogType() == null) {
            dto.setLogType("UNKNOWN");
        }
        if (dto.getRiskLevel() == null) {
            dto.setRiskLevel(0);
        }
        if (dto.getPrincipalId() == null) {
            dto.setPrincipalId("");
        }
        if (dto.getPrincipalType() == null) {
            dto.setPrincipalType(AuditLogPrincipalTypeEnum.USER);
        }
        if (dto.getTenantId() == null) {
            dto.setTenantId("0");
        }
        if (dto.getState() == null) {
            dto.setState(AuditLogStateEnum.SUCCESS);
        }
        if (dto.getOperation() == null) {
            dto.setOperation("");
        }
        if (dto.getCreatedTime() == null) {
            dto.setCreatedTime(LocalDateTime.now());
        }
    }

    /**
     * 本地磁盘备份（失败兜底）
     */
    private void backupToLocal(List<AuditLogMainDTO> logs) {
        try {
            Path dir = Paths.get(auditProperties.getBatchWriter().getLocalBackupPath());
            Files.createDirectories(dir);
            String fileName = "audit-backup-" + LocalDate.now() + ".csv";
            Path file = dir.resolve(fileName);

            boolean isNewFile = !Files.exists(file);
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (isNewFile) {
                    writer.write("backup_time,id,server_time,log_type,principal_id,operation,reason");
                    writer.newLine();
                }
                String backupTime = LocalDateTime.now().format(BACKUP_TIME_FORMATTER);
                for (AuditLogMainDTO log : logs) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s",
                        backupTime,
                        log.getId(),
                        log.getServerTime().format(BACKUP_TIME_FORMATTER),
                        escapeCsv(log.getLogType()),
                        escapeCsv(log.getPrincipalId()),
                        escapeCsv(log.getOperation()),
                        "CLICKHOUSE_WRITE_FAILED"
                    ));
                    writer.newLine();
                }
            }
            log.info("审计日志已本地备份，文件: {}，条数: {}", file, logs.size());
        } catch (IOException e) {
            log.error("本地备份审计日志失败", e);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n") || escaped.contains("\r")) {
            escaped = "\"" + escaped + "\"";
        }
        return escaped;
    }

    // ============ 监控指标 ============

    public synchronized int getActiveBufferSize() {
        return activeBuffer.size();
    }

    public long getTotalWritten() {
        return totalWritten.get();
    }

    public long getTotalFailed() {
        return totalFailed.get();
    }

    public long getBufferOverflow() {
        return bufferOverflow.get();
    }

    public long getTotalDiscarded() {
        return totalDiscarded.get();
    }

    /**
     * 专门用于 ClickHouse JSONEachRow 序列化的内部类
     * 参考 AuditLogMain 实体类及 AuditLogMainDTO 的完整字段定义
     */
    @Data
    private static class ClickHouseRow {
        // 1. 主键与时间体系
        @JsonProperty("server_time")
        private String serverTime;
        @JsonProperty("event_date")
        private String eventDate;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("client_time")
        private String clientTime;
        @JsonProperty("event_time")
        private String eventTime;
        @JsonProperty("duration_ms")
        private Integer durationMs;
        @JsonProperty("retention_deadline")
        private String retentionDeadline;

        // 2. 分类与版本
        @JsonProperty("log_schema_version")
        private Integer logSchemaVersion;
        @JsonProperty("category")
        private String category;
        @JsonProperty("log_type")
        private String logType;
        @JsonProperty("risk_level")
        private Integer riskLevel;

        // 3. 操作者体系
        @JsonProperty("principal_id")
        private String principalId;
        @JsonProperty("principal_type")
        private String principalType;
        @JsonProperty("principal_name")
        private String principalName;
        @JsonProperty("tenant_id")
        private String tenantId;
        @JsonProperty("dept_id")
        private String deptId;
        @JsonProperty("dept_name")
        private String deptName;
        @JsonProperty("dept_path")
        private String deptPath;

        // 4. 目标对象体系
        @JsonProperty("target_type")
        private String targetType;
        @JsonProperty("target_id")
        private String targetId;
        @JsonProperty("target_name")
        private String targetName;
        @JsonProperty("target_snapshot")
        private String targetSnapshot;

        // 5. 操作结果
        @JsonProperty("approval_comment")
        private String approvalComment;
        @JsonProperty("state")
        private String state;
        @JsonProperty("result_code")
        private String resultCode;
        @JsonProperty("result_message")
        private String resultMessage;
        @JsonProperty("operation")
        private String operation;

        // 6. 网络与设备
        @JsonProperty("client_ip")
        private String clientIp;
        @JsonProperty("client_port")
        private Integer clientPort;
        @JsonProperty("server_ip")
        private String serverIp;
        @JsonProperty("user_agent")
        private String userAgent;
        @JsonProperty("device_fingerprint")
        private String deviceFingerprint;
        @JsonProperty("location_country")
        private String locationCountry;
        @JsonProperty("location_province")
        private String locationProvince;
        @JsonProperty("location_city")
        private String locationCity;
        @JsonProperty("location_lat")
        private java.math.BigDecimal locationLat;
        @JsonProperty("location_lon")
        private java.math.BigDecimal locationLon;

        // 7. 认证与权限
        @JsonProperty("auth_type")
        private String authType;
        @JsonProperty("auth_provider")
        private String authProvider;
        @JsonProperty("roles")
        private String roles;
        @JsonProperty("permissions")
        private String permissions;

        // 8. 业务上下文
        @JsonProperty("biz_channel")
        private String bizChannel;
        @JsonProperty("biz_scene")
        private String bizScene;
        @JsonProperty("biz_order_no")
        private String bizOrderNo;
        @JsonProperty("related_biz_ids")
        private String relatedBizIds;
        @JsonProperty("monetary_amount")
        private java.math.BigDecimal monetaryAmount;
        @JsonProperty("process_id")
        private String processId;

        // 9. 批量操作
        @JsonProperty("batch_id")
        private String batchId;
        @JsonProperty("batch_index")
        private Integer batchIndex;
        @JsonProperty("batch_total")
        private Integer batchTotal;

        // 10. 审批工作流
        @JsonProperty("task_id")
        private String taskId;
        @JsonProperty("approver_id")
        private String approverId;

        // 11. 数据变更详情
        @JsonProperty("has_data_change")
        private Boolean hasDataChange;
        @JsonProperty("entity_class")
        private String entityClass;
        @JsonProperty("table_name")
        private String tableName;
        @JsonProperty("change_summary")
        private String changeSummary;
        @JsonProperty("changed_field_count")
        private Integer changedFieldCount;
        @JsonProperty("old_data")
        private String oldData;
        @JsonProperty("new_data")
        private String newData;
        @JsonProperty("old_data_compressed")
        private Boolean oldDataCompressed;
        @JsonProperty("new_data_compressed")
        private Boolean newDataCompressed;

        // 12. 技术上下文
        @JsonProperty("trace_id")
        private String traceId;
        @JsonProperty("span_id")
        private String spanId;
        @JsonProperty("parent_span_id")
        private String parentSpanId;
        @JsonProperty("trace_path")
        private String tracePath;
        @JsonProperty("db_query_count")
        private Integer dbQueryCount;
        @JsonProperty("db_query_time_ms")
        private Integer dbQueryTimeMs;
        @JsonProperty("external_call_count")
        private Integer externalCallCount;
        @JsonProperty("external_call_time_ms")
        private Integer externalCallTimeMs;
        @JsonProperty("custom_metrics")
        private String customMetrics;

        // Payload 存储引用
        @JsonProperty("request_payload_ref")
        private String requestPayloadRef;
        @JsonProperty("response_payload_ref")
        private String responsePayloadRef;
        @JsonProperty("payload_storage_type")
        private String payloadStorageType;

        // 环境信息
        @JsonProperty("app_name")
        private String appName;
        @JsonProperty("app_version")
        private String appVersion;
        @JsonProperty("cluster")
        private String cluster;
        @JsonProperty("host_name")
        private String hostName;

        // 13. 动态标签
        @JsonProperty("tag_keys")
        private String tagKeys;
        @JsonProperty("tag_values")
        private String tagValues;

        // 14. 附件元数据
        @JsonProperty("attachment_count")
        private Integer attachmentCount;
        @JsonProperty("attachment_types")
        private String attachmentTypes;
        @JsonProperty("attachment_total_size")
        private Long attachmentTotalSize;
        @JsonProperty("first_attachment_ref")
        private String firstAttachmentRef;
        @JsonProperty("attachment_refs")
        private String attachmentRefs;

        // 15. 动态扩展
        @JsonProperty("dynamic_tags")
        private String dynamicTags;
        @JsonProperty("dynamic_extras")
        private String dynamicExtras;

        // 16. 系统字段
        @JsonProperty("created_time")
        private String createdTime;
        @JsonProperty("updated_time")
        private String updatedTime;
    }
}
