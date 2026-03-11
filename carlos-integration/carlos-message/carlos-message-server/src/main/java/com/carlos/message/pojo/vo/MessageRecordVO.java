package com.carlos.message.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息记录VO
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@Schema(description = "消息记录")
public class MessageRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息ID")
    private String messageId;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "消息类型")
    private String messageType;

    @Schema(description = "消息标题")
    private String messageTitle;

    @Schema(description = "消息内容")
    private String messageContent;

    @Schema(description = "发送人ID")
    private String senderId;

    @Schema(description = "发送人名称")
    private String senderName;

    @Schema(description = "优先级: 1-紧急 2-高 3-普通 4-低")
    private Integer priority;

    @Schema(description = "总接收人数")
    private Integer totalCount;

    @Schema(description = "成功发送数")
    private Integer successCount;

    @Schema(description = "失败发送数")
    private Integer failCount;

    @Schema(description = "状态")
    private String statusDesc;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
