package com.yunjin.org.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.yunjin.boot.util.ExtendInfoUtil;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.log.annotation.Log;
import com.yunjin.log.enums.BusinessType;
import com.yunjin.org.UserUtil;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.DepartmentConvert;
import com.yunjin.org.enums.DeptRelationEnum;
import com.yunjin.org.manager.DepartmentManager;
import com.yunjin.org.param.DepartmentCreateOrUpdateParam;
import com.yunjin.org.param.DepartmentDeleteParam;
import com.yunjin.org.pojo.dto.DepartmentDTO;
import com.yunjin.org.pojo.dto.UserDTO;
import com.yunjin.org.pojo.dto.UserDepartmentDTO;
import com.yunjin.org.pojo.param.*;
import com.yunjin.org.pojo.vo.*;
import com.yunjin.org.service.DepartmentRoleService;
import com.yunjin.org.service.DepartmentService;
import com.yunjin.org.service.UserDepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 部门 rest服务接口
 * </p>
 *
 * @author yunjin
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
    @Operation(summary = "部门树形列表", notes = "所有部门")
    public List<DepartmentTreeVO> tree(
            @RequestParam(required = false) String departmentId,
            @RequestParam(defaultValue = "false", required = false) Boolean userFlag
    ) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.departmentTreeMianYang(departmentId, userFlag);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }

    @GetMapping("tree/export")
    @Operation(summary = "部门树形列表导出", notes = "所有部门")
    public void treeExport(@RequestParam(required = false) String departmentId,
                           @RequestParam(defaultValue = "false", required = false) Boolean userFlag,
                                   HttpServletResponse response) {
        this.departmentService.treeExport(departmentId, userFlag, response);
    }

    @GetMapping("loadStep")
    @Operation(summary = "部门树逐级加载", notes = "逐级加载")
    public List<DepartmentTreeVO> loadStep(@RequestParam(required = false) String departmentId, @RequestParam(defaultValue = "false", required = false) Boolean thirdFlag) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.getByParentId(departmentId,thirdFlag);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }

    /**
     * 查询本部门以及下级部门、平级部门以及平级部门的下级部门
     * @param departmentId
     * @return
     */
    @GetMapping("tree/with-children")
    @Operation(summary = "部门树形列表", notes = "所有部门")
    public List<DepartmentTreeVO> getDepartmentTreeWithChildren(
            @RequestParam(required = false) String departmentId
    ) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.getFullDepartment(departmentId);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }

    @GetMapping("subtree")
    @Operation(summary = "子级部门树形列表", notes = "当前用户下级部门树（不包括当前部门）")
    public List<DepartmentTreeVO> subTree(@RequestParam(defaultValue = "false", required = false) Boolean userFlag) {
        List<DepartmentDTO> departmentDTOS = this.departmentService.subTree(userFlag);
        return DepartmentConvert.INSTANCE.toTreeVO(departmentDTOS);
    }


    @GetMapping("/current/sameLeve")
    @Operation(summary = "当前用户部门同级部门")
    public List<DepartmentTreeVO> currentSameLeveDept() {
        List<DepartmentDTO> dtos = this.departmentService.getSameLevelDepartment(ExtendInfoUtil.getDepartmentId(), false, false);
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }

    @GetMapping("/current/peerLeve")
    @Operation(summary = "当前用户部门同级部门")
    public List<DepartmentTreeVO> currentPeerLeveDept() {
        List<DepartmentDTO> dtos = this.departmentService.getPeerLevelDepartment(ExtendInfoUtil.getDepartmentId());
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }

    @PostMapping("/setAdmin")
    @ApiOperation("设置部门管理员权限")
    public boolean setDeptAdmin(@RequestBody DepartmentAdminParam param) {
        return this.departmentService.setDeptAdmin(param);
    }


    @GetMapping("/childrenList")
    @ApiOperation("获取当前登录用户下级部门列表")
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

    @GetMapping("/currentAndAllSubset")
    @Operation(summary = "查询部门树-获取本级及下级全量部门数据")
    public List<DepartmentTreeVO> currentAndAllSubset() {
        List<DepartmentDTO> dtos = this.departmentService.getCurrentAndAllSubset();
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


    @GetMapping("/sameAndSuperiorDept")
    @Operation(summary = "当前层级及同层级上级部门树")
    public List<DepartmentTreeVO> sameAndSuperiorDept() {
        List<DepartmentDTO> dtos = this.departmentService.sameAndSuperiorDept();
        return DepartmentConvert.INSTANCE.toTreeVO(dtos);
    }

    @PostMapping("regionUpdate")
    @Operation(summary = "批量修改用户区域")
    public void regionUpdate(String regionName) {
        this.departmentService.changeRegion(regionName);
    }

    @PostMapping("initCache")
    @Operation(summary = "初始化缓存")
    public void initCache() {
        this.departmentService.initCache();
    }

    @PostMapping("initRoles")
    @Operation(summary = "初始化部门角色")
    @Deprecated   //fixme 3.11版本后deptId有问题 ,部门id和角色无直接关系
    public void initRoles(String deptId) {
        this.departmentRoleService.initRoles(deptId);
    }

    @GetMapping("getMeToFourthLevelDept")
    @Operation(summary = "获取当前用户下所有的四级部门")
    public List<DepartmentTreeVO> getMeToFourthLevelDept() {
        List<DepartmentDTO> meToFourthLevelDept = this.departmentService.getMeToFourthLevelDept();
        return DepartmentConvert.INSTANCE.toTreeVO(meToFourthLevelDept);
    }

    @PostMapping("/third/add")
    @Operation(summary = "第三方调用-新增部门")
    public String add(@RequestBody @Validated DepartmentCreateOrUpdateParam param) {
        departmentService.saveOrUpdateForThird(DepartmentConvert.INSTANCE.paramToDTO(param));
        return param.getDeptId();
    }

    @PostMapping("/third/batchAdd")
    @Operation(summary = "第三方调用-批量新增部门")
    public Integer batchAdd(@RequestBody @Validated Set<DepartmentCreateOrUpdateParam> param) {
        departmentService.batchSaveOrUpdateForThird(param);
        return param.size();
    }

    @PostMapping("/third/update")
    @Operation(summary = "第三方调用-更新部门")
    public void update(@RequestBody @Validated DepartmentCreateOrUpdateParam param) {
        departmentService.saveOrUpdateForThird(DepartmentConvert.INSTANCE.paramToDTO(param));
    }

    @PostMapping("/third/delete")
    @Operation(summary = "第三方调用-删除部门")
    public void delete(@RequestBody @Validated DepartmentDeleteParam param) {
        departmentService.deleteForThird(param.getDeptId());
    }

    //加签部门（同级以及上级部门查询）
    @PostMapping("/subscribing/department")
    @Operation(summary = "加签部门")
    public List<DepartmentDTO> subscribingDepartment() {
        return departmentService.subscribingDepartment();
    }

    @GetMapping("/getAllParentDepartments")
    @Operation(summary = "根据code，获取上级部门,传入C,并按照A、B、C有序返回")
    public List<DepartmentTreeVO> getAllParentDepartments(@RequestParam(required = false) String deptCode,
                                                          @RequestParam(required = false) String deptId) {
        List<DepartmentDTO> dto = departmentService.getAllParentDepartments(deptCode, deptId);
        return DepartmentConvert.INSTANCE.toTreeVO(dto);
    }

}
