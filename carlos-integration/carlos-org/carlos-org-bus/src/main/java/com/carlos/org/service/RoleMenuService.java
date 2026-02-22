package com.carlos.org.service;

import com.carlos.org.pojo.dto.RoleMenuDTO;
import com.carlos.org.pojo.vo.MenuIdVO;
import com.carlos.system.pojo.ao.MenuAO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色菜单 业务接口
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
public interface RoleMenuService {

    /**
     * 添加角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     * @author Carlos
     * @date 2022/1/6 14:23
     */
    void addRoleMenu(Serializable roleId, Set<String> menuIds);

    /**
     * 批量添加角色菜单
     *
     * @param roleIds 角色id
     * @param menuIds 菜单id
     * @author Carlos
     * @date 2022/1/6 14:23
     */
    void batchAddRoleMenu(Set<String> roleIds, Set<String> menuIds);

    /**
     * 添加移动端角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     */
    void addMobileRoleMenu(String roleId, Set<Serializable> menuIds);

    /**
     * 批量添加移动端角色菜单
     *
     * @param roleIds 角色id
     * @param menuIds 菜单id
     */
    void batchAddMobileRoleMenu(Set<String> roleIds, Set<Serializable> menuIds);

    /**
     * 删除角色所有菜单
     *
     * @param roleId 角色id
     * @author Carlos
     * @date 2021/12/29 16:07
     */
    void removeByRoleId(Serializable roleId);

    /**
     * 获取角色菜单id
     *
     * @param roleId 角色id
     * @return java.util.Set<java.lang.Long>
     * @author Carlos
     * @date 2022/1/12 14:42
     */
    MenuIdVO getMenuIdByRoleId(Serializable roleId);

    /**
     * 获取角色菜单
     *
     * @param roleId 角色id
     * @return java.util.List<com.carlos.org.dto.sys.MenuAO>
     * @author Carlos
     * @date 2022/11/14 19:07
     */
    List<MenuAO> getByRoleId(Serializable roleId);

    /**
     * 删除RoleMenu 通过角色id
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     * @author Carlos
     * @date 2022/12/4 20:36
     */
    void deleteRoleMenu(String roleId, Set<Serializable> menuIds);

    /**
     * @Title: getByRoleIds
     * @Description: 获取菜单，根据角色ids
     * @Date: 2023/3/31 15:35
     * @Parameters: [roleIds]
     * @Return java.util.List<com.carlos.system.pojo.dto.MenuAO>
     */
    List<RoleMenuDTO> getByRoleIds(Set<String> roleIds);
}
