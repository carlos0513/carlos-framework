package com.yunjin.resource.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 资源组 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "资源组新增参数", description = "资源组新增参数")
public class ResourceGroupCreateParam {
    @NotBlank(message = "资源组code不能为空")
    @Schema(value = "资源组code")
    private String groupCode;
    @NotBlank(message = "资源组名称不能为空")
    @Schema(value = "资源组名称")
    private String groupName;
    @Schema(value = "资源说明")
    private String description;
    @NotNull(message = "是否启用 1启用 0禁用不能为空")
    @Schema(value = "是否启用 1启用 0禁用")
    private Boolean state;
}
