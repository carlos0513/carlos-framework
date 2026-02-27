package com.carlos.msg.base.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息发送记录 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class MsgMessageSendRecordDTO {
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
