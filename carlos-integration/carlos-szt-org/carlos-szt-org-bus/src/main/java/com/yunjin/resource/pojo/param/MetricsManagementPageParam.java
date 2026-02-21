package com.yunjin.resource.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统指标管理 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "系统指标管理列表查询参数", description = "系统指标管理列表查询参数")
public class MetricsManagementPageParam extends ParamPage {
    @Schema(value = "指标编码")
    private String metricsCode;
    @Schema(value = "指标名称")
    private String metricsName;
    @Schema(value = "指标类型(可扩展)：0 首页指标 ")
    private Integer metricsType;
    @Schema(value = "是否启用 1启用 0禁用")
    private Boolean state;
    @Schema(value = "指标描述")
    private String description;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
