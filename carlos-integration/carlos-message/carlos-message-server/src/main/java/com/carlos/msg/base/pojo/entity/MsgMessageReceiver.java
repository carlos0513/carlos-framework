package com.carlos.msg.base.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息接受者 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
@TableName("msg_message_receiver")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageReceiver implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 消息id
     */
    @TableField(value = "message_id")
    private Long messageId;
    /**
     * 接收者id
     */
    @TableField(value = "receiver_id")
    private String receiverId;
    /**
     * 接收者号码 钉钉号 手机号码
     */
    @TableField(value = "receiver_number")
    private String receiverNumber;
    /**
     * 接收者设备
     */
    @TableField(value = "receiver_audience")
    private String receiverAudience;
    /**
     * 是否已读
     */
    @TableField(value = "is_read")
    private Boolean read;
    /**
     * 是否发送成功
     */
    @TableField(value = "is_success")
    private Boolean success;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

}
