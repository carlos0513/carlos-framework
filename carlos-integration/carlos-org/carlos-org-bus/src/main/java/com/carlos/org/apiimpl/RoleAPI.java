package com.carlos.org.apiimpl;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiRole;
import com.carlos.org.convert.RoleConvert;
import com.carlos.org.manager.RoleManager;
import com.carlos.org.manager.UserRoleManager;
import com.carlos.org.pojo.ao.RoleAO;
import com.carlos.org.pojo.dto.RoleDTO;
import com.carlos.org.service.RoleService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 api接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/role")
@Tag(name = "用户角色Feign接口")
@Hidden
public class RoleAPI implements ApiRole {

    private final RoleService roleService;

    private final UserRoleManager userRoleManager;

    private final RoleManager roleManager;


    @Override

    @GetMapping("{id}")
    @Operation(summary = "获取角色详情")
    public Result<RoleAO> getById(@PathVariable("id") String id) {
        RoleDTO dto = roleService.getById(id);
        return Result.ok(RoleConvert.INSTANCE.toAO(dto));
    }

    @Override
    @GetMapping("getRoleList")
    @Operation(summary = "获取角色列表")
    public Result<List<RoleAO>> getRoleList() {
        List<RoleDTO> dtoList = roleService.getAll(null);
        return Result.ok(RoleConvert.INSTANCE.toAOS(dtoList));
    }

    @Override
    @GetMapping("getUserIdByRoleId")
    @Operation(summary = "获取所有的角色列表")
    public Result<Set<Serializable>> getUserIdByRoleId(@RequestParam("roleIds") Set<Serializable> roleIds) {
        Set<Serializable> userIds = userRoleManager.listUserIdByRoleId(roleIds);
        return Result.ok(userIds);
    }

    @Override
    @GetMapping("/getByName")
    @Operation(summary = "通过角色名称获取角色详情")
    public Result<RoleAO> getByName(String roleName) {
        RoleDTO dto = roleManager.getByName(roleName);
        return Result.ok(RoleConvert.INSTANCE.toAO(dto));
    }
}
