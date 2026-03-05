package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 审计日志配置 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志配置修改参数")
public class AuditLogConfigUpdateParam {
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private Long id;
    @Schema(description = "日志类型")
    private String logType;
    @Schema(description = "保留天数")
    private Integer retentionDays;
    @Schema(description = "采样率 0.00-1.00")
    private BigDecimal samplingRate;
    @Schema(description = "是否异步写入")
    private Boolean asyncWrite;
    @Schema(description = "是否存储数据变更")
    private Boolean storeDataChange;
    @Schema(description = "是否存储技术上下文")
    private Boolean storeTechnical;
    @Schema(description = "租户ID，0表示系统级")
    private String tenantId;
}
