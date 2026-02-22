package com.carlos.system.menu.pojo.vo;

import com.carlos.system.enums.MenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统菜单 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "父级ID")
    private String parentId;
    @Schema(description = "controller名称")
    private String title;
    @Schema(description = "前端路由")
    private String path;
    @Schema(description = "前端名称")
    private String name;
    @Schema(description = "前端图标")
    private String icon;
    @Schema(description = "菜单配置")
    private String meta;
    @Schema(description = "状态")
    private Boolean state;
    @Schema(description = "请求地址")
    private String url;
    @Schema(description = "目标组件")
    private String component;
    @Schema(description = "菜单级数")
    private Integer level;
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    @Schema(description = "是否有子菜单")
    Boolean haveChildren;
    @Schema(description = "菜单排序")
    private Integer sort;
    @Schema(description = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType;
}
