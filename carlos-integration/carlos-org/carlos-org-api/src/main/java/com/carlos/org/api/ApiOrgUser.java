package com.carlos.org.api;

import com.carlos.core.response.Result;
import com.carlos.org.ServiceNameConstant;
import com.carlos.org.api.fallback.ApiOrgUserFallbackFactory;
import com.carlos.org.api.pojo.ao.OrgUserAO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 根据标识查询用户（支持用户名、手机号、邮箱）
     *
     * @param identifier 用户标识
     * @return 用户信息
     */
    @GetMapping("/user/by-identifier")
    Result<OrgUserAO> getUserByIdentifier(@RequestParam("identifier") String identifier);


}
