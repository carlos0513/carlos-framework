package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Schema(description = "消息接受者修改参数")
public class MsgMessageReceiverUpdateParam {
    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "消息id")
    private Long messageId;
    @Schema(description = "接收者id")
    private Long receiverId;
    @Schema(description = "接收者号码 钉钉号 手机号码")
    private String receiverNumber;
    @Schema(description = "接收者设备")
    private String receiverAudience;
    @Schema(description = "是否已读")
    private Boolean read;
    @Schema(description = "是否发送成功")
    private Boolean success;
}
