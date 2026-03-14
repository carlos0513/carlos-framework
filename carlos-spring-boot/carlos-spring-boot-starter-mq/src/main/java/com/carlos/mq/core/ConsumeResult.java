package com.carlos.mq.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息消费结果
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消费状态
     */
    private ConsumeStatus status;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消费时间戳
     */
    private Long consumeTime;

    /**
     * 消费耗时（毫秒）
     */
    private Long consumeCostTime;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 异常信息
     */
    private Throwable exception;

    /**
     * 消费状态枚举
     */
    public enum ConsumeStatus {
        /**
         * 消费成功
         */
        SUCCESS,

        /**
         * 消费失败，需要重试
         */
        RETRY,

        /**
         * 消费失败，丢弃消息
         */
        DROP,

        /**
         * 稍后重新消费
         */
        RECONSUME_LATER
    }

    /**
     * 创建成功结果
     *
     * @param messageId 消息ID
     * @return ConsumeResult
     */
    public static ConsumeResult success(String messageId) {
        return ConsumeResult.builder()
            .status(ConsumeStatus.SUCCESS)
            .messageId(messageId)
            .consumeTime(System.currentTimeMillis())
            .build();
    }

    /**
     * 创建重试结果
     *
     * @param messageId 消息ID
     * @param message   错误消息
     * @return ConsumeResult
     */
    public static ConsumeResult retry(String messageId, String message) {
        return ConsumeResult.builder()
            .status(ConsumeStatus.RETRY)
            .messageId(messageId)
            .consumeTime(System.currentTimeMillis())
            .errorMessage(message)
            .build();
    }

    /**
     * 创建重试结果
     *
     * @param messageId 消息ID
     * @param exception 异常
     * @return ConsumeResult
     */
    public static ConsumeResult retry(String messageId, Throwable exception) {
        return ConsumeResult.builder()
            .status(ConsumeStatus.RETRY)
            .messageId(messageId)
            .consumeTime(System.currentTimeMillis())
            .errorMessage(exception.getMessage())
            .exception(exception)
            .build();
    }

    /**
     * 创建丢弃结果
     *
     * @param messageId 消息ID
     * @param message   错误消息
     * @return ConsumeResult
     */
    public static ConsumeResult drop(String messageId, String message) {
        return ConsumeResult.builder()
            .status(ConsumeStatus.DROP)
            .messageId(messageId)
            .consumeTime(System.currentTimeMillis())
            .errorMessage(message)
            .build();
    }

    /**
     * 创建稍后消费结果
     *
     * @param messageId 消息ID
     * @return ConsumeResult
     */
    public static ConsumeResult reconsumedLater(String messageId) {
        return ConsumeResult.builder()
            .status(ConsumeStatus.RECONSUME_LATER)
            .messageId(messageId)
            .consumeTime(System.currentTimeMillis())
            .build();
    }
}
