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
public class YjAIQaResult implements Serializable {

    /**
     * 借宿标识
     */
    private boolean endFlag;
    /**
     * 消息内容
     */
    private String content;
}
