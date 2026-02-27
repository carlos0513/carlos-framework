package com.carlos.org.controller;


import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.org.UserUtil;
import com.carlos.org.config.AuthorConstant;
import com.carlos.org.convert.DepartmentConvert;
import com.carlos.org.enums.DeptRelationEnum;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.*;
import com.carlos.org.service.DepartmentRoleService;
import com.carlos.org.service.DepartmentService;
import com.carlos.org.service.UserDepartmentService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 部门 rest服务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/org/department")
@Tag(name = "用户-部门")
@ApiSupport(author = AuthorConstant.ZHU_JUN)
public class DepartmentController {

    public static final String BASE_NAME = "部门";

    private final DepartmentService departmentService;

    private final DepartmentManager departmentManager;

    private final UserDepartmentService userDepartmentService;

    private final DepartmentRoleService departmentRoleService;

    @PostMapping
    @Operation(summary = "新增或修改部门")

    @Log(title = "新增或修改部门", businessType = BusinessType.INSERT)
    public DepartmentBaseVO add(@RequestBody @Validated DepartmentCreateParam param) {
        DepartmentDTO dto = DepartmentConvert.INSTANCE.toDTO(param);
        this.departmentService.saveOrUpdate(dto);
        return DepartmentConvert.INSTANCE.toBaseVO(dto);
    }

    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    @Log(title = "删除" + BASE_NAME, businessType = BusinessType.DELETE)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        this.departmentService.deleteDepartment(param.getIds());
    }


    @GetMapping("{id}")
    @Operation(summary = "部门编辑详情")
    public DepartmentDetailVO detail(@PathVariable String id, UserPageParam param) {
        return DepartmentConvert.INSTANCE.toDetailVO(this.departmentService.getDepartmentDetail(id, param));
    }



    @GetMapping("user/{id}")
    @Operation(summary = "部门所有用户")
    public List<DepartmentUserVO> getUser(@PathVariable String id) {
        List<UserDepartmentDTO> users = this.departmentService.getUser(id);
        return DepartmentConvert.INSTANCE.toUserVO(users);
    }


    @GetMapping("current/dept/user")
    @Operation(summary = "当前登录部门所有用户")
    public List<DepartmentUserVO> getCurrentLoginDepatmentUsers() {
        List<UserDepartmentDTO> users = this.departmentService.getUser(UserUtil.getDepartment().getId());
        return DepartmentConvert.INSTANCE.toUserVO(users);
    }


    @GetMapping("treeStep")
    @Operation(summary = "当前登录部门(或子部门)及用户")
    public DepartmentStepTreeVO treeStep(@RequestParam(required = false) String departmentId) {
        return this.departmentService.getTreeStep(departmentId);
    }


    @GetMapping("user/notIn")
    @Operation(summary = "非部门下的用户")
    public Paging<DepartmentUserNotInVO> getNotInDeptUser(UserExcludeDeptPageParam param) {
        Paging<UserDTO> users = this.departmentService.getNotInDeptUser(param);
        return DepartmentConvert.INSTANCE.toDepartmentUserVO(users);
    }

    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<DepartmentVO> page(DepartmentPageParam param) {
        return this.departmentManager.getPage(param);
    }

    @PostMapping("user/add")
    @Operation(summary = "部门新增用户")
    @Log(title = "部门新增人员", businessType = BusinessType.UPDATE)
    public void addUser(@RequestBody @Validated DepartmentUserListParam param) {
        userDepartmentService.addDepartmentUser(param.getId(), param.getUsers());
    }

    @PostMapping("user/remove")
    @Operation(summary = "部门移除用户")
    @Log(title = "部门移除人员", businessType = BusinessType.UPDATE)
    public void removeUser(@RequestBody @Validated DepartmentUserListParam param) {
        userDepartmentService.removeDepartmentUser(param.getId(), param.getUsers());
    }


    @GetMapping("tree")
    @Operation(summary = "部门树形列表", description = "所有部门")
    public List<DepartmentTreeVO> tree(
            @RequestParam(required = false) String departmentId,
            @RequestParam(defaultValue = "false", required = false) Boolean userFlag
    ) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.departmentTreeMianYang(departmentId, userFlag);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }

    @GetMapping("tree/export")
    @Operation(summary = "部门树形列表导出", description = "所有部门")
    public void treeExport(@RequestParam(required = false) String departmentId,
                           @RequestParam(defaultValue = "false", required = false) Boolean userFlag,
                                   HttpServletResponse response) {
        this.departmentService.treeExport(departmentId, userFlag, response);
    }

    @GetMapping("loadStep")
    @Operation(summary = "部门树逐级加载", description = "逐级加载")
    public List<DepartmentTreeVO> loadStep(@RequestParam(required = false) String departmentId, @RequestParam(defaultValue = "false", required = false) Boolean thirdFlag) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.getByParentId(departmentId,thirdFlag);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }


    @GetMapping("subtree")
    @Operation(summary = "子级部门树形列表", description = "当前用户下级部门树（不包括当前部门）")
    public List<DepartmentTreeVO> subTree(@RequestParam(defaultValue = "false", required = false) Boolean userFlag) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.subTree(userFlag);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }



    @PostMapping("/setAdmin")
    @Operation(summary = "设置部门管理员权限")
    public boolean setDeptAdmin(@RequestBody DepartmentAdminParam param) {
        return this.departmentService.setDeptAdmin(param);
    }


    @GetMapping("/childrenList")
    @Operation(summary = "获取当前登录用户下级部门列表")
    public List<DepartmentBaseVO> getDeptsBase() {
        return this.departmentService.getCurrDepartments();

    }


    @GetMapping("/deptTreeLoad")
    @Operation(summary = "查询部门树-部门逐级加载")
    public List<DepartmentTreeVO> deptTreeLoad(@RequestParam(value = "deptRelationEnum", defaultValue = "ALL") DeptRelationEnum deptRelationEnum,
                                               @RequestParam(value = "level", defaultValue = "1") Integer level,
                                               @RequestParam(value = "keyword", required = false) String keyword) {
        List<DepartmentDTO> dtos = this.departmentService.deptTreeLoad(deptRelationEnum, level);
        
        // 如果提供了搜索关键字，则进行过滤
        if (org.apache.commons.lang3.StringUtils.isNotBlank(keyword)) {
            dtos = filterDepartmentsByKeyword(dtos, keyword);
        }
        
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }

    /**
     * 根据关键字过滤部门列表
     * @param departments 部门列表
     * @param keyword 搜索关键字
     * @return 过滤后的部门列表
     */
    private List<DepartmentDTO> filterDepartmentsByKeyword(List<DepartmentDTO> departments, String keyword) {
        List<DepartmentDTO> result = new ArrayList<>();
        for (DepartmentDTO dept : departments) {
            if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(dept.getDeptName(), keyword)) {
                result.add(dept);
            } else if (!CollectionUtils.isEmpty(dept.getChildren())) {
                List<DepartmentDTO> filteredChildren = filterDepartmentsByKeyword(dept.getChildren(), keyword);
                if (!CollectionUtils.isEmpty(filteredChildren)) {
                    // 创建新的部门对象，避免修改原始数据
                    DepartmentDTO newDept = new DepartmentDTO();
                    BeanUtils.copyProperties(newDept, dept);
                    newDept.setChildren(filteredChildren);
                    result.add(newDept);
                }
            }
        }
        return result;
    }
    

    @GetMapping("/fullName")
    @Operation(summary = "获取部门完整名称")
    public List<String> getFullName(String deptId, @RequestParam(required = false) Integer level) {
        if (level == null) {
            level = 0;
        }
        return departmentService.previewDepartmentName(deptId, level);
    }


    @PostMapping("initCache")
    @Operation(summary = "初始化缓存")
    public void initCache() {
        this.departmentService.initCache();
    }





}
