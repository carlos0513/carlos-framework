package com.carlos.system.menu.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.system.enums.MenuType;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "前端图标")
    private String icon;
    @ApiModelProperty(value = "菜单配置")
    private String meta;
    @ApiModelProperty(value = "状态")
    private Boolean state;
    @ApiModelProperty(value = "请求地址")
    private String url;
    @ApiModelProperty(value = "目标组件")
    private String component;
    @ApiModelProperty(value = "菜单级数")
    private Integer level;
    @ApiModelProperty(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "是否有子菜单")
    Boolean haveChildren;
    @ApiModelProperty(value = "菜单排序")
    private Integer sort;
    @ApiModelProperty(value = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType;
}
