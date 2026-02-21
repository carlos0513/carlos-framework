package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "关联message_template.id")
    private Long templateId;
    @Schema(value = "系统来源标识")
    private String sender;
    @Schema(value = "消息类型")
    private String messageType;
    @Schema(value = "标题")
    private String messageTitle;
    @Schema(value = "消息内容")
    private String messageContent;
    @Schema(value = "消息备注")
    private String messageRemark;
    @Schema(value = "系统来源标识")
    private String sourceBusiness;
    @Schema(value = "发送人id")
    private String sendUserId;
    @Schema(value = "发送人名称")
    private String sendUserName;
    @Schema(value = "操作反馈类型(无, 详情, 站内跳转, 外链)")
    private String feedbackType;
    @Schema(value = "操作反馈内容")
    private String feedbackContent;
    @Schema(value = "优先级")
    private Integer priority;
    @Schema(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
    @Schema(value = "创建人")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;

}
