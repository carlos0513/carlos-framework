package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgUserRoleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 用户角色 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "userRole", path = "/api/org/user/role", fallbackFactory = ApiOrgUserRoleFallbackFactory.class)
public interface ApiOrgUserRole {


}
