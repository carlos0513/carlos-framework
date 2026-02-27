package com.carlos.org.apiimpl;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiUserScope;
import com.carlos.org.service.UserScopeService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 用户数据权限接口
 * </p>
 *
 * @author Carlos
 * @date 2022/11/23 11:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/scope")
@Tag(name = "系统用户Feign接口")
@Hidden
public class UserScopeAPI implements ApiUserScope {

    private final UserScopeService scopeService;

    @Operation(summary = "获取当前角色所有用户")

    @GetMapping("currentRole")
    @Override
    public Result<Set<Serializable>> getCurrentRoleUserId() {
        return Result.ok(scopeService.getCurrentRoleUserId());
    }

    @Operation(summary = "获取当前部门所有用户")
    @GetMapping("currentDept")

    @Override
    public Result<Set<Serializable>> getCurrentDeptUserId() {
        return Result.ok(scopeService.getCurrentDeptUserId());
    }


    @Operation(summary = "获取当前部门及子部门所有用户")
    @GetMapping("currentDeptAll")
    @Override
    public Result<Set<Serializable>> getCurrentDeptAllUserId(@RequestParam("departmentId") Serializable departmentId) {
        return Result.ok(scopeService.getCurrentDeptAllUserId(departmentId));
    }


    @Operation(summary = "获取当前部门及子部门")
    @GetMapping("currentDeptAllId")
    @Override
    public Result<Set<Serializable>> getCurrentDeptAllId(@RequestParam("departmentId") Serializable departmentId) {
        return Result.ok(scopeService.getCurrentDeptTreeIds(departmentId));
    }


    @Operation(summary = "获取当前用户区域及子区域code")
    @GetMapping("currentRegionAllCode")
    @Override
    public Result<Set<String>> getCurrentRegionAllCode() {
        return Result.ok(scopeService.getCurrentRegionTreeIds());
    }
}
