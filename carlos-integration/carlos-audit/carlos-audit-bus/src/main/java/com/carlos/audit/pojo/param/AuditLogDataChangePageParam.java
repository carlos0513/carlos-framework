package com.carlos.audit.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-数据变更详情 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计日志-数据变更详情列表查询参数")
public class AuditLogDataChangePageParam extends ParamPage {
    @Schema(description = "关联主表ID")
    private Long auditLogId;
    @Schema(description = "变更前数据(JSON)")
    private String oldData;
    @Schema(description = "是否压缩")
    private Boolean oldDataCompressed;
    @Schema(description = "变更后数据(JSON)")
    private String newData;
    @Schema(description = "是否压缩")
    private Boolean newDataCompressed;
    @Schema(description = "变更字段数量")
    private Integer changedFieldCount;
    @Schema(description = "")
    private LocalDateTime createdTime;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
