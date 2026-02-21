package com.yunjin.org.apiimpl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.base.UserInfo;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiUserRole;
import com.yunjin.org.convert.UserRoleConvert;
import com.yunjin.org.manager.RoleManager;
import com.yunjin.org.manager.UserDepartmentManager;
import com.yunjin.org.pojo.ao.UserRoleAO;
import com.yunjin.org.pojo.dto.RoleDTO;
import com.yunjin.org.pojo.entity.UserDepartment;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user/role")
@Tag(name = "用户角色Feign接口", hidden = true)
@Slf4j
public class ApiUserRoleImpl implements ApiUserRole {

    private final RoleManager roleManager;
    private final UserDepartmentManager userDepartmentManager;

    @Override
    @GetMapping("allUserRole")
    @Operation(summary = "获取所有用户角色对应信息")
    public Result<List<UserRoleAO>> allUserRole() {
        try {
            List<UserDepartment> list = userDepartmentManager.list();
            return Result.ok(UserRoleConvert.INSTANCE.userDeptRoleToAO(list));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("用户角色查询失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<UserRoleAO>> getUserRole(List<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Result.ok(Collections.emptyList());
        }
        try {
            log.info("获取用户角色信息,用户ids：{}", userIds);
            List<UserDepartment> userRoles = userDepartmentManager.list(new LambdaQueryWrapper<UserDepartment>().in(UserDepartment::getUserId, userIds));
            log.info("查询到用户角色关联信息：{}", userRoles);
            Set<String> collect = userRoles.stream().map(UserDepartment::getRoleId).filter(Objects::nonNull).collect(Collectors.toSet());
            log.info("整理角色id信息：{}", collect);
            if (collect.isEmpty()) {
                return Result.ok(Collections.emptyList());
            }
            Map<String, RoleDTO> idNames = roleManager.getDtoByIds(collect).stream().collect(Collectors.toMap(RoleDTO::getId, i -> i));
            log.info("查询具体角色信息：{}", idNames);
            List<UserRoleAO> userRoleAOS = UserRoleConvert.INSTANCE.userDeptRoleToAO(userRoles).stream().peek(e -> {
                if (e.getRoleId() != null && idNames.containsKey(e.getRoleId())) {
                    e.setRoleName(idNames.get(e.getRoleId()).getName());
                }
            }).collect(Collectors.toList());
            log.info("返回用户角色信息：{}", userRoleAOS);
            return Result.ok(userRoleAOS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("根据用户ids查询用户角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<String>> getUserRoleNameByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        try {
            List<UserDepartment> userRoles = userDepartmentManager.list(new LambdaQueryWrapper<UserDepartment>().eq(UserDepartment::getUserId, userId));
            log.info("查询到用户角色关联信息：{}", userRoles);
            Set<String> collect = userRoles.stream().map(UserDepartment::getRoleId).filter(Objects::nonNull).collect(Collectors.toSet());
            log.info("整理角色id信息：{}", collect);
            if (collect.isEmpty()) {
                return Result.ok(Collections.emptyList());
            }
            Map<String, RoleDTO> idNames = roleManager.getDtoByIds(collect).stream().collect(Collectors.toMap(RoleDTO::getId, i -> i));
            log.info("查询具体角色信息：{}", idNames);
            List<String> list = new ArrayList<>();
            UserRoleConvert.INSTANCE.userDeptRoleToAO(userRoles).stream().peek(e -> {
                if (e.getRoleId() != null && idNames.containsKey(e.getRoleId())) {
                    e.setRoleName(idNames.get(e.getRoleId()).getName());
                    list.add(idNames.get(e.getRoleId()).getName());
                }
            }).collect(Collectors.toList());
            return Result.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("根据用户ids查询用户角色失败：" + e.getMessage());
        }
    }
}
