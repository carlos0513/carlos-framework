package com.carlos.org.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.org.UserUtil;
import com.carlos.org.convert.UserConvert;
import com.carlos.org.manager.UserManager;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.*;
import com.carlos.org.service.UserService;
import com.carlos.system.enums.MenuType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统用户 rest服务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/org/user")
@Tag(name = "用户-系统用户")
@Slf4j
public class UserController {

    public static final String BASE_NAME = "系统用户";

    private final UserService userService;

    private final UserManager userManager;


    @GetMapping
    @Operation(summary = "获取当前登录用户信息", description = "用户信息包括用户菜单, 基本信息")
    @Log(title = "获取当前用户信息", businessType = BusinessType.QUERY)
    public UserSessionVO loginInfo() {
        UserDTO dto = this.userService.getCurrentUser(MenuType.PC);
        return UserConvert.INSTANCE.toVO(dto);
    }


    @GetMapping("/manage")
    @Operation(summary = "管理端获取当前登录用户信息", description = "用户信息包括用户菜单, 基本信息")
    public UserSessionVO manageLoginInfo() {
        UserDTO dto = this.userService.getCurrentUser(MenuType.MANAGE);
        return UserConvert.INSTANCE.toVO(dto);
    }


    @PostMapping
    @Operation(summary = "新增用户")
    @Log(title = "新增系统用户", businessType = BusinessType.INSERT)
    public UserBaseInfoVO add(@RequestBody @Validated UserCreateParam param) {
        UserDTO dto = UserConvert.INSTANCE.toDTO(param);
        this.userService.addUser(dto);
        return UserConvert.INSTANCE.toBaseVO(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    @Log(title = "删除系统用户", businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.userService.deleteUser(param.getIds());
    }


    @PostMapping("writeoff")
    @Operation(summary = "writeoff" + BASE_NAME)
    @Log(title = "注销系统用户", businessType = BusinessType.DELETE)
    public void writeoff(@RequestBody ParamIdSet<Serializable> param) {
        this.userService.writeOffUser(param.getIds());
    }

    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    @Log(title = "更新用户信息", businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated UserUpdateParam param) {
        UserDTO dto = UserConvert.INSTANCE.toDTO(param);
        this.userService.updateUser(dto);
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "重置密码")
    @Log(title = "重置用户密码", businessType = BusinessType.UPDATE)

    public void resetPassword(@RequestBody @Validated UserResetPwdParam param) {
        this.userService.resetPassword(param.getId(), param.getPwd());
    }

    @PostMapping("/forgetPassword")
    @Operation(summary = "忘记密码")
    @Log(title = "用户忘记密码", businessType = BusinessType.UPDATE)

    public void forgetPassword(@RequestBody @Validated UserForgetPwdParam param) {
        this.userService.forgetPassword(param);
    }

    @PostMapping("/state")
    @Operation(summary = "更改状态")
    @Log(title = "更改状态", businessType = BusinessType.UPDATE_STATUS)

    public void updateState(@RequestBody @Validated UserStateParam param) {
        userService.changeState(param.getId(), param.getState());
    }

    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    @Log(title = "获取用户详情", businessType = BusinessType.QUERY)
    public UserDetailVO detail(String id) {
        return UserConvert.INSTANCE.toDetailVO(this.userService.getUserById(id, true));
    }

    @PostMapping("/sendCreateMsg")
    @Operation(summary = BASE_NAME + "详情")
    @Log(title = "根据手机号列表发送用户创建信息", businessType = BusinessType.QUERY)
    public void sendCreateMsg(@RequestBody Set<String> phones) {
        userService.sendCreateMsg(phones);
    }

    @GetMapping("account/{account}")
    @Operation(summary = "通过账号获取用户")
    @Log(title = "通过账号获取用户", businessType = BusinessType.QUERY)
    public UserBaseInfoVO getByAccount(@PathVariable String account) {
        return UserConvert.INSTANCE.toBaseVO(this.userService.getUserByAccount(account));
    }

    @GetMapping("baseinfo/{id}")
    @Operation(summary = "通过id获取用户基本信息")
    @Log(title = "通过账号获取用户", businessType = BusinessType.QUERY)
    public UserBaseInfoVO getBaseInfo(@PathVariable String id) {
        return UserConvert.INSTANCE.toBaseVO(this.userService.getBaseInfo(id, true));
    }


    @GetMapping("floatCard")
    @Operation(summary = "用户信息悬浮卡片")
    public UserFloatCardInfoVO floatCard(UserFloatCardParam param) {
        UserFloatCardInfoVO user = null;
        try {
            user = userManager.getUserCardInfo(param.getUserId(), param.getDeptId(), param.getDeptCode());
        } catch (Exception e) {
            log.error("用户信息获取失败：{}", e.getMessage(), e);
            return user;
        }
        if (user == null) {
            return null;
        }
        List<UserFloatCardInfoVO.DepartmentRole> depts = user.getDepts();
        if (CollUtil.isNotEmpty(depts)) {
            String roleName = depts.stream()
                    .map(dr -> dr.getDeptName() + "(" + dr.getRoleName() + ")")
                    .collect(Collectors.joining("|"));
            user.setRoleName(roleName);
        }
        if (StrUtil.isNotBlank(user.getHead())) {
            user.setHead(userService.getFileUrl(user.getHead()));

        }
        return user;
    }

    @GetMapping("page")
    @Operation(summary = "用户分页列表")
    public Result<Paging<UserPageVO>> page(UserPageParam param) {
        return Result.ok(this.userService.listAuthLimit(param));
    }

    @GetMapping("export")
    @Operation(summary = "用户列表导出")
    public void export(UserPageParam param,  HttpServletResponse response) {
        this.userService.export(param, response);
    }

    @GetMapping("list")
    @Operation(summary = "用户列表(模糊匹配)")
    public List<UserListVO> list(String keyword, @RequestParam(required = false) Set<String> deptLevels) {
        List<UserDTO> dtos = this.userService.list(keyword, deptLevels);
        return UserConvert.INSTANCE.toListVO(dtos);
    }

    @PostMapping("search")
    @Operation(summary = "用户搜索(模糊匹配)-分页")
    public PageInfo<UserListVO> search(@RequestBody UserPageParam param) {
        PageInfo<UserListVO> search = userManager.search(param);
        return search;
    }

    @GetMapping("complete/list")
    @Operation(summary = "用户列表(完全匹配)")
    public List<UserListVO> completeMatchList(String keyword) {
        List<UserDTO> dtos = this.userService.completeMatchList(keyword);
        return UserConvert.INSTANCE.toListVO(dtos);
    }

    @PostMapping("changePwd")
    @Operation(summary = "修改密码")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    public boolean changePwd(@RequestBody @Validated UserChangePwdParam param) {
        param.setId(UserUtil.getId());
        return this.userService.changePwd(param);
    }

    @PostMapping("force/changePwd")
    @Operation(summary = "第一次登录强制修改密码")
    @Log(title = "第一次登录强制修改密码", businessType = BusinessType.UPDATE_FORCE)
    public boolean forceChangedPwd(@RequestBody @Validated UserChangePwdParam param) {
        return this.userService.forceChangedPwd(param);
    }

    @PostMapping("updateUserInfo")
    @Operation(summary = "更新个人资料")
    @Log(title = "更新个人资料", businessType = BusinessType.UPDATE)
    public boolean updateUserInfo(@Validated @RequestBody UserInfoUpdateParam param) {
        param.setId(UserUtil.getId());
        return this.userService.updateUserInfo(param);
    }

    @GetMapping("/getDefaultPwd")
    @Operation(summary = "获取重置密码默认密码")
    @Log(title = "获取重置密码默认密码", businessType = BusinessType.UPDATE)

    public String getDefaultPwd(String id) {
        return userService.getDefaultPwd(id);
    }


    @GetMapping("excludeByDept")
    @Operation(summary = "获取非某个部门下的用户")
    public Paging<UserBaseVO> excludeByDept(UserExcludeDeptPageParam param) {
        return userManager.excludeByDept(param);
    }


    @PostMapping("regionUpdate")
    @Operation(summary = "批量修改用户区域")
    public void regionUpdate(String regionName) {
        this.userService.changeRegin(regionName);
    }

    //
    //
    // @GetMapping("role/{roleId}")
    // @Operation(summary = "获取角色下的用户")
    // public List<UserDetailVO> roleUser(@PathVariable final String roleId) {
    //     if (StrUtil.isBlank(roleId)) {
    //         throw new RestException("部门不能为空！");
    //     }
    //     final List<UserDTO> dtos = this.userService.getUserByRoleId(roleId);
    //     return UserConvert.INSTANCE.dto2vo(dtos);
    // }
    //
    //
    //
    // @GetMapping("roles/{account}")
    // @Operation(summary = "获取用户角色")
    // public List<UserRoleVO> userRole(@PathVariable final String account) {
    //     return UserConvert.INSTANCE.toUserRoleVO(this.userService.getUserRoles(account));
    // }
    //
    //
    // @GetMapping("roles/current")
    // @Operation(summary = "获取当前用户所有角色")
    // public List<UserRoleVO> currentRole() {
    //     return UserConvert.INSTANCE.toUserRoleVO(this.userService.getUserRoles(ExtendInfoUtil.getUserContext().getAccount()));
    // }
    //
    //
    //
    // @GetMapping("departmentUser")
    // @Operation(summary = "获取部门下的用户")
    // public List<UserDetailVO> departmentUser(final String departmentId) {
    //     return this.userService.getUserByDeptId(departmentId);
    // }
    //
    //
    // @PostMapping("role")
    // @Operation(summary = "用户分配角色")
    // @Log(title = "用户分配角色", businessType = BusinessType.UPDATE)
    // public void allocateUser(@RequestBody @Validated UserRoleCreateParam param) {
    //     this.userRoleService.batchAddUserRole(param.getUserIds(), param.getRoleIds());
    // }

    @GetMapping("resetUserDeptRole")
    @Operation(summary = "历史数据初始化用户-部门-角色关系")
    public void resetUserDeptRole() {
        this.userService.resetUserDeptRole();
    }

}
