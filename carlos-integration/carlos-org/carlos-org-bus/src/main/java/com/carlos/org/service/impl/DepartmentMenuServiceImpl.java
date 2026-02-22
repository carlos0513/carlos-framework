package com.carlos.org.service.impl;

import com.carlos.core.exception.ServiceException;
import com.carlos.org.manager.DepartmentMenuManager;
import com.carlos.org.pojo.dto.DepartmentMenuDTO;
import com.carlos.org.service.DepartmentMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 部门菜单表 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentMenuServiceImpl implements DepartmentMenuService {

    private final DepartmentMenuManager departmentMenuManager;

    @Override
    public void addDepartmentMenu(List<DepartmentMenuDTO> dtos) {
        boolean success = departmentMenuManager.add(dtos);
        if (!success) {
            throw new ServiceException("部门菜单权限添加失败！");
        }
    }

    @Override
    public void deleteDepartmentMenu(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = departmentMenuManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }


    @Override
    public List<DepartmentMenuDTO> getMenuByDepartmentId(String departmentId) {
        return departmentMenuManager.getMenuByDepartmentId(departmentId);
    }

    @Override
    public List<DepartmentMenuDTO> getDepartmentByMenuId(String menuId) {
        return departmentMenuManager.getDepartmentByMenuId(menuId);
    }


    @Override
    public void deleteDepartmentMenuByDeptId(String deptId) {
        boolean success = departmentMenuManager.deleteDepartmentMenuByDeptId(deptId);
        if (!success) {
            throw new ServiceException("部门菜单删除失败！");
        }
    }

}
