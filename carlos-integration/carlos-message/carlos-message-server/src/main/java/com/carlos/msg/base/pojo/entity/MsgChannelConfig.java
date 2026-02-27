package com.carlos.msg.base.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.carlos.message.channel.ChannelConfig;
import com.carlos.msg.api.pojo.enums.ChannelType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息渠道配置 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
@TableName(value = "msg_channel_config", autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgChannelConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 渠道编码
     */
    @TableField(value = "channel_type")
    private ChannelType channelType;
    /**
     * 渠道名称
     */
    @TableField(value = "channel_name")
    private String channelName;
    /**
     * 样例配置信息
     */
    @TableField(value = "channel_config", typeHandler = JacksonTypeHandler.class)
    private ChannelConfig channelConfig;
    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;
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
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by")
    private Long updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}
