package com.yunjin.socket;


import cn.hutool.json.JSONUtil;
import com.yunjin.socket.message.YjAIQaResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * <p>
 * 只能问答回调
 * </p>
 *
 * @author Carlos
 * @date 2024/4/19 13:32
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketYjAIQACallback {

    private final WebSocketSession session;

    public void callback(YjAIQaResult result) {
        try {
            session.sendMessage(new TextMessage(JSONUtil.toJsonStr(result)));
        } catch (IOException e) {
            log.error("send message error{}", e.getMessage(), e);
        }
    }
}
