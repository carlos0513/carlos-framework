package com.carlos.mq.support;

import cn.hutool.core.util.StrUtil;
import com.carlos.mq.exception.MessageException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * RocketMq消息构建工具 {@link RocketMqMessage}
 * </p>
 *
 * @author Carlos
 * @date 2022/1/18 14:00
 */
public final class RocketMqMessageBuilder<T> {

    public static final String TAG_SEPARATE = "||";

    private final T payload;

    private MessageHeaders headers = new MessageHeaders(null);
    private String topic;
    private boolean order = false;
    private String orderValue;
    private long timeout = 3000L;
    private DelayLevel delayLevel = DelayLevel.NONE;
    private Set<String> tags;
    private SendType sendType = SendType.SYNC;
    private SendCallback callback;

    private RocketMqMessageBuilder(T payload) {
        this.payload = payload;
    }

    public static <T> RocketMqMessageBuilder<T> withPayload(final T payload) {
        return new RocketMqMessageBuilder<>(payload);
    }

    public RocketMqMessageBuilder<T> headers(final MessageHeaders headers) {
        this.headers = headers;
        return this;
    }

    public RocketMqMessageBuilder<T> headers(final Map<String, Object> headers) {
        if (headers != null) {
            this.headers = new MessageHeaders(headers);
        }
        return this;
    }

    public RocketMqMessageBuilder<T> topic(final String topic) {
        this.topic = topic;
        return this;
    }

    public RocketMqMessageBuilder<T> order(String orderValue) {
        this.order = true;
        this.orderValue = orderValue;
        return this;
    }


    public RocketMqMessageBuilder<T> timeout(final long timeout) {
        this.timeout = timeout;
        return this;
    }

    public RocketMqMessageBuilder<T> delayLevel(final DelayLevel delayLevel) {
        this.delayLevel = delayLevel;
        return this;
    }

    public RocketMqMessageBuilder<T> tags(final Set<String> tags) {
        this.tags = tags;
        return this;
    }

    public RocketMqMessageBuilder<T> sendType(final SendType sendType) {
        this.sendType = sendType;
        return this;
    }

    public RocketMqMessageBuilder<T> callback(final SendCallback callback) {
        this.callback = callback;
        return this;
    }

    public RocketMqMessage<T> build() {
        if (headers == null) {
            headers = new MessageHeaders(null);
        }

        RocketMqMessage<T> message = new RocketMqMessage<>(this.payload, headers);

        /**
         * 发送目的地 topic:tag||tag||....
         */
        String destination = this.topic;

        if (this.tags != null) {
            destination += StrUtil.COLON + StrUtil.join(TAG_SEPARATE, this.tags);
        }
        message.setDestination(destination);

        if (this.order) {
            String orderValue = this.orderValue;
            if (StrUtil.isBlank(orderValue)) {
                throw new MessageException("When 'order' is true, 'orderValue' can't be null or ''.");
            }
            message.setOrder(this.order);
            message.setOrderValue(this.orderValue);
        }

        message.setTimeout(this.timeout);
        message.setDelayLevel(this.delayLevel);
        message.setSendType(this.sendType);
        message.setCallback(this.callback);

        return message;
    }


}
