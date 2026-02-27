package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息接受者 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageReceiverVO implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @Schema(description = "创建人")
    private Long createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
