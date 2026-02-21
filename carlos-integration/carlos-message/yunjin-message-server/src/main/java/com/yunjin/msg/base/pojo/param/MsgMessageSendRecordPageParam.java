package com.carlos.msg.base.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息发送记录 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "消息发送记录列表查询参数", description = "消息发送记录列表查询参数")
public class MsgMessageSendRecordPageParam extends ParamPage {
    @Schema(value = "消息id")
    private Long messageId;
    @Schema(value = "重试次数")
    private Integer retryCount;
    @Schema(value = "发送时间")
    private LocalDateTime sendTime;
    @Schema(value = "原始请求参数")
    private String requestParam;
    @Schema(value = "渠道返回数据")
    private String responseData;
    @Schema(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
    @Schema(value = "是否发送成功 0 失败 1 成功")
    private Boolean success;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
