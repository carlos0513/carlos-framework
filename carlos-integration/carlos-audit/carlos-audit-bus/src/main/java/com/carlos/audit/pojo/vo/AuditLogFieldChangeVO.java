package com.carlos.audit.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-字段级变更明细 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogFieldChangeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "")
    private Long id;
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

}
