package com.carlos.system.resource.pojo.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 菜单树形数据
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceGroupVO {

    @ApiModelProperty(value = "菜单分类")
    private String name;

    @ApiModelProperty(value = "资源列表")
    List<SysResourceBaseVO> resources;

}
