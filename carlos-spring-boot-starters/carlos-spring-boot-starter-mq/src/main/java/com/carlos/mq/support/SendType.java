package com.carlos.mq.support;

/**
 * <p>
 * Rocketmq消息发送方式
 * </p>
 *
 * @author Carlos
 * @date 2022/1/18 9:56
 */
public enum SendType {
    /**
     * 同步发送
     */
    SYNC,
    /**
     * 异步发送
     */
    ASYNC,
    /**
     * 单向发送
     */
    ONEWAY

}
