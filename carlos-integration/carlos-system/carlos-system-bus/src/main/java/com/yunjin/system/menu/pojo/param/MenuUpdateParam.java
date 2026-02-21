package com.carlos.system.menu.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统菜单 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统菜单修改参数", description = "系统菜单修改参数")
public class MenuUpdateParam {

    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "父级ID")
    private String parentId;
    @ApiModelProperty(value = "controller名称")
    private String title;
    @ApiModelProperty(value = "前端路由")
    private String path;
    @ApiModelProperty(value = "前端名称")
    private String name;
    @ApiModelProperty(value = "状态")
    private String state;
    @ApiModelProperty(value = "请求地址")
    private String url;
    @ApiModelProperty(value = "前端图标")
    private String icon;
    @ApiModelProperty(value = "菜单配置")
    private String meta;
    @ApiModelProperty(value = "目标组件")
    private String component;
    @ApiModelProperty(value = "菜单排序")
    private Integer sort;
    private String remark;
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
}
