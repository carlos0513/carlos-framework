package com.carlos.mq.core;

import com.carlos.mq.support.DelayLevel;

import java.util.concurrent.TimeUnit;

/**
 * 统一消息队列操作模板接口
 * <p>
 * 提供统一的消息发送和接收操作，屏蔽底层 MQ 实现差异
 * </p>
 *
 * @author Carlos
 * @date 2026/3/14
 */
public interface MqTemplate {

    /* ==================== 普通消息 ==================== */

    /**
     * 发送消息（同步）
     *
     * @param topic   主题/队列名称
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult send(String topic, Object message);

    /**
     * 发送消息（带标签）
     *
     * @param topic   主题
     * @param tag     标签（用于消息过滤）
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult send(String topic, String tag, Object message);

    /**
     * 发送消息（带 Key）
     *
     * @param topic   主题
     * @param tag     标签
     * @param key     消息键（用于查询）
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult send(String topic, String tag, String key, Object message);

    /**
     * 发送消息（异步）
     *
     * @param topic    主题
     * @param message  消息内容
     * @param callback 回调函数
     */
    void sendAsync(String topic, Object message, SendCallback callback);

    /**
     * 发送消息（异步，带标签）
     *
     * @param topic    主题
     * @param tag      标签
     * @param message  消息内容
     * @param callback 回调函数
     */
    void sendAsync(String topic, String tag, Object message, SendCallback callback);

    /**
     * 发送单向消息（不等待响应）
     *
     * @param topic   主题
     * @param message 消息内容
     */
    void sendOneWay(String topic, Object message);

    /**
     * 发送单向消息（带标签）
     *
     * @param topic   主题
     * @param tag     标签
     * @param message 消息内容
     */
    void sendOneWay(String topic, String tag, Object message);

    /* ==================== 延迟消息 ==================== */

    /**
     * 发送延迟消息
     *
     * @param topic      主题
     * @param message    消息内容
     * @param delayLevel 延迟级别
     * @return 发送结果
     */
    SendResult sendDelayed(String topic, Object message, DelayLevel delayLevel);

    /**
     * 发送延迟消息（带标签）
     *
     * @param topic      主题
     * @param tag        标签
     * @param message    消息内容
     * @param delayLevel 延迟级别
     * @return 发送结果
     */
    SendResult sendDelayed(String topic, String tag, Object message, DelayLevel delayLevel);

    /**
     * 发送延迟消息（自定义延迟时间）
     * <p>
     * 注意：不是所有 MQ 都支持自定义延迟时间，不支持时将使用最接近的延迟级别
     * </p>
     *
     * @param topic     主题
     * @param message   消息内容
     * @param delayTime 延迟时间
     * @param timeUnit  时间单位
     * @return 发送结果
     */
    SendResult sendDelayed(String topic, Object message, long delayTime, TimeUnit timeUnit);

    /**
     * 发送延迟消息（带标签，自定义延迟时间）
     *
     * @param topic     主题
     * @param tag       标签
     * @param message   消息内容
     * @param delayTime 延迟时间
     * @param timeUnit  时间单位
     * @return 发送结果
     */
    SendResult sendDelayed(String topic, String tag, Object message, long delayTime, TimeUnit timeUnit);

    /* ==================== 顺序消息 ==================== */

    /**
     * 发送顺序消息
     *
     * @param topic   主题
     * @param message 消息内容
     * @param hashKey 分片键（用于保证顺序）
     * @return 发送结果
     */
    SendResult sendOrdered(String topic, Object message, String hashKey);

    /**
     * 发送顺序消息（带标签）
     *
     * @param topic   主题
     * @param tag     标签
     * @param message 消息内容
     * @param hashKey 分片键
     * @return 发送结果
     */
    SendResult sendOrdered(String topic, String tag, Object message, String hashKey);

    /* ==================== 事务消息 ==================== */

    /**
     * 发送事务消息
     *
     * @param topic   主题
     * @param message 消息内容
     * @param arg     事务参数
     * @return 发送结果
     */
    SendResult sendTransaction(String topic, Object message, Object arg);

    /**
     * 发送事务消息（带标签）
     *
     * @param topic   主题
     * @param tag     标签
     * @param message 消息内容
     * @param arg     事务参数
     * @return 发送结果
     */
    SendResult sendTransaction(String topic, String tag, Object message, Object arg);

    /* ==================== 工具方法 ==================== */

    /**
     * 检查 MQ 连接状态
     *
     * @return 是否可用
     */
    boolean ping();

    /**
     * 获取当前使用的 MQ 类型
     *
     * @return MQ 类型
     */
    String getMqType();
}
