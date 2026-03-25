package com.carlos.audit.controller;

import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.vo.AuditLogStatsVO;
import com.carlos.audit.service.AuditLogQueryService;
import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 审计日志查询控制器
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/audit/log/query")
@RequiredArgsConstructor
@Tag(name = "审计日志查询", description = "审计日志查询接口")
public class AuditLogQueryController {

    private final AuditLogQueryService queryService;

    /**
     * 分页查询审计日志
     */
    @PostMapping("/logs/page")
    @Operation(summary = "分页查询审计日志")
    public Result<Paging<AuditLogMainDTO>> page(
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "20") int pageSize,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
        @RequestParam(required = false) String logType,
        @RequestParam(required = false) String state) {

        String tenantId = getCurrentTenantId();
        Paging<AuditLogMainDTO> result = queryService.pageQuery(tenantId, pageNum, pageSize,
            startTime, endTime, logType, state);
        return Result.success(result);
    }

    /**
     * 查询单条日志详情
     */
    @GetMapping("/logs/{id}")
    @Operation(summary = "查询审计日志详情")
    public Result<AuditLogMainDTO> getById(@PathVariable Long id) {
        AuditLogMainDTO dto = queryService.getById(id);
        return Result.success(dto);
    }

    /**
     * 获取实时统计
     */
    @GetMapping("/stats/realtime")
    @Operation(summary = "获取实时统计")
    public Result<AuditLogStatsVO> getRealtimeStats(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String tenantId = getCurrentTenantId();
        AuditLogStatsVO stats = queryService.getRealtimeStats(tenantId, date);
        return Result.success(stats);
    }

    /**
     * 查询用户行为轨迹
     */
    @GetMapping("/trail/{principalId}")
    @Operation(summary = "查询用户行为轨迹")
    public Result<List<AuditLogMainDTO>> getUserTrail(
        @PathVariable String principalId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AuditLogMainDTO> trail = queryService.getUserTrail(principalId, start, end);
        return Result.success(trail);
    }

    /**
     * 查询风险事件
     */
    @GetMapping("/risks")
    @Operation(summary = "查询风险事件")
    public Result<List<AuditLogMainDTO>> listRiskEvents(
        @RequestParam(defaultValue = "50") int minRiskLevel,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        String tenantId = getCurrentTenantId();
        List<AuditLogMainDTO> risks = queryService.listRiskEvents(tenantId, minRiskLevel, start, end);
        return Result.success(risks);
    }

    /**
     * 获取当前租户ID
     */
    private String getCurrentTenantId() {
        // Long tenantId = WebFrameworkUtils.getTenantId();
        Long tenantId = 1L;
        return tenantId != null ? String.valueOf(tenantId) : "0";
    }
}
