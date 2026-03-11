package com.carlos.message.service;

import com.carlos.core.response.Result;
import com.carlos.message.pojo.param.MessageSendParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 消息发送服务
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
public interface MessageSendService {

    /**
     * 同步发送消息
     *
     * @param param 发送参数
     * @return 发送结果
     */
    Result<String> send(MessageSendParam param);

    /**
     * 异步发送消息
     *
     * @param param 发送参数
     * @return 消息ID
     */
    Result<String> sendAsync(MessageSendParam param);

    /**
     * 批量发送消息
     *
     * @param params 发送参数列表
     * @return 批量发送结果
     */
    Result<String> sendBatch(List<MessageSendParam> params);

    /**
     * 定时发送消息
     *
     * @param param        发送参数
     * @param scheduleTime 定时时间
     * @return 消息ID
     */
    Result<String> schedule(MessageSendParam param, LocalDateTime scheduleTime);

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @return 是否成功
     */
    Result<Boolean> revoke(String messageId);
}
