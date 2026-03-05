package com.carlos.audit.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-字段级变更明细 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计日志-字段级变更明细列表查询参数")
public class AuditLogFieldChangePageParam extends ParamPage {
    @Schema(description = "")
    private Long auditLogId;
    @Schema(description = "")
    private Long dataChangeId;
    @Schema(description = "字段名")
    private String fieldName;
    @Schema(description = "字段中文描述")
    private String fieldDesc;
    @Schema(description = "ADDED/MODIFIED/REMOVED")
    private Integer changeType;
    @Schema(description = "旧值")
    private String oldValue;
    @Schema(description = "值类型: STRING/NUMBER/JSON")
    private Integer oldValueType;
    @Schema(description = "新值")
    private String newValue;
    @Schema(description = "值类型")
    private Integer newValueType;
    @Schema(description = "是否敏感字段")
    private Boolean sensitive;
    @Schema(description = "")
    private LocalDateTime createdTime;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
