package com.carlos.msg.base.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
@TableName("msg_message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private Long id;
    /**
     * 关联message_template.id
     */
    @TableField(value = "template_id")
    private Long templateId;
    /**
     * 系统来源标识
     */
    @TableField(value = "sender")
    private String sender;
    /**
     * 消息类型
     */
    @TableField(value = "message_type")
    private String messageType;
    /**
     * 标题
     */
    @TableField(value = "message_title")
    private String messageTitle;
    /**
     * 消息内容
     */
    @TableField(value = "message_content")
    private String messageContent;
    /**
     * 消息备注
     */
    @TableField(value = "message_remark")
    private String messageRemark;
    /**
     * 系统来源标识
     */
    @TableField(value = "source_business")
    private String sourceBusiness;
    /**
     * 发送人id
     */
    @TableField(value = "send_user_id")
    private String sendUserId;
    /**
     * 发送人名称
     */
    @TableField(value = "send_user_name")
    private String sendUserName;
    /**
     * 操作反馈类型(无, 详情, 站内跳转, 外链)
     */
    @TableField(value = "feedback_type")
    private String feedbackType;
    /**
     * 操作反馈内容
     */
    @TableField(value = "feedback_content")
    private String feedbackContent;
    /**
     * 优先级
     */
    @TableField(value = "priority")
    private Integer priority;
    /**
     * 推送渠道(短信、站内信、钉钉等)
     */
    @TableField(value = "push_channel")
    private String pushChannel;
    /**
     * 是否发送成功
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Boolean delete;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

}
