package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgDepartmentRoleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 部门角色 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "departmentRole", path = "/api/org/department/role", fallbackFactory = ApiOrgDepartmentRoleFallbackFactory.class)
public interface ApiOrgDepartmentRole {


}
