package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-字段级变更明细 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志-字段级变更明细新增参数")
public class AuditLogFieldChangeCreateParam {
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private Long auditLogId;
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private Long dataChangeId;
    @NotBlank(message = "字段名不能为空")
    @Schema(description = "字段名")
    private String fieldName;
    @Schema(description = "字段中文描述")
    private String fieldDesc;
    @NotNull(message = "ADDED/MODIFIED/REMOVED不能为空")
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
    @NotNull(message = "是否敏感字段不能为空")
    @Schema(description = "是否敏感字段")
    private Boolean sensitive;
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private LocalDateTime createdTime;
}
