package com.carlos.auth.app.api.fallback;

import com.carlos.auth.app.api.ApiAppClient;
import com.carlos.auth.app.api.pojo.ao.AppClientAO;
import com.carlos.core.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.io.Serializable;


/**
 * <p>
 * 应用信息 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Slf4j
public class ApiAppClientFallbackFactory implements FallbackFactory
    <ApiAppClient> {

    @Override
    public ApiAppClient create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("应用信息服务调用失败: message:{}", message);
        return new ApiAppClient() {

            @Override
            public Result<AppClientAO> getById(Serializable id) {
                return Result.fail("应用信息获取失败！");
            }

            @Override
            public Result<AppClientAO> getByAppKey(String appKey) {
                return Result.fail("应用信息获取失败！");
            }
        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAppClientFallbackFactory registeredClientFallbackFactory() {
    //     return new ApiAppClientFallbackFactory();
    // }
}
