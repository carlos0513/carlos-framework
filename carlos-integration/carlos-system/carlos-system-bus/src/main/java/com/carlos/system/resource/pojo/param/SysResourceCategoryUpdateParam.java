package com.carlos.system.resource.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资源分类 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
@Schema(description = "资源分类修改参数")
public class SysResourceCategoryUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "类型名称")
    private String name;
}
