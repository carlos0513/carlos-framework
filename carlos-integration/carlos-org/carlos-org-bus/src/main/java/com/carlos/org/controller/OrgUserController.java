package com.carlos.org.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.org.convert.OrgUserConvert;
import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.dto.OrgUserDeptDTO;
import com.carlos.org.pojo.dto.OrgUserPositionDTO;
import com.carlos.org.pojo.dto.OrgUserRoleDTO;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.OrgUserDeptVO;
import com.carlos.org.pojo.vo.OrgUserPositionVO;
import com.carlos.org.pojo.vo.OrgUserRoleVO;
import com.carlos.org.pojo.vo.OrgUserVO;
import com.carlos.org.service.OrgUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 系统用户 rest服务接口
 * </p>
 * <p>实现UM001-UM014所有用户管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user")
@Tag(name = "系统用户-UM001到UM014完整实现")
public class OrgUserController {

    public static final String BASE_NAME = "系统用户";

    private final OrgUserService userService;

    private final OrgUserManager userManager;


    /**
     * UM-003 新增用户
     */
    @PostMapping("add")
    @Operation(summary = "UM-003 新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserCreateParam param) {
        OrgUserDTO dto = OrgUserConvert.INSTANCE.toDTO(param);
        userService.addOrgUser(dto);
    }


    /**
     * UM-005 删除用户
     */
    @PostMapping("delete")
    @Operation(summary = "UM-005 删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        userService.deleteOrgUser(param.getIds());
    }


    /**
     * UM-004 编辑用户
     */
    @PostMapping("update")
    @Operation(summary = "UM-004 更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserUpdateParam param) {
        OrgUserDTO dto = OrgUserConvert.INSTANCE.toDTO(param);
        userService.updateOrgUser(dto);
    }


    /**
     * UM-002 用户详情查询
     */
    @GetMapping("detail")
    @Operation(summary = "UM-002 " + BASE_NAME + "详情")
    public OrgUserVO detail(Serializable id) {
        OrgUserDTO dto = userManager.getDtoById(id);
        return OrgUserConvert.INSTANCE.toVO(dto);
    }


    /**
     * UM-001 用户列表查询（分页）
     */
    @GetMapping("page")
    @Operation(summary = "UM-001 " + BASE_NAME + "分页列表")
    public Paging<OrgUserVO> page(OrgUserPageParam param) {
        return userManager.getPage(param);
    }


    /**
     * UM-006 启用/禁用用户
     */
    @PostMapping("changeState")
    @Operation(summary = "UM-006 启用/禁用" + BASE_NAME)
    public void changeState(@RequestBody @Validated OrgUserChangeStateParam param) {
        userService.changeState(param.getId(), param.getState());
    }


    /**
     * UM-007 解锁用户
     */
    @PostMapping("unlock")
    @Operation(summary = "UM-007 解锁" + BASE_NAME)
    public void unlock(@RequestBody @Validated OrgUserUnlockParam param) {
        userService.unlockOrgUser(param.getId());
    }


    /**
     * UM-008 重置密码
     */
    @PostMapping("resetPassword")
    @Operation(summary = "UM-008 重置密码")
    public void resetPassword(@RequestBody @Validated OrgUserResetPwdParam param) {
        userService.resetPassword(param.getId(), param.getNewPwd());
    }


    /**
     * UM-009 修改密码（用户自己修改）
     */
    @PostMapping("changePassword")
    @Operation(summary = "UM-009 修改密码")
    public void changePassword(@RequestBody @Validated OrgUserChangePwdParam param) {
        userService.changePassword(param);
    }


    /**
     * UM-010 用户导入
     */
    @PostMapping("import")
    @Operation(summary = "UM-010 用户导入")
    public Result<List<Serializable>> importUser(@RequestParam("file") MultipartFile file) {
        return Result.ok(userService.importUser(file));
    }


    /**
     * UM-011 用户导出
     */
    @GetMapping("export")
    @Operation(summary = "UM-011 用户导出")
    public void exportUser(OrgUserPageParam param, HttpServletResponse response) {
        userService.exportUser(param, response);
    }


    /**
     * UM-012 用户分配部门
     */
    @PostMapping("assignDepartments")
    @Operation(summary = "UM-012 用户分配部门")
    public void assignDepartments(@RequestBody @Validated OrgUserAssignDeptParam param) {
        userService.assignDepartments(param);
    }


    /**
     * UM-013 用户分配角色
     */
    @PostMapping("assignRoles")
    @Operation(summary = "UM-013 用户分配角色")
    public void assignRoles(@RequestBody @Validated OrgUserAssignRoleParam param) {
        userService.assignRoles(param);
    }


    /**
     * UM-014 用户分配岗位
     */
    @PostMapping("assignPositions")
    @Operation(summary = "UM-014 用户分配岗位")
    public void assignPositions(@RequestBody @Validated OrgUserAssignPositionParam param) {
        userService.assignPositions(param);
    }


    /**
     * 获取用户已分配部门
     */
    @GetMapping("departments")
    @Operation(summary = "获取用户部门列表")
    public List<OrgUserDeptVO> getUserDepartments(Serializable userId) {
        List<OrgUserDeptDTO> dtoList = userService.getUserDepartments(userId);
        return OrgUserConvert.INSTANCE.toDeptVOList(dtoList);
    }


    /**
     * 获取用户已分配角色
     */
    @GetMapping("roles")
    @Operation(summary = "获取用户角色列表")
    public List<OrgUserRoleVO> getUserRoles(Serializable userId) {
        List<OrgUserRoleDTO> dtoList = userService.getUserRoles(userId);
        return OrgUserConvert.INSTANCE.toRoleVOList(dtoList);
    }


    /**
     * 获取用户已分配岗位
     */
    @GetMapping("positions")
    @Operation(summary = "获取用户岗位列表")
    public List<OrgUserPositionVO> getUserPositions(Serializable userId) {
        List<OrgUserPositionDTO> dtoList = userService.getUserPositions(userId);
        return OrgUserConvert.INSTANCE.toPositionVOList(dtoList);
    }

}
