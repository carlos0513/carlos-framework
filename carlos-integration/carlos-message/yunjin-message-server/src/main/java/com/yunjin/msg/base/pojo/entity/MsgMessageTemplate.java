package com.carlos.msg.base.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息模板 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
@TableName("msg_message_template")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private Long id;
    /**
     * 消息类型
     */
    @TableField(value = "type_id")
    private Long typeId;
    /**
     * 模板编码
     */
    @TableField(value = "template_code")
    private String templateCode;
    /**
     * 模板内容(含变量占位符)
     */
    @TableField(value = "template_content")
    private String templateContent;
    /**
     * 渠道特殊配置(如短信模板ID),配置对应渠道编码
     */
    @TableField(value = "channel_config")
    private String channelConfig;
    /**
     * 是否启用
     */
    @TableField(value = "is_active")
    private Boolean active;
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
