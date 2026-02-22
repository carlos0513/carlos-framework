package com.carlos.org.service;

import com.carlos.org.pojo.dto.DepartmentRoleDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门角色 业务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface DepartmentRoleService {

    /**
     * 新增部门角色
     *
     * @param dto 部门角色数据
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    void addDepartmentRole(DepartmentRoleDTO dto);


    /**
     * 删除角色关联的部门信息
     *
     * @param roleId 角色id
     * @author Carlos
     * @date 2022/12/1 0:04
     */
    void deleteByRoleId(Serializable roleId);

    /**
     * 批量添加部门角色
     *
     * @param departmentRoleList 部门角色
     * @author shenyong
     */
    void batchAddDepartmentRole(List<DepartmentRoleDTO> departmentRoleList);

    /**
     * listDepartmentIdByRoleId
     *
     * @param roleIds 参数0
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/12/5 14:35
     */
    Set<String> listDepartmentIdByRoleId(Set<String> roleIds);


    /**
     * 初始化部门角色
     *
     * @param deptId 部门id
     * @author Carlos
     * @date 2024/4/15 8:56
     */
    void initRoles(String deptId);

    boolean existRelation(String departmentLevelCode, String roleId);

    List<DepartmentRoleDTO> listAll();


    Set<String> listDepartmentTypeByRoleId(Set<String> singleton);
}
