package com.carlos.msg.api.pojo.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息发送记录 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class ApiMsgMessageSendRecordParam implements Serializable {
    /** 主键 */
    private Long id;
    /** 消息id */
    private Long messageId;
    /** 重试次数 */
    private Integer retryCount;
    /** 发送时间 */
    private LocalDateTime sendTime;
    /** 原始请求参数 */
    private String requestParam;
    /** 渠道返回数据 */
    private String responseData;
    /** 推送渠道(短信、站内信、钉钉等) */
    private String pushChannel;
    /** 是否发送成功 0 失败 1 成功 */
    private Boolean success;
    /** 创建人 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
}
