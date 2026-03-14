package com.carlos.mq.core;

import com.carlos.mq.support.DelayLevel;

import java.util.concurrent.TimeUnit;

/**
 * MQ 客户端抽象接口
 * <p>
 * 底层 MQ 操作封装，由具体 MQ 实现类实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
public interface MqClient {

    /**
     * 初始化客户端
     */
    void init();

    /**
     * 关闭客户端
     */
    void shutdown();

    /**
     * 发送消息（同步）
     *
     * @param message 消息
     * @return 发送结果
     */
    SendResult send(MqMessage<?> message);

    /**
     * 发送消息（异步）
     *
     * @param message  消息
     * @param callback 回调
     */
    void sendAsync(MqMessage<?> message, SendCallback callback);

    /**
     * 发送单向消息
     *
     * @param message 消息
     */
    void sendOneWay(MqMessage<?> message);

    /**
     * 发送延迟消息
     *
     * @param message    消息
     * @param delayLevel 延迟级别
     * @return 发送结果
     */
    SendResult sendDelayed(MqMessage<?> message, DelayLevel delayLevel);

    /**
     * 发送延迟消息（自定义时间）
     *
     * @param message   消息
     * @param delayTime 延迟时间
     * @param timeUnit  时间单位
     * @return 发送结果
     */
    SendResult sendDelayed(MqMessage<?> message, long delayTime, TimeUnit timeUnit);

    /**
     * 发送顺序消息
     *
     * @param message 消息
     * @param hashKey 分片键
     * @return 发送结果
     */
    SendResult sendOrdered(MqMessage<?> message, String hashKey);

    /**
     * 发送事务消息
     *
     * @param message 消息
     * @param arg     事务参数
     * @return 发送结果
     */
    SendResult sendTransaction(MqMessage<?> message, Object arg);

    /**
     * 订阅消息
     *
     * @param topic    主题
     * @param group    消费组
     * @param listener 监听器
     */
    void subscribe(String topic, String group, MqMessageListener listener);

    /**
     * 订阅消息（带标签过滤）
     *
     * @param topic    主题
     * @param tag      标签表达式
     * @param group    消费组
     * @param listener 监听器
     */
    void subscribe(String topic, String tag, String group, MqMessageListener listener);

    /**
     * 取消订阅
     *
     * @param topic 主题
     * @param group 消费组
     */
    void unsubscribe(String topic, String group);

    /**
     * 检查连接状态
     *
     * @return 是否可用
     */
    boolean isConnected();

    /**
     * 获取客户端类型
     *
     * @return MQ 类型
     */
    String getClientType();
}
