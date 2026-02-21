package com.yunjin.org.api;

import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignUserScopeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 用户权限相关接口
 * </p>
 *
 * @author Carlos
 * @date 2022/11/23 11:41
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/user/scope", contextId = "UserScope", fallbackFactory = FeignUserScopeFallbackFactory.class)
public interface ApiUserScope {


    /**
     * 获取当前角色所有用户id
     *
     * @return com.yunjin.common.core.response.Result<java.util.Set < java.io.Serializable>>
     * @author Carlos
     * @date 2022/11/23 11:43
     */
    @GetMapping("currentRole")
    Result<Set<Serializable>> getCurrentRoleUserId();

    /**
     * 获取当前部门所有用户id
     *
     * @return com.yunjin.common.core.response.Result<java.util.Set < java.io.Serializable>>
     * @author Carlos
     * @date 2022/11/23 11:43
     */
    @GetMapping("currentDept")
    Result<Set<Serializable>> getCurrentDeptUserId();

    /**
     * 获取当前部门及子部门所有用户id
     *
     * @return com.yunjin.common.core.response.Result<java.util.Set < java.io.Serializable>>
     * @author Carlos
     * @date 2022/11/23 11:43
     */
    @GetMapping("/currentDeptAll")
    Result<Set<Serializable>> getCurrentDeptAllUserId(@RequestParam("departmentId") Serializable departmentId);


    /**
     * 获取当前用户部门及所有子部门id
     *
     * @return com.yunjin.common.core.response.Result<java.util.Set < java.io.Serializable>>
     * @author Carlos
     * @date 2022/12/13 16:20
     */
    @GetMapping("currentDeptAllId")
    Result<Set<Serializable>> getCurrentDeptAllId(@RequestParam("departmentId") Serializable departmentId);


    /**
     * 获取当前用户区域及子区域code
     *
     * @return com.yunjin.common.core.response.Result<java.util.Set < java.lang.String>>
     * @author Carlos
     * @date 2022/12/13 16:20
     */
    @GetMapping("currentRegionAllCode")
    Result<Set<String>> getCurrentRegionAllCode();
}
