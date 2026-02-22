package com.carlos.system.resource.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统资源 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@TableName("sys_resource")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResource implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 分类id
     */
    @TableField(value = "category_id")
    private String categoryId;
    /**
     * 资源名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 接口路径
     */
    @TableField(value = "path")
    private String path;
    /**
     * 请求前缀
     */
    @TableField(value = "path_prefix")
    private String pathPrefix;
    /**
     * 请求方式
     */
    @TableField(value = "method")
    private String method;
    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 资源类型，按钮
     */
    @TableField(value = "type")
    private String type;
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
     * 白名单地址
     */
    @TableField(value = "is_whitelist")
    private Boolean whitelist;
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
