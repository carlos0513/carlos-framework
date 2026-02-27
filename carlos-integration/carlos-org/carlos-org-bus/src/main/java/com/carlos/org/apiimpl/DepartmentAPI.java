package com.carlos.org.apiimpl;

import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiDepartment;
import com.carlos.org.convert.DepartmentConvert;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.pojo.ao.DepartmentAO;
import com.carlos.org.pojo.ao.DepartmentUserAO;
import com.carlos.org.pojo.ao.UserDepartmentVO;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.param.CurDeptExecutorPageParam;
import com.carlos.org.pojo.param.CurSubExecutorPageParam;
import com.carlos.org.service.DepartmentService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
@RequestMapping("api/org/department")
@Tag(name = "系统用户Feign接口")
@Hidden
@Slf4j
public class DepartmentAPI implements ApiDepartment {

    private final DepartmentService departmentService;

    private final UserDepartmentManager userDepartmentManager;

    private final DepartmentManager departmentManager;

    @Override
    public Result<List<DepartmentAO>> getDeptByUserId(String userId) {
        List<DepartmentDTO> dtos = departmentService.getByUserId(userId);
        return Result.ok(DepartmentConvert.INSTANCE.toAOS(dtos));
    }

    @Override
    @GetMapping("/getDepartmentName")
    @Operation(summary = "根据部门id获取上级部门名称")
    public Result<List<String>> getDepartmentName(@RequestParam("departmentId") String departmentId, @RequestParam("limit") Integer limit) {
        try {
            List<String> superDepartmentName = departmentService.previewDepartmentName(departmentId, limit);
            return Result.ok(superDepartmentName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("部门查询失败：" + e.getMessage());
        }
    }

    /**
     * 批量获取部门的上级部门名称映射
     *
     * @param departmentIds 部门ID集合
     * @param limit         限制层级
     * @return Map<部门ID, 上级部门名称列表>
     */
    @Override
    @PostMapping("/parentDepartmentNameMap")
    @Operation(summary = "批量获取部门的上级部门名称映射")
    public Result<Map<String, List<DepartmentAO>>> getParentDepartmentMap(@RequestBody Set<String> departmentIds, @RequestParam("limit") Integer limit) {
        try {
            Map<String, List<DepartmentDTO>> superDepartmentName = departmentService.getParentDepartmentMap(departmentIds, limit);
            Map<String, List<DepartmentAO>> superDepartmentNameMap = DepartmentConvert.INSTANCE.toAOS(superDepartmentName);
            return Result.ok(superDepartmentNameMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("部门查询失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, List<DepartmentAO>>> getParentMapByCodes(@RequestBody Set<String> deptCodes, @RequestParam("limit") Integer limit) {
        try {
            Map<String, List<DepartmentDTO>> superDepartmentName = departmentService.getParentMapByCodes(deptCodes, limit);
            Map<String, List<DepartmentAO>> superDepartmentNameMap = DepartmentConvert.INSTANCE.toAOS(superDepartmentName);
            return Result.ok(superDepartmentNameMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("部门查询失败：" + e.getMessage());
        }
    }

    @Override
    @GetMapping("/getDepartmentList")
    @Operation(summary = "获取所有部门")
    public Result<List<DepartmentAO>> getDepartmentList() {
        try {
            List<DepartmentDTO> departments = departmentService.getDepartments();
            return Result.ok(DepartmentConvert.INSTANCE.toAOS(departments));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("部门查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/getDepartmentByCode")
    @Operation(summary = "根据code获取部门")
    @Override
    public Result<DepartmentAO> getDepartmentByCode(@RequestParam("code") String code) {
        DepartmentDTO department = departmentService.getDepartmentByCode(code);
        return Result.ok(DepartmentConvert.INSTANCE.toAO(department));
    }

    @GetMapping("/getSubDepartment")
    @Operation(summary = "根据id获取子部门集合")
    @Override
    public Result<List<DepartmentAO>> getSubDepartment(@RequestParam("id") String id) {
        List<DepartmentDTO> departments = departmentService.getSubDepartment(id);
        return Result.ok(DepartmentConvert.INSTANCE.toAOS(departments));
    }

    @GetMapping("/getDepartmentById")
    @Operation(summary = "根据id获取部门详细信息")
    @Override
    public Result<DepartmentAO> getDepartmentById(@RequestParam("departmentId") String departmentId) {
        DepartmentDTO departmentById = departmentService.getDepartmentById(departmentId);
        return Result.ok(DepartmentConvert.INSTANCE.toAO(departmentById));
    }

    @GetMapping("/previewDepartmentName")
    @Operation(summary = "预览部门名称")
    @Override
    public Result<List<String>> previewDepartmentName(@RequestParam("id") String id, @RequestParam("i") int i) {
        List<String> list = departmentService.previewDepartmentName(id, i);
        return Result.ok(list);
    }

    @GetMapping("/previewDepartmentNameByCode")
    @Operation(summary = "通过code预览部门名称")
    @Override
    public Result<List<String>> previewDepartmentNameByCode(@RequestParam("code") String code, @RequestParam("i") int i) {
        List<String> list = departmentService.previewDepartmentNameByCode(code, i);
        return Result.ok(list);
    }

    @GetMapping("/listByIds")
    @Operation(summary = "根据ids获取")
    @Override
    public Result<List<DepartmentAO>> listByIds(@RequestParam("ids") Set<String> deIds) {
        List<DepartmentDTO> departments = departmentService.listByIds(deIds);
        return Result.ok(DepartmentConvert.INSTANCE.toAOS(departments));
    }


    @Override
    @GetMapping("/info")
    @Operation(summary = "获取部门信息")
    public Result<DepartmentInfo> getDepartmentInfo(@RequestParam("departmentId") Serializable departmentId, @RequestParam("limit") Integer limit) {
        return Result.ok(departmentService.getDepartmentInfo(departmentId, limit));
    }


    @Override
    public Result<Set<String>> listRecurIds(String root) {
        return Result.ok(departmentService.getDepartmentIdsRecurById(root));
    }

    @Override
    public Result<Set<Serializable>> getUserIdByDeptId(Set<Serializable> deIds) {
        return Result.ok(userDepartmentManager.getUserIdByDepartmentId(deIds));
    }

    @Override

    @GetMapping("user/deptById")
    @Operation(summary = "部门所有用户")
    public Result<List<DepartmentUserAO>> getUser(@RequestParam("id") String id) {
        List<UserDepartmentDTO> users = departmentService.getUser(id);
        List<DepartmentUserAO> userAOS = DepartmentConvert.INSTANCE.toUserAO(users);
        return Result.ok(userAOS);
    }

    @Override
    public Result<List<DepartmentUserAO>> getUserByDeptCode(String deptCode) {
        DepartmentInfo departmentInfo = departmentService.getDepartmentInfo(deptCode, 0);
        List<UserDepartmentDTO> users = departmentService.getUser(departmentInfo.getId().toString());
        List<DepartmentUserAO> userAOS = DepartmentConvert.INSTANCE.toUserAO(users);
        return Result.ok(userAOS);

    }

    @Override
    @GetMapping("parentDept/{deptCode}")
    @Operation(summary = "上级部门")
    public Result<DepartmentAO> parentDepartment(@PathVariable final String deptCode) {
        DepartmentDTO department = departmentService.parentDepartment(deptCode);
        return Result.ok(DepartmentConvert.INSTANCE.toAO(department));
    }

    @Override
    @GetMapping("all/subDept/deptCode")
    @Operation(summary = "deptCode下级部门code")
    public Result<Set<String>> allSubDeptCodeByDeptCode(String deptCode) {
        String id = departmentService.getDepartmentByCode(deptCode).getId();
        Set<String> codes = departmentService.allSubDepartmentCode(id, true);
        return Result.ok((codes));
    }

    @Override
    @GetMapping("/listAllByDeptName")
    @Operation(summary = "根据部门名称模糊查询获取所有部门")
    public Result<List<DepartmentAO>> allDepartmentByName(String name) {
        List<DepartmentDTO> departments = departmentService.allDepartmentByName(name);
        return Result.ok(DepartmentConvert.INSTANCE.toAOS(departments));
    }

    @Override
    @GetMapping("/getDepartmentByCodes")
    @Operation(summary = "根据code获取部门s")
    public Result<List<DepartmentAO>> getDepartmentByCodes(List<String> codes) {
        List<DepartmentDTO> departmentdepartments = departmentService.getDepartmentByCodes(codes);
        List<DepartmentAO> departmentAOS = DepartmentConvert.INSTANCE.toAOS(departmentdepartments);

        return Result.ok(departmentAOS);
    }

    @Override
    @PostMapping("/getCurSubUser")
    @Operation(summary = "根据code获取当前部门以及下级部门的所有用户")
    public Result<Paging<DepartmentUserAO>> getCurSubUser(CurSubExecutorPageParam param) {
        Paging<UserDepartmentDTO> users = departmentService.getCurSubUser(param);
        List<DepartmentUserAO> userAOS = DepartmentConvert.INSTANCE.toUserAO(users.getRecords());
        Paging<DepartmentUserAO> paging = new Paging<>();
        paging.setRecords(userAOS);
        paging.setSize(param.getSize());
        paging.setTotal(users.getTotal());
        paging.setCurrent(param.getCurrent());
        return Result.ok(paging);
    }

    @Override
    @PostMapping("/listCurDeptUser")
    @Operation(summary = "获取当前部门的所有用户")
    public Result<Paging<DepartmentUserAO>> listCurDeptUser(@RequestBody CurDeptExecutorPageParam param) {
        Paging<UserDepartmentDTO> users = departmentService.getCurDeptUser(param);
        List<DepartmentUserAO> userAOS = DepartmentConvert.INSTANCE.toUserAO(users.getRecords());
        Paging<DepartmentUserAO> paging = new Paging<>();
        paging.setRecords(userAOS);
        paging.setSize(param.getSize());
        paging.setTotal(users.getTotal());
        paging.setCurrent(param.getCurrent());
        return Result.ok(paging);
    }

    @Override
    public Result<List<String>> getAllParentDeptCodeByRecursive(String deptCode) {
        return Result.ok(departmentManager.getAllParentDeptCodeByRecursive(deptCode));
    }

    @Override
    public Result<List<DepartmentAO>> getAllSubDepartment(String deptCode) {
        List<DepartmentDTO> dtos = departmentService.getAllSubDepartment(deptCode);
        return Result.ok(DepartmentConvert.INSTANCE.toAOS(dtos));
    }

    @Override
    public Result<List<String>> getAllParentDepartmentIds(String deptCode) {
        List<String> allParentDepartmentIds = departmentService.getAllParentDepartmentIds(deptCode);
        return Result.ok(allParentDepartmentIds);
    }

    @Override
    public Result<Set<String>> getAllDepartCodes(String parentId) {
        Set<String> allParentDepartmentIds = departmentService.getAllDepartCodes(parentId);
        return Result.ok(allParentDepartmentIds);
    }

    @Override
    public Result<List<UserDepartmentVO>> listByDepartmentIdPage(@RequestParam("ids") List<String> ids){
        List<UserDepartmentDTO> userDepartmentDTOS = userDepartmentManager.listByDepartmentIdPage(ids);
        List<UserDepartmentVO> departmentVOS=DepartmentConvert.INSTANCE.toDetailVO(userDepartmentDTOS);
        return Result.ok(departmentVOS);
    }

}
