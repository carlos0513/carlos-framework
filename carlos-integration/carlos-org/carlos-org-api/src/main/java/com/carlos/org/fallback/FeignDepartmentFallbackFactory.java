package com.carlos.org.fallback;

import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiDepartment;
import com.carlos.org.pojo.ao.DepartmentAO;
import com.carlos.org.pojo.ao.DepartmentUserAO;
import com.carlos.org.pojo.ao.UserDepartmentVO;
import com.carlos.org.pojo.param.CurDeptExecutorPageParam;
import com.carlos.org.pojo.param.CurSubExecutorPageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 用户降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignDepartmentFallbackFactory implements FallbackFactory<ApiDepartment> {

    @Override
    public ApiDepartment create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("部门服务调用失败: message:{}", message);
        return new ApiDepartment() {

            @Override
            public Result<List<DepartmentAO>> getDeptByUserId(String userId) {
                return Result.fail("根据用户ID获取部门列表失败", message);
            }

            @Override
            public Result<List<String>> getDepartmentName(String departmentId, Integer limit) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<Map<String, List<DepartmentAO>>> getParentDepartmentMap(Set<String> departmentIds, Integer limit) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<Map<String, List<DepartmentAO>>> getParentMapByCodes(Set<String> deptCodes, Integer limit) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<DepartmentInfo> getDepartmentInfo(Serializable departmentId, Integer limit) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> getDepartmentList() {
                return Result.fail("部门列表获取失败", message);
            }

            @Override
            public Result<DepartmentAO> getDepartmentByCode(String code) {
                return Result.fail("部门列表获取失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> getSubDepartment(String id) {
                return Result.fail("部门列表获取失败", message);
            }

            @Override
            public Result<DepartmentAO> getDepartmentById(String departmentId) {
                return Result.fail("查询部门详情失败", message);
            }

            @Override
            public Result<List<String>> previewDepartmentName(String id, int i) {
                return Result.fail("部门列表获取失败", message);
            }

            @Override
            public Result<List<String>> previewDepartmentNameByCode(String id, int i) {
                return Result.fail("通过code获取部门列表失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> listByIds(Set<String> deIds) {
                return Result.fail("部门列表获取失败", message);
            }

            @Override
            public Result<Set<String>> listRecurIds(String root) {
                return Result.fail("获取部门id列表失败", message);
            }

            @Override
            public Result<Set<Serializable>> getUserIdByDeptId(Set<Serializable> deIds) {
                return Result.fail("根据部门获取用户失败", message);
            }

            @Override
            public Result<List<DepartmentUserAO>> getUser(String id) {
                return Result.fail("根据部门获取用户失败", message);
            }

            @Override
            public Result<List<DepartmentUserAO>> getUserByDeptCode(String deptCode) {
                return Result.fail("根据部门获取用户失败", message);
            }

            @Override
            public Result<DepartmentAO> parentDepartment(String deptCode) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<Set<String>> allSubDeptCodeByDeptCode(String deptCode) {
                return Result.fail("下级部门code获取失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> allDepartmentByName(String name) {
                return Result.fail("模糊查询部门失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> getDepartmentByCodes(List<String> codes) {
                return Result.fail("模糊查询部门失败", message);
            }

            @Override
            public Result<Paging<DepartmentUserAO>> getCurSubUser(CurSubExecutorPageParam param) {
                return Result.fail("查询当前以及下级部门人员失败", message);
            }

            @Override
            public Result<List<String>> getAllParentDeptCodeByRecursive(String deptCode) {
                return Result.fail("递归获取所有上级部门code失败", message);
            }

            @Override
            public Result<Paging<DepartmentUserAO>> listCurDeptUser(CurDeptExecutorPageParam param) {
                return Result.fail("查询当前部门人员失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> getAllSubDepartment(String deptCode) {
                return Result.fail("获取当前部门的所有子部门失败", message);
            }

            @Override
            public Result<List<String>> getAllParentDepartmentIds(String deptCode) {
                return Result.fail("获取当前传入部门，获取所有父部门id失败", message);
            }

            @Override
            public Result<Set<String>> getAllDepartCodes(String parentId) {
                return Result.fail("获取当前传入部门及子级所有部门id失败", message);
            }

            @Override
            public Result<List<UserDepartmentVO>> listByDepartmentIdPage(List<String> parentId) {
                return Result.fail("获取当前传入部门及子级所有部门id失败", message);
            }

        };
    }
}