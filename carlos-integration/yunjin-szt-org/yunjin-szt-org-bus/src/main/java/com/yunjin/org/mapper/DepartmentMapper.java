package com.yunjin.org.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.yunjin.org.pojo.dto.UserDepartmentDTO;
import com.yunjin.org.pojo.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Mapper
public interface DepartmentMapper extends MPJBaseMapper<Department> {


    Long getDeptCodeCountWithDeleted(@Param("level") Integer level, @Param("parentCode") String parentCode);

    Set<String> getAllSubDeptIdsByDeptCode(@Param("deptCode") String deptCode);

    List<String> getAllParentDeptCodeByRecursive(@Param("deptCode") String deptCode);

    List<Department> selectAllSubDepartmentsByCodes(@Param("deptCodes") Set<String> deptCodes);

    /**
     * 获取当前部门以及下级所有部门的用户id集合
     *
     * @param departmentId
     * @return
     */
    List<UserDepartmentDTO> getCurrentAndChildrenDepartmentUserIds(@Param("departmentId") String departmentId);

    /**
     * 获取当前部门以及下级所有部门的deptCode
     *
     * @param parentId
     * @return
     */
    List<String> getAllDepartCodes(@Param("parentId") String parentId);
}
