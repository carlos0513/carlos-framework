package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 系统用户 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "user", path = "/api/org/user", fallbackFactory = ApiOrgUserFallbackFactory.class)
public interface ApiOrgUser {


}
