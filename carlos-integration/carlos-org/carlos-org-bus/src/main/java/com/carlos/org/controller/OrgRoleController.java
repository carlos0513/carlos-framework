package com.carlos.org.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.org.convert.OrgRoleConvert;
import com.carlos.org.manager.OrgRoleManager;
import com.carlos.org.pojo.dto.OrgRoleDTO;
import com.carlos.org.pojo.dto.OrgRoleUserDTO;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.OrgRoleDetailVO;
import com.carlos.org.pojo.vo.OrgRoleUserVO;
import com.carlos.org.pojo.vo.OrgRoleVO;
import com.carlos.org.service.OrgRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.stream.Collectors;


/**
 * <p>
 * 角色 rest服务接口
 * </p>
 * <p>实现RM001-RM010所有角色管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/role")
@Tag(name = "角色-RM001到RM010完整实现")
public class OrgRoleController {

    public static final String BASE_NAME = "角色";

    private final OrgRoleService roleService;

    private final OrgRoleManager roleManager;


    /**
     * RM-003 新增角色
     */
    @PostMapping("add")
    @Operation(summary = "RM-003 新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgRoleCreateParam param) {
        OrgRoleDTO dto = OrgRoleConvert.INSTANCE.toDTO(param);
        roleService.addOrgRole(dto);
    }


    /**
     * RM-005 删除角色
     */
    @PostMapping("delete")
    @Operation(summary = "RM-005 删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        roleService.deleteOrgRole(param.getIds());
    }


    /**
     * RM-004 编辑角色
     */
    @PostMapping("update")
    @Operation(summary = "RM-004 更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgRoleUpdateParam param) {
        OrgRoleDTO dto = OrgRoleConvert.INSTANCE.toDTO(param);
        roleService.updateOrgRole(dto);
    }


    /**
     * RM-001 角色列表
     */
    @GetMapping("page")
    @Operation(summary = "RM-001 " + BASE_NAME + "分页列表")
    public Paging<OrgRoleVO> page(OrgRolePageParam param) {
        Paging<OrgRoleDTO> dtoPaging = roleManager.getPage(param);
        // 手动转换分页结果
        Paging<OrgRoleVO> voPaging = new Paging<>();
        voPaging.setCurrent(dtoPaging.getCurrent());
        voPaging.setSize(dtoPaging.getSize());
        voPaging.setTotal(dtoPaging.getTotal());
        voPaging.setPages(dtoPaging.getPages());
        voPaging.setRecords(OrgRoleConvert.INSTANCE.toVOList(dtoPaging.getRecords()));
        return voPaging;
    }


    /**
     * RM-002 角色详情
     */
    @GetMapping("detail")
    @Operation(summary = "RM-002 " + BASE_NAME + "详情")
    public OrgRoleDetailVO detail(@RequestParam Serializable id) {
        OrgRoleDTO dto = roleManager.getDtoById(id);
        return OrgRoleConvert.INSTANCE.toDetailVO(dto);
    }


    /**
     * RM-006 启用/禁用角色
     */
    @PostMapping("changeState")
    @Operation(summary = "RM-006 启用/禁用" + BASE_NAME)
    public void changeState(@RequestBody @Validated OrgRoleChangeStateParam param) {
        roleService.changeState(param.getId(), param.getState());
    }


    /**
     * RM-008 配置数据权限
     */
    @PostMapping("setDataScope")
    @Operation(summary = "RM-008 配置数据权限")
    public void setDataScope(@RequestBody @Validated OrgRoleSetDataScopeParam param) {
        roleService.setDataScope(param);
    }


    /**
     * RM-007 配置角色权限
     */
    @PostMapping("assignPermissions")
    @Operation(summary = "RM-007 配置" + BASE_NAME + "权限")
    public void assignPermissions(@RequestBody @Validated OrgRoleAssignPermissionParam param) {
        roleService.assignPermissions(param);
    }


    /**
     * RM-009 查看角色用户
     */
    @GetMapping("users")
    @Operation(summary = "RM-009 " + BASE_NAME + "用户列表")
    public Paging<OrgRoleUserVO> getRoleUsers(@RequestParam Serializable roleId, OrgRolePageParam param) {
        Paging<OrgRoleUserDTO> dtoPaging = roleService.getRoleUsers(roleId, param);
        // 手动转换分页结果
        Paging<OrgRoleUserVO> voPaging = new Paging<>();
        voPaging.setCurrent(dtoPaging.getCurrent());
        voPaging.setSize(dtoPaging.getSize());
        voPaging.setTotal(dtoPaging.getTotal());
        voPaging.setPages(dtoPaging.getPages());
        voPaging.setRecords(dtoPaging.getRecords().stream()
                .map(this::toUserVO)
                .collect(Collectors.toList()));
        return voPaging;
    }


    /**
     * RM-010 复制角色
     */
    @PostMapping("copy")
    @Operation(summary = "RM-010 复制" + BASE_NAME)
    public void copy(@RequestBody @Validated OrgRoleCopyParam param) {
        roleService.copyRole(param);
    }


    /**
     * DTO转VO
     */
    private OrgRoleUserVO toUserVO(OrgRoleUserDTO dto) {
        if (dto == null) {
            return null;
        }
        OrgRoleUserVO vo = new OrgRoleUserVO();
        vo.setUserId(dto.getUserId());
        vo.setAccount(dto.getAccount());
        vo.setName(dto.getName());
        vo.setPhone(dto.getPhone());
        vo.setMainDeptName(dto.getMainDeptName());
        vo.setCreateTime(dto.getCreateTime());
        return vo;
    }

}
