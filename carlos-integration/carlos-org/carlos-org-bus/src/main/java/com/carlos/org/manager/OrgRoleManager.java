package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgRoleDTO;
import com.carlos.org.pojo.dto.OrgRoleUserDTO;
import com.carlos.org.pojo.entity.OrgRole;
import com.carlos.org.pojo.param.OrgRolePageParam;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
public interface OrgRoleManager extends BaseService<OrgRole> {

    /**
     * 新增角色
     *
     * @param dto 角色数据
     * @return boolean
     */
    boolean add(OrgRoleDTO dto);

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return boolean
     */
    boolean delete(Serializable id);

    /**
     * 修改角色信息
     *
     * @param dto 对象信息
     * @return boolean
     */
    boolean modify(OrgRoleDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return OrgRoleDTO
     */
    OrgRoleDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @return Paging<OrgRoleDTO>
     */
    Paging<OrgRoleDTO> getPage(OrgRolePageParam param);

    /**
     * 根据角色编码获取角色
     *
     * @param roleCode 角色编码
     * @return OrgRoleDTO
     */
    OrgRoleDTO getByRoleCode(String roleCode);

    /**
     * 获取角色的权限ID列表
     *
     * @param roleId 角色ID
     * @return List<Long>
     */
    List<Long> getPermissionIdsByRoleId(Serializable roleId);

    /**
     * 获取使用某角色的用户数量
     *
     * @param roleId 角色ID
     * @return int
     */
    int getUserCountByRoleId(Serializable roleId);

    /**
     * 分配角色权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return boolean
     */
    boolean assignPermissions(Serializable roleId, List<Serializable> permissionIds);

    /**
     * 获取角色用户列表
     *
     * @param roleId 角色ID
     * @param param 分页参数
     * @return Paging<OrgRoleUserDTO>
     */
    Paging<OrgRoleUserDTO> getRoleUsers(Serializable roleId, OrgRolePageParam param);

}
