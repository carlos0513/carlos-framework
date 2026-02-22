package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 消息接受者 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息接受者新增参数")
public class MsgMessageReceiverCreateParam {
    @NotNull(message = "消息id不能为空")
    @Schema(description = "消息id")
    private Long messageId;
    @NotBlank(message = "接收者id不能为空")
    @Schema(description = "接收者id")
    private String receiverId;
    @NotBlank(message = "接收者号码 钉钉号 手机号码不能为空")
    @Schema(description = "接收者号码 钉钉号 手机号码")
    private String receiverNumber;
    @NotBlank(message = "接收者设备不能为空")
    @Schema(description = "接收者设备")
    private String receiverAudience;
    @NotNull(message = "是否已读不能为空")
    @Schema(description = "是否已读")
    private Boolean read;
    @NotNull(message = "是否发送成功不能为空")
    @Schema(description = "是否发送成功")
    private Boolean success;
}
