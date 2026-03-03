package com.carlos.org.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.org.convert.OrgDepartmentConvert;
import com.carlos.org.manager.OrgDepartmentManager;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import com.carlos.org.pojo.dto.OrgDepartmentUserDTO;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.OrgDepartmentTreeVO;
import com.carlos.org.pojo.vo.OrgDepartmentUserVO;
import com.carlos.org.pojo.vo.OrgDepartmentVO;
import com.carlos.org.service.OrgDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 部门 rest服务接口
 * </p>
 * <p>实现DM001-DM010所有部门管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/department")
@Tag(name = "部门-DM001到DM010完整实现")
public class OrgDepartmentController {

    public static final String BASE_NAME = "部门";

    private final OrgDepartmentService departmentService;

    private final OrgDepartmentManager departmentManager;


    /**
     * DM-003 新增部门
     */
    @PostMapping("add")
    @Operation(summary = "DM-003 新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgDepartmentCreateParam param) {
        OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(param);
        departmentService.addOrgDepartment(dto);
    }


    /**
     * DM-005 删除部门
     */
    @PostMapping("delete")
    @Operation(summary = "DM-005 删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        departmentService.deleteOrgDepartment(param.getIds());
    }


    /**
     * DM-004 编辑部门
     */
    @PostMapping("update")
    @Operation(summary = "DM-004 更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgDepartmentUpdateParam param) {
        OrgDepartmentDTO dto = OrgDepartmentConvert.INSTANCE.toDTO(param);
        departmentService.updateOrgDepartment(dto);
    }


    /**
     * DM-002 部门列表（平铺）
     */
    @GetMapping("page")
    @Operation(summary = "DM-002 " + BASE_NAME + "分页列表")
    public Paging<OrgDepartmentVO> page(OrgDepartmentPageParam param) {
        Paging<OrgDepartmentDTO> dtoPaging = departmentManager.getPage(param);
        // 手动转换分页结果
        Paging<OrgDepartmentVO> voPaging = new Paging<>();
        voPaging.setCurrent(dtoPaging.getCurrent());
        voPaging.setSize(dtoPaging.getSize());
        voPaging.setTotal(dtoPaging.getTotal());
        voPaging.setPages(dtoPaging.getPages());
        voPaging.setRecords(OrgDepartmentConvert.INSTANCE.toVOList(dtoPaging.getRecords()));
        return voPaging;
    }


    /**
     * DM-001 部门树查询
     */
    @GetMapping("tree")
    @Operation(summary = "DM-001 " + BASE_NAME + "树")
    public Result<List<OrgDepartmentTreeVO>> tree() {
        List<OrgDepartmentDTO> dtoList = departmentService.getDepartmentTree();
        return Result.ok(OrgDepartmentConvert.INSTANCE.buildTreeVOList(dtoList));
    }


    /**
     * DM-006 移动部门
     */
    @PostMapping("move")
    @Operation(summary = "DM-006 移动" + BASE_NAME)
    public void move(@RequestBody @Validated OrgDepartmentMoveParam param) {
        departmentService.moveDepartment(param);
    }


    /**
     * DM-007 部门排序
     */
    @PostMapping("sort")
    @Operation(summary = "DM-007 " + BASE_NAME + "排序")
    public void sort(@RequestBody @Validated OrgDepartmentSortParam param) {
        departmentService.sortDepartment(param);
    }


    /**
     * DM-008 部门人员列表
     */
    @GetMapping("users")
    @Operation(summary = "DM-008 " + BASE_NAME + "人员列表")
    public Paging<OrgDepartmentUserVO> getDepartmentUsers(@RequestParam Serializable deptId, OrgDepartmentPageParam param) {
        Paging<OrgDepartmentUserDTO> dtoPaging = departmentService.getDepartmentUsers(deptId, param);
        // 手动转换分页结果
        Paging<OrgDepartmentUserVO> voPaging = new Paging<>();
        voPaging.setCurrent(dtoPaging.getCurrent());
        voPaging.setSize(dtoPaging.getSize());
        voPaging.setTotal(dtoPaging.getTotal());
        voPaging.setPages(dtoPaging.getPages());
        voPaging.setRecords(OrgDepartmentConvert.INSTANCE.toUserVOList(dtoPaging.getRecords()));
        return voPaging;
    }


    /**
     * DM-009 设置部门负责人
     */
    @PostMapping("setLeader")
    @Operation(summary = "DM-009 设置" + BASE_NAME + "负责人")
    public void setLeader(@RequestBody @Validated OrgDepartmentSetLeaderParam param) {
        departmentService.setDepartmentLeader(param);
    }


    /**
     * DM-010 部门角色配置（预留接口）
     */
    @PostMapping("setRole")
    @Operation(summary = "DM-010 " + BASE_NAME + "角色配置")
    public void setRole(@RequestParam Serializable deptId, @RequestParam Serializable roleId) {
        departmentService.setDepartmentRole(deptId, roleId);
    }


    /**
     * 启用/禁用部门
     */
    @PostMapping("changeState")
    @Operation(summary = "启用/禁用" + BASE_NAME)
    public void changeState(@RequestBody @Validated OrgDepartmentChangeStateParam param) {
        departmentService.changeState(param.getId(), param.getState());
    }

}
