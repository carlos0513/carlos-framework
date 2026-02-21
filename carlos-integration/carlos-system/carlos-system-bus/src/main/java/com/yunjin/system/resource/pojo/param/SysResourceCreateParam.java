package com.carlos.system.resource.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * <p>
 * 系统资源 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统资源新增参数", description = "系统资源新增参数")
public class SysResourceCreateParam {

    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "分类id")
    private Long categoryId;
    @NotBlank(message = "资源名称不能为空")
    @ApiModelProperty(value = "资源名称")
    private String name;
    @NotBlank(message = "接口路径不能为空")
    @ApiModelProperty(value = "接口路径")
    private String path;
    @NotBlank(message = "接口路径前缀不能为空")
    @ApiModelProperty(value = "接口路径前缀")
    private String pathPrefix;
    @NotBlank(message = "请求方式不能为空")
    @ApiModelProperty(value = "请求方式", allowableValues = "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE")
    private String method;
    @ApiModelProperty(value = "图标")
    private String icon;
    @NotBlank(message = "资源类型，按钮不能为空")
    @ApiModelProperty(value = "资源类型，按钮")
    private String type;
    @NotBlank(message = "状态不能为空")
    @ApiModelProperty(value = "状态，0：禁用，1：启用")
    private String state;
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden = false;
    @ApiModelProperty(value = "资源描述")
    private String description;
}
