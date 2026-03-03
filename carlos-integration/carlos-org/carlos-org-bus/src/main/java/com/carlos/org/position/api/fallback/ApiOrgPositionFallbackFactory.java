package com.carlos.org.position.api.fallback;

import com.carlos.org.position.api.ApiOrgPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 岗位表 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@Slf4j
public class ApiOrgPositionFallbackFactory implements FallbackFactory<ApiOrgPosition> {

    @Override
    public ApiOrgPosition create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("岗位表服务调用失败: message:{}", message);
        return new ApiOrgPosition() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiOrgPositionFallbackFactory positionFallbackFactory() {
    //     return new ApiOrgPositionFallbackFactory();
    // }
}