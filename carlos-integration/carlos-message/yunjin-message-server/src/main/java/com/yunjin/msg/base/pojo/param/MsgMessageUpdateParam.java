package com.carlos.msg.base.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

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
@ApiModel(value = "消息修改参数", description = "消息修改参数")
public class MsgMessageUpdateParam {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "关联message_template.id")
    private Long templateId;
    @ApiModelProperty(value = "系统来源标识")
    private String sender;
    @ApiModelProperty(value = "消息类型")
    private String messageType;
    @ApiModelProperty(value = "标题")
    private String messageTitle;
    @ApiModelProperty(value = "消息内容")
    private String messageContent;
    @ApiModelProperty(value = "消息备注")
    private String messageRemark;
    @ApiModelProperty(value = "系统来源标识")
    private String sourceBusiness;
    @ApiModelProperty(value = "发送人id")
    private String sendUserId;
    @ApiModelProperty(value = "发送人名称")
    private String sendUserName;
    @ApiModelProperty(value = "操作反馈类型(无, 详情, 站内跳转, 外链)")
    private String feedbackType;
    @ApiModelProperty(value = "操作反馈内容")
    private String feedbackContent;
    @ApiModelProperty(value = "优先级")
    private Integer priority;
    @ApiModelProperty(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
}
