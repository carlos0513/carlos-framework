package com.carlos.org.api.fallback;

import com.carlos.org.api.ApiOrgDepartment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 部门 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
public class ApiOrgDepartmentFallbackFactory implements FallbackFactory<ApiOrgDepartment> {

    @Override
    public ApiOrgDepartment create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("部门服务调用失败: message:{}", message);
        return new ApiOrgDepartment() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiOrgDepartmentFallbackFactory departmentFallbackFactory() {
    //     return new ApiOrgDepartmentFallbackFactory();
    // }
}