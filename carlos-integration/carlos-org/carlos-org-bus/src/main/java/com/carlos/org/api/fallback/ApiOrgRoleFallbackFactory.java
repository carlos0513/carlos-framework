package com.carlos.org.api.fallback;

import com.carlos.org.api.ApiOrgRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 系统角色 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
public class ApiOrgRoleFallbackFactory implements FallbackFactory<ApiOrgRole> {

    @Override
    public ApiOrgRole create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("系统角色服务调用失败: message:{}", message);
        return new ApiOrgRole() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiOrgRoleFallbackFactory roleFallbackFactory() {
    //     return new ApiOrgRoleFallbackFactory();
    // }
}