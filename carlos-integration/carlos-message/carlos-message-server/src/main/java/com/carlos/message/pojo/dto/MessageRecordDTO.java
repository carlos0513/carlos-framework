package com.carlos.message.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 消息记录DTO
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
public class MessageRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String templateCode;

    private String messageType;

    private String messageTitle;

    private String messageContent;

    private String senderId;

    private String senderName;

    private String senderSystem;

    private Integer priority;

    private Integer totalCount;

    private Integer successCount;

    private Integer failCount;

    private Integer status;

    private LocalDateTime createTime;

    private Map<String, Object> extras;
}
