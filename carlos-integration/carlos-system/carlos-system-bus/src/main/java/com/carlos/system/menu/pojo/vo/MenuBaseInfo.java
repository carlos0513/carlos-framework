package com.carlos.system.menu.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统菜单基本信息
 * </p>
 *
 * @author yunjin
 * @date 2021/12/27
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "路径")
    private String path;
    @Schema(description = "菜单名称")
    private String title;
    @Schema(description = "目标组件")
    private String component;
    @Schema(description = "菜单级数")
    private Integer level;
    @Schema(description = "菜单排序")
    private Integer sort;
    @Schema(description = "前端名称")
    private String name;
    @Schema(description = "前端图标")
    private String icon;
    @Schema(description = "菜单配置")
    private String meta;
}
