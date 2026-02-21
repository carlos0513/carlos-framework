package com.yunjin.socket;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.yunjin.core.auth.UserContext;
import com.yunjin.docking.yjai.YjAIUtil;
import com.yunjin.socket.message.WsMessage;
import com.yunjin.socket.message.WsMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2024/4/19 0:51
 */
@Slf4j
public class YjAISocketHandler extends TextWebSocketHandler {

    /**
     * 连接成功后调用
     *
     * @param session 参数0
     * @throws
     * @author Carlos
     * @date 2024/4/19 0:54
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        log.info("连接成功： {}", attributes.get(WebsocketConstant.USER_CONTEXT));
    }

    /**
     * 处理发送来的消息
     *
     * @param session 参数0
     * @param message 参数1
     * @author Carlos
     * @date 2024/4/19 0:55
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Receive message: type:{}  payload:{}", message.getClass().getSimpleName(), message.getPayload());
        if (message instanceof TextMessage) {
            handleTextMessage(session, (TextMessage) message);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        Serializable userId = null;
        if (MapUtil.isNotEmpty(attributes)) {
            UserContext userContext = (UserContext) attributes.get(WebsocketConstant.USER_CONTEXT);
            userId = userContext.getUserId();
        }


        String payload = message.getPayload();
        WsMessage<?> wsMessage = JSONUtil.toBean(payload, WsMessage.class);
        WsMessageType type = wsMessage.getType();
        Object context = wsMessage.getPayload();
        switch (type) {
            case YJAI_QA:
                // 问题内容

                YjAIUtil.wsQa(userId.toString(), context.toString(), new WebSocketYjAIQACallback(session));
                break;
            case YJAI_QA_STOP:
                YjAIUtil.wsQaStop(userId.toString(), new WebSocketYjAIQACallback(session));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

    }

    /**
     * 连接出错时调用
     *
     * @param session   参数0
     * @param exception 参数1
     * @throws
     * @author Carlos
     * @date 2024/4/19 0:55
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    /**
     * 连接关闭后调用
     *
     * @param session 参数0
     * @param status  参数1
     * @throws
     * @author Carlos
     * @date 2024/4/19 0:56
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    /**
     * 支持分片消息
     *
     * @return boolean
     * @throws
     * @author Carlos
     * @date 2024/4/19 0:56
     */
    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
}
