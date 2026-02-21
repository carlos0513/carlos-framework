package com.carlos.system.menu.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 菜单操作 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "菜单操作列表查询参数", description = "菜单操作列表查询参数")
public class MenuOperateSearchParam {
    @ApiModelProperty(value = "关键字")
    private String keyword;
}
