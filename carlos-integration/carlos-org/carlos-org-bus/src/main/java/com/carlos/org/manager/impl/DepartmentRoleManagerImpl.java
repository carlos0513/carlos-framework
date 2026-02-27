package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.org.convert.DepartmentRoleConvert;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.DepartmentRoleManager;
import com.carlos.org.mapper.DepartmentRoleMapper;
import com.carlos.org.pojo.dto.DepartmentRoleDTO;
import com.carlos.org.pojo.entity.DepartmentRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门角色 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DepartmentRoleManagerImpl extends BaseServiceImpl<DepartmentRoleMapper, DepartmentRole> implements DepartmentRoleManager {
    private final DepartmentManager departmentManager;

    @Override
    public boolean add(DepartmentRoleDTO dto) {
        DepartmentRole entity = DepartmentRoleConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'DepartmentRole' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'DepartmentRole' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public List<DepartmentRoleDTO> listByDeptType(String departmentType) {
        LambdaQueryWrapper<DepartmentRole> wrapper = queryWrapper();
        wrapper.eq(DepartmentRole::getDepartmentId, departmentType);
        List<DepartmentRole> list = list(wrapper);
        return DepartmentRoleConvert.INSTANCE.toDTO(list);
    }

    @Override
    public DepartmentRoleDTO getDepartmentByRoleId(Serializable roleId) {
        DepartmentRole departmentRole = lambdaQuery().eq(DepartmentRole::getRoleId, roleId).one();
        return DepartmentRoleConvert.INSTANCE.toDTO(departmentRole);
    }


    @Override
    public Set<Serializable> listRoleIdByDeptType(Set<String> deptTypes) {
        if (CollectionUtils.isEmpty(deptTypes)) {
            return null;
        }
        List<DepartmentRole> list = lambdaQuery().select(DepartmentRole::getRoleId).in(DepartmentRole::getDepartmentId, deptTypes).list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(DepartmentRole::getRoleId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> listDepartmentTypeByRoleId(Set<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        List<DepartmentRole> list = lambdaQuery().select(DepartmentRole::getDepartmentId).in(DepartmentRole::getRoleId, roleIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(DepartmentRole::getDepartmentId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> listRoleIdByDeptIds(Set<String> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return null;
        }
        return baseMapper.listRoleIdsByDeptIds(deptIds);
    }

    @Override
    public Set<Serializable> listRoleIdByParentDeptCode(String deptCode) {
        //由ids查code
        // Set<String> deptTypes = departmentManager.listLevelCodeByTopParentDeptCode(deptCode);
        // if (CollUtil.isNotEmpty(deptTypes)) {
        //     return listRoleIdByDeptType(deptTypes);
        // }
        return null;
    }

    @Override
    public DepartmentRoleDTO getDto(DepartmentRoleDTO dto) {
        DepartmentRole entity = lambdaQuery()
                .eq(StrUtil.isNotBlank(dto.getRoleId()), DepartmentRole::getRoleId, dto.getRoleId())
                .one();
        return DepartmentRoleConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public void deleteByRoleId(Serializable roleId) {
        lambdaUpdate().eq(DepartmentRole::getRoleId, roleId).remove();
    }

    @Override
    public void deleteByDepartmentType(Serializable deptId) {
        lambdaUpdate().eq(DepartmentRole::getDepartmentId, deptId).remove();
    }
}
