package com.carlos.system.resource.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 资源分类 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "资源分类修改参数", description = "资源分类修改参数")
public class SysResourceCategoryUpdateParam {

    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "父级ID")
    private Long parentId;
    @ApiModelProperty(value = "类型名称")
    private String name;
}
