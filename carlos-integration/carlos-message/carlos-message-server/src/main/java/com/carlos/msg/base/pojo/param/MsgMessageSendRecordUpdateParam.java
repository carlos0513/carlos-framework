package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Schema(description = "消息发送记录修改参数")
public class MsgMessageSendRecordUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "消息id")
    private Long messageId;
    @Schema(description = "重试次数")
    private Integer retryCount;
    @Schema(description = "发送时间")
    private LocalDateTime sendTime;
    @Schema(description = "原始请求参数")
    private String requestParam;
    @Schema(description = "渠道返回数据")
    private String responseData;
    @Schema(description = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
    @Schema(description = "是否发送成功 0 失败 1 成功")
    private Boolean success;
}
