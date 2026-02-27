package com.carlos.system.menu.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统菜单 更新参数封装
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统菜单修改参数")
public class MenuUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "controller名称")
    private String title;
    @Schema(description = "前端路由")
    private String path;
    @Schema(description = "前端名称")
    private String name;
    @Schema(description = "状态")
    private String state;
    @Schema(description = "请求地址")
    private String url;
    @Schema(description = "前端图标")
    private String icon;
    @Schema(description = "菜单配置")
    private String meta;
    @Schema(description = "目标组件")
    private String component;
    @Schema(description = "菜单排序")
    private Integer sort;
    private String remark;
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
}
