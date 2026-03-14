package com.carlos.mq.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息发送结果
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息标签
     */
    private String tag;

    /**
     * 消息键
     */
    private String key;

    /**
     * 发送时间戳
     */
    private Long sendTime;

    /**
     * 发送耗时（毫秒）
     */
    private Long sendCostTime;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 异常信息（发送失败时）
     */
    private Throwable exception;

    /**
     * 事务ID（事务消息时）
     */
    private String transactionId;

    /**
     * 队列ID（顺序消息时）
     */
    private Integer queueId;

    /**
     * 队列偏移量
     */
    private Long queueOffset;

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 创建成功结果
     *
     * @param messageId 消息ID
     * @return SendResult
     */
    public static SendResult success(String messageId) {
        return SendResult.builder()
            .success(true)
            .messageId(messageId)
            .sendTime(System.currentTimeMillis())
            .code("SUCCESS")
            .message("Send message success")
            .build();
    }

    /**
     * 创建失败结果
     *
     * @param messageId 消息ID
     * @param code      错误码
     * @param message   错误消息
     * @return SendResult
     */
    public static SendResult failure(String messageId, String code, String message) {
        return SendResult.builder()
            .success(false)
            .messageId(messageId)
            .sendTime(System.currentTimeMillis())
            .code(code)
            .message(message)
            .build();
    }

    /**
     * 创建失败结果
     *
     * @param messageId 消息ID
     * @param code      错误码
     * @param message   错误消息
     * @param exception 异常
     * @return SendResult
     */
    public static SendResult failure(String messageId, String code, String message, Throwable exception) {
        return SendResult.builder()
            .success(false)
            .messageId(messageId)
            .sendTime(System.currentTimeMillis())
            .code(code)
            .message(message)
            .exception(exception)
            .build();
    }
}
