package com.carlos.msg.base.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 消息接受者 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息接受者修改参数", description = "消息接受者修改参数")
public class MsgMessageReceiverUpdateParam {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "消息id")
    private Long messageId;
    @ApiModelProperty(value = "接收者id")
    private String receiverId;
    @ApiModelProperty(value = "接收者号码 钉钉号 手机号码")
    private String receiverNumber;
    @ApiModelProperty(value = "接收者设备")
    private String receiverAudience;
    @ApiModelProperty(value = "是否已读")
    private Boolean read;
    @ApiModelProperty(value = "是否发送成功")
    private Boolean success;
}
