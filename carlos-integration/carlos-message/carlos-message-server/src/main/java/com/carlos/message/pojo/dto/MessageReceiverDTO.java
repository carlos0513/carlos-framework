package com.carlos.message.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息接收人DTO
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
public class MessageReceiverDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String channelCode;

    private String receiverId;

    private Integer receiverType;

    private String receiverNumber;

    private Integer status;

    private Integer failCount;

    private String failReason;

    private LocalDateTime sendTime;

    private LocalDateTime deliverTime;

    private LocalDateTime readTime;

    private LocalDateTime createTime;
}
