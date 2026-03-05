package com.carlos.audit.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-动态标签 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogTagsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "")
    private Long id;
    @Schema(description = "")
    private Long auditLogId;
    @Schema(description = "标签键")
    private String tagKey;
    @Schema(description = "标签值")
    private String tagValue;
    @Schema(description = "")
    private LocalDateTime createdTime;

}
