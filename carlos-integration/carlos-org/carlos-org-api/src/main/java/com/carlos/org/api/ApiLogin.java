package com.carlos.org.api;

import com.carlos.core.response.Result;
import com.carlos.org.ServiceNameConstant;
import com.carlos.org.fallback.FeignLoginFallbackFactory;
import com.carlos.org.pojo.ao.LoginSuccessAO;
import com.carlos.org.pojo.param.ApiLoginParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 系统登录 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, contextId = "login", path = "/api/org", fallbackFactory = FeignLoginFallbackFactory.class)
public interface ApiLogin {


    /**
     * 根据名称查询用户
     *
     * @return com.carlos.core.auth.LoginUserInfo
     * @author yunjin
     * @date 2022/3/7 16:09
     */
    @PostMapping("/login")
    Result<LoginSuccessAO> login(@RequestBody ApiLoginParam loginParam);

}
