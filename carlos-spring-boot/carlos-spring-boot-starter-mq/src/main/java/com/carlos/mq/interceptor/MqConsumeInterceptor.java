package com.carlos.mq.interceptor;

import com.carlos.mq.core.ConsumeResult;
import com.carlos.mq.core.MqMessage;

/**
 * MQ 消息消费拦截器接口
 * <p>
 * 用于在消息消费前后进行拦截处理，如日志记录、监控统计、消息解密等
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
public interface MqConsumeInterceptor {

    /**
     * 消费前拦截
     * <p>
     * 在消息消费前执行，可用于：
     * - 消息解密
     * - 消息校验
     * - 添加追踪信息
     * - 日志记录
     * </p>
     *
     * @param message 消息
     * @return true 继续消费，false 跳过消费
     */
    default boolean beforeConsume(MqMessage<?> message) {
        return true;
    }

    /**
     * 消费后拦截
     * <p>
     * 在消息消费后执行，可用于：
     * - 记录消费结果
     * - 监控统计
     * - 异常处理
     * </p>
     *
     * @param message      消息
     * @param consumeResult 消费结果
     */
    default void afterConsume(MqMessage<?> message, ConsumeResult consumeResult) {
    }

    /**
     * 消费异常拦截
     * <p>
     * 在消息消费异常时执行
     * </p>
     *
     * @param message   消息
     * @param exception 异常
     */
    default void onConsumeException(MqMessage<?> message, Throwable exception) {
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
