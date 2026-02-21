package com.carlos.system.menu.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.system.enums.MenuType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统菜单 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@TableName("sys_menu")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 父级ID
     */
    @TableField(value = "parent_id")
    private String parentId;
    /**
     * controller名称
     */
    @TableField(value = "title")
    private String title;
    /**
     * 前端路由
     */
    @TableField(value = "path")
    private String path;
    /**
     * 前端名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 前端图标
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 菜单配置
     */
    @TableField(value = "meta")
    private String meta;
    /**
     * 目标组件
     */
    @TableField(value = "component")
    private String component;
    /**
     * 请求地址
     */
    @TableField(value = "url")
    private String url;
    /**
     * 状态
     */
    @TableField(value = "state")
    private Boolean state;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 菜单级数
     */
    @TableField(value = "level")
    private Integer level;
    /**
     * 菜单排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    @TableField(value = "is_hidden")
    private Boolean hidden;
    /**
     * 菜单类型，PC：pc端菜单，MOBILE：移动端菜单
     */
    @TableField(value = "menu_type", fill = FieldFill.INSERT)
    private MenuType menuType;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
