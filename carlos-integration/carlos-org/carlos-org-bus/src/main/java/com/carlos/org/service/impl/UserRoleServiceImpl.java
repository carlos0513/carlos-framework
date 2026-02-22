package com.carlos.org.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.exception.ServiceException;
import com.carlos.org.manager.UserRoleManager;
import com.carlos.org.pojo.dto.UserRoleDTO;
import com.carlos.org.pojo.entity.UserRole;
import com.carlos.org.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户角色 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleManager userRoleManager;


    @Override
    public void addUserRole(String userId, Set<String> roleIds) {
        //用户id空校验
        if (StrUtil.isBlank(userId)) {
            throw new ServiceException("用户id不能为空");
        }

        //角色id校验
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new ServiceException("角色id不可为空!");
        }

        List<UserRoleDTO> dtos = roleIds.stream().map(i -> new UserRoleDTO().setUserId(userId).setRoleId(i)).collect(Collectors.toList());

        boolean success = this.userRoleManager.add(dtos);
        if (!success) {
            // 保存失败的应对措施
            throw new ServiceException("角色设置失败!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddUserRole(Set<String> ids, Set<String> roleIds) {
        //this.userRoleManager.batchDeleteByUserId(ids);
        this.userRoleManager.batchDeleteByRoleId(roleIds);
        for (String id : ids) {
            addUserRole(id, roleIds);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        this.userRoleManager.deleteByUserId(userId);
    }


    @Override
    public List<UserRoleDTO> getByRoleId(Set<String> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return null;
        }

        return this.userRoleManager.getByRoleIds(roleIds);
    }

    @Override
    public Set<String> getRoleIdsByUserId(String userId) {
        List<UserRole> list = userRoleManager.list(new LambdaQueryWrapper<>(UserRole.class).select(UserRole::getRoleId).eq(UserRole::getUserId, userId));
        Set<String> res = new HashSet<>();
        if (CollectionUtil.isNotEmpty(list)) {
            res = list.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        }
        return res;
    }

    @Override
    public Boolean removeByRoleId(Set<String> roleIds) {
        return this.userRoleManager.removeByRoleId(roleIds);
    }


}
