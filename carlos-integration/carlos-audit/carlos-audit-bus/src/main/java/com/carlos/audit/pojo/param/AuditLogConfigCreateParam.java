package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * <p>
 * 审计日志配置 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志配置新增参数")
public class AuditLogConfigCreateParam {
    @NotBlank(message = "日志类型不能为空")
    @Schema(description = "日志类型")
    private String logType;
    @NotNull(message = "保留天数不能为空")
    @Schema(description = "保留天数")
    private Integer retentionDays;
    @NotNull(message = "采样率 0.00-1.00不能为空")
    @Schema(description = "采样率 0.00-1.00")
    private BigDecimal samplingRate;
    @NotNull(message = "是否异步写入不能为空")
    @Schema(description = "是否异步写入")
    private Boolean asyncWrite;
    @NotNull(message = "是否存储数据变更不能为空")
    @Schema(description = "是否存储数据变更")
    private Boolean storeDataChange;
    @NotNull(message = "是否存储技术上下文不能为空")
    @Schema(description = "是否存储技术上下文")
    private Boolean storeTechnical;
    @NotBlank(message = "租户ID，0表示系统级不能为空")
    @Schema(description = "租户ID，0表示系统级")
    private String tenantId;
}
