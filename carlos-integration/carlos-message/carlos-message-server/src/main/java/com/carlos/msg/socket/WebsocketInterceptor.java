package com.carlos.msg.socket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * <p>
 * websocket请求拦截器
 * </p>
 *
 * @author Carlos
 * @date 2022/4/1 10:30
 */
public class WebsocketInterceptor implements HandshakeInterceptor {

    /**
     * handler处理前调用,attributes属性最终在WebSocketSession里, 可能通过webSocketSession.getAttributes().get(key值)获得
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes)
        throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            // 获取请求路径携带的参数
            String user = serverHttpRequest.getServletRequest().getParameter("user");
            attributes.put("user", user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * handler处理后调用
     *
     * @param request   服务请求
     * @param response  服务响应
     * @param wsHandler 处理器
     * @param exception 异常信息
     * @author Carlos
     * @date 2022/4/1 10:31
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
