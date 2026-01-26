package com.yunjin.mq.utils;

import com.yunjin.mq.support.RocketMqMessage;
import com.yunjin.mq.support.RocketMqMessageBuilder;
import com.yunjin.mq.support.SendType;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Rocketmq工具类
 * </p>
 *
 * @author Carlos
 * @date 2022/1/17 17:38
 */
@Slf4j
@Component
public class RocketMqUtil {


    private static RocketMQTemplate template;

    public RocketMqUtil(RocketMQTemplate rocketMQTemplate) {
        RocketMqUtil.template = rocketMQTemplate;
    }

    /**
     * 发送mq消息
     *
     * @param builder 消息构建器
     * @return org.apache.rocketmq.client.producer.SendResult
     * @author Carlos
     * @date 2022/1/18 14:28
     */
    public static SendResult send(RocketMqMessageBuilder<?> builder) {
        RocketMqMessage<?> message = builder.build();
        Message<?> payload = MessageBuilder.withPayload(message.getPayload()).build();
        String destination = message.getDestination();
        SendType sendType = message.getSendType();
        long timeout = message.getTimeout();
        int delayLevel = message.getDelayLevel().getLevel();

        boolean order = message.isOrder();
        String hasKey = message.getOrderValue();
        switch (sendType) {
            case SYNC:
                SendResult result;
                if (order) {
                    result = template.syncSendOrderly(destination, message.getPayload(), hasKey);
                } else {
                    result = template.syncSend(destination, payload, timeout, delayLevel);
                }
                return result;
            case ASYNC:
                SendCallback callback = message.getCallback();

                if (order) {
                    template.asyncSendOrderly(destination, message.getPayload(), hasKey, callback);
                } else {
                    template.asyncSend(destination, payload, callback, timeout);
                }
                break;
            case ONEWAY:
                if (order) {
                    template.sendOneWayOrderly(destination, payload, hasKey);
                } else {
                    template.sendOneWay(destination, message.getPayload());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sendType);
        }


        return null;
    }
}
