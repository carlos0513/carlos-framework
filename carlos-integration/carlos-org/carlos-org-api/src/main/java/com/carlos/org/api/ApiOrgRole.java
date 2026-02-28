package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgRoleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 系统角色 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "role", path = "/api/org/role", fallbackFactory = ApiOrgRoleFallbackFactory.class)
public interface ApiOrgRole {


}
