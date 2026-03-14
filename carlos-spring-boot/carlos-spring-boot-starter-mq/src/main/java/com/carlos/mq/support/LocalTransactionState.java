package com.carlos.mq.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 本地事务状态枚举
 *
 * @author Carlos
 * @date 2026/3/14
 */
@Getter
@RequiredArgsConstructor
public enum LocalTransactionState {

    /**
     * 事务提交
     * <p>
     * 本地事务执行成功，消息可以被消费
     * </p>
     */
    COMMIT("commit", "提交事务"),

    /**
     * 事务回滚
     * <p>
     * 本地事务执行失败，消息将被丢弃
     * </p>
     */
    ROLLBACK("rollback", "回滚事务"),

    /**
     * 未知状态
     * <p>
     * 本地事务状态未知，需要回查
     * </p>
     */
    UNKNOWN("unknown", "未知状态");

    /**
     * 状态编码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String desc;
}
