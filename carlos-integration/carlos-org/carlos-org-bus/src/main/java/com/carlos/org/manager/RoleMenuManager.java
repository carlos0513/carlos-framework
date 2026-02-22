package com.carlos.org.manager;

import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.RoleMenuDTO;
import com.carlos.org.pojo.entity.RoleMenu;
import com.carlos.system.pojo.ao.MenuAO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色菜单 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
public interface RoleMenuManager extends BaseService<RoleMenu> {

    /**
     * 新增角色菜单
     *
     * @param dto 角色菜单数据
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    boolean add(RoleMenuDTO dto);

    /**
     * 删除角色菜单
     *
     * @param id 角色菜单id
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    boolean delete(Serializable id);

    /**
     * 修改角色菜单信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    boolean modify(RoleMenuDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.pojo.dto.RoleMenuDTO
     * @author yunjin
     * @date 2022-11-11 19:21:46
     */
    RoleMenuDTO getDtoById(String id);


    /**
     * 删除角色所有菜单
     *
     * @param roleId  角色id
     * @param menuIds 为空代表删除所有
     * @return boolean
     * @author Carlos
     * @date 2022/1/6 14:11
     */
    boolean deleteByRoleId(Serializable roleId, Set<Serializable> menuIds);


    /**
     * 获取角色菜单
     *
     * @param roleId 角色id
     * @return java.util.List<com.carlos.org.dto.sys.MenuAO>
     * @author Carlos
     * @date 2022/11/14 19:09
     */
    List<MenuAO> getMenuByRoleId(Serializable roleId);

    /**
     * @Title: getByRoleIds
     * @Description: 获取菜单 根据角色ids
     * @Date: 2023/3/31 15:38
     * @Parameters: [roleIds]
     * @Return java.util.List<com.carlos.system.pojo.dto.MenuAO>
     */
    List<RoleMenuDTO> getByRoleIds(Set<String> roleIds);
}
