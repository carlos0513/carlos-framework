package com.carlos.system.menu.pojo.param;


import com.carlos.system.enums.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(value = "系统菜单新增参数", description = "系统菜单新增参数")
public class MenuCreateParam {

    @Schema(value = "父级ID")
    private String parentId;
    @NotBlank(message = "功能名称不能为空")
    @Schema(value = "标题")
    private String title;
    @NotBlank(message = "视图地址不能为空")
    @Schema(value = "视图地址")
    private String path;
    @Schema(value = "前端名称")
    private String name;
    @Schema(value = "状态")
    private Boolean state;
    @Schema(value = "请求地址")
    private String url;
    @Schema(value = "前端图标")
    private String icon;
    @Schema(value = "菜单配置")
    private String meta;
    @Schema(value = "目标组件")
    private String component;
    @Schema(value = "备注")
    private String remark;
    @Schema(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden = false;
    @Schema(value = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType = MenuType.PC;
    @Schema(value = "菜单排序")
    private Integer sort;
}
