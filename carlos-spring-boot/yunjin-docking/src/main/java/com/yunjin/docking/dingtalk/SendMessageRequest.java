package com.yunjin.docking.dingtalk;

import com.yunjin.docking.dingtalk.msg.Msg;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *   消息发送参数
 * </p>
 *
 * @author Carlos
 * @date 2025-04-15 00:08
 */
@Data
@Accessors(chain = true)
public class SendMessageRequest {

    /**
     * 推送内容 已过时，使用msg进行替代
     */
    @Deprecated
    private String context;
    /**
     * 推送对象电话
     */
    private List<String> pushPhoneList;
    /**
     * 推送部门id
     */
    private List<String> pushDeptList;

    /** 消息内容 */
    private Msg msg;

}
