package com.yunjin.resource.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.enums.ResourceTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源组详情项 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@TableName("sys_resource_group_item")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceGroupItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 资源组id
     */
    @TableField(value = "group_id")
    private String groupId;
    /**
     * 资源类型(可扩展):0按钮, 1指标
     */
    @TableField(value = "resource_type")
    private ResourceTypeEnum resourceType;
    /**
     * 资源key
     */
    @TableField(value = "resource_key")
    private String resourceKey;
    /**
     * 创建者编号
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
