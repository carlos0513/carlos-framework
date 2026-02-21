package com.yunjin.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * <p>
 * websocket相关配置
 * </p>
 *
 * @author Carlos
 * @date 2022/4/1 10:28
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册websocket组件 添加处理器和拦截器
        // websocket是websocket服务器的请求路径可以自己定义
        registry.addHandler(new YjAISocketHandler(), "/websocket/yjai/qa")
                // 指定自定义拦截器
                .addInterceptors(new YjAIWebsocketInterceptor())
                // 允许跨域
                .setAllowedOrigins("*");
        // 在某些低版本的浏览器中不支持websocket可以用sock-js替代
        // registry.addHandler(new CustomSocketHandler(), "/sock-js")
        //         .addInterceptors(new WebsocketInterceptor())
        //         .setAllowedOrigins("*")
        //         // 开启sockJs支持
        //         .withSockJS();

    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

}
