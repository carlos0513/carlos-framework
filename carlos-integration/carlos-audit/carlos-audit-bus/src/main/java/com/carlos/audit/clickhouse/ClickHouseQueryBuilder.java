package com.carlos.audit.clickhouse;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.core.pagination.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ClickHouse SQL 查询构建器
 *
 * <p>构建审计日志相关的 SQL 查询语句</p>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Component
public class ClickHouseQueryBuilder {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 主表名
     */
    private static final String MAIN_TABLE = "audit_log_main_local";

    /**
     * 统计物化视图
     */
    private static final String STATS_MATERIALIZED_VIEW = "audit_log_stats_mv_local";

    // ==================== 查询构建方法 ====================

    /**
     * 构建分页查询 SQL
     *
     * @param tenantId   租户ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param logType    日志类型
     * @param state      状态
     * @param principalId 主体ID
     * @param riskLevel  风险等级
     * @param offset     偏移量
     * @param pageSize   页大小
     * @return SQL 语句
     */
    public String buildPageQuery(String tenantId,
                                  LocalDateTime startTime,
                                  LocalDateTime endTime,
                                  String logType,
                                  String state,
                                  String principalId,
                                  Integer riskLevel,
                                  long offset,
                                  int pageSize) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(MAIN_TABLE).append(" WHERE 1=1");

        // 租户ID
        if (isNotEmpty(tenantId)) {
            sql.append(" AND tenant_id = '").append(escape(tenantId)).append("'");
        }

        // 时间范围
        if (startTime != null) {
            sql.append(" AND server_time >= '").append(formatDateTime(startTime)).append("'");
        }
        if (endTime != null) {
            sql.append(" AND server_time <= '").append(formatDateTime(endTime)).append("'");
        }

        // 日志类型
        if (isNotEmpty(logType)) {
            sql.append(" AND log_type = '").append(escape(logType)).append("'");
        }

        // 状态
        if (isNotEmpty(state)) {
            sql.append(" AND state = '").append(escape(state)).append("'");
        }

        // 主体ID
        if (isNotEmpty(principalId)) {
            sql.append(" AND principal_id = '").append(escape(principalId)).append("'");
        }

        // 风险等级
        if (riskLevel != null) {
            sql.append(" AND risk_level >= ").append(riskLevel);
        }

        // 排序和分页
        sql.append(" ORDER BY server_time DESC");
        sql.append(" LIMIT ").append(pageSize);
        sql.append(" OFFSET ").append(offset);

        return sql.toString();
    }

    /**
     * 构建计数查询 SQL
     *
     * @param tenantId   租户ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param logType    日志类型
     * @param state      状态
     * @param principalId 主体ID
     * @param riskLevel  风险等级
     * @return SQL 语句
     */
    public String buildCountQuery(String tenantId,
                                   LocalDateTime startTime,
                                   LocalDateTime endTime,
                                   String logType,
                                   String state,
                                   String principalId,
                                   Integer riskLevel) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(MAIN_TABLE).append(" WHERE 1=1");

        // 租户ID
        if (isNotEmpty(tenantId)) {
            sql.append(" AND tenant_id = '").append(escape(tenantId)).append("'");
        }

        // 时间范围
        if (startTime != null) {
            sql.append(" AND server_time >= '").append(formatDateTime(startTime)).append("'");
        }
        if (endTime != null) {
            sql.append(" AND server_time <= '").append(formatDateTime(endTime)).append("'");
        }

        // 日志类型
        if (isNotEmpty(logType)) {
            sql.append(" AND log_type = '").append(escape(logType)).append("'");
        }

        // 状态
        if (isNotEmpty(state)) {
            sql.append(" AND state = '").append(escape(state)).append("'");
        }

        // 主体ID
        if (isNotEmpty(principalId)) {
            sql.append(" AND principal_id = '").append(escape(principalId)).append("'");
        }

        // 风险等级
        if (riskLevel != null) {
            sql.append(" AND risk_level >= ").append(riskLevel);
        }

        return sql.toString();
    }

    /**
     * 构建根据 ID 查询的 SQL
     *
     * @param id 日志ID
     * @return SQL 语句
     */
    public String buildGetByIdQuery(Long id) {
        return "SELECT * FROM " + MAIN_TABLE + " WHERE id = " + id;
    }

    /**
     * 构建统计查询 SQL
     *
     * @param tenantId 租户ID
     * @param date     日期
     * @return SQL 语句
     */
    public String buildStatsQuery(String tenantId, LocalDate date) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(STATS_MATERIALIZED_VIEW).append(" WHERE 1=1");

        if (isNotEmpty(tenantId)) {
            sql.append(" AND tenant_id = '").append(escape(tenantId)).append("'");
        }

        if (date != null) {
            sql.append(" AND event_date = '").append(formatDate(date)).append("'");
        }

        return sql.toString();
    }

    /**
     * 构建用户行为轨迹查询 SQL
     *
     * @param principalId 主体ID
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return SQL 语句
     */
    public String buildUserTrailQuery(String principalId,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(MAIN_TABLE).append(" WHERE principal_id = '").append(escape(principalId)).append("'");

        if (startTime != null) {
            sql.append(" AND server_time >= '").append(formatDateTime(startTime)).append("'");
        }

        if (endTime != null) {
            sql.append(" AND server_time <= '").append(formatDateTime(endTime)).append("'");
        }

        sql.append(" ORDER BY server_time DESC");

        return sql.toString();
    }

    /**
     * 构建风险事件查询 SQL
     *
     * @param tenantId     租户ID
     * @param minRiskLevel 最低风险等级
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param limit        限制条数
     * @return SQL 语句
     */
    public String buildRiskEventsQuery(String tenantId,
                                        int minRiskLevel,
                                        LocalDateTime startTime,
                                        LocalDateTime endTime,
                                        int limit) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(MAIN_TABLE).append(" WHERE 1=1");

        if (isNotEmpty(tenantId)) {
            sql.append(" AND tenant_id = '").append(escape(tenantId)).append("'");
        }

        sql.append(" AND risk_level >= ").append(minRiskLevel);

        if (startTime != null) {
            sql.append(" AND server_time >= '").append(formatDateTime(startTime)).append("'");
        }

        if (endTime != null) {
            sql.append(" AND server_time <= '").append(formatDateTime(endTime)).append("'");
        }

        sql.append(" ORDER BY risk_level DESC, server_time DESC");
        sql.append(" LIMIT ").append(limit);

        return sql.toString();
    }

    /**
     * 构建聚合统计 SQL
     *
     * @param tenantId   租户ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param groupBy    分组字段
     * @return SQL 语句
     */
    public String buildAggregationQuery(String tenantId,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime,
                                         String groupBy) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(groupBy).append(", COUNT(*) as count, AVG(duration_ms) as avg_duration");
        sql.append(" FROM ").append(MAIN_TABLE).append(" WHERE 1=1");

        if (isNotEmpty(tenantId)) {
            sql.append(" AND tenant_id = '").append(escape(tenantId)).append("'");
        }

        if (startTime != null) {
            sql.append(" AND server_time >= '").append(formatDateTime(startTime)).append("'");
        }

        if (endTime != null) {
            sql.append(" AND server_time <= '").append(formatDateTime(endTime)).append("'");
        }

        sql.append(" GROUP BY ").append(groupBy);
        sql.append(" ORDER BY count DESC");

        return sql.toString();
    }

    // ==================== 辅助方法 ====================

    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * SQL 字符串转义
     *
     * @param input 输入字符串
     * @return 转义后的字符串
     */
    private String escape(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''").replace("\\", "\\\\");
    }
}
