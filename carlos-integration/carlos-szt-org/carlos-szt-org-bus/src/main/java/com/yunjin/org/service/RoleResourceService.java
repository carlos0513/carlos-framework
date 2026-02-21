package com.yunjin.org.service;


import com.yunjin.system.pojo.ao.SysResourceAO;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色资源 业务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
public interface RoleResourceService {

    /**
     * 删除角色所有资源
     *
     * @param roleId 角色id
     * @author Carlos
     * @date 2021/12/29 16:07
     */
    void removeByRoleId(Serializable roleId);

    /**
     * 添加角色资源
     *
     * @param roleId      角色id
     * @param resourceIds 资源id
     * @author Carlos
     * @date 2022/1/4 15:47
     */
    void addRoleResource(Serializable roleId, Set<String> resourceIds);

    /**
     * 使用角色id获取资源
     *
     * @param roleId 角色id
     * @return java.util.List<com.carlos.sys.pojo.dto.ResourceDTO>
     * @author Carlos
     * @date 2022/1/5 10:45
     */
    List<SysResourceAO> getByRoleId(Serializable roleId);

    /**
     * 获取角色的资源id
     *
     * @param roleId 角色id
     * @return java.util.Set<java.lang.Long>
     * @author Carlos
     * @date 2022/1/12 14:54
     */
    Set<String> getResourceIdByRoleId(Serializable roleId);

}
