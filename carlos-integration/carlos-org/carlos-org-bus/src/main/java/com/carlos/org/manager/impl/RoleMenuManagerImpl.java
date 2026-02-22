package com.carlos.org.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.org.convert.RoleMenuConvert;
import com.carlos.org.manager.RoleMenuManager;
import com.carlos.org.mapper.RoleMenuMapper;
import com.carlos.org.pojo.dto.RoleMenuDTO;
import com.carlos.org.pojo.entity.RoleMenu;
import com.carlos.system.api.ApiMenu;
import com.carlos.system.pojo.ao.MenuAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RoleMenuManagerImpl extends BaseServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuManager {


    @Override
    public boolean add(RoleMenuDTO dto) {
        RoleMenu entity = RoleMenuConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'RoleMenu' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'RoleMenu' data: id:{}", entity.getId());
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
            log.warn("Remove 'RoleMenu' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'RoleMenu' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(RoleMenuDTO dto) {
        RoleMenu entity = RoleMenuConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'RoleMenu' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'RoleMenu' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public RoleMenuDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        RoleMenu entity = getBaseMapper().selectById(id);
        return RoleMenuConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public boolean deleteByRoleId(Serializable roleId, Set<Serializable> menuIds) {
        if (roleId == null) {
            log.warn("roleId can't be null");
            return false;
        }
        boolean success = remove(queryWrapper().eq(RoleMenu::getRoleId, roleId).in(CollectionUtil.isNotEmpty(menuIds), RoleMenu::getMenuId, menuIds));
        if (!success) {
            log.warn("Remove 'RoleMenu' data fail, roleId:{}", roleId);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'RoleMenu' data by roleId:{}", roleId);
        }
        return true;
    }

    @Override
    public List<MenuAO> getMenuByRoleId(Serializable roleId) {
        List<RoleMenu> list = lambdaQuery().eq(RoleMenu::getRoleId, roleId).list();
        Set<String> menuIds = list.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        ApiMenu api = SpringUtil.getBean(ApiMenu.class);
        Result<List<MenuAO>> result = api.listMenus(menuIds);
        if (!result.getSuccess()) {
            log.error("Get menu failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public List<RoleMenuDTO> getByRoleIds(Set<String> roleIds) {
        List<RoleMenu> list = lambdaQuery().in(RoleMenu::getRoleId, roleIds).list();
        return RoleMenuConvert.INSTANCE.toDTO(list);
    }
}
