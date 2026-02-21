package com.yunjin.org.api;

import com.yunjin.core.base.DepartmentInfo;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignDepartmentFallbackFactory;
import com.yunjin.org.param.DepartmentCreateOrUpdateParam;
import com.yunjin.org.param.DepartmentDeleteParam;
import com.yunjin.org.pojo.ao.CommonCustomAO;
import com.yunjin.org.pojo.ao.DepartmentAO;
import com.yunjin.org.pojo.ao.DepartmentBaseAO;
import com.yunjin.org.pojo.ao.DepartmentUserAO;
import com.yunjin.org.pojo.ao.UserDepartmentVO;
import com.yunjin.org.pojo.param.CurDeptExecutorPageParam;
import com.yunjin.org.pojo.param.CurSubExecutorPageParam;
import com.yunjin.org.pojo.param.TaskExecutorPageMianYangParam;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
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
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/department", contextId = "department", fallbackFactory = FeignDepartmentFallbackFactory.class)
public interface ApiDepartment {
    @GetMapping("/getAactivityRatio")
    @Operation(summary = "根据部门ID获取一段时间内部门用户活跃度列表")
    Result<List<CommonCustomAO>> getAactivityRatio(@RequestParam("startTime") String startTime,
                                                   @RequestParam("endTime") String endTime,
                                                   @RequestParam("deptCode") List<String> deptIds);

    @GetMapping("/getDepyByUserId")
    @Operation(summary = "根据用户ID获取部门列表")
    Result<List<DepartmentAO>> getDepyByUserId(@RequestParam("userId") String userId);

    @GetMapping("/deptCountStatistic")
    @Operation(summary = "获取所有部门数量")
    Result<Integer> deptCountStatistic();


    @GetMapping("/tree")
    @Operation(summary = "部门树形列表")
    Result<List<DepartmentAO>> tree(@RequestParam("departmentId") String departmentId, @RequestParam("userFlag") boolean userFlag);

    @GetMapping("/listAll")
    @Operation(summary = "获取所有部门")
    Result<List<DepartmentAO>> allDepartment();

    @GetMapping("/current/sameLeve")
    @Operation(summary = "当前用户部门同级部门")
    Result<List<DepartmentAO>> currentSameLeveDept(@RequestParam("userFlag") boolean userFlag);

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

    @GetMapping(value = "all/subDept/{id}", headers = {"content-type:application/x-www-form-urlencoded"})
    @Operation(summary = "下级部门code")
    Result<Set<String>> allSubDepartmentCode(@PathVariable final String id, @RequestParam("addSelf") boolean addSelf);

    @GetMapping("all/subDept/deptCode")
    @Operation(summary = "deptCode下级部门codes")
    Result<Set<String>> allSubDeptCodeByDeptCode(@RequestParam("deptCode") String deptCode);

    @GetMapping("all/subDept/regionCode")
    @Operation(summary = "regionCode下级部门codes")
    Result<Set<String>> allSubDeptCodeByReginCode(@RequestParam("regionCode") String regionCode);

    @GetMapping("/listAllByDeptName")
    @Operation(summary = "根据部门名称模糊查询获取所有部门")
    Result<List<DepartmentAO>> allDepartmentByName(@PathVariable String name);

    @GetMapping("/getDepartmentByCodes")
    @Operation(summary = "根据code获取部门s")
    Result<List<DepartmentAO>> getDepartmentByCodes(@RequestParam("codes") List<String> codes);

    @GetMapping("/getSubDepartmentByTypeLike")
    @Operation(summary = "根据机构类型获取部门（模糊匹配，左like）")
    Result<List<DepartmentAO>> getSubDepartmentByTypeLike(@RequestParam String deptTypeListStr);

    @PostMapping("/getCurSubUser")
    @Operation(summary = "根据code获取当前部门以及下级部门的所有用户")
    Result<Paging<DepartmentUserAO>> getCurSubUser(@RequestBody CurSubExecutorPageParam param);

    @PostMapping("/listCurDeptUser")
    @Operation(summary = "获取当前部门的所有用户")
    Result<Paging<DepartmentUserAO>> listCurDeptUser(@RequestBody CurDeptExecutorPageParam param);

    @GetMapping("/getAllParentDeptCodeByRecursive")
    @Operation(summary = "递归获取上级部门code列表")
    Result<List<String>> getAllParentDeptCodeByRecursive(@RequestParam("deptCode") String deptCode);

    //绵阳定开 获取部门树形列表包含用户 速度更快
    @GetMapping("/treeMianYang")
    @Operation(summary = "部门树形列表")
    Result<List<DepartmentAO>> treeMianYang(@RequestParam("departmentId") String departmentId, @RequestParam("userFlag") boolean userFlag);

    @GetMapping("listByThirdIds")
    @Operation(summary = "根据三方id获取部门")
    Result<List<DepartmentBaseAO>> listByThirdIds(@RequestParam("thirdIds") Set<String> thirdIds);

    @GetMapping("listThirdInfoByIds")
    @Operation(summary = "根据id获取三方部门信息")
    Result<List<DepartmentBaseAO>> listThirdInfoByIds(@RequestParam("ids") Set<String> ids);

    @PostMapping("getUserPageByDeptId")
    @Operation(summary = "分页获取部门下用户")
    Result<Paging<DepartmentUserAO>> getUserPageByDeptId(@RequestBody TaskExecutorPageMianYangParam param);

    @PostMapping("/third/add")
    @Operation(summary = "第三方调用-新增部门")
    Result<String> add(@RequestBody @Validated DepartmentCreateOrUpdateParam param);

    @PostMapping("/third/batchAdd")
    @Operation(summary = "第三方调用-批量新增部门")
    Result<Integer> batchAdd(@RequestBody @Validated Set<DepartmentCreateOrUpdateParam> param);

    @PostMapping("/third/update")
    @Operation(summary = "第三方调用-更新部门")
    void update(@RequestBody @Validated DepartmentCreateOrUpdateParam param);

    @PostMapping("/third/delete")
    @Operation(summary = "第三方调用-删除部门")
    void delete(@RequestBody @Validated DepartmentDeleteParam param);

    @GetMapping("listByRegionCode")
    @Operation(summary = "根据区域id获取部门")
    Result<List<DepartmentAO>> listDepartmentByRegionCode(@RequestParam("regionCode") String regionCode);

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