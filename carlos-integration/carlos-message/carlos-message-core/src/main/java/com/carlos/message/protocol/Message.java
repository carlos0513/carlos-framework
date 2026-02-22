package com.carlos.message.protocol;

import com.carlos.message.enums.MessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息传输对象
 * </p>
 *
 * @author Carlos
 * @date 2022/1/11
 */
@Data
@Accessors(chain = true)
public class Message<T extends com.carlos.message.protocol.MessageContent> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 消息类型
     */
    private MessageType type;
    /**
     * 消息类型
     */
    private T content;
    /**
     * 消息创建时间
     */
    private LocalDateTime time;
}
