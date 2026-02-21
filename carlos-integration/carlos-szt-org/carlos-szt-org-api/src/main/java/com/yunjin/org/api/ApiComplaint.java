package com.yunjin.org.api;

import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignComplaintFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 系统登录 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, contextId = "complaint", path = "/api/org/complaint", fallbackFactory = FeignComplaintFallbackFactory.class)
public interface ApiComplaint {


    /**
     * 根据名称查询用户
     *
     * @return com.yunjin.core.auth.LoginUserInfo
     * @author yunjin
     * @date 2022/3/7 16:09
     */
    @GetMapping("/processCallback")
    Result<Boolean> processCallback(@RequestParam String id, @RequestParam String content);

}
