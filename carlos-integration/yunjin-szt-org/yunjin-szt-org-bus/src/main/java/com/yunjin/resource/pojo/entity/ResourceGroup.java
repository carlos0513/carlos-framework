package com.yunjin.resource.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源组 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@TableName("sys_resource_group")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 资源组code
     */
    @TableField(value = "group_code")
    private String groupCode;
    /**
     * 资源组名称
     */
    @TableField(value = "group_name")
    private String groupName;
    /**
     * 资源说明
     */
    @TableField(value = "description")
    private String description;
    /**
     * 是否启用 1启用 0禁用
     */
    @TableField(value = "state")
    private Boolean state;
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
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
