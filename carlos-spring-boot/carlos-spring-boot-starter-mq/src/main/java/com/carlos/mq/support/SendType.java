package com.carlos.mq.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息发送方式枚举
 * <p>
 * 定义消息的发送模式
 * </p>
 *
 * @author Carlos
 * @date 2022/1/18 9:56
 */
@Getter
@RequiredArgsConstructor
public enum SendType {

    /**
     * 同步发送
     * <p>
     * 发送消息后等待服务器响应，发送超时或失败会抛出异常
     * </p>
     */
    SYNC("sync", "同步发送"),

    /**
     * 异步发送
     * <p>
     * 发送消息后立即返回，通过回调函数处理发送结果
     * </p>
     */
    ASYNC("async", "异步发送"),

    /**
     * 单向发送
     * <p>
     * 发送消息后不关心发送结果，不等待服务器响应
     * 适用于对可靠性要求不高的场景，如日志收集
     * </p>
     */
    ONEWAY("oneway", "单向发送");

    /**
     * 类型编码
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 根据编码获取类型
     *
     * @param code 编码
     * @return SendType
     */
    public static SendType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return SYNC;
        }
        for (SendType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return SYNC;
    }
}
