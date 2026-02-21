package com.carlos.msg.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 消息发送响应结果
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
public class MsgSendResponseAO implements Serializable {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 异常信息
     */
    private String errorMsg;

}
