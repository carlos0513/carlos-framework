package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgUserDepartmentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 用户部门 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "userDepartment", path = "/api/org/user/department", fallbackFactory = ApiOrgUserDepartmentFallbackFactory.class)
public interface ApiOrgUserDepartment {


}
