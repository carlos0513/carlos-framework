package com.carlos.message.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息接收人实体
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message_receiver")
public class MessageReceiver implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String messageId;

    private String channelCode;

    private String receiverId;

    private Integer receiverType;

    private String receiverNumber;

    private String receiverAudience;

    private String channelMessageId;

    private Integer status;

    private Integer failCount;

    private String failReason;

    private LocalDateTime scheduleTime;

    private LocalDateTime sendTime;

    private LocalDateTime deliverTime;

    private LocalDateTime readTime;

    private String callbackUrl;

    private String extras;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
