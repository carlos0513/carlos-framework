package com.carlos.audit.apiimpl;

import com.carlos.audit.api.ApiAuditLogMain;
import com.carlos.audit.api.pojo.ao.AuditLogMainAO;
import com.carlos.audit.api.pojo.enums.AuditLogCategoryEnum;
import com.carlos.audit.api.pojo.enums.AuditLogPrincipalTypeEnum;
import com.carlos.audit.api.pojo.enums.AuditLogStateEnum;
import com.carlos.audit.api.pojo.param.ApiAuditLogMainParam;
import com.carlos.audit.convert.AuditLogMainConvert;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.service.AuditLogMainService;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审计日志 Feign 接口实现
 * 提供给其他微服务通过 Feign 调用保存审计日志
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audit/log/main")
@Tag(name = "审计日志Feign接口", description = "供其他微服务调用保存审计日志")
public class ApiAuditLogMainImpl implements ApiAuditLogMain {

    private final AuditLogMainService logMainService;
    private final AuditLogMainConvert auditLogMainConvert;

    /**
     * 保存审计日志（异步）
     *
     * @param param 审计日志参数
     * @return 保存结果
     */
    @Override
    @PostMapping("/save")
    @Operation(summary = "保存审计日志（异步）", description = "通过Disruptor异步写入，性能最优")
    public Result<AuditLogMainAO> saveAuditLog(@RequestBody ApiAuditLogMainParam param) {
        try {
            if (param == null) {
                return Result.fail("审计日志参数不能为空");
            }

            // 参数转换为 DTO
            AuditLogMainDTO dto = convertToDTO(param);

            // 异步保存
            logMainService.addAuditLogMain(dto);

            // 转换为 AO 返回
            AuditLogMainAO ao = auditLogMainConvert.toAO(dto);

            log.debug("审计日志异步保存成功，logType: {}", param.getLogType());
            return Result.ok(ao, "审计日志保存成功");

        } catch (Exception e) {
            log.error("审计日志异步保存失败: {}", e.getMessage(), e);
            return Result.fail("审计日志保存失败: " + e.getMessage());
        }
    }

    /**
     * 保存审计日志（同步）
     *
     * @param param     审计日志参数
     * @param timeoutMs 超时时间（毫秒）
     * @return 保存结果
     */
    @Override
    @PostMapping("/save-sync")
    @Operation(summary = "保存审计日志（同步）", description = "重要日志使用，确保写入成功")
    public Result<AuditLogMainAO> saveAuditLogSync(@RequestBody ApiAuditLogMainParam param,
                                                   @RequestParam(value = "timeoutMs", defaultValue = "5000") long timeoutMs) {
        try {
            if (param == null) {
                return Result.fail("审计日志参数不能为空");
            }

            // 参数转换为 DTO
            AuditLogMainDTO dto = convertToDTO(param);

            // 同步保存
            boolean success = logMainService.addAuditLogMainSync(dto, timeoutMs);

            if (!success) {
                log.warn("审计日志同步保存超时，logType: {}", param.getLogType());
                return Result.fail("审计日志保存超时");
            }

            // 转换为 AO 返回
            AuditLogMainAO ao = auditLogMainConvert.toAO(dto);

            log.debug("审计日志同步保存成功，logType: {}", param.getLogType());
            return Result.ok(ao, "审计日志保存成功");

        } catch (Exception e) {
            log.error("审计日志同步保存失败: {}", e.getMessage(), e);
            return Result.fail("审计日志保存失败: " + e.getMessage());
        }
    }

    /**
     * 批量保存审计日志（异步）
     *
     * @param params 审计日志参数列表
     * @return 保存结果
     */
    @Override
    @PostMapping("/batch-save")
    @Operation(summary = "批量保存审计日志（异步）")
    public Result<Void> batchSaveAuditLog(@RequestBody List<ApiAuditLogMainParam> params) {
        try {
            if (params == null || params.isEmpty()) {
                return Result.fail("审计日志参数列表不能为空");
            }

            // 批量转换为 DTO
            List<AuditLogMainDTO> dtos = params.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

            // 批量异步保存
            logMainService.batchAddAuditLogMain(dtos);

            log.debug("批量审计日志异步保存成功，数量: {}", params.size());
            return Result.ok();

        } catch (Exception e) {
            log.error("批量审计日志保存失败: {}", e.getMessage(), e);
            return Result.fail("批量审计日志保存失败: " + e.getMessage());
        }
    }

    /**
     * 保存简单审计日志（最小参数）
     *
     * @param logType     日志类型
     * @param category    日志大类
     * @param operation   操作描述
     * @param principalId 操作主体ID
     * @param targetId    目标对象ID
     * @param state       状态
     * @return 保存结果
     */
    @Override
    @PostMapping("/save-simple")
    @Operation(summary = "保存简单审计日志", description = "最小参数快速记录")
    public Result<AuditLogMainAO> saveSimpleAuditLog(@RequestParam("logType") String logType,
                                                     @RequestParam("category") AuditLogCategoryEnum category,
                                                     @RequestParam("operation") String operation,
                                                     @RequestParam("principalId") String principalId,
                                                     @RequestParam(value = "targetId", required = false) String targetId,
                                                     @RequestParam(value = "state", defaultValue = "SUCCESS") AuditLogStateEnum state) {
        try {
            // 构建最小化的 DTO
            AuditLogMainDTO dto = new AuditLogMainDTO();
            dto.setLogType(logType);
            dto.setCategory(category);
            dto.setOperation(operation);
            dto.setPrincipalId(principalId);
            dto.setTargetId(targetId);
            dto.setState(state);

            // 设置必要的时间字段
            LocalDateTime now = LocalDateTime.now();
            dto.setServerTime(now);
            dto.setEventTime(now);
            dto.setEventDate(LocalDate.now());
            dto.setClientTime(now);

            // 设置默认值
            dto.setLogSchemaVersion(1);
            dto.setRiskLevel(0);
            dto.setDurationMs(0);
            dto.setRetentionDeadline(LocalDate.now().plusDays(90));
            dto.setHasDataChange(false);
            dto.setPrincipalType(AuditLogPrincipalTypeEnum.USER);
            dto.setState(state != null ? state : AuditLogStateEnum.SUCCESS);

            // 异步保存
            logMainService.addAuditLogMain(dto);

            // 转换为 AO 返回
            AuditLogMainAO ao = auditLogMainConvert.toAO(dto);

            log.debug("简单审计日志保存成功，logType: {}", logType);
            return Result.ok(ao, "审计日志保存成功");

        } catch (Exception e) {
            log.error("简单审计日志保存失败: {}", e.getMessage(), e);
            return Result.fail("审计日志保存失败: " + e.getMessage());
        }
    }

    /**
     * 将 API 参数转换为 DTO
     *
     * @param param API 参数
     * @return DTO
     */
    private AuditLogMainDTO convertToDTO(ApiAuditLogMainParam param) {
        AuditLogMainDTO dto = new AuditLogMainDTO();

        // 基础字段
        dto.setId(param.getId());
        dto.setServerTime(param.getServerTime() != null ? param.getServerTime() : LocalDateTime.now());
        dto.setEventDate(param.getEventDate() != null ? param.getEventDate() : LocalDate.now());
        dto.setClientTime(param.getClientTime());
        dto.setEventTime(param.getEventTime() != null ? param.getEventTime() : LocalDateTime.now());
        dto.setDurationMs(param.getDurationMs());
        dto.setRetentionDeadline(param.getRetentionDeadline() != null ? param.getRetentionDeadline() : LocalDate.now().plusDays(90));
        dto.setLogSchemaVersion(param.getLogSchemaVersion() != null ? param.getLogSchemaVersion() : 1);

        // 分类和类型
        dto.setCategory(param.getCategory());
        dto.setLogType(param.getLogType());
        dto.setRiskLevel(param.getRiskLevel() != null ? param.getRiskLevel() : 0);

        // 主体信息
        dto.setPrincipalId(param.getPrincipalId());
        dto.setPrincipalType(param.getPrincipalType());
        dto.setPrincipalName(param.getPrincipalName());
        dto.setTenantId(param.getTenantId());
        dto.setDeptId(param.getDeptId());
        dto.setDeptName(param.getDeptName());
        dto.setDeptPath(param.getDeptPath());

        // 目标对象
        dto.setTargetType(param.getTargetType());
        dto.setTargetId(param.getTargetId());
        dto.setTargetName(param.getTargetName());
        dto.setTargetSnapshot(param.getTargetSnapshot());

        // 状态和结果
        dto.setState(param.getState());
        dto.setResultCode(param.getResultCode());
        dto.setResultMessage(param.getResultMessage());
        dto.setOperation(param.getOperation());
        dto.setApprovalComment(param.getApprovalComment());

        // 客户端信息
        dto.setClientIp(param.getClientIp());
        dto.setClientPort(param.getClientPort());
        dto.setServerIp(param.getServerIp());
        dto.setUserAgent(param.getUserAgent());
        dto.setDeviceFingerprint(param.getDeviceFingerprint());

        // 地理位置
        dto.setLocationCountry(param.getLocationCountry());
        dto.setLocationProvince(param.getLocationProvince());
        dto.setLocationCity(param.getLocationCity());
        dto.setLocationLat(param.getLocationLat());
        dto.setLocationLon(param.getLocationLon());

        // 认证信息
        dto.setAuthType(param.getAuthType());
        dto.setAuthProvider(param.getAuthProvider());
        dto.setRoles(param.getRoles());
        dto.setPermissions(param.getPermissions());

        // 业务信息
        dto.setBizChannel(param.getBizChannel());
        dto.setBizScene(param.getBizScene());
        dto.setBizOrderNo(param.getBizOrderNo());
        dto.setRelatedBizIds(param.getRelatedBizIds());
        dto.setMonetaryAmount(param.getMonetaryAmount());

        // 流程和批次
        dto.setProcessId(param.getProcessId());
        dto.setBatchId(param.getBatchId());
        dto.setBatchIndex(param.getBatchIndex());
        dto.setBatchTotal(param.getBatchTotal());
        dto.setTaskId(param.getTaskId());
        dto.setApproverId(param.getApproverId());

        // 数据变更
        dto.setHasDataChange(param.getHasDataChange() != null ? param.getHasDataChange() : false);
        dto.setEntityClass(param.getEntityClass());
        dto.setTableName(param.getTableName());
        dto.setChangeSummary(param.getChangeSummary());
        dto.setChangedFieldCount(param.getChangedFieldCount());
        dto.setOldData(param.getOldData());
        dto.setNewData(param.getNewData());
        dto.setOldDataCompressed(param.getOldDataCompressed());
        dto.setNewDataCompressed(param.getNewDataCompressed());

        // 链路追踪
        dto.setTraceId(param.getTraceId());
        dto.setSpanId(param.getSpanId());
        dto.setParentSpanId(param.getParentSpanId());
        dto.setTracePath(param.getTracePath());

        // 性能指标
        dto.setDbQueryCount(param.getDbQueryCount());
        dto.setDbQueryTimeMs(param.getDbQueryTimeMs());
        dto.setExternalCallCount(param.getExternalCallCount());
        dto.setExternalCallTimeMs(param.getExternalCallTimeMs());
        dto.setCustomMetrics(param.getCustomMetrics());

        // 负载存储
        dto.setRequestPayloadRef(param.getRequestPayloadRef());
        dto.setResponsePayloadRef(param.getResponsePayloadRef());

        // 应用信息
        dto.setAppName(param.getAppName());
        dto.setAppVersion(param.getAppVersion());
        dto.setCluster(param.getCluster());
        dto.setHostName(param.getHostName());

        // 标签和附件
        dto.setTagKeys(param.getTagKeys());
        dto.setTagValues(param.getTagValues());
        dto.setAttachmentCount(param.getAttachmentCount());
        dto.setAttachmentTypes(param.getAttachmentTypes());
        dto.setAttachmentTotalSize(param.getAttachmentTotalSize());
        dto.setFirstAttachmentRef(param.getFirstAttachmentRef());
        dto.setAttachmentRefs(param.getAttachmentRefs());

        // 动态扩展
        dto.setDynamicTags(param.getDynamicTags());
        dto.setDynamicExtras(param.getDynamicExtras());

        // 时间戳
        dto.setCreatedTime(param.getCreatedTime());
        dto.setUpdatedTime(param.getUpdatedTime());

        return dto;
    }
}
