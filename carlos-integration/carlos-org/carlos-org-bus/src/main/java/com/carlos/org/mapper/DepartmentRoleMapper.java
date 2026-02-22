package com.carlos.org.mapper;

import com.carlos.org.pojo.entity.DepartmentRole;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 部门角色 查询接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Mapper
public interface DepartmentRoleMapper extends MPJBaseMapper<DepartmentRole> {
    Set<String> listRoleIdsByDeptIds(@Param("deptIds") Set<String> deptIds);
}
