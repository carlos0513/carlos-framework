package com.carlos.org.api;

import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.org.ServiceNameConstant;
import com.carlos.org.fallback.FeignDepartmentFallbackFactory;
import com.carlos.org.pojo.ao.DepartmentAO;
import com.carlos.org.pojo.ao.DepartmentUserAO;
import com.carlos.org.pojo.ao.UserDepartmentVO;
import com.carlos.org.pojo.param.CurDeptExecutorPageParam;
import com.carlos.org.pojo.param.CurSubExecutorPageParam;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 部门 feign 提供接口
 * </p>
 *
 * @author carlos
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/department", contextId = "department", fallbackFactory = FeignDepartmentFallbackFactory.class)
public interface ApiDepartment {

    @GetMapping("/getDeptByUserId")
    @Operation(summary = "根据用户ID获取部门列表")
    Result<List<DepartmentAO>> getDeptByUserId(@RequestParam("userId") String userId);

    @GetMapping("/getDepartmentName")
    @Operation(summary = "根据部门id获取上级部门名称")
    Result<List<String>> getDepartmentName(@RequestParam("departmentId") String departmentId, @RequestParam("limit") Integer limit);

    @PostMapping("/parentDepartmentNameMap")
    @Operation(summary = "批量获取部门的上级部门映射")
    Result<Map<String, List<DepartmentAO>>> getParentDepartmentMap(@RequestBody Set<String> departmentIds, @RequestParam("limit") Integer limit);

    @PostMapping("/getParentMapByCodes")
    @Operation(summary = "批量获取部门的上级部门映射")
    Result<Map<String, List<DepartmentAO>>> getParentMapByCodes(@RequestBody Set<String> deptCodes, @RequestParam("limit") Integer limit);


    @GetMapping("/info")
    @Operation(summary = "获取部门信息")
    Result<DepartmentInfo> getDepartmentInfo(@RequestParam("departmentId") Serializable departmentId, @RequestParam("limit") Integer limit);

    @GetMapping("/getDepartmentList")
    @Operation(summary = "获取所有部门")
    Result<List<DepartmentAO>> getDepartmentList();

    @GetMapping("/getDepartmentByCode")
    @Operation(summary = "根据code获取部门")
    Result<DepartmentAO> getDepartmentByCode(@RequestParam("code") String code);

    @GetMapping("/getSubDepartment")
    @Operation(summary = "根据id获取子部门集合")
    Result<List<DepartmentAO>> getSubDepartment(@RequestParam("id") String id);

    @GetMapping("/getDepartmentById")
    @Operation(summary = "根据id获取子部门集合")
    Result<DepartmentAO> getDepartmentById(@RequestParam("departmentId") String departmentId);

    @GetMapping("/previewDepartmentName")
    @Operation(summary = "预览部门名称")
    Result<List<String>> previewDepartmentName(@RequestParam("id") String id, @RequestParam("i") int i);

    @GetMapping("/previewDepartmentNameByCode")
    @Operation(summary = "通过code预览部门名称")
    Result<List<String>> previewDepartmentNameByCode(@RequestParam("code") String code, @RequestParam("i") int i);

    @GetMapping("/listByIds")
    @Operation(summary = "根据ids获取")
    Result<List<DepartmentAO>> listByIds(@RequestParam("ids") Set<String> deIds);

    @GetMapping("/listRecurIds")
    @Operation(summary = "递归获取下级部门id列表")
    Result<Set<String>> listRecurIds(@RequestParam("root") String root);

    @GetMapping("/getUserIdByDeptId")
    @Operation(summary = "根据部门Id获取用户Id")
    Result<Set<Serializable>> getUserIdByDeptId(@RequestParam("ids") Set<Serializable> deIds);


    @GetMapping("user/deptById")
    @Operation(summary = "部门所有用户")
    Result<List<DepartmentUserAO>> getUser(@RequestParam("id") String id);

    @GetMapping("user/{deptCode}")
    @Operation(summary = "部门所有用户")
    Result<List<DepartmentUserAO>> getUserByDeptCode(@PathVariable final String deptCode);

    @GetMapping("parentDept/{deptCode}")
    @Operation(summary = "上级部门")
    Result<DepartmentAO> parentDepartment(@PathVariable final String deptCode);

    @GetMapping("all/subDept/deptCode")
    @Operation(summary = "deptCode下级部门codes")
    Result<Set<String>> allSubDeptCodeByDeptCode(@RequestParam("deptCode") String deptCode);

    @GetMapping("/listAllByDeptName")
    @Operation(summary = "根据部门名称模糊查询获取所有部门")
    Result<List<DepartmentAO>> allDepartmentByName(@PathVariable String name);

    @GetMapping("/getDepartmentByCodes")
    @Operation(summary = "根据code获取部门s")
    Result<List<DepartmentAO>> getDepartmentByCodes(@RequestParam("codes") List<String> codes);

    @PostMapping("/getCurSubUser")
    @Operation(summary = "根据code获取当前部门以及下级部门的所有用户")
    Result<Paging<DepartmentUserAO>> getCurSubUser(@RequestBody CurSubExecutorPageParam param);

    @PostMapping("/listCurDeptUser")
    @Operation(summary = "获取当前部门的所有用户")
    Result<Paging<DepartmentUserAO>> listCurDeptUser(@RequestBody CurDeptExecutorPageParam param);

    @GetMapping("/getAllParentDeptCodeByRecursive")
    @Operation(summary = "递归获取上级部门code列表")
    Result<List<String>> getAllParentDeptCodeByRecursive(@RequestParam("deptCode") String deptCode);


    @GetMapping("getAllSubDepartment")
    @Operation(summary = "获取当前部门的所有子部门")
    Result<List<DepartmentAO>> getAllSubDepartment(@RequestParam("deptCode") String deptCode);

    @GetMapping("getAllParentDepartmentIds")
    @Operation(summary = "获取当前传入部门，获取所有父部门id，并按照A、B、C有序返回")
    Result<List<String>> getAllParentDepartmentIds(@RequestParam("deptCode") String deptCode);

    @GetMapping("getAllDepartCodes")
    @Operation(summary = "获取当前传入部门以及下面所有子部门的depatCode")
    Result<Set<String>> getAllDepartCodes(@RequestParam("parentId") String parentId);

    @GetMapping("listByDepartmentIdPage")
    @Operation(summary = "获取当前传入部门以及下面所有子部门的信息")
    Result<List<UserDepartmentVO>> listByDepartmentIdPage(@RequestParam("ids") List<String> ids);
}