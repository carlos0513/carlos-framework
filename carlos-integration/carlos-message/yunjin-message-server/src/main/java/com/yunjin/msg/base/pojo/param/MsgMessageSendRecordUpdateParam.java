package com.carlos.msg.base.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息发送记录 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息发送记录修改参数", description = "消息发送记录修改参数")
public class MsgMessageSendRecordUpdateParam {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "消息id")
    private Long messageId;
    @ApiModelProperty(value = "重试次数")
    private Integer retryCount;
    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;
    @ApiModelProperty(value = "原始请求参数")
    private String requestParam;
    @ApiModelProperty(value = "渠道返回数据")
    private String responseData;
    @ApiModelProperty(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
    @ApiModelProperty(value = "是否发送成功 0 失败 1 成功")
    private Boolean success;
}
