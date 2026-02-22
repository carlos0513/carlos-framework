package com.carlos.message.protocol;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统消息内容
 * </p>
 *
 * @author Carlos
 * @date 2023/3/13 10:52
 */
@Data
@Accessors(chain = true)
public class SystemMessageContent implements com.carlos.message.protocol.MessageContent {

    private static final long serialVersionUID = 1L;
    /**
     * 通知的内容
     */
    private String content;
    /**
     * 布控任务id
     */
    private Long taskId;
    /**
     * 中标结果id
     */
    private Long resultId;
}
