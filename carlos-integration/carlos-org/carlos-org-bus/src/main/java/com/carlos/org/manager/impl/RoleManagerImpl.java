package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.UserUtil;
import com.carlos.org.convert.RoleConvert;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.DepartmentRoleManager;
import com.carlos.org.manager.RoleManager;
import com.carlos.org.mapper.RoleMapper;
import com.carlos.org.pojo.ao.UserLoginAO;
import com.carlos.org.pojo.dto.RoleDTO;
import com.carlos.org.pojo.entity.Role;
import com.carlos.org.pojo.param.RolePageParam;
import com.carlos.org.pojo.vo.RolePageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * <p>
 * 系统角色 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RoleManagerImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleManager {

    private final DepartmentManager departmentManager;
    private final DepartmentRoleManager departmentRoleManager;


    @Override
    public boolean add(RoleDTO dto) {
        Role entity = RoleConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'Role' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'Role' data: id:{}", entity.getId());
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
            log.warn("Remove 'Role' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'Role' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(RoleDTO dto) {
        Role entity = RoleConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'Role' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'Role' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public RoleDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        Role entity = getBaseMapper().selectById(id);
        return RoleConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public List<RoleDTO> getDtoByIds(Set<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            log.warn("role ids is null");
            return null;
        }
        List<Role> roles = getBaseMapper().selectList(queryWrapper().in(Role::getId, ids));
        return RoleConvert.INSTANCE.toDTO(roles);
    }

    @Override
    public Paging<RolePageVO> getPage(RolePageParam param) {
        //查询当前用户本级及下级的
        UserLoginAO.Department department = UserUtil.getDepartment();
        if (Objects.isNull(department)) {
            throw new ServiceException("当前用户信息不存在组织机构！");
        }
/*        Set<Serializable> currentUserDepartmentIds = departmentManager.getCurrentAndAllSubDepartmentId(department.getDeptCode());
        Set<Serializable> roleIds = departmentRoleManager.listRoleIdByDeptIds(currentUserDepartmentIds);*/

        Set<Serializable> roleIds = departmentRoleManager.listRoleIdByParentDeptCode(department.getDeptCode());
        if (roleIds == null || roleIds.isEmpty()) {
            return new Paging<>();
        }
        LambdaQueryWrapper<Role> wrapper = queryWrapper();
        wrapper.in(Role::getId, roleIds);
        wrapper.orderByDesc(Role::getCreateTime);
        wrapper.select(
                Role::getId,
                Role::getName,
                Role::getCode,
                Role::getState,
                Role::getDescription,
                Role::getCreateBy,
                Role::getCreateTime,
                Role::getUpdateBy,
                Role::getUpdateTime
        );
        if (StrUtil.isNotEmpty(param.getKeyword())) {
            wrapper.like(Role::getName, param.getKeyword());
        }

        PageInfo<Role> rolePageInfo = pageInfo(param);
        PageInfo<Role> page = page(rolePageInfo, wrapper);

        return MybatisPage.convert(page, RoleConvert.INSTANCE::toVOS);
    }

    @Override
    public RoleDTO getByName(String name) {
        Role one = lambdaQuery().eq(Role::getName, name).one();
        return RoleConvert.INSTANCE.toDTO(one);
    }

    @Override
    public RoleDTO getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        Role one = lambdaQuery().eq(Role::getCode, code).one();
        return RoleConvert.INSTANCE.toDTO(one);
    }

    @Override
    public List<RoleDTO> listAll(String name) {
        List<Role> list = lambdaQuery()
                .select(
                        Role::getId,
                        Role::getName,
                        Role::getCode,
                        Role::getDescription
                ).like(StrUtil.isNotBlank(name), Role::getName, name)
                .list();

        return RoleConvert.INSTANCE.toDTO(list);
    }

    @Override
    public RoleDTO getLatestOne() {
        LambdaQueryWrapper<Role> wrapper = queryWrapper();
        wrapper.select(
                Role::getId,
                Role::getName,
                Role::getCode
        ).orderByDesc(Role::getCreateTime).last("LIMIT 1");
        Role role = baseMapper.selectOne(wrapper);
        return RoleConvert.INSTANCE.toDTO(role);
    }
}
