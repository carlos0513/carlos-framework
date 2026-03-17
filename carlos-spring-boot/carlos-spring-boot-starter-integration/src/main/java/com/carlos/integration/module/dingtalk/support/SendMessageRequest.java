package com.carlos.integration.module.dingtalk.support;

import com.carlos.integration.module.dingtalk.support.msg.Msg;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *   消息发送参�?
 * </p>
 *
 * @author Carlos
 * @date 2025-04-15 00:08
 */
@Data
@Accessors(chain = true)
public class SendMessageRequest {

    /**
     * 推送内�?已过时，使用msg进行替代
     */
    @Deprecated
    private String context;
    /**
     * 推送对象电�?
     */
    private List<String> pushPhoneList;
    /**
     * 推送部门id
     */
    private List<String> pushDeptList;

    /** 消息内容 */
    private Msg msg;

}
