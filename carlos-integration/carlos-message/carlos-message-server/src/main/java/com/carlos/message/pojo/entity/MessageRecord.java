package com.carlos.message.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息记录实体
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message_record")
public class MessageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String messageId;

    private String templateCode;

    private String templateParams;

    private String messageType;

    private String messageTitle;

    private String messageContent;

    private String senderId;

    private String senderName;

    private String senderSystem;

    private String feedbackType;

    private String feedbackContent;

    private Integer priority;

    private LocalDateTime validUntil;

    private Integer totalCount;

    private Integer successCount;

    private Integer failCount;

    private String extras;

    @TableLogic
    private Integer isDeleted;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;
}
