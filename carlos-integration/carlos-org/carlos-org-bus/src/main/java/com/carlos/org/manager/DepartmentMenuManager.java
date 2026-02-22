package com.carlos.org.manager;

import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.DepartmentMenuDTO;
import com.carlos.org.pojo.entity.DepartmentMenu;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门菜单表 查询封装接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface DepartmentMenuManager extends BaseService<DepartmentMenu> {

    /**
     * 新增部门菜单表
     *
     * @param dtos 部门菜单表数据
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean add(List<DepartmentMenuDTO> dtos);

    /**
     * 删除部门菜单表
     *
     * @param id 部门菜单表id
     * @return boolean
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    boolean delete(Serializable id);


    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.pojo.dto.DepartmentMenuDTO
     * @author carlos
     * @date 2022-11-11 19:21:46
     */
    DepartmentMenuDTO getDtoById(String id);


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
     * @Date: 2022/11/21 14:49
     * @Parameters: [deptId]
     * @Return boolean
     */
    boolean deleteDepartmentMenuByDeptId(String deptId);


}
