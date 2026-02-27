package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息修改参数")
public class MsgMessageUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "关联message_template.id")
    private Long templateId;
    @Schema(description = "系统来源标识")
    private String sender;
    @Schema(description = "消息类型")
    private String messageType;
    @Schema(description = "标题")
    private String messageTitle;
    @Schema(description = "消息内容")
    private String messageContent;
    @Schema(description = "消息备注")
    private String messageRemark;
    @Schema(description = "系统来源标识")
    private String sourceBusiness;
    @Schema(description = "发送人id")
    private Long sendUserId;
    @Schema(description = "发送人名称")
    private String sendUserName;
    @Schema(description = "操作反馈类型(无, 详情, 站内跳转, 外链)")
    private String feedbackType;
    @Schema(description = "操作反馈内容")
    private String feedbackContent;
    @Schema(description = "优先级")
    private Integer priority;
    @Schema(description = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
}
