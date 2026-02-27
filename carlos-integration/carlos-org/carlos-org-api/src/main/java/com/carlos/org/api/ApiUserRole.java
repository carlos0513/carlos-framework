package com.carlos.org.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.fallback.FeignUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 系统用户 feign 提供接口
 * </p>
 *
 * @author carlos
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, contextId = "userRole", path = "/api/org/user/role", fallbackFactory = FeignUserFallbackFactory.class)
public interface ApiUserRole {

}
