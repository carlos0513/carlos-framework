package com.yunjin.org.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.yunjin.org.pojo.entity.DepartmentRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 部门角色 查询接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Mapper
public interface DepartmentRoleMapper extends MPJBaseMapper<DepartmentRole> {
    Set<String> listRoleIdsByDeptIds(@Param("deptIds") Set<String> deptIds);
}
