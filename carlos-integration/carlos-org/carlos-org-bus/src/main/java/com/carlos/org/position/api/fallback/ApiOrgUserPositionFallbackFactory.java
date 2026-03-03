package com.carlos.org.position.api.fallback;

import com.carlos.org.position.api.ApiOrgUserPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Slf4j
public class ApiOrgUserPositionFallbackFactory implements FallbackFactory<ApiOrgUserPosition> {

    @Override
    public ApiOrgUserPosition create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("用户岗位职级关联表（核心任职信息）服务调用失败: message:{}", message);
        return new ApiOrgUserPosition() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiOrgUserPositionFallbackFactory userPositionFallbackFactory() {
    //     return new ApiOrgUserPositionFallbackFactory();
    // }
}