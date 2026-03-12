package com.carlos.message.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息记录表 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息记录表修改参数")
public class MessageRecordUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "消息唯一标识")
    private String messageId;
    @Schema(description = "模板编码")
    private String templateCode;
    @Schema(description = "模板参数JSON")
    private String templateParams;
    @Schema(description = "消息类型编码")
    private String messageType;
    @Schema(description = "消息标题")
    private String messageTitle;
    @Schema(description = "消息内容")
    private String messageContent;
    @Schema(description = "发送人ID")
    private String senderId;
    @Schema(description = "发送人名称")
    private String senderName;
    @Schema(description = "发送系统标识")
    private String senderSystem;
    @Schema(description = "操作反馈类型")
    private String feedbackType;
    @Schema(description = "操作反馈内容")
    private String feedbackContent;
    @Schema(description = "优先级: 1-紧急 2-高 3-普通 4-低")
    private Integer priority;
    @Schema(description = "消息有效期")
    private LocalDateTime validUntil;
    @Schema(description = "总接收人数")
    private Integer totalCount;
    @Schema(description = "成功发送数")
    private Integer successCount;
    @Schema(description = "失败发送数")
    private Integer failCount;
    @Schema(description = "扩展信息")
    private String extras;
}
