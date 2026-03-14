package com.carlos.mq.core;

/**
 * 消息发送回调接口
 *
 * @author Carlos
 * @date 2026/3/14
 */
public interface SendCallback {

    /**
     * 发送成功回调
     *
     * @param sendResult 发送结果
     */
    void onSuccess(SendResult sendResult);

    /**
     * 发送失败回调
     *
     * @param e 异常
     */
    void onException(Throwable e);
}
