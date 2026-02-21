package com.carlos.message.client;

import com.carlos.message.protocol.Message;

import java.time.LocalDateTime;

/**
 * <p>
 * 抽象消息模板，提供默认实现
 * </p>
 *
 * @author Carlos
 * @date 2022/1/12
 */
public abstract class AbstractMessageTemplate implements com.carlos.message.client.MessageSendOperations {

    @Override
    public void send(Message<?> message) {
//        switch (message.getType()) {
//            case PREWARN_MSG:
//                if (message.getBody().getClass() != ForewarnMsg.class) {
//                    throw new MessageClientException("消息类型与消息体不匹配！消息类型为：" + message.getType().getDesc() + "，消息体类型为：" + message.getClass().getName());
//                }
//                // 发送消息(OpenFeign)
//                feignMsg.send(message);
//                break;
//            case SYS_MSG:
//                if (message.getBody().getClass() != SysMsg.class) {
//                    throw new MessageClientException("消息类型与消息体不匹配！消息类型为：" + message.getType().getDesc() + "，消息体类型为：" + message.getClass().getName());
//                }
//                // 发送消息(OpenFeign)
//                feignMsg.send(message);
//            default:
//                throw new MessageClientException("不存在的消息类型，消息类型为：" + message.getType());
//        }
    }

    @Override
    public Message<?> beforeSend(Message<?> message) {
        // 设置时间
        message.setTime(LocalDateTime.now());
        return message;
    }
}
