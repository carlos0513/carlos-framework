package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-数据变更详情 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志-数据变更详情新增参数")
public class AuditLogDataChangeCreateParam {
    @NotNull(message = "关联主表ID不能为空")
    @Schema(description = "关联主表ID")
    private Long auditLogId;
    @Schema(description = "变更前数据(JSON)")
    private String oldData;
    @NotNull(message = "是否压缩不能为空")
    @Schema(description = "是否压缩")
    private Boolean oldDataCompressed;
    @Schema(description = "变更后数据(JSON)")
    private String newData;
    @NotNull(message = "是否压缩不能为空")
    @Schema(description = "是否压缩")
    private Boolean newDataCompressed;
    @Schema(description = "变更字段数量")
    private Integer changedFieldCount;
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private LocalDateTime createdTime;
}
