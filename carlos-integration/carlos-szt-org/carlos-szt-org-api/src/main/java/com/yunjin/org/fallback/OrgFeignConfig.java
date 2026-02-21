package com.yunjin.org.fallback;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 组织架构feign配置类
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:24
 */
@Configuration
public class OrgFeignConfig {

    @Bean
    public FeignUserFallbackFactory userFallbackFactory() {
        return new FeignUserFallbackFactory();
    }

    // 将此段配置放入配置类中进行Bean加载
    @Bean
    public FeignUserScopeFallbackFactory userScopeFallbackFactory() {
        return new FeignUserScopeFallbackFactory();
    }

    @Bean
    public FeignDepartmentFallbackFactory departmentFallbackFactory() {
        return new FeignDepartmentFallbackFactory();
    }

    @Bean
    public FeignRoleFallbackFactory roleFallbackFactory() {
        return new FeignRoleFallbackFactory();
    }
}
