package com.yunjin.org.apiimpl;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiUserScope;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.service.impl.UserScopeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "系统用户Feign接口", hidden = true)
public class UserScopeAPI implements ApiUserScope {

    private final UserScopeService scopeService;

    @ApiOperation("获取当前角色所有用户id")

    @GetMapping("currentRole")
    @Override
    public Result<Set<Serializable>> getCurrentRoleUserId() {
        return Result.ok(scopeService.getCurrentRoleUserId());
    }

    @ApiOperation("获取当前部门所有用户id")
    @GetMapping("currentDept")

    @Override
    public Result<Set<Serializable>> getCurrentDeptUserId() {
        return Result.ok(scopeService.getCurrentDeptUserId());
    }



    @ApiOperation("获取当前部门及子部门所有用户id")
    @GetMapping("currentDeptAll")
    @Override
    public Result<Set<Serializable>> getCurrentDeptAllUserId(@RequestParam("departmentId") Serializable departmentId) {
        return Result.ok(scopeService.getCurrentDeptAllUserId(departmentId));
    }


    @ApiOperation("获取当前部门及子部门id")
    @GetMapping("currentDeptAllId")
    @Override
    public Result<Set<Serializable>> getCurrentDeptAllId(@RequestParam("departmentId") Serializable departmentId) {
        return Result.ok(scopeService.getCurrentDeptTreeIds(departmentId));
    }


    @ApiOperation("获取当前用户区域及子区域code")
    @GetMapping("currentRegionAllCode")
    @Override
    public Result<Set<String>> getCurrentRegionAllCode() {
        return Result.ok(scopeService.getCurrentRegionTreeIds());
    }
}
