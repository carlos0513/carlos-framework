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
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统菜单修改参数", description = "系统菜单修改参数")
public class MenuUpdateParam {

    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
    @Schema(value = "父级ID")
    private String parentId;
    @Schema(value = "controller名称")
    private String title;
    @Schema(value = "前端路由")
    private String path;
    @Schema(value = "前端名称")
    private String name;
    @Schema(value = "状态")
    private String state;
    @Schema(value = "请求地址")
    private String url;
    @Schema(value = "前端图标")
    private String icon;
    @Schema(value = "菜单配置")
    private String meta;
    @Schema(value = "目标组件")
    private String component;
    @Schema(value = "菜单排序")
    private Integer sort;
    private String remark;
    @Schema(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
}
