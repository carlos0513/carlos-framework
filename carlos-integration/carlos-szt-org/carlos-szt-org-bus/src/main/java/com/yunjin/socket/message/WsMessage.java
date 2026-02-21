package com.yunjin.socket.message;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 元景大模型统一返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@Data
@Accessors(chain = true)
public class WsMessage<T> implements Serializable {

    /**
     * 消息类型
     */
    private WsMessageType type;
    /**
     * 消息内容
     */
    private T payload;
}
