package com.carlos.org.position.api;

import com.carlos.org.ServiceNameConstant;
import com.carlos.org.position.api.fallback.ApiOrgUserPositionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 用户岗位职级关联表（核心任职信息） feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月3日 下午11:19:10
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "userPosition", path = "/api/org/user/position", fallbackFactory = ApiOrgUserPositionFallbackFactory.class)
public interface ApiOrgUserPosition {


}
