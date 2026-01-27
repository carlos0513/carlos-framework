package com.carlos.mq.support;

import java.util.Map;
import lombok.Data;
import org.apache.rocketmq.client.producer.SendCallback;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.Assert;

/**
 * <p>
 * rocketmq消息
 * </p>
 *
 * @author Carlos
 * @date 2022/1/18 13:46
 */
@Data
public class RocketMqMessage<T> implements Message<T> {

    private static final long serialVersionUID = 4268801052358035098L;


    private final T payload;

    private final MessageHeaders headers;


    /**
     * Create a new message with the given payload.
     *
     * @param payload the message payload (never {@code null})
     */
    public RocketMqMessage(T payload) {
        this(payload, new MessageHeaders(null));
    }

    /**
     * Create a new message with the given payload and headers. The content of the given header map is copied.
     *
     * @param payload the message payload (never {@code null})
     * @param headers message headers to use for initialization
     */
    public RocketMqMessage(T payload, Map<String, Object> headers) {
        this(payload, new MessageHeaders(headers));
    }

    /**
     * A constructor with the {@link MessageHeaders} instance to use.
     * <p><strong>Note:</strong> the given {@code MessageHeaders} instance is used
     * directly in the new message, i.e. it is not copied.
     *
     * @param payload the message payload (never {@code null})
     * @param headers message headers
     */
    public RocketMqMessage(T payload, MessageHeaders headers) {
        Assert.notNull(payload, "Payload must not be null");
        Assert.notNull(headers, "MessageHeaders must not be null");
        this.payload = payload;
        this.headers = headers;
    }

    /**
     * 消息目的地
     */
    private String destination;

    /**
     * 是否是顺序消息 默认非顺序消息
     */
    private boolean order;

    /**
     * 跟踪顺序的值
     */
    private String orderValue;


    /**
     * 发送超时时间
     */
    private long timeout;

    /**
     * 延时级别
     */
    private DelayLevel delayLevel;

    /**
     * 发送方式 默认同步发送
     */
    private SendType sendType;


    /**
     * 异步回调
     */
    private SendCallback callback;

    @Override
    public T getPayload() {
        return this.payload;
    }

    @Override
    public MessageHeaders getHeaders() {
        return this.headers;
    }
}
