package com.carlos.audit.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-数据变更详情 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogDataChangeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "")
    private Long id;
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

}
