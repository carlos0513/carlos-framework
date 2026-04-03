package com.carlos.audit.clickhouse;

import com.carlos.audit.api.pojo.enums.*;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.clickhouse.client.ClickHouseRecord;
import com.clickhouse.client.ClickHouseValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * 审计日志行映射器
 *
 * <p>将 ClickHouse 查询结果映射为 AuditLogMainDTO</p>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Component
public class AuditLogRowMapper implements Function<ClickHouseRecord, AuditLogMainDTO> {

    @Override
    public AuditLogMainDTO apply(ClickHouseRecord record) {
        if (record == null) {
            return null;
        }

        try {
            AuditLogMainDTO dto = new AuditLogMainDTO();

            // 基础信息
            dto.setId(getLong(record, "id"));
            dto.setServerTime(getLocalDateTime(record, "server_time"));
            dto.setEventDate(getLocalDate(record, "event_date"));
            dto.setClientTime(getLocalDateTime(record, "client_time"));
            dto.setEventTime(getLocalDateTime(record, "event_time"));
            dto.setDurationMs(getLong(record, "duration_ms"));
            dto.setRetentionDeadline(getLocalDate(record, "retention_deadline"));
            dto.setLogSchemaVersion(getInteger(record, "log_schema_version"));
            dto.setLogType(getString(record, "log_type"));
            dto.setRiskLevel(getInteger(record, "risk_level"));
            dto.setOperation(getString(record, "operation"));

            // 主体信息
            dto.setPrincipalId(getString(record, "principal_id"));
            dto.setPrincipalName(getString(record, "principal_name"));
            dto.setPrincipalType(parsePrincipalType(getString(record, "principal_type")));
            dto.setTenantId(getString(record, "tenant_id"));
            dto.setDeptId(getString(record, "dept_id"));
            dto.setDeptName(getString(record, "dept_name"));
            dto.setDeptPath(getString(record, "dept_path"));

            // 目标对象
            dto.setTargetType(getString(record, "target_type"));
            dto.setTargetId(getString(record, "target_id"));
            dto.setTargetName(getString(record, "target_name"));
            dto.setTargetSnapshot(getString(record, "target_snapshot"));

            // 执行状态
            dto.setState(parseState(getString(record, "state")));
            dto.setResultCode(getString(record, "result_code"));
            dto.setResultMessage(getString(record, "result_message"));
            dto.setCategory(parseCategory(getString(record, "category")));

            // 请求信息
            dto.setClientIp(getString(record, "client_ip"));
            dto.setClientPort(getInteger(record, "client_port"));
            dto.setServerIp(getString(record, "server_ip"));
            dto.setUserAgent(getString(record, "user_agent"));
            dto.setDeviceFingerprint(getString(record, "device_fingerprint"));

            // 认证授权
            dto.setAuthType(parseAuthType(getString(record, "auth_type")));
            dto.setAuthProvider(parseAuthProvider(getString(record, "auth_provider")));

            // 业务信息
            dto.setBizChannel(parseBizChannel(getString(record, "biz_channel")));
            dto.setBizScene(getString(record, "biz_scene"));

            return dto;

        } catch (Exception e) {
            log.error("映射 AuditLogMainDTO 失败", e);
            return null;
        }
    }

    // ==================== 类型转换方法 ====================

    private String getString(ClickHouseRecord record, String column) {
        try {
            ClickHouseValue value = record.getValue(column);
            return value.isNullOrEmpty() ? null : value.asString();
        } catch (Exception e) {
            return null;
        }
    }

    private Long getLong(ClickHouseRecord record, String column) {
        try {
            ClickHouseValue value = record.getValue(column);
            return value.isNullOrEmpty() ? null : value.asLong();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getInteger(ClickHouseRecord record, String column) {
        try {
            ClickHouseValue value = record.getValue(column);
            return value.isNullOrEmpty() ? null : value.asInteger();
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime getLocalDateTime(ClickHouseRecord record, String column) {
        try {
            ClickHouseValue value = record.getValue(column);
            return value.isNullOrEmpty() ? null : value.asDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate getLocalDate(ClickHouseRecord record, String column) {
        try {
            ClickHouseValue value = record.getValue(column);
            return value.isNullOrEmpty() ? null : value.asDate();
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== 枚举解析方法 ====================

    private AuditLogPrincipalTypeEnum parsePrincipalType(String value) {
        if (value == null) return null;
        try {
            return AuditLogPrincipalTypeEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AuditLogStateEnum parseState(String value) {
        if (value == null) return null;
        try {
            return AuditLogStateEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AuditLogCategoryEnum parseCategory(String value) {
        if (value == null) return null;
        try {
            return AuditLogCategoryEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AuditLogAuthTypeEnum parseAuthType(String value) {
        if (value == null) return null;
        try {
            return AuditLogAuthTypeEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AuditLogAuthProviderEnum parseAuthProvider(String value) {
        if (value == null) return null;
        try {
            return AuditLogAuthProviderEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private AuditLogBizChannelEnum parseBizChannel(String value) {
        if (value == null) return null;
        try {
            return AuditLogBizChannelEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
