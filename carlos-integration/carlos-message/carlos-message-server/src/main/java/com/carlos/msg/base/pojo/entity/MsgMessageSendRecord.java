package com.carlos.msg.base.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息发送记录 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
@TableName("msg_message_send_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageSendRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 消息id
     */
    @TableField(value = "message_id")
    private Long messageId;
    /**
     * 重试次数
     */
    @TableField(value = "retry_count")
    private Integer retryCount;
    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private LocalDateTime sendTime;
    /**
     * 原始请求参数
     */
    @TableField(value = "request_param")
    private String requestParam;
    /**
     * 渠道返回数据
     */
    @TableField(value = "response_data")
    private String responseData;
    /**
     * 推送渠道(短信、站内信、钉钉等)
     */
    @TableField(value = "push_channel")
    private String pushChannel;
    /**
     * 是否发送成功 0 失败 1 成功
     */
    @TableField(value = "is_success")
    private Boolean success;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

}
