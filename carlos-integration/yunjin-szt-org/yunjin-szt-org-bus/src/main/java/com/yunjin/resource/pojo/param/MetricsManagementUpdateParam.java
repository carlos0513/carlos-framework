package com.yunjin.resource.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 系统指标管理 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统指标管理修改参数", description = "系统指标管理修改参数")
public class MetricsManagementUpdateParam {
    @NotBlank(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
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
}
