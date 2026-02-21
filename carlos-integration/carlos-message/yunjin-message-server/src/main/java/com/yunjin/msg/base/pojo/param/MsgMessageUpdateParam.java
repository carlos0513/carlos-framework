package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

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
@Schema(value = "消息修改参数", description = "消息修改参数")
public class MsgMessageUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "关联message_template.id")
    private Long templateId;
    @Schema(value = "系统来源标识")
    private String sender;
    @Schema(value = "消息类型")
    private String messageType;
    @Schema(value = "标题")
    private String messageTitle;
    @Schema(value = "消息内容")
    private String messageContent;
    @Schema(value = "消息备注")
    private String messageRemark;
    @Schema(value = "系统来源标识")
    private String sourceBusiness;
    @Schema(value = "发送人id")
    private String sendUserId;
    @Schema(value = "发送人名称")
    private String sendUserName;
    @Schema(value = "操作反馈类型(无, 详情, 站内跳转, 外链)")
    private String feedbackType;
    @Schema(value = "操作反馈内容")
    private String feedbackContent;
    @Schema(value = "优先级")
    private Integer priority;
    @Schema(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
}
