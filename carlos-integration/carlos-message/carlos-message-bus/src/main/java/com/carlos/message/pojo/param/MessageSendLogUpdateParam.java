package com.carlos.message.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息发送日志 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息发送日志修改参数")
public class MessageSendLogUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "接收人记录ID")
    private Long receiverId;
    @Schema(description = "渠道编码")
    private String channelCode;
    @Schema(description = "请求参数")
    private String requestParam;
    @Schema(description = "响应数据")
    private String responseData;
    @Schema(description = "是否成功: 0-失败 1-成功")
    private Boolean success;
    @Schema(description = "错误码")
    private String errorCode;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "耗时(ms)")
    private Integer costTime;
}
