package com.carlos.system.resource.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 资源分类 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "资源分类新增参数", description = "资源分类新增参数")
public class SysResourceCategoryCreateParam {

    @ApiModelProperty(value = "父级ID")
    private Long parentId;
    @ApiModelProperty(value = "类型名称")
    private String name;
}
