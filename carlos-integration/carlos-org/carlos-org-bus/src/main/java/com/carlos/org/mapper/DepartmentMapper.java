package com.carlos.org.mapper;

import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.Department;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门 查询接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Mapper
public interface DepartmentMapper extends MPJBaseMapper<Department> {

    List<Department> selectAllSubDepartmentsByCodes(@Param("deptCodes") Set<String> deptCodes);

    /**
     * 获取当前部门以及下级所有部门的用户id集合
     *
     * @param departmentId
     * @return
     */
    List<UserDepartmentDTO> getCurrentAndChildrenDepartmentUserIds(@Param("departmentId") String departmentId);

}
