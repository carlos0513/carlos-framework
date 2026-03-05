package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-动态标签 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志-动态标签新增参数")
public class AuditLogTagsCreateParam {
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private Long auditLogId;
    @NotBlank(message = "标签键不能为空")
    @Schema(description = "标签键")
    private String tagKey;
    @NotBlank(message = "标签值不能为空")
    @Schema(description = "标签值")
    private String tagValue;
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private LocalDateTime createdTime;
}
