package com.carlos.org.api.fallback;

import com.carlos.org.api.ApiOrgRolePermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 角色权限 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
public class ApiOrgRolePermissionFallbackFactory implements FallbackFactory<ApiOrgRolePermission> {

    @Override
    public ApiOrgRolePermission create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("角色权限服务调用失败: message:{}", message);
        return new ApiOrgRolePermission() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiOrgRolePermissionFallbackFactory rolePermissionFallbackFactory() {
    //     return new ApiOrgRolePermissionFallbackFactory();
    // }
}