package com.carlos.org.service;

import com.carlos.org.pojo.dto.DepartmentMenuDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门菜单表 业务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface DepartmentMenuService {

    /**
     * 新增部门菜单表
     *
     * @param dtos 部门菜单表数据
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    void addDepartmentMenu(List<DepartmentMenuDTO> dtos);

    /**
     * 删除部门菜单表
     *
     * @param ids 部门菜单表id
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    void deleteDepartmentMenu(Set<Serializable> ids);


    /**
     * 根据部门id获取菜单
     *
     * @param departmentId 上级部门id
     * @author carlos
     * @date 2022-11-11 19:01:17
     */
    List<DepartmentMenuDTO> getMenuByDepartmentId(String departmentId);

    /**
     * 根据菜单id获取部门信息
     *
     * @param menuId 上级部门id
     * @author carlos
     * @date 2022-11-11 19:01:17
     */
    List<DepartmentMenuDTO> getDepartmentByMenuId(String menuId);


    /**
     * @Title: deleteDepartmentMenuByDeptId
     * @Description: 根据部门id删除部门菜单
     * @Date: 2022/11/21 14:47
     * @Parameters: [deptId]
     * @Return void
     */
    void deleteDepartmentMenuByDeptId(String deptId);
}
