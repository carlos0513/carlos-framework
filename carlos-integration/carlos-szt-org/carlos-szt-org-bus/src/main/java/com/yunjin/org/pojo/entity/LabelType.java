package com.yunjin.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.enums.LabelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 标签分类 数据源对象
    * </p>
*
* @author  yunjin
* @date    2024-3-22 15:07:09
*/
@Data
@Accessors(chain = true)
@TableName("bbt_label_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelType implements Serializable{
private static final long serialVersionUID = 1L;
    /**
    * 主键ID
    */
        @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
    * 类型名称
    */
        @TableField(value = "name")
    private String name;
    /**
    * 父级id
    */
        @TableField(value = "parent_id")
    private String parentId;
    /**
    * 排序
    */
        @TableField(value = "sort")
    private Integer sort;
    /**
    * 是否隐藏
    */
        @TableField(value = "is_hidden")
    private Boolean hidden;
    /**
    * 是否删除
    */
        @TableLogic
        @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
    * 创建者编号
    */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
    * 创建时间
    */
        @TableField(value = "create_time",fill = FieldFill.INSERT)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 标签类别
     */
    @TableField(value = "label_type", fill = FieldFill.INSERT)
    private LabelTypeEnum labelType;

}
