package com.carlos.msg.base.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息类型 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@TableName("msg_message_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageType implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private Long id;
    /**
     * 类型编码
     */
    @TableField(value = "type_code")
    private String typeCode;
    /**
     * 类型名称
     */
    @TableField(value = "type_name")
    private String typeName;
    /**
     * 是否启用
     */
    @TableField(value = "is_enabled")
    private Boolean enabled;
    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建者编号
     */
    @TableField(value = "create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}
