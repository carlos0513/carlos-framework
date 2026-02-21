package com.yunjin.org.fallback;

import com.yunjin.core.base.DepartmentInfo;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiDepartment;
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
            public Result<List<CommonCustomAO>> getAactivityRatio(String startTime, String endTime, List<String> deptIds) {
                return Result.fail("手机端获取活跃度列表失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> getDepyByUserId(String userId) {
                return Result.fail("根据用户ID获取部门列表失败", message);
            }

            @Override
            public Result<Integer> deptCountStatistic() {
                return Result.fail("所有部门数量获取失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> tree(String departmentId, boolean userFlag) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> allDepartment() {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> currentSameLeveDept(boolean userFlag) {
                return Result.fail("部门信息获取失败", message);
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
            public Result<Set<String>> allSubDepartmentCode(String id, boolean addSelf) {
                return Result.fail("下级部门code获取失败", message);
            }

            @Override
            public Result<Set<String>> allSubDeptCodeByDeptCode(String deptCode) {
                return Result.fail("下级部门code获取失败", message);
            }

            @Override
            public Result<Set<String>> allSubDeptCodeByReginCode(String regionCode) {
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
            public Result<List<DepartmentAO>> getSubDepartmentByTypeLike(String deptTypeListStr) {
                return Result.fail("根据机构类型获取部门", message);
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
            public Result<List<DepartmentAO>> treeMianYang(String departmentId, boolean userFlag) {
                return Result.fail("部门信息获取失败", message);
            }

            @Override
            public Result<Paging<DepartmentUserAO>> getUserPageByDeptId(TaskExecutorPageMianYangParam param) {
                return Result.fail("分页获取部门下用户信息失败", message);
            }

            @Override
            public Result<Paging<DepartmentUserAO>> listCurDeptUser(CurDeptExecutorPageParam param) {
                return Result.fail("查询当前部门人员失败", message);
            }

            @Override
            public Result<String> add(DepartmentCreateOrUpdateParam param) {
                return Result.fail("第三方新增部门失败", message);
            }

            @Override
            public Result<Integer> batchAdd(Set<DepartmentCreateOrUpdateParam> param) {
                return Result.fail("第三方批量新增部门失败", message);
            }

            @Override
            public void update(DepartmentCreateOrUpdateParam param) {

            }

            @Override
            public void delete(DepartmentDeleteParam param) {

            }

            @Override
            public Result<List<DepartmentBaseAO>> listByThirdIds(Set<String> thirdIds) {
                return Result.fail("根据三方id获取部门信息失败", message);
            }

            @Override
            public Result<List<DepartmentAO>> listDepartmentByRegionCode(String regionCode) {
                return Result.fail("根据区域获取部门信息失败", message);
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

            @Override
            public Result<List<DepartmentBaseAO>> listThirdInfoByIds(Set<String> ids) {
                return Result.fail("根据id获取三方部门信息失败", message);
            }
        };
    }
}