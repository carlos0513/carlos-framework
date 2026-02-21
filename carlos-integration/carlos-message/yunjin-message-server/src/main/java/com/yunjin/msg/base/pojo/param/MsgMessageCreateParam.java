package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * <p>
 * 消息 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(value = "消息新增参数", description = "消息新增参数")
public class MsgMessageCreateParam {
    @NotNull(message = "关联message_template.id不能为空")
    @Schema(value = "关联message_template.id")
    private Long templateId;
    @NotBlank(message = "系统来源标识不能为空")
    @Schema(value = "系统来源标识")
    private String sender;
    @NotBlank(message = "消息类型不能为空")
    @Schema(value = "消息类型")
    private String messageType;
    @NotBlank(message = "标题不能为空")
    @Schema(value = "标题")
    private String messageTitle;
    @NotBlank(message = "消息内容不能为空")
    @Schema(value = "消息内容")
    private String messageContent;
    @NotBlank(message = "消息备注不能为空")
    @Schema(value = "消息备注")
    private String messageRemark;
    @NotBlank(message = "系统来源标识不能为空")
    @Schema(value = "系统来源标识")
    private String sourceBusiness;
    @NotBlank(message = "发送人id不能为空")
    @Schema(value = "发送人id")
    private String sendUserId;
    @NotBlank(message = "发送人名称不能为空")
    @Schema(value = "发送人名称")
    private String sendUserName;
    @NotBlank(message = "操作反馈类型(无, 详情, 站内跳转, 外链)不能为空")
    @Schema(value = "操作反馈类型(无, 详情, 站内跳转, 外链)")
    private String feedbackType;
    @NotBlank(message = "操作反馈内容不能为空")
    @Schema(value = "操作反馈内容")
    private String feedbackContent;
    @NotNull(message = "优先级不能为空")
    @Schema(value = "优先级")
    private Integer priority;
    @NotBlank(message = "推送渠道(短信、站内信、钉钉等)不能为空")
    @Schema(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
}
