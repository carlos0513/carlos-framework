package com.yunjin.org.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.exception.RestException;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.log.annotation.Log;
import com.yunjin.log.enums.BusinessType;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.RoleConvert;
import com.yunjin.org.pojo.dto.RoleDTO;
import com.yunjin.org.pojo.param.*;
import com.yunjin.org.pojo.vo.*;
import com.yunjin.org.service.RoleMenuService;
import com.yunjin.org.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 系统角色 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/org/role")
@Tag(name = "用户-角色")
@Slf4j
public class RoleController {

    public static final String BASE_NAME = "角色";

    private final RoleService roleService;

    private final RoleMenuService roleMenuService;


    @PostMapping
    @Operation(summary = "新增" + BASE_NAME)
    @Log(title = "新增角色", businessType = BusinessType.INSERT)
    @ApiOperationSupport(author = AuthorConstant.YANGLE)
    public void add(@RequestBody @Validated RoleCreateParam param) {
        RoleDTO dto = RoleConvert.INSTANCE.toDTO(param);
        dto.setMenuIds(param.getPcMenuIds());
        dto.getMenuIds().addAll(param.getMobileMenuIds());
        this.roleService.addRole(dto);
    }


    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    @Log(title = "删除角色", businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<String> param) {
        this.roleService.deleteRole(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    @Log(title = "修改角色", businessType = BusinessType.UPDATE)
    public void update(@RequestBody @Validated RoleUpdateParam param) {
        RoleDTO dto = RoleConvert.INSTANCE.toDTO(param);
        dto.setMenuIds(param.getPcMenuIds());
        dto.getMenuIds().addAll(param.getMobileMenuIds());
        if (Objects.nonNull(param.getManageMenuIds())) {
            dto.getMenuIds().addAll(param.getManageMenuIds());
        }
        this.roleService.updateRole(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("removeUserRole")
    @Operation(summary = "移除用户" + BASE_NAME)
    @Log(title = "移除用户角色", businessType = BusinessType.UPDATE)
    public void removeUserRole(@RequestBody @Validated UserDeptRoleDTO param) {
        this.roleService.removeUserRole(param);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("addUserRole")
    @Operation(summary = "新增用户角色" + BASE_NAME)
    @Log(title = "新增用户角色", businessType = BusinessType.UPDATE)
    public void addUserRole(@RequestBody @Validated UserDeptRoleDTO param) {
        this.roleService.addUserRole(param);
    }

    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    @ApiOperationSupport(author = AuthorConstant.YANGLE)
    public RoleDetailVO detail(@RequestParam String id) {
        RoleDTO role = this.roleService.getDetail(id);
        return RoleConvert.INSTANCE.toDetailVO(role);
    }

    @GetMapping("roleUserPage")
    @Operation(summary = BASE_NAME + "用户分页")
    @ApiOperationSupport(author = AuthorConstant.YANGLE)
    public Paging<RoleDetailVO.UserInfo> roleUserPage(@RequestParam("id") String id, @RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                                      @RequestParam("keyword") String keyword) {
        return this.roleService.roleUserPage(id, current, size, keyword);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<RolePageVO> page(RolePageParam param) {
        return this.roleService.getPage(param);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("list")
    @Operation(summary = BASE_NAME + "列表")
    public List<RoleBaseVO> list(String keyword) {
        return RoleConvert.INSTANCE.toBaseVO(this.roleService.getAll(keyword));
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("allList")
    @Operation(summary = BASE_NAME + "全量列表")
    public List<RoleBaseVO> allList(String keyword) {
        return RoleConvert.INSTANCE.toBaseVO(this.roleService.listAll(keyword));
    }

    /*@ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("resource")
    @Operation(summary = "角色分配资源")
    @Log(title = "角色分配资源", businessType = BusinessType.UPDATE)
    public void allocateResource(@RequestBody @Validated RoleResourceCreateParam param) {
        this.roleResourceService.addRoleResource(param.getRoleId(), param.getResourceIds());
    }*/

   /* @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("resource")
    @Operation(summary = "获取角色资源")
    public Set<String> resources(String roleId) {
        if (roleId == null) {
            throw new RestException("roleId不能为空！");
        }
        return this.roleResourceService.getResourceIdByRoleId(roleId);
    }*/

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("menu")
    @Operation(summary = "获取角色菜单", notes = "获取所选菜单的id用户回显")
    public MenuIdVO menus(String roleId) {
        if (roleId == null) {
            throw new RestException("角色不能为空！");
        }
        return this.roleMenuService.getMenuIdByRoleId(roleId);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("menu")
    @Operation(summary = "角色分配菜单")
    @Log(title = "角色分配菜单权限", businessType = BusinessType.UPDATE)
    public void allocateMenu(@RequestBody @Validated RoleMenuCreateParam param) {
        this.roleMenuService.batchAddRoleMenu(param.getIds(), param.getMenuIds());
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("init/roleMenu")
    @Operation(summary = "初始化角色菜单")
    @Log(title = "初始化角色菜单", businessType = BusinessType.UPDATE)
    public void initRoleMenu() {
        this.roleService.initRoleMenu();
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("init/mobileRoleMenu")
    @Operation(summary = "初始化移动端角色菜单")
    @Log(title = "初始化角色菜单", businessType = BusinessType.INSERT)
    public void initMobileRoleMenu() {
        this.roleService.initMobileRoleMenu();
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @PostMapping("init/specifiedMenuForAllRole")
    @Operation(summary = "指定菜单角色初始化")
    @Log(title = "初始化角色菜单", businessType = BusinessType.UPDATE)
    public void specifiedMenuForAllRole(@RequestBody List<String> menuIds) {
        this.roleService.specifiedMenuForAllRole(menuIds);
    }

    @ApiOperationSupport(author = AuthorConstant.ZHU_JUN)
    @GetMapping("checkUserDeptRole")
    @Operation(summary = "检查用户再某个部门下是否已经存在角色")
    public UserRoleVO checkUserDeptRole(@RequestParam @NotBlank String userId, @RequestParam @NotBlank String deptId) {
        return roleService.checkUserDeptRole(userId, deptId);
    }

}
