package com.carlos.system.menu.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单操作 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@TableName("sys_menu_operate")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuOperate implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 资源名称
     */
    @TableField(value = "operate_name")
    private String operateName;
    /**
     * 资源编码
     */
    @TableField(value = "operate_code")
    private String operateCode;
    /**
     * 接口路径
     */
    @TableField(value = "path")
    private String path;
    /**
     * 菜单id
     */
    @TableField(value = "menu_id")
    private String menuId;
    /**
     * 请求方式
     */
    @TableField(value = "operate_method")
    private String operateMethod;
    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 资源类型，按钮
     */
    @TableField(value = "operate_type")
    private String operateType;
    /**
     * 状态，0：禁用，1：启用
     */
    @TableField(value = "state")
    private String state;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    @TableField(value = "is_hidden")
    private Boolean hidden;
    /**
     * 资源描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}
