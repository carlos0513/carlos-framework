package com.carlos.message.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息发送日志 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息发送日志列表查询参数")
public class MessageSendLogPageParam extends ParamPage {
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
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
