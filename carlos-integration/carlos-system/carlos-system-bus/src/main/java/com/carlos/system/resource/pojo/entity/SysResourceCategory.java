package com.carlos.system.resource.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源分类 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
@TableName("sys_resource_category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceCategory implements Serializable {

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
    private Long parentId;
    /**
     * 类型名称
     */
    @TableField(value = "name")
    private String name;
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
