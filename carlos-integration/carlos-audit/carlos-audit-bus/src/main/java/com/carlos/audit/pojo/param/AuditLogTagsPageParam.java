package com.carlos.audit.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-动态标签 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计日志-动态标签列表查询参数")
public class AuditLogTagsPageParam extends ParamPage {
    @Schema(description = "")
    private Long auditLogId;
    @Schema(description = "标签键")
    private String tagKey;
    @Schema(description = "标签值")
    private String tagValue;
    @Schema(description = "")
    private LocalDateTime createdTime;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
