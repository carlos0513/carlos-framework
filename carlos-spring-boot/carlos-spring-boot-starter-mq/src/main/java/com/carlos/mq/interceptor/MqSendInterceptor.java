package com.carlos.mq.interceptor;

import com.carlos.mq.core.MqMessage;
import com.carlos.mq.core.SendResult;

/**
 * MQ 消息发送拦截器接口
 * <p>
 * 用于在消息发送前后进行拦截处理，如日志记录、监控统计、消息加密等
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
public interface MqSendInterceptor {

    /**
     * 发送前拦截
     * <p>
     * 在消息发送前执行，可用于：
     * - 消息校验
     * - 消息加密
     * - 添加追踪信息
     * - 日志记录
     * </p>
     *
     * @param message 消息
     * @return true 继续发送，false 中断发送
     */
    default boolean beforeSend(MqMessage<?> message) {
        return true;
    }

    /**
     * 发送后拦截
     * <p>
     * 在消息发送后执行，可用于：
     * - 记录发送结果
     * - 监控统计
     * - 异常处理
     * </p>
     *
     * @param message    消息
     * @param sendResult 发送结果
     */
    default void afterSend(MqMessage<?> message, SendResult sendResult) {
    }

    /**
     * 发送异常拦截
     * <p>
     * 在消息发送异常时执行
     * </p>
     *
     * @param message   消息
     * @param exception 异常
     */
    default void onSendException(MqMessage<?> message, Throwable exception) {
    }

    /**
     * 拦截器顺序（值越小优先级越高）
     *
     * @return 顺序值
     */
    default int getOrder() {
        return 0;
    }
}
