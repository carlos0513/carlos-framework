package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgRolePermissionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 角色权限 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "rolePermission", path = "/api/org/role/permission", fallbackFactory = ApiOrgRolePermissionFallbackFactory.class)
public interface ApiOrgRolePermission {


}
