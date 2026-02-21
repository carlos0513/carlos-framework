package com.carlos.system.menu.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 菜单操作 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "菜单操作新增参数", description = "菜单操作新增参数")
public class MenuOperateCreateParam {
    @ApiModelProperty(value = "资源名称")
    private String operateName;
    @ApiModelProperty(value = "资源编码")
    private String operateCode;
    @ApiModelProperty(value = "接口路径")
    private String path;
    @NotBlank(message = "请求方式不能为空")
    @ApiModelProperty(value = "请求方式")
    private String operateMethod;
    @ApiModelProperty(value = "图标")
    private String icon;
    @NotBlank(message = "资源类型，按钮不能为空")
    @ApiModelProperty(value = "资源类型，按钮")
    private String operateType;
    @NotBlank(message = "状态，0：禁用，1：启用不能为空")
    @ApiModelProperty(value = "状态，0：禁用，1：启用")
    private String state;
    @NotNull(message = "显示和隐藏，0：显示，1：隐藏不能为空")
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @ApiModelProperty(value = "资源描述")
    private String description;
}
