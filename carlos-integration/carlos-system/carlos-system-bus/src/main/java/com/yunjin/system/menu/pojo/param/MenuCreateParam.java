package com.carlos.system.menu.pojo.param;


import com.carlos.system.enums.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;


/**
 * <p>
 * 系统菜单 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "系统菜单新增参数", description = "系统菜单新增参数")
public class MenuCreateParam {

    @ApiModelProperty(value = "父级ID")
    private String parentId;
    @NotBlank(message = "功能名称不能为空")
    @ApiModelProperty(value = "标题")
    private String title;
    @NotBlank(message = "视图地址不能为空")
    @ApiModelProperty(value = "视图地址")
    private String path;
    @ApiModelProperty(value = "前端名称")
    private String name;
    @ApiModelProperty(value = "状态")
    private Boolean state;
    @ApiModelProperty(value = "请求地址")
    private String url;
    @ApiModelProperty(value = "前端图标")
    private String icon;
    @ApiModelProperty(value = "菜单配置")
    private String meta;
    @ApiModelProperty(value = "目标组件")
    private String component;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden = false;
    @ApiModelProperty(value = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType = MenuType.PC;
    @ApiModelProperty(value = "菜单排序")
    private Integer sort;
}
