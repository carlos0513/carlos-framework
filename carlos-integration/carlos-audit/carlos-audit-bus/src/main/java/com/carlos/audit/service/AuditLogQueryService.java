package com.carlos.audit.service;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.vo.AuditLogStatsVO;
import com.carlos.core.pagination.Paging;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 审计日志查询服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
public interface AuditLogQueryService {

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
    Paging<AuditLogMainDTO> pageQuery(String tenantId, int pageNum, int pageSize,
                                      LocalDateTime startTime, LocalDateTime endTime,
                                      String logType, String state);

    /**
     * 查询单条日志详情
     *
     * @param id 日志ID
     * @return 日志详情
     */
    AuditLogMainDTO getById(Long id);

    /**
     * 获取实时统计
     *
     * @param tenantId 租户ID
     * @param date     日期
     * @return 统计信息
     */
    AuditLogStatsVO getRealtimeStats(String tenantId, LocalDate date);

    /**
     * 查询用户行为轨迹
     *
     * @param principalId 主体ID
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 行为轨迹列表
     */
    List<AuditLogMainDTO> getUserTrail(String principalId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询风险事件
     *
     * @param tenantId   租户ID
     * @param minRiskLevel 最低风险等级
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 风险事件列表
     */
    List<AuditLogMainDTO> listRiskEvents(String tenantId, int minRiskLevel,
                                         LocalDateTime startTime, LocalDateTime endTime);
}
