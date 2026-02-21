package com.carlos.system.menu.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.system.enums.MenuType;
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
    @Schema(value = "前端图标")
    private String icon;
    @Schema(value = "菜单配置")
    private String meta;
    @Schema(value = "状态")
    private Boolean state;
    @Schema(value = "请求地址")
    private String url;
    @Schema(value = "目标组件")
    private String component;
    @Schema(value = "菜单级数")
    private Integer level;
    @Schema(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @Schema(value = "备注")
    private String remark;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;
    @Schema(value = "是否有子菜单")
    Boolean haveChildren;
    @Schema(value = "菜单排序")
    private Integer sort;
    @Schema(value = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType;
}
