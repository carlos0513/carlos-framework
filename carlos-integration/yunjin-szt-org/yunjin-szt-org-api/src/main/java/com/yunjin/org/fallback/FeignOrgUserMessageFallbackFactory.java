package com.yunjin.org.fallback;

import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiOrgUserMessage;
import com.yunjin.org.pojo.ao.OrgUserMessageDetailAO;
import com.yunjin.org.pojo.param.ApiOrgUserMessageCreateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 用户消息表 feign 降级
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@Slf4j
public class FeignOrgUserMessageFallbackFactory implements FallbackFactory<ApiOrgUserMessage> {

    @Override
    public ApiOrgUserMessage create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("用户消息表服务调用失败: message:{}", message);
        return new ApiOrgUserMessage() {

            @Override
            public Result<OrgUserMessageDetailAO> getMessageOne(String id) {
                return null;
            }

            @Override
            public void messagesRead(ParamIdSet<String> ids) {

            }

            @Override
            public void addMessage(ApiOrgUserMessageCreateParam param) {

            }

            @Override
            public void smsMessage(String id) {

            }
        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public FeignOrgUserMessageFallbackFactory userMessageFallbackFactory() {
    //     return new FeignOrgUserMessageFallbackFactory();
    // }
}