package com.carlos.msg.base.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class MsgMessageDTO {
    /** 主键 */
    private Long id;
    /** 关联message_template.id */
    private Long templateId;
    /** 系统来源标识 */
    private String sender;
    /** 消息类型 */
    private String messageType;
    /** 标题 */
    private String messageTitle;
    /** 消息内容 */
    private String messageContent;
    /** 消息备注 */
    private String messageRemark;
    /** 系统来源标识 */
    private String sourceBusiness;
    /** 发送人id */
    private String sendUserId;
    /** 发送人名称 */
    private String sendUserName;
    /** 操作反馈类型(无, 详情, 站内跳转, 外链) */
    private String feedbackType;
    /** 操作反馈内容 */
    private String feedbackContent;
    /** 优先级 */
    private Integer priority;
    /** 推送渠道(短信、站内信、钉钉等) */
    private String pushChannel;
    /** 创建人 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
}
