package com.yunjin.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.org.convert.DepartmentMenuConvert;
import com.yunjin.org.manager.DepartmentMenuManager;
import com.yunjin.org.mapper.DepartmentMenuMapper;
import com.yunjin.org.pojo.dto.DepartmentMenuDTO;
import com.yunjin.org.pojo.entity.DepartmentMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门菜单表 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DepartmentMenuManagerImpl extends BaseServiceImpl<DepartmentMenuMapper, DepartmentMenu> implements DepartmentMenuManager {

    // private final FeignMenu feignMenu;

    @Override
    public boolean add(List<DepartmentMenuDTO> dtos) {
        List<DepartmentMenu> entity = DepartmentMenuConvert.INSTANCE.toDO(dtos);
        boolean success = saveBatch(entity);
        if (!success) {
            log.warn("Batch Insert 'DepartmentMenu' data fail");
            return false;
        }
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Batch Insert 'DepartmentMenu' data success");
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'DepartmentMenu' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'DepartmentMenu' data by id:{}", id);
        }
        return true;
    }

    @Override
    public DepartmentMenuDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        DepartmentMenu entity = getBaseMapper().selectById(id);
        return DepartmentMenuConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public List<DepartmentMenuDTO> getMenuByDepartmentId(String departmentId) {
        LambdaQueryWrapper<DepartmentMenu> wrapper = queryWrapper();
        wrapper.eq(DepartmentMenu::getDepartmentId, departmentId);
        // 根据部门id获取到所有相关的菜单id
        List<DepartmentMenu> list = list(wrapper);
        return DepartmentMenuConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<DepartmentMenuDTO> getDepartmentByMenuId(String menuId) {
        LambdaQueryWrapper<DepartmentMenu> wrapper = queryWrapper();
        wrapper.eq(DepartmentMenu::getMenuId, menuId);
        // 根据菜单id获取到所有相关的部门id
        List<DepartmentMenu> list = list(wrapper);
        return DepartmentMenuConvert.INSTANCE.toDTO(list);
    }

    @Override
    public boolean deleteDepartmentMenuByDeptId(String deptId) {
        if (deptId == null) {
            log.warn("deptId can't be null");
            return false;
        }
        LambdaQueryWrapper<DepartmentMenu> wrapper = queryWrapper();
        wrapper.eq(DepartmentMenu::getDepartmentId, deptId);
        return remove(wrapper);
    }


}
