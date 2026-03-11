package com.carlos.message.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息创建参数
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@Schema(description = "消息创建参数")
public class MessageCreateParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "模板编码不能为空")
    @Schema(description = "模板编码", required = true)
    private String templateCode;

    @Schema(description = "模板参数")
    private Map<String, Object> templateParams;

    @NotEmpty(message = "接收人不能为空")
    @Schema(description = "接收人列表", required = true)
    private List<ReceiverParam> receivers;

    @Schema(description = "渠道编码列表")
    private List<String> channelCodes;

    @NotBlank(message = "发送人ID不能为空")
    @Schema(description = "发送人ID", required = true)
    private String senderId;

    @Schema(description = "发送人名称")
    private String senderName;

    @NotBlank(message = "发送系统标识不能为空")
    @Schema(description = "发送系统标识", required = true)
    private String senderSystem;

    @Schema(description = "优先级: 1-紧急 2-高 3-普通 4-低", defaultValue = "3")
    private Integer priority = 3;

    @Schema(description = "定时发送时间")
    private LocalDateTime scheduleTime;

    @Schema(description = "回调URL")
    private String callbackUrl;

    @Data
    @Schema(description = "接收人参数")
    public static class ReceiverParam implements Serializable {

        private static final long serialVersionUID = 1L;

        @NotBlank(message = "接收者ID不能为空")
        @Schema(description = "接收者ID", required = true)
        private String receiverId;

        @Schema(description = "接收者类型: 1-用户 2-部门 3-角色", defaultValue = "1")
        private Integer receiverType = 1;

        @Schema(description = "接收地址")
        private String receiverNumber;
    }
}
