package com.carlos.msg.base.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息列表查询参数")
public class MsgMessagePageParam extends ParamPage {
    @Schema(description = "关联message_template.id")
    private Long templateId;
    @Schema(description = "系统来源标识")
    private String sender;
    @Schema(description = "消息类型")
    private String messageType;
    @Schema(description = "标题")
    private String messageTitle;
    @Schema(description = "消息内容")
    private String messageContent;
    @Schema(description = "消息备注")
    private String messageRemark;
    @Schema(description = "系统来源标识")
    private String sourceBusiness;
    @Schema(description = "发送人id")
    private Long sendUserId;
    @Schema(description = "发送人名称")
    private String sendUserName;
    @Schema(description = "操作反馈类型(无, 详情, 站内跳转, 外链)")
    private String feedbackType;
    @Schema(description = "操作反馈内容")
    private String feedbackContent;
    @Schema(description = "优先级")
    private Integer priority;
    @Schema(description = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
