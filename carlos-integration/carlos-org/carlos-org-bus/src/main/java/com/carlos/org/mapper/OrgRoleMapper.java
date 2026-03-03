package com.carlos.org.mapper;

import com.carlos.org.pojo.dto.OrgRoleUserDTO;
import com.carlos.org.pojo.entity.OrgRole;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 角色 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Mapper
public interface OrgRoleMapper extends MPJBaseMapper<OrgRole> {

    /**
     * 获取角色的权限ID列表
     *
     * @param roleId 角色ID
     * @return List<Long>
     */
    List<Long> getPermissionIdsByRoleId(@Param("roleId") Serializable roleId);

    /**
     * 获取使用某角色的用户数量
     *
     * @param roleId 角色ID
     * @return int
     */
    int getUserCountByRoleId(@Param("roleId") Serializable roleId);

    /**
     * 删除角色的权限关联
     *
     * @param roleId 角色ID
     */
    void deleteRolePermissions(@Param("roleId") Serializable roleId);

    /**
     * 批量插入角色权限关联
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void insertRolePermissions(@Param("roleId") Serializable roleId,
                               @Param("permissionIds") List<Serializable> permissionIds);

    /**
     * 获取角色用户列表
     *
     * @param roleId 角色ID
     * @return List<OrgRoleUserDTO>
     */
    List<OrgRoleUserDTO> getRoleUsers(@Param("roleId") Serializable roleId);

}
