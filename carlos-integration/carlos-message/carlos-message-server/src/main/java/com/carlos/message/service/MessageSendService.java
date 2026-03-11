package com.carlos.message.service;

import com.carlos.core.response.Result;
import com.carlos.message.pojo.param.MessageCreateParam;

import java.time.LocalDateTime;

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
    Result<String> send(MessageCreateParam param);

    /**
     * 异步发送消息
     *
     * @param param 发送参数
     * @return 消息ID
     */
    Result<String> sendAsync(MessageCreateParam param);

    /**
     * 定时发送消息
     *
     * @param param        发送参数
     * @param scheduleTime 定时时间
     * @return 消息ID
     */
    Result<String> schedule(MessageCreateParam param, LocalDateTime scheduleTime);

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @return 是否成功
     */
    Result<Boolean> revoke(String messageId);
}
