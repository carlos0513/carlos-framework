package com.carlos.mq.core;

import com.carlos.mq.support.LocalTransactionState;

/**
 * MQ 事务消息监听器接口
 * <p>
 * 用于处理事务消息的本地事务执行和回查
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
public interface MqTransactionListener {

    /**
     * 执行本地事务
     * <p>
     * 在发送半消息后执行本地事务
     * </p>
     *
     * @param message 事务消息
     * @param arg     事务参数
     * @return 本地事务状态
     */
    LocalTransactionState executeLocalTransaction(MqMessage<?> message, Object arg);

    /**
     * 回查本地事务状态
     * <p>
     * 当 Broker 长时间未收到本地事务结果时，会触发回查
     * </p>
     *
     * @param message 事务消息
     * @return 本地事务状态
     */
    LocalTransactionState checkLocalTransaction(MqMessage<?> message);
}
