package com.carlos.org.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carlos.org.pojo.dto.OrgDepartmentUserDTO;
import com.carlos.org.pojo.entity.OrgDepartment;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 部门 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Mapper
public interface OrgDepartmentMapper extends MPJBaseMapper<OrgDepartment> {

    /**
     * 获取部门下的用户列表
     *
     * @param deptId 部门ID
     * @return List<OrgDepartmentUserDTO>
     */
    List<OrgDepartmentUserDTO> getUsersByDeptId(@Param("deptId") Serializable deptId);

    /**
     * 分页获取部门下的用户列表
     *
     * @param page   分页参数
     * @param deptId 部门ID
     * @return List<OrgDepartmentUserDTO>
     */
    List<OrgDepartmentUserDTO> getUsersByDeptIdPaged(IPage<?> page, @Param("deptId") Serializable deptId);

}
