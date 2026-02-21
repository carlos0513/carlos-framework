package com.carlos.system.resource.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统资源 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统资源修改参数", description = "系统资源修改参数")
public class SysResourceUpdateParam {

    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "分类id")
    private Long categoryId;
    @ApiModelProperty(value = "资源名称")
    private String name;
    @ApiModelProperty(value = "接口路径")
    private String path;
    @ApiModelProperty(value = "接口路径前缀")
    private String pathPrefix;
    @ApiModelProperty(value = "请求方式", allowableValues = "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE")
    private String method;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "资源类型，按钮")
    private String type;
    @ApiModelProperty(value = "状态，0：禁用，1：启用")
    private String state;
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @ApiModelProperty(value = "资源描述")
    private String description;
}
