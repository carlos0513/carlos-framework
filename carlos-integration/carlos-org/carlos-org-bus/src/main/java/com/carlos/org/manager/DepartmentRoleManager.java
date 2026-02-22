package com.carlos.org.manager;

import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.DepartmentRoleDTO;
import com.carlos.org.pojo.entity.DepartmentRole;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门角色 查询封装接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface DepartmentRoleManager extends BaseService<DepartmentRole> {

    /**
     * 新增部门角色
     *
     * @param dto 部门角色数据
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean add(DepartmentRoleDTO dto);

    /**
     * 根据部门id获取角色
     *
     * @param departmentId 部门id
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    List<DepartmentRoleDTO> listByDeptType(String departmentId);

    /**
     * 根据角色id获取部门
     *
     * @param roleId 角色id
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    DepartmentRoleDTO getDepartmentByRoleId(Serializable roleId);

    /**
     * 批量获取部门角色id
     *
     * @param deptIds 参数0
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/11/23 13:16
     */
    Set<Serializable> listRoleIdByDeptType(Set<String> deptIds);

    /**
     * 获取关联信息
     *
     * @param dto 参数0
     * @return com.carlos.org.pojo.dto.DepartmentRoleDTO
     * @author Carlos
     * @date 2022/11/30 23:47
     */
    DepartmentRoleDTO getDto(DepartmentRoleDTO dto);

    /**
     * 删除角色关联的部门
     *
     * @param roleId 参数0
     * @author Carlos
     * @date 2022/12/1 0:05
     */
    void deleteByRoleId(Serializable roleId);

    /**
     * 删除角色关联的部门
     *
     * @param deptType 参数0
     * @author Carlos
     * @date 2022/12/1 0:05
     */
    void deleteByDepartmentType(Serializable deptType);

    /**
     * listDepartmentIdByRoleId
     *
     * @param roleIds 参数0
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/12/5 14:35
     */
    Set<String> listDepartmentTypeByRoleId(Set<String> roleIds);

    Set<String> listRoleIdByDeptIds(Set<String> currentUserDepartmentIds);

    /**
     * 根据父级部门编码获取角色ID列表
     * @param deptCode
     * @return
     */
    Set<Serializable> listRoleIdByParentDeptCode(String deptCode);
}
