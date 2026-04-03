package com.carlos.audit.service;

import com.carlos.audit.clickhouse.ClickHouseQueryBuilder;
import com.carlos.audit.clickhouse.ClickHouseQueryClient;
import com.carlos.audit.clickhouse.AuditLogRowMapper;
import com.carlos.audit.manager.AuditLogMainManager;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.vo.AuditLogStatsVO;
import com.carlos.core.pagination.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 审计日志查询服务实现
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogQueryService {

    private final AuditLogMainManager auditLogMainManager;
    private final ClickHouseQueryClient clickHouseQueryClient;
    private final ClickHouseQueryBuilder queryBuilder;
    private final AuditLogRowMapper auditLogRowMapper;

    /**
     * 分页查询审计日志
     *
     * @param tenantId   租户ID
     * @param pageNum    页码
     * @param pageSize   页大小
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param logType    日志类型
     * @param state      状态
     * @return 分页结果
     */
    public Paging<AuditLogMainDTO> pageQuery(String tenantId, int pageNum, int pageSize,
                                             LocalDateTime startTime, LocalDateTime endTime,
                                             String logType, String state) {
        log.debug("分页查询审计日志，tenantId={}, pageNum={}, pageSize={}", tenantId, pageNum, pageSize);

        // 计算偏移量
        long offset = (long) (pageNum - 1) * pageSize;

        // 构建计数 SQL
        String countSql = queryBuilder.buildCountQuery(
            tenantId, startTime, endTime, logType, state, null, null
        );

        // 构建查询 SQL
        String querySql = queryBuilder.buildPageQuery(
            tenantId, startTime, endTime, logType, state, null, null,
            offset, pageSize
        );

        // 执行分页查询
        ClickHouseQueryClient.PageResult<AuditLogMainDTO> pageResult =
            clickHouseQueryClient.queryPage(countSql, querySql, auditLogRowMapper, pageNum, pageSize);

        // 转换为 Paging 对象
        Paging<AuditLogMainDTO> paging = new Paging<>();
        paging.setPageNum(pageNum);
        paging.setPageSize(pageSize);
        paging.setTotal(pageResult.getTotal());
        paging.setPages(pageResult.getPages());
        paging.setList(pageResult.getRecords());

        return paging;
    }

    /**
     * 查询单条日志详情
     *
     * @param id 日志ID
     * @return 日志详情
     */
    public AuditLogMainDTO getById(Long id) {
        log.debug("查询审计日志详情，id={}", id);

        // 构建查询 SQL
        String sql = queryBuilder.buildGetByIdQuery(id);

        // 执行查询
        return clickHouseQueryClient.queryOne(sql, auditLogRowMapper);
    }

    /**
     * 获取实时统计
     *
     * @param tenantId 租户ID
     * @param date     日期
     * @return 统计信息
     */
    public AuditLogStatsVO getRealtimeStats(String tenantId, LocalDate date) {
        log.debug("获取实时统计，tenantId={}, date={}", tenantId, date);

        // 构建统计查询 SQL
        String sql = queryBuilder.buildStatsQuery(tenantId, date);

        // 执行查询并解析结果
        return clickHouseQueryClient.queryOne(sql, record -> {
            AuditLogStatsVO stats = new AuditLogStatsVO();
            try {
                stats.setTotalCount(record.getValue("total_count").asLong());
                stats.setSuccessCount(record.getValue("success_count").asLong());
                stats.setFailCount(record.getValue("fail_count").asLong());
                stats.setRiskEventCount(record.getValue("risk_count").asLong());
                stats.setAvgDurationMs(record.getValue("avg_duration").asDouble());
            } catch (Exception e) {
                log.warn("解析统计结果失败", e);
            }
            return stats;
        });
    }

    /**
     * 查询用户行为轨迹
     *
     * @param principalId 主体ID
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 行为轨迹列表
     */
    public List<AuditLogMainDTO> getUserTrail(String principalId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("查询用户行为轨迹，principalId={}", principalId);
        return auditLogMainManager.getUserTrail(principalId, startTime, endTime);
    }

    /**
     * 查询风险事件
     *
     * @param tenantId   租户ID
     * @param minRiskLevel 最低风险等级
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 风险事件列表
     */
    public List<AuditLogMainDTO> listRiskEvents(String tenantId, int minRiskLevel,
                                                LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("查询风险事件，tenantId={}, minRiskLevel={}", tenantId, minRiskLevel);

        // 构建查询 SQL，限制返回 1000 条
        String sql = queryBuilder.buildRiskEventsQuery(tenantId, minRiskLevel, startTime, endTime, 1000);

        // 执行查询
        return clickHouseQueryClient.query(sql, auditLogRowMapper);
    }
}
