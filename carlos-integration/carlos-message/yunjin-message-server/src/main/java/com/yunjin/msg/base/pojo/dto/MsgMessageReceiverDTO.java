package com.carlos.msg.base.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息接受者 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class MsgMessageReceiverDTO {
    /** 主键 */
    private Long id;
    /** 消息id */
    private Long messageId;
    /** 接收者id */
    private String receiverId;
    /** 接收者号码 钉钉号 手机号码 */
    private String receiverNumber;
    /** 接收者设备 */
    private String receiverAudience;
    /** 是否已读 */
    private Boolean read;
    /** 是否发送成功 */
    private Boolean success;
    /** 创建人 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
}
