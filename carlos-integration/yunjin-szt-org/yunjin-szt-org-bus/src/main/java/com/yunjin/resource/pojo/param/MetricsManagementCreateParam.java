package com.yunjin.resource.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 系统指标管理 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统指标管理新增参数", description = "系统指标管理新增参数")
public class MetricsManagementCreateParam {
    @NotBlank(message = "指标编码不能为空")
    @Schema(value = "指标编码")
    private String metricsCode;
    @NotBlank(message = "指标名称不能为空")
    @Schema(value = "指标名称")
    private String metricsName;
    @NotNull(message = "指标类型(可扩展)：0 首页指标 不能为空")
    @Schema(value = "指标类型(可扩展)：0 首页指标 ")
    private Integer metricsType;
    @NotNull(message = "是否启用 1启用 0禁用不能为空")
    @Schema(value = "是否启用 1启用 0禁用")
    private Boolean state;
    @Schema(value = "指标描述")
    private String description;
}
