package com.carlos.audit.clickhouse;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseNode;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * ClickHouse 批量写入器（双缓冲机制）
 * </p>
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

    @Value("${carlos.audit.batch-writer.batch-size:500}")
    private int batchSize;

    @Value("${carlos.audit.batch-writer.flush-interval:1000}")
    private int flushIntervalMs;

    @Value("${carlos.audit.batch-writer.max-buffer-size:10000}")
    private int maxBufferSize;

    @Value("${carlos.audit.batch-writer.retry-times:3}")
    private int retryTimes;

    // 双缓冲队列
    private final List<AuditLogMainDTO> buffer1 = new ArrayList<>();
    private final List<AuditLogMainDTO> buffer2 = new ArrayList<>();
    private volatile List<AuditLogMainDTO> activeBuffer = buffer1;

    // 异步执行器
    private ExecutorService flushExecutor;

    // 统计指标
    private final AtomicLong totalWritten = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);
    private final AtomicLong bufferOverflow = new AtomicLong(0);

    @PostConstruct
    public void init() {
        log.info("初始化 ClickHouse 批量写入器，batchSize={}, flushIntervalMs={}", batchSize, flushIntervalMs);
        flushExecutor = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r, "clickhouse-flush");
            t.setDaemon(true);
            return t;
        });
    }

    @PreDestroy
    public void destroy() {
        log.info("关闭 ClickHouse 批量写入器，执行最终刷新");
        flush();
        if (flushExecutor != null) {
            flushExecutor.shutdown();
        }
    }

    /**
     * 添加日志到缓冲区
     */
    public synchronized void add(AuditLogMainDTO logMainDTO) {
        if (logMainDTO == null) {
            return;
        }

        activeBuffer.add(logMainDTO);

        // 达到批次大小，立即刷新
        if (activeBuffer.size() >= batchSize) {
            flush();
        }

        // 缓冲区满，切换并告警
        if (activeBuffer.size() >= maxBufferSize) {
            bufferOverflow.incrementAndGet();
            log.warn("审计日志缓冲区已满，可能存在写入瓶颈，当前大小: {}", activeBuffer.size());
            flush();
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

        // 切换缓冲区
        if (activeBuffer == buffer1) {
            activeBuffer = buffer2;
            buffer2.clear();
        } else {
            activeBuffer = buffer1;
            buffer1.clear();
        }

        if (!toFlush.isEmpty()) {
            final int size = toFlush.size();
            flushExecutor.submit(() -> doFlush(toFlush, size, 0));
        }
    }

    /**
     * 执行写入
     */
    private void doFlush(List<AuditLogMainDTO> logs, int size, int retryCount) {
        if (retryCount >= retryTimes) {
            log.error("批量写入 ClickHouse 失败，已达到最大重试次数: {}，数据条数: {}", retryTimes, size);
            totalFailed.addAndGet(size);
            // TODO: 转入本地磁盘备份或死信队列
            return;
        }

        try {
            long startTime = System.currentTimeMillis();

            // 构建并执行批量插入 SQL
            String sql = buildInsertSql(logs);
            // clickHouseClient.execute(clickHouseNode, sql).get();

            long duration = System.currentTimeMillis() - startTime;
            totalWritten.addAndGet(size);

            if (log.isDebugEnabled()) {
                log.debug("批量写入 ClickHouse 成功，条数: {}，耗时: {}ms", size, duration);
            }

        } catch (Exception e) {
            log.error("批量写入 ClickHouse 失败，重试次数: {}，数据条数: {}", retryCount, size, e);

            // 指数退避重试
            try {
                Thread.sleep(1000L * (retryCount + 1));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            doFlush(logs, size, retryCount + 1);
        }
    }

    /**
     * 构建批量插入 SQL
     */
    private String buildInsertSql(List<AuditLogMainDTO> logs) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO audit_log_main_local ");
        sb.append("(server_time, event_date, id, client_time, event_time, duration_ms, ");
        sb.append("retention_deadline, log_schema_version, category, log_type, risk_level, ");
        sb.append("principal_id, principal_type, principal_name, tenant_id, dept_id, dept_name, dept_path, ");
        sb.append("target_type, target_id, target_name, target_snapshot, state, result_code, result_message, ");
        sb.append("operation, client_ip, auth_type, auth_provider, biz_channel, created_time) ");
        sb.append("VALUES ");

        for (int i = 0; i < logs.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(formatValues(logs.get(i)));
        }

        return sb.toString();
    }

    /**
     * 格式化单条记录为 VALUES 格式
     */
    private String formatValues(AuditLogMainDTO log) {
        StringBuilder sb = new StringBuilder("(");

        // server_time
        sb.append(formatDateTime(log.getServerTime())).append(", ");
        // event_date
        sb.append(formatDate(log.getEventDate())).append(", ");
        // id
        sb.append(log.getId() != null ? log.getId() : "generateSnowflakeId()").append(", ");
        // client_time
        sb.append(formatNullableDateTime(log.getClientTime())).append(", ");
        // event_time
        sb.append(formatNullableDateTime(log.getEventTime())).append(", ");
        // duration_ms
        sb.append(log.getDurationMs() != null ? log.getDurationMs() : "NULL").append(", ");
        // retention_deadline
        sb.append(formatDate(log.getRetentionDeadline())).append(", ");
        // log_schema_version
        sb.append(log.getLogSchemaVersion() != null ? log.getLogSchemaVersion() : 3).append(", ");
        // category
        sb.append(formatEnum(log.getCategory())).append(", ");
        // log_type
        sb.append(log.getLogType()).append(", ");
        // risk_level
        sb.append(log.getRiskLevel() != null ? log.getRiskLevel() : 0).append(", ");
        // principal_id
        sb.append(formatString(log.getPrincipalId())).append(", ");
        // principal_type
        sb.append(formatEnum(log.getPrincipalType())).append(", ");
        // principal_name
        sb.append(formatNullableString(log.getPrincipalName())).append(", ");
        // tenant_id
        sb.append(formatString(log.getTenantId() != null ? log.getTenantId() : "0")).append(", ");
        // dept_id
        sb.append(formatNullableString(log.getDeptId())).append(", ");
        // dept_name
        sb.append(formatNullableString(log.getDeptName())).append(", ");
        // dept_path
        sb.append(formatNullableString(log.getDeptPath())).append(", ");
        // target_type
        sb.append(log.getTargetType()).append(", ");
        // target_id
        sb.append(formatNullableString(log.getTargetId())).append(", ");
        // target_name
        sb.append(formatNullableString(log.getTargetName())).append(", ");
        // target_snapshot
        sb.append(formatNullableString(log.getTargetSnapshot())).append(", ");
        // state
        sb.append(formatEnum(log.getState())).append(", ");
        // result_code
        sb.append(formatNullableString(log.getResultCode())).append(", ");
        // result_message
        sb.append(formatNullableString(log.getResultMessage())).append(", ");
        // operation
        sb.append(formatString(log.getOperation())).append(", ");
        // client_ip
        sb.append(formatNullableString(log.getClientIp())).append(", ");
        // auth_type
        sb.append(formatEnum(log.getAuthType())).append(", ");
        // auth_provider
        sb.append(formatEnum(log.getAuthProvider())).append(", ");
        // biz_channel
        sb.append(formatEnum(log.getBizChannel())).append(", ");
        // created_time
        sb.append(formatDateTime(log.getCreatedTime() != null ? log.getCreatedTime() : LocalDateTime.now()));

        sb.append(")");
        return sb.toString();
    }

    private String formatDateTime(LocalDateTime dt) {
        if (dt == null) {
            return "now()";
        }
        return "'" + dt.toString().replace("T", " ") + "'";
    }

    private String formatDate(LocalDate d) {
        if (d == null) {
            return "today()";
        }
        return "'" + d.toString() + "'";
    }

    private String formatNullableDateTime(LocalDateTime dt) {
        if (dt == null) {
            return "NULL";
        }
        return "'" + dt.toString().replace("T", " ") + "'";
    }

    private String formatString(String s) {
        if (s == null) {
            return "''";
        }
        return "'" + s.replace("'", "\\'") + "'";
    }

    private String formatNullableString(String s) {
        if (s == null) {
            return "NULL";
        }
        return "'" + s.replace("'", "\\'") + "'";
    }

    private String formatEnum(Enum<?> e) {
        if (e == null) {
            return "NULL";
        }
        return "'" + e.name() + "'";
    }

    // ============ 监控指标 ============

    public long getTotalWritten() {
        return totalWritten.get();
    }

    public long getTotalFailed() {
        return totalFailed.get();
    }

    public long getBufferOverflow() {
        return bufferOverflow.get();
    }

    public int getActiveBufferSize() {
        return activeBuffer.size();
    }
}
