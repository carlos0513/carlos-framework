package com.yunjin.org.api;

import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignUserFallbackFactory;
import com.yunjin.org.pojo.ao.RoleAO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/role", contextId = "role", fallbackFactory = FeignUserFallbackFactory.class)
public interface ApiRole {


    @GetMapping("{id}")
    @Operation(summary = "获取角色详情")
    Result<RoleAO> getById(@PathVariable("id") String id);

    @GetMapping("getRoleList")
    @Operation(summary = "获取所有的角色列表")
    Result<List<RoleAO>> getRoleList();

    @GetMapping("getUserIdByRoleId")
    @Operation(summary = "获取所有的角色列表")
    Result<Set<Serializable>> getUserIdByRoleId(@RequestParam("roleIds") Set<Serializable> roleIds);

    @GetMapping("/getByName")
    @Operation(summary = "通过角色名称获取角色详情")
    Result<RoleAO> getByName(@RequestParam("name") String roleName);
}
