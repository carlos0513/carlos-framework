package com.yunjin.org.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.convert.DepartmentRoleConvert;
import com.yunjin.org.manager.DepartmentManager;
import com.yunjin.org.manager.DepartmentRoleManager;
import com.yunjin.org.pojo.dto.DepartmentRoleDTO;
import com.yunjin.org.pojo.entity.Department;
import com.yunjin.org.pojo.entity.DepartmentRole;
import com.yunjin.org.service.DepartmentRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 部门角色 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentRoleServiceImpl implements DepartmentRoleService {

    private final DepartmentRoleManager departmentRoleManager;

    private final DepartmentManager departmentManager;


    @Override
    public void addDepartmentRole(DepartmentRoleDTO dto) {
        if (StrUtil.isBlank(dto.getDepartmentType())) {
            throw new ServiceException("部门层级不能为空！");
        }
        if (StrUtil.isBlank(dto.getRoleId())) {
            throw new ServiceException("角色id不能为空！");
        }

        DepartmentRoleDTO departmentRole = departmentRoleManager.getDto(dto);
        if (departmentRole != null) {
            log.warn("role_department data exist");
            return;
        }
        boolean success = departmentRoleManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    @Override
    public void deleteByRoleId(Serializable roleId) {
        departmentRoleManager.deleteByRoleId(roleId);
    }

    @Override
    public void batchAddDepartmentRole(List<DepartmentRoleDTO> departmentRoleList) {
        for (DepartmentRoleDTO dto : departmentRoleList) {
            this.addDepartmentRole(dto);
        }
    }

    @Override
    public Set<String> listDepartmentIdByRoleId(Set<String> roleIds) {
        Set<String> types = departmentRoleManager.listDepartmentTypeByRoleId(roleIds);
        return departmentManager.list(new LambdaQueryWrapper<>(Department.class).select(Department::getId).in(Department::getDepartmentLevelCode, types))
                .stream().map(Department::getId).collect(Collectors.toSet());
    }

    @Override
    public void initRoles(String deptId) {
//        List<RoleDTO> roles = roleManager.listAll(null);
//        for (RoleDTO role : roles) {
//            DepartmentRoleDTO departmentRoleDTO = new DepartmentRoleDTO();
//            departmentRoleDTO.setDepartmentType(deptId);
//            departmentRoleDTO.setRoleId(role.getId());
//            this.addDepartmentRole(departmentRoleDTO);
//        }
    }


    @Override
    public boolean existRelation(String departmentLevelCode, String roleId) {
        List<DepartmentRole> list = departmentRoleManager.list(new LambdaQueryWrapper<>(DepartmentRole.class).select(DepartmentRole::getId)
                .eq(DepartmentRole::getDepartmentType, departmentLevelCode).eq(DepartmentRole::getRoleId, roleId));
        return CollUtil.isNotEmpty(list);

    }

    @Override
    public List<DepartmentRoleDTO> listAll() {
        return DepartmentRoleConvert.INSTANCE.toDTO(departmentRoleManager.list(new LambdaQueryWrapper<>(DepartmentRole.class)
                .select(DepartmentRole::getRoleId, DepartmentRole::getDepartmentType)));
    }

    @Override
    public Set<String> listDepartmentTypeByRoleId(Set<String> singleton) {
        return departmentRoleManager.listDepartmentTypeByRoleId(singleton);
    }

}
