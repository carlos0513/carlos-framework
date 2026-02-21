package com.yunjin.org.apiimpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.yunjin.boot.request.RequestInfo;
import com.yunjin.boot.request.RequestUtil;
import com.yunjin.boot.util.ExtendInfoUtil;
import com.yunjin.core.auth.UserContext;
import com.yunjin.core.base.UserInfo;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiUser;
import com.yunjin.org.config.OrgConstant;
import com.yunjin.org.convert.UserConvert;
import com.yunjin.org.manager.RoleManager;
import com.yunjin.org.manager.UserManager;
import com.yunjin.org.pojo.ao.UserDeptRoleAO;
import com.yunjin.org.pojo.ao.UserDetailAO;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.ao.UserOrgAO;
import com.yunjin.org.pojo.dto.*;
import com.yunjin.org.pojo.param.ApiUserDeptRoleParam;
import com.yunjin.org.service.DepartmentService;
import com.yunjin.org.service.UserDepartmentService;
import com.yunjin.org.service.UserService;
import com.yunjin.redis.util.RedisUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 api接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/user")
@Tag(name = "系统用户Feign接口", hidden = true)
@Slf4j
public class UserAPI implements ApiUser {

    private final UserService userService;
    private final UserManager userManager;

    private final RoleManager roleManager;

    private final DepartmentService departmentService;

    private final UserDepartmentService userDepartmentService;

    private final String SUPER_MANAGER = "超级管理员";


    @Override
    @GetMapping("getUserOrgInfo")
    public Result<UserOrgAO> getUserOrgInfo(@RequestParam("deptCode") String deptCode, @RequestParam("userId") String userId) {
        UserDTO user = userService.getUserOrgInfo(deptCode, userId);
        UserOrgAO ao = new UserOrgAO();
        ao.setId(user.getId());
        ao.setAccount(user.getAccount());
        ao.setRealname(user.getRealname());
        ao.setPhone(user.getPhone());
        ao.setRoleId(user.getRoleId());
        ao.setRoleName(user.getRoleName());
        ao.setDeptId(user.getDepartmentId());
        ao.setDeptCode(user.getDepartmentLevelCode());
        ao.setDeptName(user.getDepartmentName());
        ao.setDeptFullName(user.getDeptFullNames());
        return Result.ok(ao);
    }

    @GetMapping("allUser")
    @Override
    public Result<List<UserDetailAO>> allUser() {
        List<UserDTO> users = userService.getAllUser();
        return Result.ok(UserConvert.INSTANCE.dtoToAo(users));
    }

    @Override
    @ApiOperation("通过id获取用户信息")
    @GetMapping("getById")
    public Result<UserInfo> getUserById(@RequestParam("id") String id) {
        UserInfo userInfo = this.userService.getUserInfo(id);
        return Result.ok(userInfo);
    }

    @Override
    @ApiOperation("通过ids获取用户信息")
    @PostMapping("list")
    public Result<List<UserInfo>> getUserByIds(@RequestBody ParamIdSet<String> ids) {
        try {
            List<UserDTO> users = this.userService.listByIds(ids.getIds());
            return Result.ok(UserConvert.INSTANCE.dtoToAos(users));
        } catch (Exception e) {
            log.error("用户信息获取失败，", e);
            return Result.fail("用户信息查询失败：" + e.getMessage());
        }
    }

    @Override
    @ApiOperation("通过ids获取用户信息包括电话号码")
    @PostMapping("taskList")
    public Result<List<UserDetailAO>> getTaskUserByIds(@RequestBody ParamIdSet<String> ids) {
        try {
            List<UserDTO> users = this.userService.listByIds(ids.getIds());
            return Result.ok(UserConvert.INSTANCE.dtoToAo(users));
        } catch (Exception e) {
            log.error("用户信息获取失败，", e);
            return Result.fail("用户信息查询失败：" + e.getMessage());
        }
    }

    @Override
    @GetMapping("listByDeptCode")
    @ApiOperation("获取部门code下所属用户")
    public Result<List<UserInfo>> listByDeptCode(@RequestParam("deptCode") String deptCode) {
        try {
            DepartmentDTO departmentDTO = departmentService.getDepartmentByCode(deptCode);
            if (departmentDTO == null) {
                return Result.fail("部门code错误，获取部门失败！");
            }
            List<UserDepartmentDTO> departmentUser = userDepartmentService.getByDepartmentId(departmentDTO.getId());
            if (CollectionUtil.isEmpty(departmentUser)) {
                return Result.ok();
            }
            Set<String> userIds = departmentUser.stream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet());
            List<UserDTO> userDTOS = userService.listByIds(userIds);
            if (CollectionUtil.isEmpty(userDTOS)) {
                return Result.fail("获取部门code：" + deptCode + " 下所属用户失败！");
            }
            List<UserInfo> userInfos = UserConvert.INSTANCE.toUserInfo(userDTOS);
            return Result.ok(userInfos);
        } catch (Exception e) {
            log.error("用户信息获取失败，", e);
            return Result.fail("获取部门code下所属用户失败：" + e.getMessage());
        }
    }

    //@Override
    //@ApiOperation("通过角色id获取用户")
    //@GetMapping("role/{roleId}")
    // public Result<List<UserDTO>> getUserByRoleId(@PathVariable("roleId") String roleId) {
    //    List<UserDTO> users = this.userService.getUserByRoleId(roleId);
    //    return Result.ok(users);
    //}

    //@Override
    // public Result<List<UserDTO>> getUserByRoleIds(@RequestParam("roleIds") Set<String> roleIds) {
    //    List<UserDTO> users = this.userService.getUserByRoleIds(roleIds);
    //    return Result.ok(users);
    //}

    //@Override
    //@ApiOperation("通过id获取用户部门名称")
    //@GetMapping("departmentName/{id}")
    // public Result<List<String>> getDepartmentName(@PathVariable("id") String id, @RequestParam(value = "limit") int limit) {
    //    return Result.ok(this.userService.getDepartmentName(id, limit));
    //}

    //@Override
    //@ApiOperation("通过id获取用户区域名称")
    //@GetMapping("regionName/{id}")
    // public Result<List<String>> getUserRegionName(@PathVariable("id") String id, @RequestParam(value = "limit") int limit) {
    //    return Result.ok(this.userService.getRegionName(id, limit));
    //}

    @Override
    @ApiOperation("获取当前登录用户id")
    @GetMapping("currentUserId")
    public Result<String> getCurrentUserId() {
        try {
            UserContext userContext = ExtendInfoUtil.getUserContext();
            String userId = (String) userContext.getUserId();
            return Result.ok(userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("用户信息获取失败：" + e.getMessage());
        }
    }

    @Override
    @ApiOperation("获取当前登录用户所在部门code")
    @GetMapping("currentDepartmentCode")
    public Result<String> getCurrentDepartmentCode() {
        // TODO 2023-7-4 暂未实现
        try {
            Serializable departmentId = ExtendInfoUtil.getDepartmentId();
            DepartmentDTO departmentDTO = departmentService.getDepartmentById((String) departmentId);
            if (departmentDTO == null) {
                return Result.fail("用户部门信息查询失败！");
            }
            return Result.ok(departmentDTO.getDeptCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("用户部门信息获取失败：" + e.getMessage());
        }

    }


    @Override
    @ApiOperation("获取当前登录用户所在区域code")
    @GetMapping("currentRegionCode")
    public Result<String> getCurrentRegionCode() {
        try {
            RequestInfo requestInfo = RequestUtil.getRequestInfo();
            UserContext userContext = requestInfo.getUserContext();
            if (userContext == null) {
                throw new ServiceException("用户信息获取失败");
            }

            UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()), UserDTO.class);
            if (user == null) {
                throw new ServiceException("认证已过期，请重新登录");
            }
            return Result.ok(user.getDepartmentInfo().getRegionCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("用户区域信息获取失败：" + e.getMessage());
        }
    }

    @Override
    @GetMapping("currentUser")
    @Operation(summary = "获取当前登录用户信息")
    public Result<UserLoginAO> getCurrentUser() {
        try {
            RequestInfo requestInfo = RequestUtil.getRequestInfo();
            UserContext userContext = requestInfo.getUserContext();
            if (userContext == null) {
                throw new ServiceException("用户信息获取失败");
            }
            long start = System.currentTimeMillis();
            UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, userContext.getToken()), UserDTO.class);
            if (user == null) {
                throw new ServiceException("认证已过期，请重新登录");
            }
            UserLoginAO userLoginAO = UserConvert.INSTANCE.dtoToAo(user);
            return Result.ok(userLoginAO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("用户信息获取失败：" + e.getMessage());
        }
    }


    @Override
    @GetMapping("currentUserByWhitList")
    @Operation(summary = "白名单接口获取当前登录用户信息")
    public Result<UserLoginAO> getCurrentUserByWhiteList() {
        try {
            RequestInfo requestInfo = RequestUtil.getRequestInfo();
            String token = null;
            for (Map.Entry<String, String> entry : requestInfo.getHeader().entrySet()) {
                if (entry.getKey().equalsIgnoreCase("authorization")) {
                    token = entry.getValue();
                    break;
                }
            }
            if (token == null) {
                throw new ServiceException("用户信息获取失败");
            }

            UserDTO user = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_TOKEN_CACHE, token), UserDTO.class);
            if (user == null) {
                throw new ServiceException("认证已过期，请重新登录");
            }
            UserLoginAO userLoginAO = UserConvert.INSTANCE.dtoToAo(user);
            return Result.ok(userLoginAO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("用户信息获取失败：" + e.getMessage());
        }
    }

    @Override
    @PostMapping("getUserByDeptAndRole")
    public Result<List<UserDeptRoleAO>> getUserByDeptAndRole(@RequestBody ApiUserDeptRoleParam param) {
        List<UserDeptRoleDTO> users = userManager.getUserByDeptAndRole(param);
        return Result.ok(UserConvert.INSTANCE.userDeptRole(users));
    }

    @Override
    @GetMapping("detail/{id}")
    public Result<UserDetailAO> getUserDetailById(@PathVariable(value = "id") String id) {
        UserDTO user = userService.getBaseInfo(id, false);
        return Result.ok(UserConvert.INSTANCE.dtoToDetailAo(user));
    }

    @Override
    @GetMapping("/checkManager")
    public Result<Boolean> checkManager(@RequestParam("userId") String userId) {
        RoleDTO superManager = roleManager.getByName(SUPER_MANAGER);
        if (superManager == null) {
            log.warn("not found role:[{}] ", SUPER_MANAGER);
            return Result.ok(false);
        }
        List<UserDTO> users = userManager.getByRoleId(superManager.getId());
        if (CollUtil.isEmpty(users)) {
            log.warn("not found any user for role: {}", SUPER_MANAGER);
            return Result.ok(false);
        }
        Set<String> collect = users.stream().map(UserDTO::getId).collect(Collectors.toSet());
        if (collect.contains(userId)) {
            return Result.ok(true);
        } else {
            return Result.ok(false);
        }
    }

    @Override
    @GetMapping("getByAccount")
    public Result<UserInfo> getUserByName(@RequestParam("account") String account) {
        UserDTO dto = this.userService.getUserByAccount(account);
        return Result.ok(UserConvert.INSTANCE.toUser(dto));
    }
}
