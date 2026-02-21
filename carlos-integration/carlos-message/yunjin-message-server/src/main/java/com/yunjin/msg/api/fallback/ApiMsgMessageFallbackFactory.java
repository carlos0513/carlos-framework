package com.carlos.msg.api.fallback;

import com.carlos.msg.api.ApiMsgMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 消息 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Slf4j
public class ApiMsgMessageFallbackFactory implements FallbackFactory
        <ApiMsgMessage> {

    @Override
    public ApiMsgMessage create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("消息服务调用失败: message:{}", message);
        return new ApiMsgMessage() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiMsgMessageFallbackFactory messageFallbackFactory() {
    //     return new ApiMsgMessageFallbackFactory();
    // }
}