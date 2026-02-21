package com.carlos.message.client;

import com.carlos.message.protocol.Message;

/**
 * <p>
 * 抽象的消息发送客户端
 * </p>
 *
 * @author Carlos
 * @date 2022/1/12
 */
public interface MessageSendOperations {
    /**
     * 发送待通知消息
     *
     * @param message 消息
     * @date 2022/1/12 16:02
     */
    void send(Message<?> message);

    /**
     * 进行一些基本赋值操作
     *
     * @param message 待处理消息
     * @return com.soundtao.message.protocol.Message<?>
     * @date 2022/2/9 18:04
     */
    Message<?> beforeSend(Message<?> message);

}
