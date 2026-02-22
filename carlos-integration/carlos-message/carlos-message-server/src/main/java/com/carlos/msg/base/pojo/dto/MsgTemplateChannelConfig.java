package com.carlos.msg.base.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *   消息模板渠道配置
 * </p>
 *
 * @author Carlos
 * @date 2025-03-10 16:43
 */
@Data
@Accessors(chain = true)
public class MsgTemplateChannelConfig {

    /** 关联message_template.id */
    private Long templateId;
    /** 系统来源标识 */
    private String sender;
    /** 消息类型 */
    private String messageType;
    /** 标题 */
    private String messageTitle;

}
