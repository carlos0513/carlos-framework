package com.carlos.message.core.queue;

import com.carlos.message.core.protocol.MessageContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 消息队列接口
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
public interface MessageQueue {

    /**
     * 发送消息到队列
     *
     * @param context  消息上下文
     * @param priority 优先级
     */
    void enqueue(MessageContext context, int priority);

    /**
     * 延时发送
     *
     * @param context     消息上下文
     * @param delayMillis 延迟毫秒
     */
    void delay(MessageContext context, long delayMillis);

    /**
     * 定时发送
     *
     * @param context      消息上下文
     * @param scheduleTime 定时时间
     */
    void schedule(MessageContext context, LocalDateTime scheduleTime);

    /**
     * 消费消息
     *
     * @param priority 优先级
     * @param handler  处理器
     */
    void consume(int priority, Consumer<MessageContext> handler);

    /**
     * 批量消费消息
     *
     * @param priority 优先级
     * @param batchSize 批量大小
     * @param handler  处理器
     */
    void consumeBatch(int priority, int batchSize, Consumer<List<MessageContext>> handler);

    /**
     * 获取队列大小
     *
     * @param priority 优先级
     * @return 队列大小
     */
    long getQueueSize(int priority);
}
