package com.carlos.msg.base.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息接受者 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消息接受者列表查询参数", description = "消息接受者列表查询参数")
public class MsgMessageReceiverPageParam extends ParamPage {
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
    @ApiModelProperty("开始时间")
    private LocalDateTime start;

    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
