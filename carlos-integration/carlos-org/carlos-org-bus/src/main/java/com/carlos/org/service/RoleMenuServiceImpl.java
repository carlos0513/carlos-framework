package com.carlos.org.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.org.manager.RoleMenuManager;
import com.carlos.org.pojo.dto.RoleMenuDTO;
import com.carlos.org.pojo.vo.MenuIdVO;
import com.carlos.system.enums.MenuType;
import com.carlos.system.pojo.ao.MenuAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 角色菜单 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl implements RoleMenuService {

    private final RoleMenuManager roleMenuManager;


    @Override
    public void addRoleMenu(Serializable roleId, Set<String> menuIds) {
        // 删除原有关联
        removeByRoleId(roleId);
        for (String menuId : menuIds) {
            roleMenuManager.add(new RoleMenuDTO().setRoleId(String.valueOf(roleId)).setMenuId(String.valueOf(menuId)));
        }
    }

    @Override
    public void batchAddRoleMenu(Set<String> roleIds, Set<String> menuIds) {
        for (String roleId : roleIds) {
            this.addRoleMenu(roleId, menuIds);
        }
    }

    @Override
    public void addMobileRoleMenu(String roleId, Set<Serializable> menuIds) {
        deleteRoleMenu(roleId, menuIds);
        for (Serializable menuId : menuIds) {
            roleMenuManager.add(new RoleMenuDTO().setRoleId(String.valueOf(roleId)).setMenuId(String.valueOf(menuId)));
        }
    }

    @Override
    public void batchAddMobileRoleMenu(Set<String> roleIds, Set<Serializable> menuIds) {
        for (String roleId : roleIds) {
            this.addMobileRoleMenu(roleId, menuIds);
        }
    }

    @Override
    public void removeByRoleId(Serializable roleId) {
        roleMenuManager.deleteByRoleId(roleId, null);
    }


    @Override
    public MenuIdVO getMenuIdByRoleId(Serializable roleId) {
        MenuIdVO menuIdVO = new MenuIdVO();
        if (roleId == null) {
            return menuIdVO;
        }
        List<MenuAO> menus = roleMenuManager.getMenuByRoleId(roleId);
        Set<String> parentIds = menus.stream().map(MenuAO::getParentId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        Set<String> pc = menus.stream().filter(menu -> MenuType.PC == menu.getMenuType() && !parentIds.contains(menu.getId()))
                .map(MenuAO::getId).collect(Collectors.toSet());
        Set<String> mobile = menus.stream().filter(menu -> MenuType.MOBILE == menu.getMenuType() && !parentIds.contains(menu.getId()))
                .map(MenuAO::getId).collect(Collectors.toSet());
        Set<String> manageMenuIds = menus.stream().filter(menu -> MenuType.MANAGE == menu.getMenuType() && !parentIds.contains(menu.getId()))
                .map(MenuAO::getId).collect(Collectors.toSet());
        return menuIdVO.setPcMenuIds(pc).setMobileMenuIds(mobile).setManageMenuIds(manageMenuIds);
    }

    @Override
    public List<MenuAO> getByRoleId(Serializable roleId) {
        return roleMenuManager.getMenuByRoleId(roleId);
    }

    @Override
    public void deleteRoleMenu(String roleId, Set<Serializable> menuIds) {
        if (ObjectUtil.isEmpty(roleId)) {
            throw new ServiceException("角色id不能为空");
        }
        roleMenuManager.deleteByRoleId(roleId, menuIds);
    }

    @Override
    public List<RoleMenuDTO> getByRoleIds(Set<String> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new ServiceException("角色id不能为空");
        }
        return roleMenuManager.getByRoleIds(roleIds);
    }
}
