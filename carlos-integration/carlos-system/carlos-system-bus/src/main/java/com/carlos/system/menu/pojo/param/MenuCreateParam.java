package com.carlos.system.menu.pojo.param;


import com.carlos.system.enums.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统菜单 新增参数封装
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统菜单新增参数")
public class MenuCreateParam {

    @Schema(description = "父级ID")
    private String parentId;
    @NotBlank(message = "功能名称不能为空")
    @Schema(description = "标题")
    private String title;
    @NotBlank(message = "视图地址不能为空")
    @Schema(description = "视图地址")
    private String path;
    @Schema(description = "前端名称")
    private String name;
    @Schema(description = "状态")
    private Boolean state;
    @Schema(description = "请求地址")
    private String url;
    @Schema(description = "前端图标")
    private String icon;
    @Schema(description = "菜单配置")
    private String meta;
    @Schema(description = "目标组件")
    private String component;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden = false;
    @Schema(description = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType = MenuType.PC;
    @Schema(description = "菜单排序")
    private Integer sort;
}
