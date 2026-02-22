package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.UserRoleConvert;
import com.carlos.org.manager.RoleManager;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.manager.UserRoleManager;
import com.carlos.org.mapper.UserRoleMapper;
import com.carlos.org.pojo.dto.RoleDTO;
import com.carlos.org.pojo.dto.UserRoleDTO;
import com.carlos.org.pojo.entity.Role;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.entity.UserRole;
import com.carlos.org.pojo.param.UserRolePageParam;
import com.carlos.org.pojo.vo.UserRoleVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Sets;
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
 * 用户角色 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserRoleManagerImpl extends BaseServiceImpl<UserRoleMapper, UserRole> implements UserRoleManager {

    private final RoleManager roleManager;
    private final UserDepartmentManager userDepartmentManager;

    @Override
    public boolean add(List<UserRoleDTO> dto) {
        List<UserRole> entity = UserRoleConvert.INSTANCE.toDO(dto);
        boolean success = this.saveBatch(entity);
        if (!success) {
            log.warn("Insert 'UserRole' data fail, entity:{}", entity);
            return false;
        }
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'UserRole' data entity:{}", entity);
        }
        return true;
    }

    @Override
    public void addBatch(List<UserRoleDTO> dtos) {

        if (CollectionUtil.isEmpty(dtos)) {
            throw new ServiceException("添加失败，用户角色列表不可为空！");
        }
        List<UserRole> userRoles = UserRoleConvert.INSTANCE.toDO(dtos);
        boolean success = this.saveBatch(userRoles);
        if (!success) {
            log.warn("batchInsert 'UserRole' data fail, userRoles:{}", userRoles);
            throw new ServiceException("批量添加失败失败！");
        }
        List<UserRoleDTO> userRoleDTOS = UserRoleConvert.INSTANCE.toDTO(userRoles);
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'UserRole' data: userRoleDTOS:{}", userRoleDTOS);
        }
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = this.removeById(id);
        if (!success) {
            log.warn("Remove 'UserRole' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'UserRole' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(UserRoleDTO dto) {
        UserRole entity = UserRoleConvert.INSTANCE.toDO(dto);
        boolean success = this.updateById(entity);
        if (!success) {
            log.warn("Update 'UserRole' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'UserRole' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public UserRoleDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        UserRole entity = this.getBaseMapper().selectById(id);
        return UserRoleConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<UserRoleVO> getPage(UserRolePageParam param) {
        LambdaQueryWrapper<UserRole> wrapper = this.queryWrapper();
        wrapper.select(
                UserRole::getId,
                UserRole::getUserId,
                UserRole::getRoleId,
                UserRole::getTenantId
        );
        PageInfo<UserRole> page = this.page(this.pageInfo(param), wrapper);
        return MybatisPage.convert(page, UserRoleConvert.INSTANCE::toVO);
    }

    @Override
    public List<UserRoleDTO> getRoleByUserId(String userId) {
        LambdaQueryWrapper<UserRole> wrapper = this.queryWrapper();
        wrapper.select(
                UserRole::getId,
                UserRole::getUserId,
                UserRole::getRoleId
        );
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> list = this.list(wrapper);
        List<UserRoleDTO> dtos = UserRoleConvert.INSTANCE.toDTO(list);
        if (CollectionUtil.isNotEmpty(dtos)) {
            dtos.forEach(i -> {
                RoleDTO role = this.roleManager.getDtoById(i.getRoleId());
                if (role != null) {
                    i.setRoleName(role.getName());
                    i.setRoleCode(role.getCode());
                }
            });
        }
        return dtos;
    }

    @Override
    public List<UserRoleDTO> listByRoleId(String roleId) {
        if (StrUtil.isBlank(roleId)) {
            return null;
        }
        List<UserRole> list = this.lambdaQuery().eq(UserRole::getRoleId, roleId).list();
        return UserRoleConvert.INSTANCE.toDTO(list);
    }

    @Override
    public Set<Serializable> listUserIdByRole(Serializable roleId) {
        if (roleId == null) {
            return null;
        }
        List<UserRoleDTO> dtos = this.listByRoleId((String) roleId);
        if (CollectionUtils.isEmpty(dtos)) {
            return Sets.newHashSet();
        }
        return dtos.stream().map(UserRoleDTO::getUserId).collect(Collectors.toSet());
    }

    @Override
    public boolean deleteByUserId(String userId) {
        return this.lambdaUpdate().eq(UserRole::getUserId, userId).remove();
    }

    @Override
    public boolean batchDeleteByUserId(Set<String> userIds) {
        return this.lambdaUpdate().in(UserRole::getUserId, userIds).remove();
    }

    @Override
    public Set<Serializable> listUserIdByRoleId(Set<Serializable> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        return userDepartmentManager.list(new LambdaQueryWrapper<>(UserDepartment.class)
                        .select(UserDepartment::getUserId).in(UserDepartment::getRoleId, roleIds))
                .stream().map(UserDepartment::getUserId).collect(Collectors.toSet());
    }

    @Override
    public List<UserRoleDTO> getByRoleIds(Set<String> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return null;
        }
        return UserRoleConvert.INSTANCE.toDTO(this.lambdaQuery().in(UserRole::getRoleId, roleIds).list());
    }

    @Override
    public boolean batchDeleteByRoleId(Set<String> roleIds) {
        return this.lambdaUpdate().in(UserRole::getRoleId, roleIds).remove();
    }

    @Override
    public Boolean removeByRoleId(Set<String> roleIds) {
        LambdaQueryWrapper<UserRole> wrapper = queryWrapper().in(CollectionUtil.isNotEmpty(roleIds), UserRole::getRoleId, roleIds);
        boolean success = this.remove(wrapper);
        if (!success) {
            log.warn("remove 'UserRole' data fail");
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("remove 'UserRole' data");
        }
        return true;
    }

    @Override
    public List<UserRoleDTO> listAll() {
        return UserRoleConvert.INSTANCE.toDTO(list());
    }

    @Override
    public List<UserRoleDTO> getRoleByUserIds(List<String> userIds) {
        return getBaseMapper().selectJoinList(UserRoleDTO.class,
                new MPJLambdaWrapper<UserRole>().selectAll(UserRole.class).selectAs(Role::getName,
                        UserRoleDTO::getRoleName).selectAs(Role::getCode, UserRoleDTO::getRoleCode)
                        .leftJoin(Role.class, Role::getId, UserRole::getRoleId)
                        .eq(Role::getState, 0)
                        .eq(Role::getDeleted, 0)
                        .in(CollUtil.isNotEmpty(userIds), UserRole::getUserId, userIds));
    }
}
