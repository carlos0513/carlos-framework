package com.yunjin.socket.message;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 消息类型
 * </p>
 *
 * @author Carlos
 * @date 2024/4/19 13:26
 */
@Getter
@AllArgsConstructor
public enum WsMessageType {

    /**
     *
     */

    YJAI_QA("云景大模型智能问答"),

    YJAI_QA_STOP("云景大模型停止问答");

    /**
     * 描述信息
     */
    private final String desc;


}
